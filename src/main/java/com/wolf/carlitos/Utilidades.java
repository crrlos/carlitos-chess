/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;


import java.util.HashMap;

import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Pieza.valorPiezas;
import static com.wolf.carlitos.Search.history;

/**
 * @author carlos
 */
public class Utilidades {

    private static final HashMap<String, Integer> casillaPosicion = new HashMap<>();
    public static final HashMap<Integer, String> posicionCasilla = new HashMap<>();

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

    public static Movimiento convertirAPosicion(String movimiento) {
        Movimiento posicion = new Movimiento();

        var inicio = movimiento.substring(0, 2);
        var destino = movimiento.substring(2, 4);

        posicion.inicio =  casillaPosicion.get(inicio);
        posicion.destino =  casillaPosicion.get(destino);


        if (movimiento.length() == 5) {
            switch (movimiento.charAt(4)) {
                case 'q':
                    posicion.promocion = 1;
                    break;
                case 'r':
                    posicion.promocion = 2;
                    break;
                case 'n':
                    posicion.promocion = 3;
                    break;
                case 'b':
                    posicion.promocion = 4;
                    break;
            }
        }

        return posicion;
    }

    public static String convertirANotacion(Movimiento movimiento) {

        var mov = posicionCasilla.get(movimiento.inicio) + posicionCasilla.get(movimiento.destino);

        if (movimiento.promocion > 0) {
            switch (movimiento.promocion) {
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

        System.out.println(formato);
    }

    public static void establecerPuntuacion(Movimiento[] movimientos, int fin,Tablero tab) {

        for (int i = 0; i < fin; i++) {
            int inicio = movimientos[i].inicio;
            int destino = movimientos[i].destino;
            int ponderacion = 100_000_000;

            // si es captura usar MVVLVA, con offset de 100k para se coloque antes de una no captura
            if(tab.tablero[destino] != NOPIEZA){
                ponderacion += valorPiezas[REY] / valorPiezas[tab.tablero[inicio]] + 10 * valorPiezas[tab.tablero[destino]];
                movimientos[i].ponderacion = ponderacion;
                continue;
            }
            // si no es captura usar history heuristic

                ponderacion = history[inicio][destino];
                movimientos[i].ponderacion =  ponderacion;

        }

    }

    public static void insertionSort(Movimiento[] array, int fin) {
        for (int j = 1; j < fin; j++) {
            Movimiento key = array[j];
            int i = j - 1;
            while ((i > -1) && (array[i].ponderacion < key.ponderacion)) {
                array[i + 1] = array[i];
                i--;
            }
            array[i + 1] = key;
        }
    }


}
