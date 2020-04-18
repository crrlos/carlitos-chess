
import com.wolf.carlitos.Utilidades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test
{
     class Prueba implements Cloneable{
        public List<String> lista = new ArrayList<>();

        @Override
        protected Prueba clone() throws CloneNotSupportedException {
            var clon = (Prueba) super.clone();
            clon.lista = new ArrayList<>();
            clon.lista.addAll(lista);
            return  clon;
        }
    }

    public static void main (String ... args) throws CloneNotSupportedException {

         var test = new
                 Test();

         test.probar();

    }

    public void probar() throws CloneNotSupportedException {


        Prueba p = new Prueba();

        p.lista.add("hola");
        p.lista.add("adios");


        System.out.println(p.lista.size());

        var clon = p.clone();

        clon.lista.remove(0);

        System.out.println(clon.lista.size());

        System.out.println(p.lista.size());

    }
}