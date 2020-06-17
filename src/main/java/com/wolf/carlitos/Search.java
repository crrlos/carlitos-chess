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

    private final Posicion pos;
    private final Generador generador;

    private int nodes = 0;

    private final int[][][] history = new int[2][64][64];
    private final int[][] pv = new int[64][64];
    private final int[][] killers = new int[64][2];

    public Search(Posicion posicion) {
        this.pos = posicion;
        this.generador = new Generador(posicion);
    }

    private int quiescent(int depth, int alfa, int beta, int ply) {

        int ttval = Transposition.checkEntry(pos.getZobrist(), 0, alfa, beta);
        if (ttval != NOENTRY) return ttval;

        int flag = ALFA;

        int eval = evaluar(pos.miColor());
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
                    alfa >= standPat + valorPiezas[pos.tablero[mov.destino]] + 200
                            && pos.gameMaterial(pos.colorContrario()) - valorPiezas[pos.tablero[mov.destino]] > ENDGAME_MATERIAL
                            && !(pos.tablero[mov.inicio] == PEON && (mov.destino <= H1 || mov.destino >= A8))
            )
                continue;

            pos.makeMove(mov);

            eval = -quiescent(depth - 1, -beta, -alfa, ply + 1);

            pos.takeBack(mov);

            if (eval >= beta) {
                Transposition.setEntry(pos.getZobrist(), 0, eval, BETA, 0);
                return beta;
            }
            if (eval > alfa) {
                alfa = eval;
                flag = EXACT;
            }

        }
        Transposition.setEntry(pos.getZobrist(), 0, alfa, flag, 0);
        return alfa;
    }

    public int negaMax(int depth, int alfa, int beta, int ply, boolean allowNull, boolean isPv) {
        nodes++;
//        int mateValue = 1_000_000 - ply;
//
//        if (alfa < -mateValue) alfa = -mateValue;
//        if (beta > mateValue - 1) beta = mateValue - 1;
//        if (alfa >= beta) return alfa;

        int ttval = Transposition.checkEntry(pos.getZobrist(), depth, alfa, beta);
        if (ttval != NOENTRY) {
            if (!isPv || (ttval > alfa && ttval < beta)) return ttval;
        }

        if (depth == 0) return quiescent(depth, alfa, beta, ply);

        boolean inCheck = pos.enJaque();
        int color = pos.miColor();

        // CHECK EXTENSION
        if (inCheck) depth++;

        // NULL MOVE PRUNING
        if (
                depth > 2
                        && !inCheck
                        && !isPv
                        && allowNull
                        && evaluar(pos.miColor()) > beta
                        && pos.gameMaterial(pos.colorContrario()) >= ENDGAME_MATERIAL
        ) {
            int R = 2;
            if (depth > 6) R = 3;

            pos.doNull();

            int eval = -negaMax(depth - R - 1, -beta, -beta + 1, ply + 1, false, false);

            pos.takeBackNull();

            if (eval >= beta) {
                return beta;
            }
        }

        // STATIC NULL MOVE PRUNING
        if (depth < 3
                && !isPv
                && !inCheck) {
            int static_eval = evaluar(pos.miColor());

            int eval_margin = 120 * depth;
            if (static_eval - eval_margin >= beta)
                return static_eval - eval_margin;
        }

        // RAZORING
        if (!isPv
                && !inCheck
                && allowNull
                && depth <= 3) {
            int threshold = alfa - 500 - (depth - 1) * 60;
            if (evaluar(pos.miColor()) < threshold) {
                int val = quiescent(0, alfa, beta, ply);
                if (val < threshold) return alfa;
            }
        }

        // FUTILITY CONDITIONS
        int[] fmargin = new int[]{0, 200, 300, 500};

        boolean fPrune = false;
        if (depth <= 3
                && !isPv
                && !inCheck
                && Math.abs(alfa) < 9000
                && evaluar(pos.miColor()) + fmargin[depth] <= alfa)
            fPrune = true;

        var respuesta = generador.generarMovimientos(ply);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        establecerPuntuacion(movimientos, fin, ply);
        insertionSort(movimientos, fin);


        if (fin == 0) {
            if (pos.enJaque()) return -MATE + ply;
            else return AHOGADO;
        }

        int best = -INFINITO;
        int flag = ALFA;
        int bestMove = -1;
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
                            && !pos.moveGivesCheck(mov)

            ) {
                reduction = true;
                newDepth--;

                if (i > 5) newDepth--;
            }

            // FUTILITY PRUNING
            if (fPrune
                    && i > 0
                    && mov.ponderacion < CAPTURE_MOVE_SORT
                    && mov.promocion == 0
                    && !pos.moveGivesCheck(mov))
                continue;

            pos.makeMove(mov);
            int eval;

            eval = pvSearch(newDepth, alfa, beta, ply, best);

            if (reduction && eval > alfa) {
                eval = pvSearch(depth, alfa, beta, ply, best);
            }

            if (eval > best) best = eval;


            pos.takeBack(mov);

            if (eval >= beta) {
                establecerHistory(depth, color, mov);
                establecerKiller(ply, mov);
                bestMove = mov.promocion << 12 | mov.inicio << 6 | mov.destino;
                Transposition.setEntry(pos.getZobrist(), depth, eval, BETA, bestMove);
                return beta;
            }

            if (eval > alfa) {
                alfa = eval;
                flag = EXACT;
                bestMove = i;
                actualizarPV(mov, ply);
            }

        }
        if (bestMove != -1) {
            var mov = movimientos[bestMove];
            bestMove = mov.promocion << 12 | mov.inicio << 6 | mov.destino;
        }
        Transposition.setEntry(pos.getZobrist(), depth, alfa, flag, bestMove);
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

    private void establecerHistory(int depth, int color, Movimiento mov) {

        if (pos.tablero[mov.destino] == NOPIEZA) {
            history[color][mov.inicio][mov.destino] += depth;
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

            if (depth > 9)
                System.out.printf("info depth %d currmove %s currmovenumber %d\n",
                        depth,
                        Utilidades.convertirANotacion(mov),
                        i + 1);

            pos.makeMove(mov);


            int eval = pvSearch(depth, alfa, beta, ply, best);

            if (eval > best) best = eval;

            if (eval > alfa) {
                alfa = eval;
                actualizarPV(mov, ply);
            }

            pos.takeBack(mov);


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

        System.out.printf("info depth %d score cp %d nodes %d  time %d pv %s\n", depth, alfa, nodesPerSecond, time, builder.toString());
    }

    public void establecerPuntuacion(Movimiento[] movimientos, int fin, int ply) {

        int ttMove = Transposition.bestMove(pos.getZobrist());
        int color = pos.miColor();

        for (int i = 0; i < fin; i++) {

            int inicio = movimientos[i].inicio;
            int destino = movimientos[i].destino;
            int promocion = movimientos[i].promocion;

            int mov = promocion << 12 | inicio << 6 | destino;
            int ponderacion;

            //history heuristic
            ponderacion = history[color][inicio][destino];
            movimientos[i].ponderacion = ponderacion;

            //MVVLVA
            if (pos.tablero[destino] != NOPIEZA) {
                ponderacion = CAPTURE_MOVE_SORT +
                        valorPiezas[REY] / valorPiezas[pos.tablero[inicio]] + 10 * valorPiezas[pos.tablero[destino]];
                movimientos[i].ponderacion = ponderacion;
            }

            // killer moves
            if ((killers[ply][0] ^ mov) == 0) {
                movimientos[i].ponderacion = KILLER_SORT_SCORE;
            }

            // tt best move
            if (ttMove == mov) {
                movimientos[i].ponderacion = PV_SORT_SCORE - 1000;
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
