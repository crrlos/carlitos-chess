import com.wolf.carlitos.EstadoTablero;
import com.wolf.carlitos.Piezas.Caballo;
import com.wolf.carlitos.Piezas.Torre;
import com.wolf.carlitos.Trayectoria;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Test{



    public static void main(String... args){

        var lista = new ArrayList<Integer>();

        lista.add(1);lista.add(2);lista.add(3);

        for (int i = 0; i < lista.size(); i++) {
            if(lista.get(i) == 1){
                lista.remove(i);
            }
        }


    }
}