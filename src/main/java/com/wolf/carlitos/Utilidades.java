/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;


import java.util.HashMap;

import static com.wolf.carlitos.Bitboard.*;
import static com.wolf.carlitos.Juego.tablero;
import static com.wolf.carlitos.Search.secuencia;
import static com.wolf.carlitos.Tablero.*;
import static java.lang.Long.numberOfTrailingZeros;
import static java.lang.Math.abs;
import static com.wolf.carlitos.Constantes.*;

/**
 * @author carlos
 */
public class Utilidades {

    private static final HashMap<String, Integer> casillaPosicion = new HashMap<>();
    private static final HashMap<Integer, String> posicionCasilla = new HashMap<>();

    static {
        int correlativo = 0;

        for (int i = 1; i <= 8; i++) {
            for (char c : "abcdefgh".toCharArray()) {
                casillaPosicion.put(String.valueOf(c) + i, correlativo);
                posicionCasilla.put(correlativo++, String.valueOf(c) + i);
            }
        }


    }

    public static void imprimirPosicicion(int[] tablero, int[] color) {

        for (int i = 64; i > 0; i -= 8) {
            System.out.println("+---+---+---+---+---+---+---+---+");
            for (int j = i - 8; j < (i - 8) + 8; j++) {
                System.out.print("| " + (color[j] == NOCOLOR ? "." : (color[j] == BLANCO ? String.valueOf(PIEZAS[tablero[j]]) : String.valueOf(PIEZAS[tablero[j]]).toLowerCase())) + " ");
            }
            System.out.print("|");

            System.out.println();
        }
        System.out.println("+---+---+---+---+---+---+---+---+");
    }

    public static int convertirAPosicion(String movimiento) {
        int posicion = 0;

        var inicio = movimiento.substring(0, 2);
        var destino = movimiento.substring(2, 4);

        posicion |= casillaPosicion.get(inicio) << 6;
        posicion |= casillaPosicion.get(destino);


        if (movimiento.length() == 5) {
            switch (movimiento.charAt(4)) {
                case 'q':
                    posicion |= 1 << 12;
                    break;
                case 'r':
                    posicion |= 2 << 12;
                    break;
                case 'n':
                    posicion |= 3 << 12;
                    break;
                case 'b':
                    posicion |= 4 << 12;
                    break;
            }
        }

        return posicion;
    }

    public static String convertirANotacion(int movimiento) {

        var mov = posicionCasilla.get(movimiento >> 6 & 0b111111) + posicionCasilla.get(movimiento & 0b111111);

        if (movimiento >> 12 > 0) {
            switch (movimiento >> 12 & 0b111) {
                case 1:
                    return mov + "q";
                case 2:
                    return mov + "r";
                case 3:
                    return mov + "n";
                case 4:
                    return mov + "b";
            }
        }
        return mov;
    }

    public static int casillaANumero(String casilla) {
        int columna = "abcdefgh".indexOf(casilla.charAt(0));
        int fila = "12345678".indexOf(casilla.charAt(1));
        return columna + 8 * fila;
    }

    public static void imprimirBinario(int estadoTablero) {
        String formateado = Integer.toBinaryString(estadoTablero);
        String formato = "";
        formato = formateado.substring(formateado.length() - 2);
        formato = formateado.substring(formateado.length() - 4, formateado.length() - 2) + "_" + formato;
        formato = formateado.substring(formateado.length() - 5, formateado.length() - 4) + "_" + formato;
        formato = formateado.substring(formateado.length() - 11, formateado.length() - 5) + "_" + formato;
        formato = formateado.substring(formateado.length() - 14, formateado.length() - 11) + "_" + formato;
        formato = formateado.substring(formateado.length() - 17, formateado.length() - 14) + "_" + formato;
        formato = formateado.substring(formateado.length() - 23, formateado.length() - 17) + "_" + formato;
        formato = formateado.substring(0, formateado.length() - 23) + "_" + formato;

        System.out.println(formato);
    }

    public static void puntajeMVVLVA(int[] movimientos, int fin) {

        for (int i = 0; i < fin; i++) {
            int inicio = movimientos[i] >> 6 & 0b111111;
            int destino = movimientos[i] & 0b111111;

            movimientos[i] |= (valorPiezas[REY] / valorPiezas[tablero[inicio]] + 10 * valorPiezas[tablero[destino]]) << 15;
        }

    }

    public static void insertionSort(int[] array, int fin) {
        for (int j = 1; j < fin; j++) {
            int key = array[j];
            int i = j - 1;
            while ((i > -1) && (array[i] >>> 15 < key >>> 15)) {
                array[i + 1] = array[i];
                i--;
            }
            array[i + 1] = key;
        }
    }


}
