import com.wolf.carlitos.EstadoTablero;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test{



    public static void main(String... args) throws CloneNotSupportedException {

            double dolar = 10;
            double centavos = 50;

            double dolar2 = 3;
            double centavos2 = 50;

            var total1 = dolar + centavos /100;
            var total2 = dolar2 + centavos2 /100;

            var resultado = total1 - total2;

            var entero = (int) resultado;

            var decimal = (resultado - entero) * 100;




        System.out.println(entero + " " + decimal);

    }
}