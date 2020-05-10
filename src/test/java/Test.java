import com.wolf.carlitos.Juego;

public class Test{



    public static  void main(String... args) {

        var t = System.currentTimeMillis();


        Juego j = new Juego();
        j.perft(6);

        System.out.println(System.currentTimeMillis() - t);



    }
}