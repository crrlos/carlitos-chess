/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Evaluar.evaluar;
import static com.wolf.carlitos.Tablero.*;
import static com.wolf.carlitos.Utilidades.establecerPuntuacion;
import static com.wolf.carlitos.Utilidades.insertionSort;


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
    public static long zobrist = 0;
    public static long[][][] zobristRand = new long[2][6][64];
    static {
        Random rand = new Random();
        long[] casillas = new long[768];
        int contador = 0;
        while(contador < 768){
            long r = rand.nextLong();
            if(r > 0){
                boolean repetido = false;
                for (long casilla : casillas) {
                    if (casilla == r) {
                        repetido = true;
                        break;
                    }
                }
                if(repetido) continue;

                casillas[contador] = r;
                contador++;
            }

        }

        contador = 0;

        for (int i = 0; i < zobristRand.length; i++) {
            for (int j = 0; j < zobristRand[i].length; j++) {
                for (int k = 0; k < zobristRand[i][j].length; k++) {
                    zobristRand[i][j][k] = casillas[contador++];
                }
            }
        }
    }
    public Search(int[] tablero, int[] color, int estado) {
        this.tablero = tablero;
        this.color = color;
        this.estadoTablero = estado;

        // init zobrist key
        for (int i = 0; i < 64; i++) {
            if(tablero[i] == NOPIEZA) continue;
            zobrist ^= zobristRand[color[i]][tablero[i]][i];
        }

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

    private int quiescent(int nivel, int estado, int[] tablero, int[] color, int alfa, int beta){

        int mejorValor = evaluar(miColor(estado));
        if(mejorValor > alfa) alfa = mejorValor;
        if(mejorValor >= beta) return beta;

        var respuesta = generador.generarCapturas(tablero,color,estado,nivel);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        establecerPuntuacion(movimientos, fin);
        insertionSort(movimientos, fin);

        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            int estadoCopia = hacerMovimiento(tablero, color, estado, mov);

            int evaluacion = -quiescent(nivel - 1, estadoCopia, tablero, color, -beta, -alfa);

            revertirMovimiento(mov, estadoCopia, tablero, color);

            if (evaluacion > alfa) alfa = evaluacion;
            if (evaluacion >= beta) return beta;

        }

        return  alfa;
    }

    public int negaMax(int nivel, int estado, int[] tablero, int[] color, int alfa, int beta){
        if (nivel == 0) return quiescent(nivel, estado, tablero, color, alfa, beta);
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

            int evaluacion = -negaMax(nivel - 1, estadoCopia, tablero, color, -beta, -alfa);

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

            int eval = -negaMax(n - 1, estadoActualizado, tablero, color, -beta, -alfa);

            if(eval > alfa) {
                alfa = eval;
                pos = i;
            }

            revertirMovimiento(mov, estadoActualizado, tablero, color);

        }
        System.out.println("nodos: " + nodes);
        nodes = 0;
        return movimientos[pos];
    }

    public void actualizarZobrist(Movimiento movimiento){
        // remover pieza de inicio
        zobrist ^= zobristRand[color[movimiento.inicio]][tablero[movimiento.inicio]][movimiento.inicio];
        if(tablero[movimiento.destino] == NOPIEZA) return;
        // remover pieza de destino
        zobrist ^= zobristRand[color[movimiento.destino]][tablero[movimiento.destino]][movimiento.destino];
    }

}
