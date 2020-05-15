/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;


import java.util.concurrent.*;

import static com.wolf.carlitos.Ponderaciones.*;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Utilidades.*;
import static com.wolf.carlitos.Tablero.*;

/**
 * @author carlos
 */


public class Search {
    private final int[] pieza;
    private final int[] color;
    private final int estadoTablero;
    private final Generador generador = new Generador();

    public Search(int[] pieza, int[] color, int estado) {
        this.pieza = pieza;
        this.color = color;
        this.estadoTablero = estado;

    }

    public void puntajeMVVLVA(int[] movimientos, int fin) {

        for (int i = 0; i < fin; i++) {
            int inicio = movimientos[i] >> 6 & 0b111111;
            int destino = movimientos[i] & 0b111111;

            movimientos[i] |= (valorPiezas[pieza[inicio]] + 10 * valorPiezas[pieza[destino]]) << 14;
        }

    }

    public static void insertionSort(int[] array,int fin) {
        for (int j = 1; j < fin; j++) {
            int key = array[j];
            int i = j - 1;
            while ((i > -1) && (array[i] >> 14 < key >> 14)) {
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


    private void revertirMovimiento(int movimiento, int estado, int[] tablero, int[] color) {

        estado ^= 0b10000;

        int inicio = movimiento >> 6 & 0b111111;
        int destino = movimiento & 0b111111;

        boolean turnoBlanco = esTurnoBlanco(estado);

        switch ((estado >> POSICION_TIPO_MOVIMIENTO & 0b111)) {

            case MOVIMIENTO_NORMAL:
            case MOVIMIENTO_REY:
                tablero[inicio] = tablero[destino];
                color[inicio] = color[destino];
                break;
            case AL_PASO:
                int posicionPaso = turnoBlanco ? destino - 8 : destino + 8;

                tablero[posicionPaso] = PEON;
                color[posicionPaso] = esTurnoBlanco(estado) ? NEGRO : BLANCO;

                tablero[inicio] = tablero[destino];
                color[inicio] = color[destino];

                break;
            case PROMOCION:
                tablero[inicio] = PEON;
                color[inicio] = turnoBlanco ? BLANCO : NEGRO;
                break;
            case ENROQUE:

                tablero[inicio] = tablero[destino];
                tablero[destino] = NOPIEZA;
                color[inicio] = color[destino];
                color[destino] = NOCOLOR;

                if (destino == G1 || destino == G8) {
                    tablero[turnoBlanco ? H1 : H8] = tablero[turnoBlanco ? F1 : F8];
                    tablero[turnoBlanco ? F1 : F8] = NOPIEZA;

                    color[turnoBlanco ? H1 : H8] = color[turnoBlanco ? F1 : F8];
                    color[turnoBlanco ? F1 : F8] = NOCOLOR;

                } else if (destino == C1 || destino == C8) {
                    tablero[turnoBlanco ? A1 : A8] = tablero[turnoBlanco ? D1 : D8];
                    tablero[turnoBlanco ? D1 : D8] = NOPIEZA;

                    color[turnoBlanco ? A1 : A8] = color[turnoBlanco ? D1 : D8];
                    color[turnoBlanco ? D1 : D8] = NOCOLOR;
                }

                break;

        }

        tablero[destino] = (estado >> POSICION_PIEZA_CAPTURADA & 0b111);
        color[destino] = tablero[destino] == NOPIEZA ? NOCOLOR : esTurnoBlanco(estado) ? NEGRO : BLANCO;

    }


    private int evaluar(int[] tablero, int[] color) {

        int valorBlancas = 0;
        int valorNegras = 0;

        for (int i = 0; i < 64; i++) {
            var pieza = tablero[i];
            if (pieza == NOPIEZA) continue;

            switch (pieza) {
                case PEON:
                    valorBlancas += (color[i] == BLANCO ? ponderacionPeon[flip[i]] : -ponderacionPeon[i]);
                    break;
                case CABALLO:
                    valorBlancas += (color[i] == BLANCO ? ponderacionCaballo[flip[i]] : -ponderacionCaballo[i]);
                    break;
                case ALFIL:
                    valorBlancas += (color[i] == BLANCO ? ponderacionAlfil[flip[i]] : -ponderacionAlfil[i]);
                    break;
                case TORRE:
                    valorBlancas += (color[i] == BLANCO ? ponderacionTorre[flip[i]] : -ponderacionTorre[i]);
                    break;
                case DAMA:
                    valorBlancas += (color[i] == BLANCO ? ponderacionDama[flip[i]] : -ponderacionDama[i]);
                    break;
                case REY:
                    valorBlancas += (color[i] == BLANCO ? ponderacionRey[flip[i]] : -ponderacionRey[i]);
                    break;
            }
            if (color[i] == BLANCO)
                valorBlancas += valorPiezas[pieza];
            else valorNegras -= valorPiezas[pieza];
        }

        return valorBlancas + valorNegras;

    }

    public int mini(int nivel, int estado, int[] tablero, int[] color, int alfa, int beta) {

        if (nivel == 0) return evaluar(tablero, color);



        var respuesta = generador.generarMovimientos(tablero, color, estado, nivel);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        if (movimientos.length == 0) {
            if (reyEnJaque(tablero, color, estado)) return MATE;
            else return AHOGADO;
        }

        puntajeMVVLVA(movimientos, fin);
        insertionSort(movimientos, fin);

        for (int i = 0; i < movimientos.length; i++) {

            var mov = movimientos[i];
            int estadoActualizado = hacerMovimiento(tablero, color, estado, mov);

            estadoActualizado ^= 0b10000;

            int evaluacion = maxi(nivel - 1, estadoActualizado, tablero, color, alfa, beta);

            estadoActualizado ^= 0b10000;

            revertirMovimiento(mov, estadoActualizado, tablero, color);

            if (evaluacion <= alfa) return alfa;

            if (evaluacion < beta) beta = evaluacion;

            // System.out.println("nivel: " + nivel + " mini " + "movimiento " + Utilidades.convertirANotacion(mov) + " evaluacion:" + evaluacion);

        }

        return beta;
    }

    public int maxi(int nivel, int estado, int[] tablero, int[] color, int alfa, int beta) {

        if (nivel == 0) return evaluar(tablero, color);

        var respuesta = generador.generarMovimientos(tablero, color, estado, nivel);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        puntajeMVVLVA(movimientos,fin);
        insertionSort(movimientos,fin);

        if (movimientos.length == 0) {
            if (reyEnJaque(tablero, color, estado)) return -MATE;
            else return AHOGADO;
        }

        for (int i = 0; i < movimientos.length; i++) {
            var mov = movimientos[i];
            int estadoCopia = hacerMovimiento(tablero, color, estado, mov);
            estadoCopia ^= 0b10000;

            int evaluacion = mini(nivel - 1, estadoCopia, tablero, color, alfa, beta);

            estadoCopia ^= 0b10000;

            revertirMovimiento(mov, estadoCopia, tablero, color);

            if (evaluacion >= beta) return beta;

            if (evaluacion > alfa) alfa = evaluacion;
            //System.out.println("nivel: " + nivel + " maxi " + "movimiento " + Utilidades.convertirANotacion(mov) + " evaluacion:" + evaluacion);
        }
        return alfa;
    }

    public int search(int n) throws InterruptedException, CloneNotSupportedException, ExecutionException {
        //n = 1;
        int pos = 0;

        int alfa = -10_000;
        int beta = 10_000;

//        ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(2);
//        List<Callable<Integer>> callables = new ArrayList<>();

        var respuesta = this.generador.generarMovimientos(pieza, color, estadoTablero, n);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;

        puntajeMVVLVA(movimientos,fin);
        insertionSort(movimientos,fin);

//        for (int i = 0; i < fin; i++) {
//            var mov = movimientos[i];
//            long estadoActualizado = actualizarTablero(pieza,color, estadoTablero, mov);
//
//            estadoActualizado ^= 10000;
//
//            Movimientos clonMovimientos = (Movimientos) this.movimientos.clone();
//
//            long finalEstadoActualizado = estadoActualizado;
//
//            var piezaClon = Arrays.copyOf(pieza,pieza.length);
//            var colorClon = Arrays.copyOf(color,color.length);
//
//            callables.add(() -> esTurnoBlanco(estadoTablero) ?
//                    mini(n -1, finalEstadoActualizado, piezaClon,colorClon,alfa,beta,clonMovimientos, new Generador()) :
//                    maxi(n -1, finalEstadoActualizado, piezaClon,colorClon,alfa,beta,clonMovimientos,new Generador()));
//
//            estadoActualizado ^= 10000;
//
//            revertirMovimiento(mov, estadoActualizado, pieza,color);
//
//
//        }
//        var resultados = WORKER_THREAD_POOL.invokeAll(callables);
//        awaitTerminationAfterShutdown(WORKER_THREAD_POOL);
//
//
//        for (int i = 0; i < resultados.size(); i++) {
//            var eval = resultados.get(i).get();
//
//            if (esTurnoBlanco(estadoTablero)) {
//                if (eval > valoracion) {
//                    valoracion = eval;
//                    pos = i;
//                }
//            } else {
//                if (eval < valoracion) {
//                    valoracion = eval;
//                    pos = i;
//                }
//            }
//
//        }

        for (int i = 0; i < movimientos.length; i++) {

            var mov = movimientos[i];


//            var m = Utilidades.convertirANotacion(mov);
//            if(m.contains("g2g1")){
//                Utilidades.imprimirPosicicion(pieza,color);
//                System.out.println();
//            }

            int estadoActualizado = hacerMovimiento(pieza, color, estadoTablero, mov);
            //Utilidades.imprimirPosicicion(pieza,color);
            estadoActualizado ^= 0b10000;

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
            estadoActualizado ^= 0b10000;

            revertirMovimiento(mov, estadoActualizado, pieza, color);
            // Utilidades.imprimirPosicicion(pieza,color);

        }
        return movimientos[pos];
    }

    public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

//    private void esperarHilos(ExecutorService service) throws InterruptedException {
//        service.shutdown();
//        if(!service.awaitTermination(5, TimeUnit.SECONDS)){
//            esperarHilos(service);
//        }
//    }
//    private void comprobar(List<int[]> movimientos, List<int[]> secuencia){
//        var movimientosCopia = movimientos.stream().map( e -> {
//
//            return new int[]{e[0],e[1],e[2],e[3]};
//
//        }).collect(Collectors.toList());
//
//        var secuenciaCopia = secuencia.stream().map( e -> {
//
//            return Utilidades.convertirANotacion(e);
//
//        }).collect(Collectors.toList());
//
//        Hilos.comparar(movimientos, secuenciaCopia, estadoTablero);
//
//
//    }

//    public void setSecuencia(List<int[]> secuencia) {
//        search.secuencia = secuencia;
//    }


}
