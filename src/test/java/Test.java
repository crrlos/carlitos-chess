import com.wolf.carlitos.EstadoTablero;
import com.wolf.carlitos.Generador;
import com.wolf.carlitos.Juego;
import com.wolf.carlitos.Piezas.Pieza;
import com.wolf.carlitos.Utilidades;

import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test{

    private void Jaques(){

        String[] fens = {

                "1k6/8/8/8/1K1q4/8/8/8 w - -", // jaque dama
                "1k6/8/8/8/1K6/8/1r6/8 w - -", // jaque torre
                "1k6/8/2n5/8/1K6/8/8/8 w - -", // jaque caballo
                "1k3b2/8/8/8/1K6/8/8/8 w - -", // jaque alfil
                "1k6/8/8/p7/1K6/8/8/8 w - -", // jaque peon
        };


        for(var fen : fens){
            Juego j = new Juego();

            j.setFen(fen);

            var r = Utilidades.reyEnJaque(j.tablero,j.estadoTablero);

            if(r == null) throw new IllegalStateException(" rey no en jaque");
        }

    }

    private void TorreSinMovimientos(){
        String[] fens = {

                "6k1/8/8/8/KR5r/8/8/8 w - - 0 1"
        };
        Juego j = new Juego();

        j.setFen(fens[0]);

        var res = Utilidades.movimientoValido(new int[]{3,1,3,1},j.tablero,j.estadoTablero);

        System.out.println(res);


    }

    private  void JaquesAlfil(){
        Juego j = new Juego();

        j.setFen("2b1k3/8/8/8/Q7/8/8/8 b - - 0 1");
        Generador.piezaJaque = new int[]{3,0};
        var result = Generador.movimientosDeAlfil(j.tablero,j.estadoTablero,7,2);

        System.out.println(result.size());
    }

    public static void main(String... args)  {

        var a1 = new Pieza[64];
        var a2 = new Pieza[8][8];

        var t1 = System.currentTimeMillis();

        for (int i = 0; i < 5_000_000; i++) {

//            for (int j = 0; j < a1.length; j++) {
//                var b = a1[j];
//            }

//            for (int j = 0; j < 8; j++) {
//                for (int k = 0; k < 8; k++) {
//                    var b = a2[j][k];
//                }
//            }

        }

        System.out.println(System.currentTimeMillis() - t1);





    }
}