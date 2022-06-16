
package buscaminas;

import java.util.Scanner;

/**
 *
 * @author Óscar Núñez Aguado <aguado@edu.xunta.es>
 */

/**
 * MEJORAS:
 * - Desmarcar casilla
 * - Letras para las opciones, colores, etc.
 * - Interfaz gráfico
 */


public class Buscaminas {

    // Declaración de variables y constantes de clase
    static final int FILAS = 9;
    static final int COLUMNAS = 9;
    static final int NUM_MINAS = 9;
    static int numMarcas = 0;
    static boolean[][] tableroMinas = new boolean[FILAS][COLUMNAS];
    static char[][] tableroJuego = new char[FILAS][COLUMNAS];
    
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RESET = "\u001B[0m";
        
    public static void main(String[] args) {
        // Declaración de variables y constantes
        int opcion;
        boolean minaPisada = false;        
        
        iniciarTableroMinas();
        iniciarTableroJuego();
        
        do {
            imprimirTableroJuego(); 
            imprimirMenu();
            switch (opcion = leerOpcion()){
                case 1: 
                    minaPisada = destaparCasilla();                           
                    break;
                case 2: 
                    marcarCasilla();
                    break;
            }
            
        }while (opcion != 0 && !minaPisada && numMarcas < NUM_MINAS);
        
        if (minaPisada) {
            System.out.println(ANSI_RED + "FIN DEL JUEGO: PISASTE UNA MINA!!!" + ANSI_RESET);
        } else if (numMarcas == NUM_MINAS) {
            // TODAS LAS MINAS BIEN MARCADAS???
            System.out.println(ANSI_GREEN + "ENHORABUENA: MARCASTE CORRECTAMENTE TODAS LAS MINAS!!!" + ANSI_RESET);            
        }
        
        imprimirTableroMinas(); 
    }

    static boolean destaparCasilla(){
        // Lee las coordenadas. 
        // Si hay mina devuelve true. 
        // Si no destapa la casilla y calcula minas adyacentes.
        int f, c;
        boolean esMina = false;
        // LeerCoordenadas        
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Número de fila y columna: ");
        f = sc.nextInt();
        c = sc.nextInt();
        // Si hay una mina => Fin del juego
        // Si no hay mina => Destapar casilla e indicar el número de minas adyacentes. Si cero => destapar adyacentes también         
        if (tableroMinas[f][c]){
            esMina = true;
            tableroJuego[f][c] = '*';
        } else {
            esMina = false;
            destapar(f, c);
        } 
        return esMina;
    }    

    static void destapar(int f, int c){
        if(tableroJuego[f][c] == '-') {
            int numMinas = minasAdyacentes(f, c);

           tableroJuego[f][c] = (char) (48 + numMinas);



           if(numMinas == 0){
               if (f>0 && c>0) 
                   destapar(f-1,c-1);
               if (f>0)
                   destapar(f-1,c);
               if (f>0 && c<COLUMNAS-1)
                   destapar(f-1,c+1);

               if (c>0)
                   destapar(f,c-1);
               if (c<COLUMNAS-1)
                   destapar(f,c+1);

               if (f<FILAS-1 && c>0)
                   destapar(f+1,c-1);
               if (f<FILAS-1)
                   destapar(f+1,c);
               if (f<FILAS-1 && c<COLUMNAS-1)
                   destapar(f+1,c+1);  
           }           
        }
        


    }
    
    static int minasAdyacentes(int f, int c) {
        int numMinas = 0;
        if (f>0 && c>0 && tableroMinas[f-1][c-1]) numMinas++;
        if (f>0 && tableroMinas[f-1][c]) numMinas++;
        if (f>0 && c<COLUMNAS-1 && tableroMinas[f-1][c+1]) numMinas++;
        
        if (c>0 && tableroMinas[f][c-1]) numMinas++;
        if (c<COLUMNAS-1 && tableroMinas[f][c+1]) numMinas++;
        
        if (f<FILAS-1 && c>0 && tableroMinas[f+1][c-1]) numMinas++;
        if (f<FILAS-1 && tableroMinas[f+1][c]) numMinas++;
        if (f<FILAS-1 && c<COLUMNAS-1 && tableroMinas[f+1][c+1]) numMinas++;
        
        return numMinas;
    }        
    static void marcarCasilla(){
        int f, c;
        // LeerCoordenadas                    
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Número de fila y columna: ");
        f = sc.nextInt();
        c = sc.nextInt();
        
        // Marcar 'M' en el tablero                
        tableroJuego[f][c] = 'M'; 
        numMarcas++;
    }

    
    static void imprimirMenu() {
        System.out.println("\nMenú");
        System.out.println("1. Destapar casilla");
        System.out.println("2. Marcar mina");
        System.out.println("0. SALIR");
    }
    
    static int leerOpcion() {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Opción: ");
        
        return sc.nextInt();
    }
    
    
    static void iniciarTableroMinas(){
        int f, c;
        
        // Inicializar tablero a false (no es necesario en Java)
        for(int i = 0; i < FILAS; i++)
            for(int j = 0; j < COLUMNAS; j++)        
                tableroMinas[i][j] = false;
        
        // Coloca cada mina
        for (int i = 0; i < NUM_MINAS; i++) {
            // Genera coordenadas aleatorias mientras la casilla tenga mina.
            do {
                f = (int)(Math.random()*FILAS);
                c = (int)(Math.random()*COLUMNAS);
            } while(tableroMinas[f][c] == true);
            
            tableroMinas[f][c] = true;
        }        
    }

    static void iniciarTableroJuego(){
        int f, c;
        
        // Inicializar tablero a false (no es necesario en Java)
        for(int i = 0; i < FILAS; i++)
            for(int j = 0; j < COLUMNAS; j++)        
                tableroJuego[i][j] = '-';       
    }

    static void imprimirTableroMinas(){
        for(int j = 0; j < COLUMNAS; j++){
            if (j==0) System.out.print("   ");
            System.out.print(j % 10 + " ");
        }
        System.out.println("");

        for(int i = 0; i < FILAS; i++){
            System.out.print(i % 10 + ": ");
            
            for(int j = 0; j < COLUMNAS; j++){
                if (tableroMinas[i][j]) {
                    System.out.print(ANSI_RED + "* " + ANSI_RESET);
                } else {
                    System.out.print("- ");
                }                   
            }
            System.out.println("");
        }            
    }
    
    static void imprimirTableroJuego(){
        
        for(int j = 0; j < COLUMNAS; j++){
            if (j==0) System.out.print("   ");
            System.out.print(j % 10 + " ");
        }
        System.out.println("");
            
        for(int i = 0; i < FILAS; i++){
            System.out.print(i % 10 + ": ");
            
            for(int j = 0; j < COLUMNAS; j++)
                System.out.print(tableroJuego[i][j] + " ");
            System.out.println("");
        }            
    }
    
}

