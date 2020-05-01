package com.wolf.carlitos;

import java.util.Scanner;

public class App {
    static Juego juego = new Juego();

    public static void main(String[] args) throws Exception {


        if (Config.debug) {
            new Hilos();
        }

        var scanner = new Scanner(System.in);


        while (scanner.hasNext()) {
            var linea = scanner.nextLine();
            if (linea.contains("position startpos")) {
                if (linea.contains("moves")) {
                    var movimientos = linea.replaceAll("position startpos moves ", "").split(" ");
                    juego = new Juego();
                    juego.establecerPosicion(movimientos);
                    Utilidades.imprimirPosicicion(juego.tablero);
                }
            }
//            else if (linea.contains("go")) {
//                int n = 1;
//                try {
//
//                    n= Integer.parseInt(linea.replaceAll("go ", ""));
//
//                } catch (Exception e) {
//                }
//                System.out.println("bestmove " + juego.mover(n));
//
//            } else if (linea.contains("isready")) {
//                System.out.println("readyok");
//            } else if (linea.contains("ucinewgame")) {
//                juego = new Juego();
//                System.out.println("uciok");
//            } else if (linea.contains("uci")) {
//                System.out.println("uciok");
//            } else if (linea.contains("stop")) {
//                System.out.println("readyok");
//
            else if (linea.contains("fen")) {
                juego.setFen(linea);
            } else if (linea.contains("perft")) {
                int n = 1;

                try {
                    n = Integer.parseInt(linea.replace("perft ", ""));
                } catch (NumberFormatException ex) {
                }

                juego.perft(n);
            }

        }
        scanner.close();
    }


}
