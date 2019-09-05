package app;

import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
       var scanner = new Scanner(System.in);
       var juego = new Juego();
       juego.MovimientosValidos();

        while(scanner.hasNext()){
            var linea = scanner.nextLine();
            if(linea.contains("position")){
                var movimientos =  linea.replaceAll("position startpos moves ", "").split(" ");
                juego.EstablecerPosicion(movimientos);
                juego.ImprimirPosicicion();
                juego.MovimientosValidos();
            }
        }
        scanner.close();
    }
}