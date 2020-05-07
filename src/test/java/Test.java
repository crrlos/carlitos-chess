import com.wolf.carlitos.Generador;
import com.wolf.carlitos.Juego;

import java.io.IOException;
import java.util.*;

import static com.wolf.carlitos.Constantes.*;
import static java.lang.Math.abs;

public class Test{

    public static  int movimientosAlfil(int fila, int columna){

        var lista = new ArrayList<int[]>();

        if (fila + 1 < 8) {

            lista.add(new int[]{fila, columna, fila + 1, columna});

            lista.add(new int[]{fila, columna, fila + 1, columna + 1});

            lista.add(new int[]{fila, columna, fila + 1, columna - 1});

        }
        if (fila - 1 >= 0) {

            lista.add(new int[]{fila, columna, fila - 1, columna});

            lista.add(new int[]{fila, columna, fila - 1, columna + 1});

            lista.add(new int[]{fila, columna, fila - 1, columna - 1});

        }

        if (columna + 1 < 8) {
            lista.add(new int[]{fila, columna, fila, columna + 1});

        }
        if (columna - 1 >= 0) {
            lista.add(new int[]{fila, columna, fila, columna - 1});

        }

        return lista.size();


    }

    private  static int alfil1(int pos){

        int total = 0;

        var movs = Generador.movimientosRey.get(pos);

        total = movs.size();
        return total;

    }

    public static  void main(String... args) throws IOException, CloneNotSupportedException {

      Scanner entrada = new Scanner(System.in);
        System.out.println("Ingrese el nombre del producto: ");
        String nombre = entrada.nextLine();
        entrada.nextLine();
        System.out.println("Ingrese la descripción del producto: ");
        String descrip = entrada.nextLine();

        System.out.println(nombre + descrip);
//
//        var array = new ArrayList<Integer>();
//
//        var pieza = Config.debug ? 1: 2;
//
//        var lista = new ArrayList<Integer>();
//
//        for (int i = 0; i < 16; i++) {
//            lista.add(i);
//        }
//
//        var lista2 = new ArrayList<List<Integer>>();
//
//        for (int i = 0; i < 4; i++) {
//            lista = new ArrayList<>();
//            for (int j = 0; j < 4; j++) {
//                lista.add(j);
//            }
//            lista2.add(lista);
//        }

        var t1 = System.currentTimeMillis();
//
//        for (int i = 0; i < 1_000_000; i++) {
//            for (int j = 0; j < lista2.size(); j++) {
//                var l = lista2.get(j);
//                for (int k = 0; k < l.size(); k++) {
//                    var d = l.get(k);
//                }
//            }
//        }
        Juego j = new Juego();
        //j.setFen("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - ");
        j.perft(6);


        System.out.println(System.currentTimeMillis() -t1);


    }
}