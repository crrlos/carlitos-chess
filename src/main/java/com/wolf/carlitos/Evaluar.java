package com.wolf.carlitos;

import static com.wolf.carlitos.Bitboard.next;
import static com.wolf.carlitos.Bitboard.remainder;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Constantes.NEGRO;
import static com.wolf.carlitos.Ponderaciones.*;
import static com.wolf.carlitos.Tablero.piezas;
import static com.wolf.carlitos.Tablero.valorPiezas;
import static java.lang.Long.bitCount;

public class Evaluar {
    private static int evaluarCantidadPiezas(int bando) {
        int total = 0;
        for (int i = 0; i < piezas[bando].length; i++) {
            total += bitCount(piezas[bando][i]) * valorPiezas[i];
        }
        return total;
    }

    private static int evaluarPosicionDePiezas(int bando) {
        int total = 0;
        boolean esBlanco = bando == BLANCO;

        for (long squares = piezas[bando][PEON]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            total += esBlanco ? ponderacionPeon[flip[square]] : ponderacionPeon[square];
        }
        for (long squares = piezas[bando][CABALLO]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            total += esBlanco ? ponderacionCaballo[flip[square]] : ponderacionCaballo[square];
        }
        for (long squares = piezas[bando][ALFIL]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            total += esBlanco ? ponderacionAlfil[flip[square]] : ponderacionAlfil[square];
        }
        for (long squares = piezas[bando][TORRE]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            total += esBlanco ? ponderacionTorre[flip[square]] : ponderacionTorre[square];
        }
        for (long squares = piezas[bando][DAMA]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            total += esBlanco ? ponderacionDama[flip[square]] : ponderacionDama[square];
        }
        for (long squares = piezas[bando][REY]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            total += esBlanco ? ponderacionRey[flip[square]] : ponderacionRey[square];
        }

        return total;
    }

    public static int evaluar() {

        int valorBlancas = evaluarCantidadPiezas(BLANCO) + evaluarPosicionDePiezas(BLANCO);

        int valorNegras = evaluarCantidadPiezas(NEGRO) + evaluarPosicionDePiezas(NEGRO);

        return valorBlancas - valorNegras;

    }
}
