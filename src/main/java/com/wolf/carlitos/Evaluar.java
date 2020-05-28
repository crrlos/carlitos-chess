package com.wolf.carlitos;

import static com.wolf.carlitos.Bitboard.next;
import static com.wolf.carlitos.Bitboard.remainder;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Pieza.bitboard;
import static com.wolf.carlitos.Pieza.valorPiezas;
import static com.wolf.carlitos.Ponderaciones.*;
import static java.lang.Long.bitCount;

public class Evaluar {
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

        for (long squares = bitboard[bando][PEON]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            total += esBlanco ? ponderacionPeon[flip[square]] : ponderacionPeon[square];
        }
        for (long squares = bitboard[bando][CABALLO]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            total += esBlanco ? ponderacionCaballo[flip[square]] : ponderacionCaballo[square];
        }
        for (long squares = bitboard[bando][ALFIL]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            total += esBlanco ? ponderacionAlfil[flip[square]] : ponderacionAlfil[square];
        }
        for (long squares = bitboard[bando][TORRE]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            total += esBlanco ? ponderacionTorre[flip[square]] : ponderacionTorre[square];
        }
        for (long squares = bitboard[bando][DAMA]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            total += esBlanco ? ponderacionDama[flip[square]] : ponderacionDama[square];
        }
        for (long squares = bitboard[bando][REY]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            total += esBlanco ? ponderacionRey[flip[square]] : ponderacionRey[square];
        }

        return total;
    }

    public static int evaluar(int color) {
        Search.nodes++;
        int valorPiezas = evaluarCantidadPiezas(color) - evaluarCantidadPiezas(color ^ 1);
        int valorPosicion = evaluarPosicionDePiezas(color) - evaluarPosicionDePiezas(color ^ 1);
        return valorPiezas + valorPosicion;

    }
}
