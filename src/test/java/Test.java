import com.wolf.carlitos.EstadoTablero;

public class Test{



    public static void main(String... args) throws CloneNotSupportedException {

        long t1;
        long t2;
        EstadoTablero e = new EstadoTablero();
        t1 = System.currentTimeMillis();


        for (int i = 0; i < 100000000; i++) {
            var d = e.clone();
        }

        System.out.println(System.currentTimeMillis() - t1);




    }
}