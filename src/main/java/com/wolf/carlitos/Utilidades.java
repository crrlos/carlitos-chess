/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;


import static com.wolf.carlitos.Constantes.*;

/**
 * @author carlos
 */
public class Utilidades {


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

    public static int casillAPosicion(String posicion){
        return  "abcdefgh".indexOf(posicion.charAt(0)) + ("12345678".indexOf(posicion.charAt(1)) * 8);
    }
    public static String posicionACasilla(int posicion){
        return "abcdefgh".charAt(posicion & 7) + String.valueOf("12345678".charAt(posicion >> 3));
    }
    public static Movimiento convertirAPosicion(String movimiento) {
        Movimiento posicion = new Movimiento();

        var inicio = movimiento.substring(0, 2);
        var destino = movimiento.substring(2, 4);

        posicion.inicio =  casillAPosicion(inicio);
        posicion.destino =  casillAPosicion(destino);


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

        var mov = posicionACasilla(movimiento.inicio) + posicionACasilla(movimiento.destino);

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
    public static String convertirANotacion(int movimiento){
        Movimiento m = new Movimiento();
        m.inicio = movimiento >> 6 & 0b111111;
        m.destino = movimiento & 0b111111;
        m.promocion = movimiento >> 12 &0b111;
        return convertirANotacion(m);
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
        formato = formateado.substring(formateado.length() - 5, formateado.length() - 4) + "_" + formato;

        System.out.println(formato);
    }

}
