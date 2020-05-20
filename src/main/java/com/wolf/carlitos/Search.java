/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.wolf.carlitos.Bitboard.next;
import static com.wolf.carlitos.Bitboard.remainder;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Ponderaciones.*;
import static com.wolf.carlitos.Utilidades.*;
import static com.wolf.carlitos.Tablero.*;
import static java.lang.Long.bitCount;

/**
 * @author carlos
 */


public class Search {
    private final int[] pieza;
    private final int[] color;
    private final int estadoTablero;
    private final Generador generador = new Generador();
    public static final List<Integer> secuencia = new ArrayList<>();

    public Search(int[] pieza, int[] color, int estado) {
        this.pieza = pieza;
        this.color = color;
        this.estadoTablero = estado;

    }

    public void puntajeMVVLVA(int[] movimientos, int fin) {

        for (int i = 0; i < fin; i++) {
            int inicio = movimientos[i] >> 6 & 0b111111;
            int destino = movimientos[i] & 0b111111;

            movimientos[i] |= (valorPiezas[REY] / valorPiezas[pieza[inicio]] + 10 * valorPiezas[pieza[destino]]) << 15;
        }

    }

    public static void insertionSort(int[] array, int fin) {
        for (int j = 1; j < fin; j++) {
            int key = array[j];
            int i = j - 1;
            while ((i > -1) && (array[i] >> 15 < key >> 15)) {
                array[i + 1] = array[i];
                i--;
            }
            array[i + 1] = key;
        }
    }

    private void perftSearch(int deep, int estado, Acumulador acumulador, boolean reset) {

        if (deep == 0) {
            acumulador.contador++;
            acumulador.contadorPerft++;
            return;
        }
        var respuesta = generador.generarMovimientos(pieza, color, estado, deep);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;


        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            int estadoActualizodo = hacerMovimiento(pieza, color, estado, mov);

            perftSearch(deep - 1, estadoActualizodo, acumulador, false);

            revertirMovimiento(mov, estadoActualizodo, pieza, color);

            if (reset) {
                System.out.println(Utilidades.convertirANotacion(mov) + " " + acumulador.contadorPerft);
                acumulador.contadorPerft = 0;
            }
        }
    }

    public void perft(int deep) {
        var acumulador = new Acumulador();
        var tinicio = System.currentTimeMillis();
        perftSearch(deep, estadoTablero, acumulador, true);
        System.out.println(acumulador.contador);

        var tfin = (System.currentTimeMillis() - tinicio) / 1000;

    }

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
    private int quiescentMax(int nivel, int estado, int[] tablero, int[] color, int alfa, int beta){

        int mejorValor = evaluar();
        if(nivel == -2) return mejorValor;
        if(mejorValor >= beta) return  beta;
        if(mejorValor > alfa) alfa = mejorValor;


        var respuesta = generador.generarMovimientos(tablero, color, estado, nivel);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        int nuevoTotal = 0;
        for (int i = 0; i < fin; i++) {
            if(tablero[movimientos[i] & 0b111111] != NOPIEZA){
                movimientos[nuevoTotal++] = movimientos[i];
            }
        }

        puntajeMVVLVA(movimientos,nuevoTotal);
        insertionSort(movimientos,nuevoTotal);

        for (int i = 0; i < nuevoTotal; i++) {
            var mov = movimientos[i];

            int estadoCopia = hacerMovimiento(tablero, color, estado, mov);

            int evaluacion = quiescentMin(nivel - 1, estadoCopia, tablero, color, alfa, beta);

            revertirMovimiento(mov, estadoCopia, tablero, color);

            if (evaluacion >= beta) return beta;

            if (evaluacion > alfa) alfa = evaluacion;

        }

        return alfa;
    }
    private int quiescentMin(int nivel, int estado, int[] tablero, int[] color, int alfa, int beta){
        int mejorValor = evaluar();

        if(nivel == -2) return mejorValor;

        if(mejorValor <= alfa) return  alfa;
        if(mejorValor < beta) beta = mejorValor;

        var respuesta = generador.generarMovimientos(tablero, color, estado, nivel);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        int nuevoTotal = 0;
        for (int i = 0; i < fin; i++) {
            if(tablero[movimientos[i] & 0b111111] != NOPIEZA){
                movimientos[nuevoTotal++] = movimientos[i];
            }
        }

        puntajeMVVLVA(movimientos,nuevoTotal);
        insertionSort(movimientos,nuevoTotal);

        for (int i = 0; i < nuevoTotal; i++) {
            var mov = movimientos[i];

            int estadoCopia = hacerMovimiento(tablero, color, estado, mov);

            int evaluacion = quiescentMax(nivel - 1, estadoCopia, tablero, color, alfa, beta);

            revertirMovimiento(mov, estadoCopia, tablero, color);

            if (evaluacion < beta ) beta =  evaluacion;

            if (evaluacion <= alfa) return  alfa;

        }

        return beta;
    }

    public int mini(int nivel, int estado, int[] tablero, int[] color, int alfa, int beta) {

        if (nivel == 0) return quiescentMin(nivel,estado,tablero,color,alfa,beta);

        var respuesta = generador.generarMovimientos(tablero, color, estado, nivel);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        if (fin == 0) {
            if (reyEnJaque(tablero, color, estado)) return MATE + nivel;
            else return AHOGADO;
        }

        puntajeMVVLVA(movimientos, fin);
        insertionSort(movimientos, fin);

        for (int i = 0; i < fin; i++) {

            var mov = movimientos[i];

            int estadoActualizado = hacerMovimiento(tablero, color, estado, mov);

            int evaluacion = maxi(nivel - 1, estadoActualizado, tablero, color, alfa, beta);

            revertirMovimiento(mov, estadoActualizado, tablero, color);

            if (evaluacion <= alfa) return alfa;

            if (evaluacion < beta) beta = evaluacion;

            // System.out.println("nivel: " + nivel + " mini " + "movimiento " + Utilidades.convertirANotacion(mov) + " evaluacion:" + evaluacion);

        }

        return beta;
    }

    public int maxi(int nivel, int estado, int[] tablero, int[] color, int alfa, int beta) {

        if (nivel == 0) return quiescentMax(nivel,estado,tablero,color,alfa,beta);

        var respuesta = generador.generarMovimientos(tablero, color, estado, nivel);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        puntajeMVVLVA(movimientos, fin);
        insertionSort(movimientos, fin);

        if (fin == 0) {
            if (reyEnJaque(tablero, color, estado)) return -MATE - nivel;
            else return AHOGADO;
        }

        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            int estadoCopia = hacerMovimiento(tablero, color, estado, mov);

            int evaluacion = mini(nivel - 1, estadoCopia, tablero, color, alfa, beta);

            revertirMovimiento(mov, estadoCopia, tablero, color);

            if (evaluacion >= beta) return beta;

            if (evaluacion > alfa) alfa = evaluacion;
            //System.out.println("nivel: " + nivel + " maxi " + "movimiento " + Utilidades.convertirANotacion(mov) + " evaluacion:" + evaluacion);
        }
        return alfa;
    }

    public int search(int n)  {
        int pos = 0;

        int alfa = -10_000_000;
        int beta = 10_000_000;

        var respuesta = this.generador.generarMovimientos(pieza, color, estadoTablero, n);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        puntajeMVVLVA(movimientos, fin);
        insertionSort(movimientos, fin);

        for (int i = 0; i < fin; i++) {

            var mov = movimientos[i];
            int estadoActualizado = hacerMovimiento(pieza, color, estadoTablero, mov);
            int eval = esTurnoBlanco(estadoTablero) ? mini(n - 1, estadoActualizado, pieza, color, alfa, beta) :
                    maxi(n - 1, estadoActualizado, pieza, color, alfa, beta);

            if (esTurnoBlanco(estadoTablero)) {
                if (eval > alfa) {
                    alfa = eval;
                    pos = i;
                }
            } else {
                if (eval < beta) {
                    beta = eval;
                    pos = i;
                }
            }

            revertirMovimiento(mov, estadoActualizado, pieza, color);

        }
        return movimientos[pos];
    }

}
