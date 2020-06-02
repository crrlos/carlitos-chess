/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;


import java.util.Arrays;

import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Evaluar.evaluar;
import static com.wolf.carlitos.Pieza.valorPiezas;


/**
 * @author carlos
 */


public class Search {

    private final int[] tablero;
    private final int[] color;

    private final Tablero tab;
    private final Generador generador;

    public static int nodes = 0;

    private static final int[][] history = new int[64][64];

    private final int[][] pv = new int[64][64];

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

            tab.makeMove(mov);

            perftSearch(deep - 1, acumulador, false);

            tab.takeBack(mov);

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

    private int quiescent(int nivel, int alfa, int beta, int ply) {

        int mejorValor;
        Generador.Respuesta respuesta;
        Movimiento[] movimientos;
        int fin;

        boolean enJaque = tab.enJaque();

        if (enJaque) {
            respuesta = generador.generarMovimientos(ply);
        } else {
            mejorValor = evaluar(tab.miColor());
            if (mejorValor > alfa) alfa = mejorValor;
            if (mejorValor >= beta) return beta;

            respuesta = generador.generarCapturas(tablero, color, ply);
        }
        movimientos = respuesta.movimientosGenerados;
        fin = respuesta.cantidadDeMovimientos;

        if (fin == 0 && enJaque) return -MATE + ply;

        establecerPuntuacion(movimientos, fin);
        insertionSort(movimientos, fin);

        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            tab.makeMove(mov);

            int evaluacion = -quiescent(nivel - 1, -beta, -alfa, ply + 1);

            tab.takeBack(mov);

            if (evaluacion > alfa) alfa = evaluacion;
            if (evaluacion >= beta) return beta;

        }

        return alfa;
    }

    public int negaMax(int depth, int alfa, int beta, int ply) {
        if (depth == 0) return quiescent(depth, alfa, beta, ply);
        var respuesta = generador.generarMovimientos(ply);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        establecerPuntuacion(movimientos, fin);
        insertionSort(movimientos, fin);

        // check extension
        if (tab.enJaque()) depth++;

        if (fin == 0) {
            if (tab.enJaque()) return -MATE + ply;
            else return AHOGADO;
        }

        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            tab.makeMove(mov);
            //tab.validarKey();
            int evaluacion = -negaMax(depth - 1, -beta, -alfa, ply + 1);

            tab.takeBack(mov);

            if (evaluacion >= beta) {
                if (tablero[mov.destino] == NOPIEZA)
                    history[mov.inicio][mov.destino] += depth;
                return beta;
            }

            if (evaluacion > alfa) {
                alfa = evaluacion;

                actualizarPV(mov, ply);

            }

        }
        return alfa;
    }

    private void actualizarPV(Movimiento mov, int ply) {
        pv[ply][ply] = mov.promocion << 12 | mov.inicio << 6 | mov.destino;
        System.arraycopy(pv[ply + 1], ply + 1, pv[ply], ply + 1, 30);
    }

    public Movimiento search(int depth) {
        int ply = 0;

        var respuesta = this.generador.generarMovimientos(ply);
        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        establecerPuntuacion(movimientos, fin);
        insertionSort(movimientos, fin);
        Movimiento bestMove = null;

        for (int k = 1; k <= depth; k++) {

            int alfa = -INFINITO;

            for (int i = 0; i < fin; i++) {
                movimientos[i].ponderacion = -INFINITO;
            }

            for (int i = 0; i < fin; i++) {

                var mov = movimientos[i];

                tab.makeMove(mov);

                int eval = -negaMax(k - 1, -INFINITO, -alfa, 1);

                if (eval > alfa) {
                    alfa = eval;
                    mov.ponderacion = eval;
                    bestMove = mov;
                    actualizarPV(mov, ply);
                }

                tab.takeBack(mov);

            }
            insertionSort(movimientos, fin);

            mostrarInformacionActual(alfa, k);
        }
        System.out.println("nodos: " + nodes);
        nodes = 0;
        return bestMove;
    }

    private void mostrarInformacionActual(int alfa, int depth) {
        int i = 0;
        StringBuilder builder = new StringBuilder();

        while (i < depth) {
            builder.append(Utilidades.convertirANotacion(pv[0][i])).append(" ");
            i++;
        }

        System.out.printf("info depth %d score cp %d nodes 20  time 3 pv %s\n", depth, alfa, builder.toString());
    }

    public void establecerPuntuacion(Movimiento[] movimientos, int fin) {

        for (int i = 0; i < fin; i++) {
            int inicio = movimientos[i].inicio;
            int destino = movimientos[i].destino;
            int ponderacion = 100_000_000;

            // si es captura usar MVVLVA, con offset de 100k para se coloque antes de una no captura
            if (tablero[destino] != NOPIEZA) {
                ponderacion += valorPiezas[REY] / valorPiezas[tablero[inicio]] + 10 * valorPiezas[tablero[destino]];
                movimientos[i].ponderacion = ponderacion;
                continue;
            }
            // si no es captura usar history heuristic
            ponderacion = history[inicio][destino];
            movimientos[i].ponderacion = ponderacion;

        }

    }

    public void insertionSort(Movimiento[] array, int fin) {
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
