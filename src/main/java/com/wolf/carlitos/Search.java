/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;


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

    private int nodes = 0;

    private static final int[][] history = new int[64][64];

    private static final int[][] pv = new int[64][64];

    private static final int[][] killers = new int[64][2];

    public Search(Tablero tablero) {
        this.tablero = tablero.tablero;
        this.color = tablero.color;
        this.tab = tablero;
        this.generador = new Generador(tablero);
    }

    private int quiescent(int depth, int alfa, int beta, int ply) {

        int ttval = Transposition.checkEntry(tab.getZobrist(), 0, alfa, beta);
        if (ttval != NOENTRY) return ttval;

        int flag = ALFA;

        int eval = evaluar(tab.miColor());
        int standPat = eval;
        if (eval >= beta) return beta;

        if (eval > alfa) {
            alfa = eval;
            flag = EXACT;
        }

        Generador.Respuesta respuesta = generador.generarCapturas(ply);
        Movimiento[] movimientos;
        int fin;

        movimientos = respuesta.movimientosGenerados;
        fin = respuesta.cantidadDeMovimientos;

        establecerPuntuacion(movimientos, fin, ply);
        insertionSort(movimientos, fin);

        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            // delta pruning
            if (
                    alfa >= standPat + valorPiezas[tablero[mov.destino]] + 200
                            && tab.gameMaterial(tab.colorContrario()) - valorPiezas[tablero[mov.destino]] > ENDGAME_MATERIAL
                            && !(tablero[mov.inicio] == PEON && (mov.destino <= H1 || mov.destino >= A8))
            )
                continue;

            tab.makeMove(mov);

            eval = -quiescent(depth - 1, -beta, -alfa, ply + 1);

            tab.takeBack(mov);

            if (eval >= beta) {
                Transposition.setEntry(tab.getZobrist(), 0, eval, BETA);
                return beta;
            }
            if (eval > alfa) {
                alfa = eval;
                flag = EXACT;
            }

        }
        Transposition.setEntry(tab.getZobrist(), 0, alfa, flag);
        return alfa;
    }

    public int negaMax(int depth, int alfa, int beta, int ply, boolean allowNull, boolean isPv) {
        nodes++;
//        int mateValue = 1_000_000 - ply;
//
//        if (alfa < -mateValue) alfa = -mateValue;
//        if (beta > mateValue - 1) beta = mateValue - 1;
//        if (alfa >= beta) return alfa;

        int ttval = Transposition.checkEntry(tab.getZobrist(), depth, alfa, beta);
        if (ttval != NOENTRY) {
            if (!isPv || (ttval > alfa && ttval < beta)) return ttval;
        }

        if (depth == 0) return quiescent(depth, alfa, beta, ply);

        boolean inCheck = tab.enJaque();

        // CHECK EXTENSION
        if (inCheck) depth++;

        // NULL MOVE PRUNING
        if (
                depth > 2
                        && !inCheck
                        && !isPv
                        && allowNull
                        && evaluar(tab.miColor()) > beta
                        && tab.gameMaterial(tab.colorContrario()) >= ENDGAME_MATERIAL
        ) {
            int R = 2;
            if (depth > 6) R = 3;

            tab.doNull();

            int eval = -negaMax(depth - R - 1, -beta, -beta + 1, ply + 1, false, false);

            tab.takeBackNull();

            if (eval >= beta) {
                return beta;
            }
        }

        if (depth < 3
                && !isPv
                && !inCheck)
        {
            int static_eval = evaluar(tab.miColor());

            int eval_margin = 120 * depth;
            if (static_eval - eval_margin >= beta)
                return static_eval - eval_margin;
        }

        /**************************************************************************
         *  RAZORING - if a node is close to the leaf and its static score is low, *
         *  we drop directly to the quiescence search.                             *
         **************************************************************************/

        if (!isPv
                &&  !inCheck
                //&&  tt_move_index == -1
                && allowNull
                && depth <= 3) {
            int threshold = alfa - 500 - (depth - 1) * 60;
            if (evaluar(tab.miColor()) < threshold) {
                int val = quiescent(0,alfa,beta,ply);
                if (val < threshold) return alfa;
            }
        } // end of razoring code

        int[] fmargin = new int[]{ 0, 200, 300, 500 };

        boolean f_prune = false;
        if (depth <= 3
                &&  !isPv
                &&  !inCheck
                &&   Math.abs(alfa) < 9000
                &&   evaluar(tab.miColor()) + fmargin[depth] <= alfa)
            f_prune = true;

        var respuesta = generador.generarMovimientos(ply);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        establecerPuntuacion(movimientos, fin, ply);
        insertionSort(movimientos, fin);


        if (fin == 0) {
            if (tab.enJaque()) return -MATE + ply;
            else return AHOGADO;
        }

        int best = -INFINITO;
        int flag = ALFA;

        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            boolean reduction = false;
            int newDepth = depth;

            //LMR
            if (
                    depth > 3
                            && i > 2
                            && !isPv
                            && !inCheck
                            && mov.ponderacion < CAPTURE_MOVE_SORT
                            && mov.promocion == 0
                            && !tab.moveGivesCheck(mov)

            ) {
                reduction = true;
                newDepth--;

                if (i > 5) newDepth--;
            }

            if (f_prune
                    &&   i > 0
                    &&  mov.ponderacion < CAPTURE_MOVE_SORT
                    &&  mov.promocion == 0
                    &&  !tab.moveGivesCheck(mov))
                continue;

            tab.makeMove(mov);
            int eval;

            eval = pvSearch(newDepth, alfa, beta, ply, best);

            if (reduction && eval > alfa) {
                eval = pvSearch(depth, alfa, beta, ply, best);
            }

            if (eval > best) best = eval;


            tab.takeBack(mov);

            if (eval >= beta) {
                establecerHistory(depth, mov);
                establecerKiller(ply, mov);
                Transposition.setEntry(tab.getZobrist(), depth, eval, BETA);
                return beta;
            }

            if (eval > alfa) {
                alfa = eval;
                flag = EXACT;
                actualizarPV(mov, ply);
            }

        }
        Transposition.setEntry(tab.getZobrist(), depth, alfa, flag);
        return alfa;
    }

    private int pvSearch(int depth, int alfa, int beta, int ply, int best) {
        int eval;
        if (best == -INFINITO) {
            eval = -negaMax(depth - 1, -beta, -alfa, ply + 1, true, true);
        } else {
            eval = -negaMax(depth - 1, -alfa - 1, -alfa, ply + 1, true, false);
            if (eval > alfa && eval < beta)
                eval = -negaMax(depth - 1, -beta, -alfa, ply + 1, true, true);
        }
        return eval;
    }

    public int search(int depth) {
        int ply = 0;

        var respuesta = this.generador.generarMovimientos(ply);
        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;
        establecerPuntuacion(movimientos, fin, ply);
        insertionSort(movimientos, fin);

        int eval;

        int alfa = -INFINITO;
        int beta = INFINITO;

        for (int k = 1; k <= depth; k++) {

            for (int i = 0; i < fin; i++) {
                movimientos[i].ponderacion = -INFINITO;
            }

            long initTime = System.currentTimeMillis();

            eval = searchRoot(k, ply, movimientos, fin, alfa, beta);

            if (eval <= alfa || eval >= beta) {
                alfa = -INFINITO;
                beta = INFINITO;
                k--;
                continue;
            }

            alfa = eval - 85;
            beta = eval + 85;

            establecerPuntuacion(movimientos, fin, ply);
            insertionSort(movimientos, fin);

            mostrarInformacionActual(eval, k, nodes, System.currentTimeMillis() - initTime);
            nodes = 0;
        }


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
        int best = -INFINITO;
        for (int i = 0; i < fin; i++) {

            var mov = movimientos[i];
            tab.makeMove(mov);


            int eval = pvSearch(depth, alfa, beta, ply, best);

            if (eval > best) best = eval;

            if (eval > alfa) {
                alfa = eval;
                actualizarPV(mov, ply);
            }

            tab.takeBack(mov);
        }
        return alfa;
    }

    private void mostrarInformacionActual(int alfa, int depth, int nodesPerSecond, long time) {
        int i = 0;
        StringBuilder builder = new StringBuilder();

        while (i < depth) {
            builder.append(Utilidades.convertirANotacion(pv[0][i])).append(" ");
            i++;
        }

        System.out.printf("info depth %d score cp %d nodes %d  time %d pv %s\n", depth, alfa, nodesPerSecond,time, builder.toString());
    }

    public void establecerPuntuacion(Movimiento[] movimientos, int fin, int ply) {

        for (int i = 0; i < fin; i++) {

            int inicio = movimientos[i].inicio;
            int destino = movimientos[i].destino;
            int promocion = movimientos[i].promocion;

            int mov = promocion << 12 | inicio << 6 | destino;
            int ponderacion;

            //history heuristic
            ponderacion = history[inicio][destino];
            movimientos[i].ponderacion = ponderacion;

            //MVVLVA
            if (tablero[destino] != NOPIEZA) {
                ponderacion = CAPTURE_MOVE_SORT +
                        valorPiezas[REY] / valorPiezas[tablero[inicio]] + 10 * valorPiezas[tablero[destino]];
                movimientos[i].ponderacion = ponderacion;
            }

            // killer moves
            if ((killers[ply][0] ^ mov) == 0) {
                movimientos[i].ponderacion = KILLER_SORT_SCORE;
            }

            // pv
            if ((pv[ply][ply] ^ mov) == 0) {
                movimientos[i].ponderacion = PV_SORT_SCORE;
            }
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
