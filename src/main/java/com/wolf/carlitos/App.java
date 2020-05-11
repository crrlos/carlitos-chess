package com.wolf.carlitos;

import java.util.Scanner;
import java.util.regex.Pattern;
public class App {
    static Juego juego = new Juego();

    public static void main(String[] args) throws Exception {

        int fuerza = 0;


        if (Config.debug) {
            //new Hilos();
        }

        var scanner = new Scanner(System.in);


        while (scanner.hasNext()) {
            var linea = scanner.nextLine();
            if(linea.contains("fuerza")){
                fuerza = Integer.parseInt(linea.replace("fuerza",""));
            }

            if(linea.contains("fen")){
                var fen = linea.split("fen")[1].split("moves")[0].trim();

                juego = new Juego();
               // juego.setFen(fen);

                var regex = Pattern.compile("(([a-h][1-8]){2}([qrbn])?)");
                var movimientos = regex.matcher(linea).results().map(r -> r.group(0)).toArray(String[]::new);
                //juego.establecerPosicion(movimientos);
            }
            else
            if (linea.contains("startpos")) {
                juego = new Juego();

                var regex = Pattern.compile("(([a-h][1-8]){2}([qrbn])?)");
                var movimientos = regex.matcher(linea).results().map(r -> r.group(0)).toArray(String[]::new);
                juego.establecerPosicion(movimientos);

            } else if (linea.contains("go")) {
               // System.out.println("bestmove " + juego.mover(fuerza == 0? 4 : fuerza));

            } else if (linea.contains("isready")) {
                System.out.println("readyok");
            } else if (linea.contains("ucinewgame")) {
                juego = new Juego();
                System.out.println("uciok");
            } else if (linea.contains("uci")) {
                System.out.println("uciok");
            } else if (linea.contains("stop")) {
                System.out.println("readyok");
            }
            else if (linea.contains("perft")) {
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
