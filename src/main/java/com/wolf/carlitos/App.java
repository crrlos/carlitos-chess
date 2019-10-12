package com.wolf.carlitos;

import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
       var scanner = new Scanner(System.in);
       var juego = new Juego();
       

        while(scanner.hasNext()){
            var linea = scanner.nextLine();
            if(linea.contains("position")){
                var movimientos =  linea.replaceAll("position startpos moves ", "").split(" ");
                juego = new Juego();
                juego.EstablecerPosicion(movimientos);
                //juego.ImprimirPosicicion();
            }else if(linea.contains("go")){
                juego.MovimientosValidos();
            }else if(linea.contains("isready"))
                System.out.println("readyok");
        }
        scanner.close();
    }
}