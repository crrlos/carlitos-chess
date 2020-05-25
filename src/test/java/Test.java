import com.wolf.carlitos.*;

import java.util.Arrays;
import java.util.Random;

import static com.wolf.carlitos.Bitboard.next;
import static com.wolf.carlitos.Bitboard.remainder;
import static com.wolf.carlitos.Constantes.*;

public class Test {


    public static void main(String[] args) {


        int[] arreglo = new int[]{1,2,3,4,5,6};
       // Utilidades.insertionSort(arreglo,6);

        System.out.println(500 | 100);

//        for (int i = A2; i <= H7; i++) {
//            System.out.print("movimientos pos " + i + ": ");
//            for (long squares = ataquePeon[NEGRO][i]; squares != 0; squares = remainder(squares)) {
//                int square = next(squares);
//
//                System.out.print(convertirANotacion(i << 6 | square) + " ");
//            }
//            System.out.println();
//        }


//        Ataque.iniciar();
//        Generador g = new Generador();
//
//        Juego ju = new Juego();
//        ju.setFen("rnbqkbnr/pppp1ppp/8/4p3/3P4/8/PPP1PPPP/RNBQKBNR w KQkq e6 0 2");
//
//        var t1 = System.currentTimeMillis();
//        int pieza = 0;
//
//
//        System.out.println(pieza);
//        System.out.println(System.currentTimeMillis() - t1);


//        var maskk = 0b101110_00010000_00010000_00010000_00000000_00000000_00010000_00000000L;
//        var magic = Ataque._rookMagics[E8];
//        var key = maskk * magic >>> 53;
//
//        System.out.println(Long.toBinaryString(Ataque.ataqueTorre[E8][(int) key]));
//
//
//
//        long[] valores = new long[4096];
//        Random rand = new Random();
//        int posicion = 0;
//        long magicPrueba = 0xa8002c000108020L;
//        int bits = 52;
//        int l = 4096;
//        int[] rookMask = new int[64];
//
////        rookMask[1] = 1;
////        rookMask[2] = 1;
////        rookMask[3] = 1;
////        rookMask[4] = 1;
////        rookMask[5] = 1;
////        rookMask[6] = 1;
////        rookMask[9] = 1;
////        rookMask[17] = 1;
////        rookMask[25] = 1;
////        rookMask[33] = 1;
////        rookMask[41] = 1;
////        rookMask[49] = 1;
//        rookMask[1] = 1;
//        rookMask[2] = 1;
//        rookMask[3] = 1;
//        rookMask[4] = 1;
//        rookMask[5] = 1;
//        rookMask[6] = 1;
//        rookMask[8] = 1;
//        rookMask[16] = 1;
//        rookMask[24] = 1;
//        rookMask[32] = 1;
//        rookMask[40] = 1;
//        rookMask[48] = 1;
//
//        int total = 0;
//
//        int[] blocked = new int[64];
//
//        while (total < l) {
//
//            Arrays.fill(blocked, -1);
//
//            for (int i = 0; i < rookMask.length; i++) {
//                if (rookMask[i] == 1)
//                    blocked[i] = rand.nextInt(2);
//            }
//            long ataque = 0;
//            long bloqueadores = 0;
//
//            for (int i = 0; i < Tablero.offsetMailBox[TORRE].length; i++) {
//                boolean limite = false;
//
//                int pos = posicion;
//
//                while (mailBox[direccion[pos] + offsetMailBox[TORRE][i]] != -1) {
//                    pos += offset64[TORRE][i];
//
//                    if (!limite) {
//                        ataque |= 1L << pos;
//                    }
//
//                    if (blocked[pos] == 1)
//                        bloqueadores |= 1L << pos;
//
//                    if (blocked[pos] == 1) limite = true;
//
//                }
//
//            }
//
//            long res = bloqueadores * magicPrueba;
//
//            int index = (int) (res >>> bits);
//
//
//            if (valores[index] == 0) {
//                ++total;
//                valores[index] = ataque;
//                //System.out.println("generado " + total);
//            }
//
//        }
////        System.out.println("pruebas iniciadas");
////
////        long mask = 0b0100000;
////        mask = mask * magic;
////       System.out.println(Long.toBinaryString(valores[(int) (mask >>> 52)]));
//        System.out.println("iniciando pruebas");
//
//
//        for (int j = 0; j < 1_0000000; j++) {
//            Arrays.fill(blocked, -1);
//
//            // crear blockers aleatorios para la mascara de la torre en A1
//            for (int i = 0; i < rookMask.length; i++) {
//                if (rookMask[i] == 1)
//                    blocked[i] = rand.nextInt(2);
//            }
//
//            long ataque = 0;
//            long bloqueadores = 0;
//
//            for (int i = 0; i < Tablero.offsetMailBox[TORRE].length; i++) {
//                boolean limite = false;
//
//                int pos = posicion;
//
//                while (mailBox[direccion[pos] + offsetMailBox[TORRE][i]] != -1) {
//                    pos += offset64[TORRE][i];
//
//                    if (!limite) {
//                        ataque |= 1L << pos;
//                    }
//
//                    if (blocked[pos] == 1)
//                        bloqueadores |= 1L << pos;
//
//                    if (blocked[pos] == 1) limite = true;
//
//                }
//
//            }
//
//            long res = bloqueadores * magicPrueba;
//
//            int index = (int) (res >>> bits);
//
//            if (Ataque.ataqueTorre[posicion][index] != ataque) {
//                System.out.println("no coincide");
//
//                System.out.println(Long.toBinaryString(Ataque.ataqueTorre[posicion][index]));
//                System.out.println(Long.toBinaryString(ataque));
//                System.out.println("indice " + index);
//            }
//
//        }


    }
}