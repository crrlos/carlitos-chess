import static com.wolf.carlitos.Bitboard.*;
import com.wolf.carlitos.Juego;
import com.wolf.carlitos.Utilidades;

import java.util.Arrays;
import java.util.Scanner;

import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Tablero.piezas;
import static com.wolf.carlitos.Tablero.valorPiezas;
import static java.lang.Long.*;

public class Test {


    public static void main(String[] args) {
       int r = (valorPiezas[REY] / valorPiezas[PEON]) + (10 * 900);
        System.out.println(Integer.toBinaryString(r));
    }
}