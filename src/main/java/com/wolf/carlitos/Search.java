/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;


import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Evaluar.evaluar;
import static com.wolf.carlitos.Pieza.valorPiezas;
import static java.lang.Math.abs;


/**
 * @author carlos
 */


public class Search {

    private final int[] tablero;
    private final int[] color;

    private final Tablero tab;
    private final Generador generador;

    public static int nodes = 0;

    private static int[][] history = new int[64][64];

    private static int[][] pv = new int[64][64];

    private static int[][] killers = new int[64][2];

    public Search(Tablero tablero) {
        this.tablero = tablero.tablero;
        this.color = tablero.color;
        this.tab = tablero;
        this.generador = new Generador(tablero);
    }

    private int quiescent(int depth, int alfa, int beta, int ply) {

//        int ttval =  Transposition.checkEntry(tab.getZobrist(),0,alfa,beta);
//        if(ttval != NOENTRY) return ttval;

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

        establecerPuntuacion(movimientos, fin, ply);
        insertionSort(movimientos, fin);
        long zobrist = 0;

        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            tab.makeMove(mov);
            zobrist = tab.getZobrist();

            int evaluacion = -quiescent(depth - 1, -beta, -alfa, ply + 1);

            tab.takeBack(mov);

            if (evaluacion >= beta) {
                Transposition.setEntry(zobrist, 0, evaluacion, BETA);
                return beta;
            }
            if (evaluacion > alfa) {
                Transposition.setEntry(zobrist, 0, evaluacion, EXACT);
                alfa = evaluacion;
            } else {
                Transposition.setEntry(zobrist, 0, evaluacion, ALFA);
            }

        }

        return alfa;
    }

    public int negaMax(int depth, int alfa, int beta, int ply) {

        // se consulta la tabla de transposición, alfa y beta se invierten porque la consulta se hace un nivel
        // abajo de donde se generó la posición por lo tanto se llamó -negaMax(-beta,-alfa)
        int ttval = Transposition.checkEntry(tab.getZobrist(), depth, -beta, -alfa);
        // valor retornado negativo para que sea consistente con -negaMax(-beta,-alfa)
        if (ttval != NOENTRY) return -ttval;

        if (depth == 0) return quiescent(depth, alfa, beta, ply);

        boolean enJaque = tab.enJaque();

        if (depth < 3
                && !enJaque
                &&  abs(beta - 1) > -INFINITO + 100)
        {
            int static_eval = evaluar(tab.miColor());

            int eval_margin =  15 * depth;
            if (static_eval - eval_margin >= beta)
                return static_eval - eval_margin;
        }

        var respuesta = generador.generarMovimientos(ply);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        establecerPuntuacion(movimientos, fin, ply);
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

            long zobrist = tab.getZobrist();

            int evaluacion = -negaMax(depth - 1, -beta, -alfa, ply + 1);

            tab.takeBack(mov);

            if (evaluacion >= beta) {
                establecerHistory(depth, mov);
                establecerKiller(ply, mov);
                Transposition.setEntry(zobrist, depth, evaluacion, BETA);
                return beta;
            }

            if (evaluacion > alfa) {
                alfa = evaluacion;
                Transposition.setEntry(zobrist, depth, evaluacion, EXACT);
                actualizarPV(mov, ply);

            } else {
                Transposition.setEntry(zobrist, depth, evaluacion, ALFA);
            }

        }
        return alfa;
    }
    public int search(int depth) {
        int ply = 0;

        var respuesta = this.generador.generarMovimientos(ply);
        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;
        //history = new int[64][64];
        //pv = new int[64][64];
        establecerPuntuacion(movimientos, fin, ply);
        insertionSort(movimientos, fin);


        int eval;
        int alfa = -INFINITO;
        int beta = INFINITO;

        for (int k = 1; k <= depth; k++) {

            for (int i = 0; i < fin; i++) {
                movimientos[i].ponderacion = -INFINITO;
            }

            eval = searchRoot(k, ply, movimientos, fin, alfa, beta);

            if(eval <= alfa || eval >= beta){
                alfa = -INFINITO;
                beta = INFINITO;
                k--;
                continue;
            }

            alfa = eval - 85;
            beta = eval + 85;

            establecerPuntuacion(movimientos, fin, ply);
            insertionSort(movimientos, fin);

            mostrarInformacionActual(eval, k);
        }

        System.out.println("nodos: " + nodes);
        nodes = 0;
        return pv[0][0];
    }
    private void establecerKiller(int ply, Movimiento mov) {
        int movimiento = mov.promocion << 12 | mov.inicio << 6 | mov.destino;

        if ((killers[ply][0] ^ movimiento) == 0) return;

        killers[ply][1] = killers[ply][0];
        killers[ply][0] = movimiento;
    }

    private void establecerHistory(int depth, Movimiento mov) {

        if (tablero[mov.destino] == NOPIEZA) {
            history[mov.inicio][mov.destino] += depth;
        }
    }

    private void actualizarPV(Movimiento mov, int ply) {
        pv[ply][ply] = mov.promocion << 12 | mov.inicio << 6 | mov.destino;
        System.arraycopy(pv[ply + 1], ply + 1, pv[ply], ply + 1, 30);
    }

    private int searchRoot(int depth, int ply, Movimiento[] movimientos, int fin, int alfa, int beta) {
        for (int i = 0; i < fin; i++) {

            var mov = movimientos[i];
            tab.makeMove(mov);

            int eval = -negaMax(depth - 1, -beta, -alfa, 1);

            if (eval > alfa) {
                alfa = eval;
                actualizarPV(mov, ply);
            }

            tab.takeBack(mov);
        }
        return alfa;
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

    public void establecerPuntuacion(Movimiento[] movimientos, int fin, int ply) {

        for (int i = 0; i < fin; i++) {
            int inicio = movimientos[i].inicio;
            int destino = movimientos[i].destino;
            int promocion = movimientos[i].promocion;

            int mov = promocion << 12 | inicio << 6 | destino;

            int ponderacion = 100_000_000;

            // pv
            if ((pv[ply][ply] ^ mov) == 0) {
                movimientos[i].ponderacion = 150_000_000;
                continue;
            }

            // killer moves
            if ((killers[ply][0] ^ mov) == 0) {
                movimientos[i].ponderacion = 140_000_000;
                continue;
            }

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
