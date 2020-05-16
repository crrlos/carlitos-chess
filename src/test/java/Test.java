import static com.wolf.carlitos.Bitboard.*;
import com.wolf.carlitos.Juego;
import com.wolf.carlitos.Utilidades;

import java.util.Arrays;
import java.util.Scanner;

import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Tablero.piezas;
import static java.lang.Long.bitCount;
import static java.lang.Long.numberOfTrailingZeros;

public class Test {


    public static void main(String[] args) {
        add(true,PEON,A3);
        if(bitCount(piezas[0][PEON]) != 1 || numberOfTrailingZeros(piezas[0][PEON]) != A3){
            throw  new IllegalStateException();
        }
        remove(true,PEON,A3);
        if(bitCount(piezas[0][PEON]) != 0 || numberOfTrailingZeros(piezas[0][PEON]) != 64){
            throw  new IllegalStateException();
        }
        add(true,REY,H8);
        add(true,REY,G8);
        add(true,REY,F8);
        add(true,REY,E8);
        if(bitCount(piezas[0][REY]) != 4 || numberOfTrailingZeros(piezas[0][REY]) != E8){
            throw  new IllegalStateException();
        }
        update(true,REY,E8,A3);
        if(bitCount(piezas[0][REY]) != 4 || numberOfTrailingZeros(piezas[0][REY]) != A3){
            throw  new IllegalStateException();
        }

    }
}