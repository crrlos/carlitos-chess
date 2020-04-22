import com.wolf.carlitos.EstadoTablero;
import com.wolf.carlitos.Piezas.Caballo;
import com.wolf.carlitos.Piezas.Torre;
import com.wolf.carlitos.Trayectoria;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Test{



    public static void main(String... args){

        var pos = "12323";

        if(!pos.matches("^[0-9]+$")){
            //mostrar mensaje
            System.out.println("naddfd");
            return;
        }

        if (!pos.chars().allMatch(Character::isDigit)) {

            // mostrar mensaje que el valor ingresado no es un numero

            return;
        }


        var lista = new ArrayList<Integer>();

        for (int i = 0; i < 100000; i++) {
            lista.add(i);
        }

        var dd = lista.toArray();
        var t1 = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < lista.size(); j++) {
                var c = dd[i];
            }
        }

        var t2 = System.currentTimeMillis();

        System.out.println(t2- t1);

        for (int i = 0; i < 10000; i++) {
            for (var d : lista.toArray()){
                var c = d;
            }
        }

        System.out.println(System.currentTimeMillis() - t2);




    }
}