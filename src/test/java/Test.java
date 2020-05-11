import com.wolf.carlitos.Juego;

import java.util.Scanner;

public class Test {


    public static void main(String[] args) {
        Scanner lector = new Scanner(System.in);
        System.out.println("Escriba una frase");
        String cadena = lector.next();

        StringBuilder caracteres = new StringBuilder();

        var t = System.currentTimeMillis();


        Juego j = new Juego();
        j.perft(6);
        j.perft(6);

        System.out.println(System.currentTimeMillis() - t);

        for (int i = 0; i < cadena.length(); i++) {
            if(caracteres.toString().indexOf(cadena.charAt(i)) == -1){
                System.out.println("Caracter: " + cadena.charAt(i));
                break;
            }

        }


    }
}