package com.wolf.carlitos;

import static com.wolf.carlitos.Bitboard.next;
import static com.wolf.carlitos.Bitboard.remainder;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Pieza.*;
import static com.wolf.carlitos.Ponderaciones.*;
import static java.lang.Long.bitCount;

public class Evaluar {

    private static long[][] bitboard;

    private static int evaluarCantidadPiezas(int bando) {
        int total = 0;
        for (int i = 0; i < bitboard[bando].length; i++) {
            total += bitCount(bitboard[bando][i]) * valorPiezas[i];
        }
        return total;
    }

    private static int evaluarPosicionDePiezas(int bando) {
        int total = 0;
        boolean esBlanco = bando == BLANCO;

        if(esFinal()){

            for (long squares = bitboard[bando][PEON]; squares != 0; squares = remainder(squares)) {
                int square = next(squares);
                total += esBlanco ? pt2b[flip[square]] : pt2b[square];
            }
            for (long squares = bitboard[bando][CABALLO]; squares != 0; squares = remainder(squares)) {
                int square = next(squares);
                total += esBlanco ? nt2b[flip[square]] : nt2b[square];
            }
            for (long squares = bitboard[bando][ALFIL]; squares != 0; squares = remainder(squares)) {
                int square = next(squares);
                total += esBlanco ? bt2b[flip[square]] : bt2b[square];
            }
            for (long squares = bitboard[bando][REY]; squares != 0; squares = remainder(squares)) {
                int square = next(squares);
                total += esBlanco ? kt2b[flip[square]] : kt2b[square];
            }

        }else{
            for (long squares = bitboard[bando][PEON]; squares != 0; squares = remainder(squares)) {
                int square = next(squares);
                total += esBlanco ? pt1b[flip[square]] : pt1b[square];
            }
            for (long squares = bitboard[bando][CABALLO]; squares != 0; squares = remainder(squares)) {
                int square = next(squares);
                total += esBlanco ? nt1b[flip[square]] : nt1b[square];
            }
            for (long squares = bitboard[bando][ALFIL]; squares != 0; squares = remainder(squares)) {
                int square = next(squares);
                total += esBlanco ? bt1b[flip[square]] : bt1b[square];
            }
            for (long squares = bitboard[bando][REY]; squares != 0; squares = remainder(squares)) {
                int square = next(squares);
                total += esBlanco ? kt1b[flip[square]] : kt1b[square];
            }

        }

        for (long squares = bitboard[bando][TORRE]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            total += esBlanco ? rt1b[flip[square]] : rt1b[square];
        }
        for (long squares = bitboard[bando][DAMA]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            total += esBlanco ? qt1b[flip[square]] : qt1b[square];
        }

        return total;
    }

    private static boolean esFinal() {

        if (bitboard[BLANCO][DAMA] == 0 && bitboard[NEGRO][DAMA] == 0) return true;

        return bitboard[BLANCO][DAMA] > 0 && bitboard[BLANCO][TORRE] == 0 && bitboard[NEGRO][DAMA] > 0 && bitboard[NEGRO][TORRE] == 0;
    }

    public static int evaluar(Posicion pos) {
        bitboard = pos.bitboard;
        int color = pos.miColor();

        int valorPiezas = evaluarCantidadPiezas(color) - evaluarCantidadPiezas(color ^ 1);
        int valorPosicion = evaluarPosicionDePiezas(color) - evaluarPosicionDePiezas(color ^ 1);
        return valorPiezas + valorPosicion;

    }
}
