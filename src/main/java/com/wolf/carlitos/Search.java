/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;


import java.util.ArrayList;
import java.util.List;

import static com.wolf.carlitos.Constantes.*;

import static com.wolf.carlitos.Tablero.*;

import static com.wolf.carlitos.Evaluar.*;
import static com.wolf.carlitos.Utilidades.*;


/**
 * @author carlos
 */


public class Search {

    private final int[] tablero;
    private final int[] color;
    private final int estadoTablero;
    private final Generador generador = new Generador();
    public static final List<Integer> secuencia = new ArrayList<>();
    public static int nodes = 0;
    public static int[][] history = new int[64][64];

    public Search(int[] tablero, int[] color, int estado) {
        this.tablero = tablero;
        this.color = color;
        this.estadoTablero = estado;

    }

    private void perftSearch(int deep, int estado, Acumulador acumulador, boolean reset) {

        if (deep == 0) {
            acumulador.contador++;
            acumulador.contadorPerft++;
            return;
        }
        var respuesta = generador.generarMovimientos(estado, deep);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;


        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            int estadoActualizodo = hacerMovimiento(tablero, color, estado, mov);

            perftSearch(deep - 1, estadoActualizodo, acumulador, false);

            revertirMovimiento(mov, estadoActualizodo, tablero, color);

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

    private int quiescentMax(int nivel, int estado, int[] tablero, int[] color, int alfa, int beta) {

        int mejorValor = evaluar();
        if (mejorValor >= beta) return beta;
        if (mejorValor > alfa) alfa = mejorValor;


        var respuesta = generador.generarCapturas(tablero, color, estado, nivel);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        establecerPuntuacion(movimientos, fin);
        insertionSort(movimientos, fin);

        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            int estadoCopia = hacerMovimiento(tablero, color, estado, mov);

            int evaluacion = quiescentMin(nivel - 1, estadoCopia, tablero, color, alfa, beta);

            revertirMovimiento(mov, estadoCopia, tablero, color);

            if (evaluacion >= beta) return beta;

            if (evaluacion > alfa) alfa = evaluacion;

        }

        return alfa;
    }

    private int quiescentMin(int nivel, int estado, int[] tablero, int[] color, int alfa, int beta) {
        int mejorValor = evaluar();

        if (mejorValor <= alfa) return alfa;
        if (mejorValor < beta) beta = mejorValor;

        var respuesta = generador.generarCapturas(tablero, color, estado, nivel);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        establecerPuntuacion(movimientos, fin);
        insertionSort(movimientos, fin);

        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            int estadoCopia = hacerMovimiento(tablero, color, estado, mov);

            int evaluacion = quiescentMax(nivel - 1, estadoCopia, tablero, color, alfa, beta);

            revertirMovimiento(mov, estadoCopia, tablero, color);

            if (evaluacion < beta) beta = evaluacion;

            if (evaluacion <= alfa) return alfa;

        }
        return beta;
    }

    public int mini(int nivel, int estado, int[] tablero, int[] color, int alfa, int beta) {
        if (nivel == 0) return quiescentMin(nivel, estado, tablero, color, alfa, beta);
        var respuesta = generador.generarMovimientos(estado, nivel);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        if (fin == 0) {
            if (reyEnJaque(estado)) return MATE + nivel;
            else return AHOGADO;
        }

        establecerPuntuacion(movimientos, fin);
        insertionSort(movimientos, fin);

        for (int i = 0; i < fin; i++) {

            var mov = movimientos[i];

            int estadoActualizado = hacerMovimiento(tablero, color, estado, mov);

            int evaluacion = maxi(nivel - 1, estadoActualizado, tablero, color, alfa, beta);

            revertirMovimiento(mov, estadoActualizado, tablero, color);

            if (evaluacion <= alfa) {
                if (tablero[mov.destino] == NOPIEZA)
                    history[mov.inicio][mov.destino] += nivel;
                return alfa;
            }

            if (evaluacion < beta) beta = evaluacion;


        }

        return beta;
    }

    public int maxi(int nivel, int estado, int[] tablero, int[] color, int alfa, int beta) {

        if (nivel == 0) return quiescentMax(nivel, estado, tablero, color, alfa, beta);

        var respuesta = generador.generarMovimientos(estado, nivel);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        establecerPuntuacion(movimientos, fin);
        insertionSort(movimientos, fin);

        if (fin == 0) {
            if (reyEnJaque(estado)) return -MATE - nivel;
            else return AHOGADO;
        }

        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            int estadoCopia = hacerMovimiento(tablero, color, estado, mov);

            int evaluacion = mini(nivel - 1, estadoCopia, tablero, color, alfa, beta);

            revertirMovimiento(mov, estadoCopia, tablero, color);

            if (evaluacion >= beta) {
                if (tablero[mov.destino] == NOPIEZA)
                    history[mov.inicio][mov.destino] += nivel;
                return beta;
            }

            if (evaluacion > alfa) alfa = evaluacion;

        }
        return alfa;
    }

    public Movimiento search(int n) {
        int pos = 0;

        int alfa = -10_000_000;
        int beta = 10_000_000;

        var respuesta = this.generador.generarMovimientos(estadoTablero, n);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        establecerPuntuacion(movimientos, fin);
        insertionSort(movimientos, fin);

        for (int i = 0; i < fin; i++) {

            var mov = movimientos[i];
            int estadoActualizado = hacerMovimiento(tablero, color, estadoTablero, mov);
            int eval = esTurnoBlanco(estadoTablero) ? mini(n - 1, estadoActualizado, tablero, color, alfa, beta) :
                    maxi(n - 1, estadoActualizado, tablero, color, alfa, beta);

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

            revertirMovimiento(mov, estadoActualizado, tablero, color);

        }
        System.out.println("nodos: " + nodes);
        nodes = 0;
        return movimientos[pos];
    }

}
