package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.*;
import java.util.Scanner;

public class App {
    static Juego juego = new Juego();
    
    public static void main(String[] args) throws Exception {
        
        
        //new Hilos();

        var scanner = new Scanner(System.in);
        

        while (scanner.hasNext()) {
            var linea = scanner.nextLine();
            if (linea.contains("position startpos")) {
                if (linea.contains("moves")) {
                    var movimientos = linea.replaceAll("position startpos moves ", "").split(" ");
                    juego = new Juego();
                    juego.EstablecerPosicion(movimientos);
                    Utilidades.ImprimirPosicicion(juego.tablero);
                }
            } else if (linea.contains("go")) {
                int n = 1;
                try {

                    n= Integer.parseInt(linea.replaceAll("go ", ""));

                } catch (Exception e) {
                }
                System.out.println("bestmove " + juego.mover(n));

            } else if (linea.contains("isready")) {
                System.out.println("readyok");
            } else if (linea.contains("ucinewgame")) {
                juego = new Juego();
                System.out.println("uciok");
            } else if (linea.contains("uci")) {
                System.out.println("uciok");
            } else if (linea.contains("stop")) {
                System.out.println("readyok");
                
            } else if (linea.contains("fen")) {
                juego.tablero = new Pieza[8][8];
                String[] filas = linea.replace("fen ", "").split("/");

                for (int i = 0; i < filas.length; i++) {

                    if (i == 7) {
                        String[] ops = filas[i].split(" ");
                        IniciarFen(ops[0], 7 - i);

                        juego.estadoTablero.TurnoBlanco = ops[1].equals("w");
                        juego.estadoTablero.EnroqueCBlanco = ops[2].contains("K");
                        juego.estadoTablero.EnroqueLBlanco = ops[2].contains("Q");
                        juego.estadoTablero.EnroqueCNegro = ops[2].contains("k");
                        juego.estadoTablero.EnroqueLNegro = ops[2].contains("q");

                    } else {
                        IniciarFen(filas[i], 7 - i);
                    }

                }

                System.out.println("fen procesado");
                Utilidades.ImprimirPosicicion(juego.tablero);

            }else if(linea.contains("perft")){
                int n = 1;
                
                try{n = Integer.parseInt(linea.replace("perft ",""));}catch(NumberFormatException ex){}
                
                juego.perft(n);
            }

        }
        scanner.close();
    }

    private static void IniciarFen(String fila, int i) {

        int j = 0;

        for (char c : fila.toCharArray()) {
            if (c == 'k') {
                juego.tablero[i][j] = new Rey(false);
            }
            if (c == 'q') {
                juego.tablero[i][j] = new Dama(false);
            }
            if (c == 'r') {
                juego.tablero[i][j] = new Torre(false);
            }
            if (c == 'b') {
                juego.tablero[i][j] = new Alfil(false);
            }
            if (c == 'n') {
                juego.tablero[i][j] = new Caballo(false);
            }
            if (c == 'p') {
                juego.tablero[i][j] = new Peon(false);
            }

            if (c == 'K') {
                juego.tablero[i][j] = new Rey(true);
            }
            if (c == 'Q') {
                juego.tablero[i][j] = new Dama(true);
            }
            if (c == 'R') {
                juego.tablero[i][j] = new Torre(true);
            }
            if (c == 'B') {
                juego.tablero[i][j] = new Alfil(true);
            }
            if (c == 'N') {
                juego.tablero[i][j] = new Caballo(true);
            }
            if (c == 'P') {
                juego.tablero[i][j] = new Peon(true);
            }

            if (Character.isDigit(c)) {
                j += Integer.parseInt(String.valueOf(c));
            } else {
                j++;
            }
        }

    }
}
