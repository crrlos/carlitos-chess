import com.wolf.carlitos.EstadoTablero;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test{



    public static void main(String... args) throws CloneNotSupportedException {
        boolean c=false;
        Scanner entrada = new Scanner(System.in);
        int x ;
        try
        {

            while(!c)
            {
                System.out.println("Escriba 3 numeros enteros");
                if(entrada.hasNextInt())
                {
                    x=entrada.nextInt();
                    c=true;
                }
                else
                {
                    System.out.println("Que sea entero, try again");
                    entrada.nextLine();
                }
            }
        }
        catch(InputMismatchException exc)
        {
            System.out.println(exc);
        }


    }
}