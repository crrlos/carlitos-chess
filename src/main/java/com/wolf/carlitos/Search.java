/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;


import java.util.Random;

import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Evaluar.evaluar;
import static com.wolf.carlitos.Utilidades.establecerPuntuacion;
import static com.wolf.carlitos.Utilidades.insertionSort;


/**
 * @author carlos
 */


public class Search {

    private final int[] tablero;
    private final int[] color;

    private final Tablero tab;
    private final Generador generador;

    public static int nodes = 0;

    public static int[][] history = new int[64][64];

    public Search(Tablero tablero) {
        this.tablero = tablero.tablero;
        this.color = tablero.color;
        this.tab = tablero;
        this.generador = new Generador(tablero);
    }

    private void perftSearch(int deep, Acumulador acumulador, boolean reset) {

        if (deep == 0) {
            acumulador.contador++;
            acumulador.contadorPerft++;
            return;
        }
        var respuesta = generador.generarMovimientos(deep);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;


        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            tab.hacerMovimiento(tablero, color, mov);

            perftSearch(deep - 1, acumulador, false);

            tab.revertirMovimiento(mov, tablero, color);

            if (reset) {
                System.out.println(Utilidades.convertirANotacion(mov) + " " + acumulador.contadorPerft);
                acumulador.contadorPerft = 0;
            }
        }
    }

    public void perft(int deep) {
        var acumulador = new Acumulador();
        var tinicio = System.currentTimeMillis();
        perftSearch(deep, acumulador, true);
        System.out.println(acumulador.contador);

        var tfin = (System.currentTimeMillis() - tinicio) / 1000;

    }

    private int quiescent(int nivel, int[] tablero, int[] color, int alfa, int beta){

        int mejorValor = evaluar(tab.miColor());
        if(mejorValor > alfa) alfa = mejorValor;
        if(mejorValor >= beta) return beta;

        var respuesta = generador.generarCapturas(tablero,color,nivel);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        establecerPuntuacion(movimientos, fin,tab);
        insertionSort(movimientos, fin);

        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            tab.hacerMovimiento(tablero, color, mov);

            int evaluacion = -quiescent(nivel - 1, tablero, color, -beta, -alfa);

            tab.revertirMovimiento(mov, tablero, color);

            if (evaluacion > alfa) alfa = evaluacion;
            if (evaluacion >= beta) return beta;

        }

        return  alfa;
    }

    public int negaMax(int nivel, int[] tablero, int[] color, int alfa, int beta){
        if (nivel == 0) return quiescent(nivel, tablero, color, alfa, beta);
        var respuesta = generador.generarMovimientos(nivel);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        establecerPuntuacion(movimientos, fin,tab);
        insertionSort(movimientos, fin);

        if (fin == 0) {
            if (tab.reyEnJaque()) return -MATE - nivel;
            else return AHOGADO;
        }

        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            tab.hacerMovimiento(tablero, color, mov);

            int evaluacion = -negaMax(nivel - 1, tablero, color, -beta, -alfa);

            tab.revertirMovimiento(mov, tablero, color);

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

        var respuesta = this.generador.generarMovimientos(n);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        establecerPuntuacion(movimientos, fin,tab);
        insertionSort(movimientos, fin);

        for (int i = 0; i < fin; i++) {

            var mov = movimientos[i];

            tab.hacerMovimiento(tablero, color, mov);

            int eval = -negaMax(n - 1, tablero, color, -beta, -alfa);

            if(eval > alfa) {
                alfa = eval;
                pos = i;
            }

            tab.revertirMovimiento(mov, tablero, color);

        }
        System.out.println("nodos: " + nodes);
        nodes = 0;
        return movimientos[pos];
    }

}
