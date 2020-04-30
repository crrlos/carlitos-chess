import com.wolf.carlitos.Generador;
import com.wolf.carlitos.Piezas.Caballo;

import java.util.ArrayList;

import static com.wolf.carlitos.Piezas.Casillas.*;

public class Test{

    public static  int movimientosAlfil(int fila, int columna){

        var lista = new ArrayList<int[]>();
                if (fila + 2 < 8) {
            if (columna + 1 < 8) {
                lista.add(new int[]{fila, columna, fila + 2, columna + 1});
            }
            if (columna - 1 >= 0) {
                lista.add(new int[]{fila, columna, fila + 2, columna - 1});
            }
        }

        if (fila - 2 >= 0) {
            if (columna + 1 < 8) {
                lista.add(new int[]{fila, columna, fila - 2, columna + 1});
            }
            if (columna - 1 >= 0) {
                lista.add(new int[]{fila, columna, fila - 2, columna + 1});
            }
        }

        if (columna - 2 >= 0) {
            if (fila + 1 < 8) {
                lista.add(new int[]{fila, columna, fila - 2, columna + 1});
            }
            if (fila - 1 >= 0) {
                lista.add(new int[]{fila, columna, fila - 2, columna + 1});
            }
        }
        if (columna + 2 < 8) {
            if (fila + 1 < 8) {
                lista.add(new int[]{fila, columna, fila - 2, columna + 1});
            }
            if (fila - 1 >= 0) {
                lista.add(new int[]{fila, columna, fila - 2, columna + 1});
            }
        }
        return lista.size();


    }

    private  static int alfil1(int pos){

        int total = 0;

        var movs = Generador.movimientosCaballo.get(pos);

        total = movs.size();
        return total;

    }

    public static  void main(String... args){
        var t1 = System.currentTimeMillis();
        long total = 0;

        var movimientosCaballo = Generador.movimientosCaballo.get(E4);
       for (int i = 0; i < 5_000_000; i++) {
//            for (int l = 0; l < 8; l++) {
//                for (int j = 0; j < 8; j++) {
//                    total += movimientosAlfil(l,j);
//                }
//            }

//            for (int j = 0; j < 64; j++) {
//                total += alfil1(j);
//            }


           for (Integer m : movimientosCaballo) {
               var d = m;
           }

        }

        System.out.println(System.currentTimeMillis() -t1);
        System.out.println(total);


    }
}