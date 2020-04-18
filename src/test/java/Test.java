


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


 class pruebas04 {

    static InputStreamReader entrada = new InputStreamReader(System.in);
    static BufferedReader br = new BufferedReader(entrada);

    public  void main () throws Exception{
        //@FranBarcos

        String base  = "";
        String archivos [] = {"archivo1","archivo2","archivo3", "archivo4"};
        int num=pideEntero("Indique el Nº de Archivo a Borrar: ");
        num--;


        File borrado = new File (base + archivos[num]);
        borrado.delete();
        System.out.print("El archivo seleccionado es: "+borrado+"\n");
        System.out.print("El Archivo "+borrado+" se ha Eliminado con Éxito!!.\n");

        for(int i=0; i<archivos.length;i++) {
            System.out.println(archivos[i]);
        }//end for


    }//end main

    static public int pideEntero(String text) throws Exception{




        int num=0;
        boolean correcto;

        do {correcto=true;
            try {
                System.out.print(text);
                num=Integer.parseInt(br.readLine());
            }//end try
            catch(Exception er) {
                System.out.print("ERROR!, ");
                correcto=false;
            }//end catch
        }while(!correcto);
        return num;
    }//end pideEntero

}//end class

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

    public static void main (String ... args) throws Exception {

        pruebas04 ere = new pruebas04();
        ere.main();

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