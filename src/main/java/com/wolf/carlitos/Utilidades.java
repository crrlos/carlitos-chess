/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.Alfil;
import com.wolf.carlitos.Piezas.Caballo;
import com.wolf.carlitos.Piezas.Dama;
import com.wolf.carlitos.Piezas.Peon;
import com.wolf.carlitos.Piezas.Pieza;
import com.wolf.carlitos.Piezas.Rey;
import com.wolf.carlitos.Piezas.Torre;
import com.wolf.carlitos.Trayectoria.TRAYECTORIA;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author carlos
 */
public class Utilidades {

    public static void imprimirPosicicion(Pieza[][] tablero) {
        for (int i = 7; i >= 0; i--) {
            System.out.println("+---+---+---+---+---+---+---+---+");
            for (int j = 0; j < 8; j++) {
                var pieza = tablero[i][j];
                System.out.print("| " + (pieza != null ? pieza.nombre() : " ") + " ");
            }
            System.out.print("|");
            System.out.println();
        }

        System.out.println("+---+---+---+---+---+---+---+---+");
    }

    public static int[] convertirAPosicion(String movimiento) {
        int[] posicion;

        if (movimiento.length() == 5) {
            posicion = new int[5];

        } else {
            posicion = new int[4];
        }

        posicion[1] = "abcdefgh".indexOf(movimiento.charAt(0));
        posicion[0] = "12345678".indexOf(movimiento.charAt(1));

        posicion[3] = "abcdefgh".indexOf(movimiento.charAt(2));
        posicion[2] = "12345678".indexOf(movimiento.charAt(3));

        if (movimiento.length() == 5) {
            switch (movimiento.charAt(4)) {
                case 'q':
                    posicion[4] = 1;
                    break;
                case 'r':
                    posicion[4] = 2;
                    break;
                case 'n':
                    posicion[4] = 3;
                    break;
                case 'b':
                    posicion[4] = 4;
                    break;
            }
        }

        return posicion;
    }

    public static String convertirANotacion(int[] movimiento) {

        final String FILAS = "12345678";
        final String COLUMNAS = "abcdefgh";

        var mov = COLUMNAS.charAt(movimiento[1]) + "" + FILAS.charAt(movimiento[0])
                + COLUMNAS.charAt(movimiento[3]) + "" + FILAS.charAt(movimiento[2]);
        if (movimiento.length == 5) {
            switch (movimiento[4]) {
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

    private static void actualizarPosicion(Pieza[][] tablero, EstadoTablero estadoTablero, int[] movimiento) {

        int filaInicio = movimiento[0];
        int colInicio = movimiento[1];

        int filaFinal = movimiento[2];
        int colFinal = movimiento[3];

        var pieza = tablero[filaInicio][colInicio];

        estadoTablero.piezaCapturada = null;
        estadoTablero.tipoMovimiento = -1;

        if (pieza instanceof Peon) {

            if (Math.abs(filaInicio - filaFinal) == 2) {
                estadoTablero.alPaso = true;
                estadoTablero.piezaALPaso = pieza;

                tablero[filaFinal][colFinal] = pieza;
                tablero[filaInicio][colInicio] = null;
                estadoTablero.tipoMovimiento = 0;
                return;
            }
            if (estadoTablero.alPaso) {
                if (colFinal > colInicio || colFinal < colInicio) {
                    if (tablero[filaInicio][colFinal] == estadoTablero.piezaALPaso) {

                        // aqui se remueve la pieza al paso
                        tablero[filaInicio][colFinal] = null;
                        estadoTablero.tipoMovimiento = 1;
                    }
                }

                estadoTablero.alPaso = false;
            }

            if (filaFinal == 7 || filaFinal == 0) {
                switch (movimiento[4]) {
                    case 1:
                        pieza = new Dama(estadoTablero.turnoBlanco);
                        break;
                    case 2:
                        pieza = new Torre(estadoTablero.turnoBlanco);
                        break;
                    case 3:
                        pieza = new Caballo(estadoTablero.turnoBlanco);
                        break;
                    case 4:
                        pieza = new Alfil(estadoTablero.turnoBlanco);
                        break;
                }
                estadoTablero.tipoMovimiento = 2;
            }

        } else if (pieza instanceof Rey) {
            // en los enroques solo se mueven las torres por ser el movimiento especial
            if (Math.abs(colInicio - colFinal) == 2) {
                if (pieza.esBlanca()) {
                    if (colFinal == 6) {//enroque corto
                        tablero[0][5] = tablero[0][7];
                        tablero[0][7] = null;
                    } else {//enroque largo
                        tablero[0][3] = tablero[0][0];
                        tablero[0][0] = null;
                    }
                } else {
                    if (colFinal == 6) {//enroque corto
                        tablero[7][5] = tablero[7][7];
                        tablero[7][7] = null;
                    } else {//enroque largo
                        tablero[7][3] = tablero[7][0];
                        tablero[7][0] = null;
                    }
                }
                estadoTablero.tipoMovimiento = 3;
            } else {
                estadoTablero.tipoMovimiento = 100;
            }
            if (pieza.esBlanca()) {
                estadoTablero.enroqueCBlanco = estadoTablero.enroqueLBlanco = false;
                estadoTablero.posicionReyBlanco[0] = filaFinal;
                estadoTablero.posicionReyBlanco[1] = colFinal;
            } else {
                estadoTablero.enroqueCNegro = estadoTablero.enroqueLNegro = false;
                estadoTablero.posicionReyNegro[0] = filaFinal;
                estadoTablero.posicionReyNegro[1] = colFinal;
            }

        } else if (pieza instanceof Torre) {

            if(colInicio == 7 && filaInicio == 7){
                estadoTablero.enroqueCNegro = false;
            }else if(colInicio == 0 && filaInicio == 7){
                estadoTablero.enroqueLNegro = false;
            }else if(colInicio == 7 && filaInicio == 0){
                estadoTablero.enroqueCBlanco = false;
            }else if(colInicio == 0 && filaInicio == 0){
                estadoTablero.enroqueLBlanco = false;
            }
        }

        estadoTablero.piezaCapturada = tablero[filaFinal][colFinal];
        if (estadoTablero.piezaCapturada instanceof Torre) {

            if(colFinal == 7 && filaFinal == 7){
                estadoTablero.enroqueCNegro = false;
            }else if(colFinal == 0 && filaFinal == 7){
                estadoTablero.enroqueLNegro = false;
            }else if(colFinal == 7 && filaFinal == 0){
                estadoTablero.enroqueCBlanco = false;
            }else if(colFinal == 0 && filaFinal == 0){
                estadoTablero.enroqueLBlanco = false;
            }
        }

        tablero[filaFinal][colFinal] = pieza;
        tablero[filaInicio][colInicio] = null;

        estadoTablero.alPaso = false;

        if (estadoTablero.tipoMovimiento == -1) {
            estadoTablero.tipoMovimiento = 0;
        }
    }

    public static void actualizarTablero(Pieza[][] tablero, EstadoTablero estadoTablero, int[] movimiento) {

        //var antes = analizarTablero(tablero,estadoTablero,movimiento);

//        if(!antes){
//            Utilidades.ImprimirPosicicion(tablero);
//            System.out.println();
//        }else{
//            System.out.println();
//        }

        actualizarPosicion(tablero, estadoTablero, movimiento);
        //actualizarTrayectorias(movimiento[2], movimiento[3], estadoTablero, tablero, false);

       // var despues =  analizarTablero(tablero,estadoTablero,movimiento);

//        if(!antes && despues)
//        {
//            Utilidades.ImprimirPosicicion(tablero);
//            System.out.println();
//        }

    }


}
