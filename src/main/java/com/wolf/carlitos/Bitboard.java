package com.wolf.carlitos;

import static com.wolf.carlitos.Constantes.BLANCO;
import static com.wolf.carlitos.Constantes.NEGRO;
import static com.wolf.carlitos.Tablero.piezas;
import static java.lang.Long.*;

public final class Bitboard {

    private Bitboard() {
    }

    public static void  add(boolean esBlanco, int pieza, int posicion) {
        int color = esBlanco ? BLANCO : NEGRO;
        long bitboard = piezas[color][pieza];
        piezas[color][pieza] = bitboard | 1L << posicion;
    }


    public static void remove(boolean esBlanco, int pieza, int posicion) {
        int color = esBlanco ? BLANCO : NEGRO;
        long bitboard = piezas[color][pieza];
        piezas[color][pieza] = bitboard & ~(1L << posicion);
    }
    public static  void update(boolean esBlanco, int pieza, int inicio, int destino){
        int color = esBlanco ? BLANCO : NEGRO;
        long bitboard = piezas[color][pieza];
        piezas[color][pieza] = (bitboard & ~(1L << inicio)) | 1L << destino;

    }

    static int next(long bitboard) {
        return numberOfTrailingZeros(bitboard);
    }

    static long remainder(long bitboard) {
        return bitboard & (bitboard - 1);
    }

}
