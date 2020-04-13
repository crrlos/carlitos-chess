package com.wolf.carlitos;

import static com.wolf.carlitos.Juego.tablero;
import com.wolf.carlitos.Piezas.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        
        

       new Hilos();

        var scanner = new Scanner(System.in);
        Juego juego = new Juego();

        while (scanner.hasNext()) {
            var linea = scanner.nextLine();
            if (linea.contains("position startpos")) {
                if (linea.contains("moves")) {
                    var movimientos = linea.replaceAll("position startpos moves ", "").split(" ");
                    juego = new Juego();
                    juego.EstablecerPosicion(movimientos);
                    juego.ImprimirPosicicion();
                }
            } else if (linea.contains("go")) {
                try {

                    EstadoTablero.deep = Integer.parseInt(linea.replaceAll("go", ""));

                } catch (Exception e) {
                }
                System.out.println("bestmove " + juego.Mover((EstadoTablero) Juego.estadoTablero.clone()));

            } else if (linea.contains("isready")) {
                System.out.println("readyok");
            } else if (linea.contains("ucinewgame")) {
                juego = new Juego();
                System.out.println("uciok");
            } else if (linea.contains("uci")) {
                System.out.println("uciok");
            } else if (linea.contains("stop")) {
                System.out.println("readyok");
                juego.MovimientosValidos();
            } else if (linea.contains("fen")) {
                tablero = new Pieza[8][8];
                String[] filas = linea.replace("fen ", "").split("/");

                for (int i = 0; i < filas.length; i++) {

                    if (i == 7) {
                        String[] ops = filas[i].split(" ");
                        IniciarFen(ops[0], 7 - i);

                        Juego.estadoTablero.TurnoBlanco = ops[1].equals("w");
                        Juego.estadoTablero.EnroqueCBlanco = ops[2].contains("K");
                        Juego.estadoTablero.EnroqueLBlanco = ops[2].contains("Q");
                        Juego.estadoTablero.EnroqueCNegro = ops[2].contains("k");
                        Juego.estadoTablero.EnroqueLNegro = ops[2].contains("q");

                    } else {
                        IniciarFen(filas[i], 7 - i);
                    }

                }

                System.out.println("fen procesado");
                Juego.ImprimirPosicicion();

            }

        }
        scanner.close();
    }

    private static void IniciarFen(String fila, int i) {

        int j = 0;

        for (char c : fila.toCharArray()) {
            if (c == 'k') {
                Juego.tablero[i][j] = new Rey(false);
            }
            if (c == 'q') {
                Juego.tablero[i][j] = new Dama(false);
            }
            if (c == 'r') {
                Juego.tablero[i][j] = new Torre(false);
            }
            if (c == 'b') {
                Juego.tablero[i][j] = new Alfil(false);
            }
            if (c == 'n') {
                Juego.tablero[i][j] = new Caballo(false);
            }
            if (c == 'p') {
                Juego.tablero[i][j] = new Peon(false);
            }

            if (c == 'K') {
                Juego.tablero[i][j] = new Rey(true);
            }
            if (c == 'Q') {
                Juego.tablero[i][j] = new Dama(true);
            }
            if (c == 'R') {
                Juego.tablero[i][j] = new Torre(true);
            }
            if (c == 'B') {
                Juego.tablero[i][j] = new Alfil(true);
            }
            if (c == 'N') {
                Juego.tablero[i][j] = new Caballo(true);
            }
            if (c == 'P') {
                Juego.tablero[i][j] = new Peon(true);
            }

            if (Character.isDigit(c)) {
                j += Integer.parseInt(String.valueOf(c));
            } else {
                j++;
            }
        }

    }
}
