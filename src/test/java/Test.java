import com.wolf.carlitos.EstadoTablero;
import com.wolf.carlitos.Piezas.Caballo;
import com.wolf.carlitos.Piezas.Torre;
import com.wolf.carlitos.Trayectoria;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Test{



    public static void main(String... args){
        EstadoTablero estado = new EstadoTablero();

        var pieza = new Torre(true);

        var trayectoria = new Trayectoria(pieza,1,1, Trayectoria.TRAYECTORIA.Recta);
        trayectoria.piezasAtacadas.add(new Torre(true));
        estado.trayectorias.add(trayectoria);

        var t1 = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            var trayectoriaAnterior = estado.trayectorias.stream()
                    .filter(t -> t.pieza == pieza)
                    .findFirst();
        }

        var t2 = System.currentTimeMillis();

        System.out.println(t2 - t1);


        var t3 = System.currentTimeMillis();

        Trayectoria tra;

        for (int i = 0; i < 10000; i++) {
            for(var t : estado.trayectorias){
                if(t.pieza == pieza) {
                    tra = t;
                    break;
                }
            }
        }

        System.out.println(t3 -t2);



    }
}