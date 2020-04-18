import java.util.ArrayList;
import java.util.stream.Collectors;

public class Test{



    public static void main(String... args){

        var numeros = new ArrayList<Integer>();
        for (int i = 0; i < 10000000; i++) {
            numeros.add(i);
        }

       var t1 =  System.currentTimeMillis();
        var pares = numeros.stream().filter(n -> n % 2 == 0).collect(Collectors.toList());
        var t2 = System.currentTimeMillis();
        System.out.println(t2- t1);

        var pares2 =  numeros.parallelStream().filter(n -> n % 2 == 0).collect(Collectors.toList());

        System.out.println(System.currentTimeMillis() - t2);





    }
}