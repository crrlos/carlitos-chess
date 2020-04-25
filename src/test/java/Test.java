import com.wolf.carlitos.EstadoTablero;
import com.wolf.carlitos.Generador;
import com.wolf.carlitos.Juego;
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

                "8/2p4k/3p3r/KP6/4Pp2/7R/6P1/8 b - - 0 1"
        };
        Juego j = new Juego();

        j.setFen(fens[0]);

        var pieza  = Utilidades.reyEnJaque(j.tablero,j.estadoTablero);

        //if(pieza == null) throw new IllegalStateException("");

        var d = Generador.movimientosDeTorre(j.tablero,j.estadoTablero,5,7);

        System.out.println(d.size());
        d.stream().map(Utilidades::convertirANotacion)
                .forEach(s -> System.out.print(s + " "));


    }

    public static void main(String... args)  {


    Test t = new Test();
   // t.Jaques();
    t.TorreSinMovimientos();





    }
}