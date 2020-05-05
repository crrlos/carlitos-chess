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

        System.out.println(Generador.movimientosTorre.get(G8));

        Stack<ArrayList<Integer>> e = new Stack<>();
        for (int i = 0; i < 100; i++) {
            e.add(new ArrayList<>());
        }
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
        //j.setFen("fen r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        j.perft(6);


        System.out.println(System.currentTimeMillis() -t1);


    }
}