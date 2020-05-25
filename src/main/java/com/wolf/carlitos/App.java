package com.wolf.carlitos;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.regex.Pattern;
public class App {
    static Juego juego = new Juego();

    public static void main(String[] args) throws Exception {

        var scanner = new Scanner(System.in);

        Ataque.iniciar();

        while (scanner.hasNext()) {

            var linea = scanner.nextLine();


            if(linea.contains("fen")){
                var fen = linea.split("fen")[1].split("moves")[0].trim();
                juego = new Juego();
                juego.setFen(fen);

                if(linea.contains("moves")){
                    var regex = Pattern.compile("(([a-h][1-8]){2}([qrbn])?)");
                    var movimientos = regex.matcher(linea).results().map(r -> r.group(0)).toArray(String[]::new);
                    juego.establecerPosicion(movimientos);
                }
            }
            else
            if (linea.contains("startpos")) {
                juego = new Juego();

                var regex = Pattern.compile("(([a-h][1-8]){2}([qrbn])?)");
                var movimientos = regex.matcher(linea).results().map(r -> r.group(0)).toArray(String[]::new);
                juego.establecerPosicion(movimientos);

            } else if (linea.contains("go")) {
               System.out.println("bestmove " + juego.mover(8));

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
            else if(linea.contains("static")){
                juego.evaluarPosicion();
            }
            else if (linea.contains("perft")) {
                int n = 1;

               var patter = Pattern.compile("\\d+");
               var matcher = patter.matcher(linea);

               if(matcher.find()){
                   n = Integer.parseInt(matcher.group(0));
               }

                var t = System.currentTimeMillis();
                juego.perft(n);

                System.out.println(System.currentTimeMillis() - t);
            }

        }
        scanner.close();
    }


}
