import com.wolf.carlitos.Generador;

import java.util.ArrayList;

import static com.wolf.carlitos.Constantes.*;

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
        var t1 = System.currentTimeMillis();
        long total = 0;

        var movimientosCaballo = Generador.movimientosCaballo.get(E4);
       for (int i = 0; i < 1; i++) {
            for (int l = 0; l < 8; l++) {
                for (int j = 0; j < 8; j++) {
                    total += movimientosAlfil(l,j);
                }
            }

//            for (int j = 0; j < 64; j++) {
//                total += alfil1(j);
//            }


        }

        System.out.println(System.currentTimeMillis() -t1);
        System.out.println(total);


    }
}