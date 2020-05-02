import com.wolf.carlitos.Config;
import com.wolf.carlitos.Generador;
import com.wolf.carlitos.Juego;
import com.wolf.carlitos.Piezas.Caballo;
import com.wolf.carlitos.Piezas.Torre;
import com.wolf.carlitos.Utilidades;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    public static  void main(String... args){

        System.out.println(Utilidades.casillaANumero("a1"));
        System.out.println(Utilidades.casillaANumero("a8"));
        System.out.println(Utilidades.casillaANumero("h8"));
        System.out.println(Utilidades.casillaANumero("h1"));

        System.out.println(Generador.movimientosCaballo.get(B1));

        var array = new ArrayList<Integer>();

        var pieza = Config.debug ? 1: 2;

        var t1 = System.currentTimeMillis();


        for (int i = 0; i < 1_000_000; i++) {

            if(pieza == 1){
                var dato = "abc";
            }

        }
        System.out.println(System.currentTimeMillis() -t1);


    }
}