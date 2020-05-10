/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;


import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static com.wolf.carlitos.Ponderaciones.*;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Utilidades.*;
import static java.lang.Math.abs;
import static java.lang.Math.tan;

/**
 * @author carlos
 */

public class Search {
    public static List<int[]> secuencia = new ArrayList<>();
    private  int[] pieza;
    private  int[] color;
    private long estadoTablero;

    public Search(int[] pieza, int[] color, long estado) {
        this.pieza = pieza;
        this.color = color;
        this.estadoTablero = estado;
    }


    private void perftSearch(int deep, long estado, Acumulador acumulador, boolean reset) {

        if (deep == 0) {
            acumulador.contador++;
            acumulador.contadorPerft++;
            return;
        }

       var movimientos = new Generador().generarMovimientos(pieza,color, estado);

        for (int i = 0; i < movimientos.size(); i++) {
            var mov = movimientos.get(i);

            long estadoActualizodo = actualizarTablero(pieza,color, estado, mov);

            // cambiar bando
            estadoActualizodo ^= 0b10000;



            perftSearch(deep - 1, estadoActualizodo, acumulador, false);

            // cambiar bando
            estadoActualizodo ^= 0b10000;

            revertirMovimiento(mov, estadoActualizodo, pieza,color);

            if (reset) {
                System.out.println(Utilidades.convertirANotacion(mov) + " " + acumulador.contadorPerft);
                acumulador.contadorPerft = 0;
            }
        }
    }

    public void perft(int deep){
        var acumulador = new Acumulador();
        var tinicio = System.currentTimeMillis();
        perftSearch(deep, estadoTablero, acumulador, true);
        System.out.println(acumulador.contador);

        var tfin = (System.currentTimeMillis() - tinicio) / 1000;

    }


    private void revertirMovimiento(int[] movimiento, long estado, int[] tablero,int[]color) {

        int inicio = movimiento[0];
        int destino = movimiento[1];

        boolean turnoBlanco = esTurnoBlanco(estado);

        switch ((int) (estado >> 18 & 0b111)) {

            case MOVIMIENTO_NORMAL:
            case MOVIMIENTO_REY:
                tablero[inicio] = tablero[destino];
                color[inicio] = color[destino];
                break;
            case AL_PASO:
                int posicionPaso = turnoBlanco ? destino - 8 : destino + 8;

                tablero[posicionPaso] = PEON;
                color[posicionPaso] = (int) (estado >> 12 & 0b1);

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

        // pieza capturada
        tablero[destino] = (int) (estado >> 13 & 0b111);

        color[destino] = (int) (estado >> 13 & 0b111)  == NOPIEZA ? NOCOLOR : (int) (estado >> 16 & 0b11);

    }

//    public int mini(int nivel, EstadoTablero estado, Pieza[] tablero) throws CloneNotSupportedException, IOException {
//
//
//        if (nivel == 0) return evaluar(tablero);
//
//        int eval = 1_000_000;
//        var movimientos = new Generador().generarMovimientos(tablero, estado);
//
//        if (movimientos.isEmpty()) {
//            if (reyEnJaque(tablero, estado) != NO_JAQUE) return MATE;
//            else return AHOGADO;
//        }
//
//
//        var estadoLocal = estado.clone();
//
//        for (int i = 0; i < movimientos.size(); i++) {
//            var mov = movimientos.get(i);
//            Utilidades.actualizarTablero(tablero, estadoLocal, mov);
//
//            estadoLocal.turnoBlanco = !estadoLocal.turnoBlanco;
//
//            int evaluacion = maxi(nivel - 1, estadoLocal.clone(), tablero);
//
//            if (evaluacion < eval)
//                eval = evaluacion;
//            estadoLocal.turnoBlanco = !estadoLocal.turnoBlanco;
//            revertirMovimiento(mov, estadoLocal, tablero);
//            estadoLocal = estado.clone();
//
//        }
//
//        return eval;
//    }

    private int evaluar(Pieza[] tablero) {


        int valorBlancas = 0;
        int valorNegras = 0;

        for (int i = 0; i < 64; i++) {
            var pieza = tablero[i];
            if (pieza == null) continue;

            switch (pieza.tipo) {
                case PEON:
                    valorBlancas += (pieza.esBlanca ? ponderacionPeon[flip[i]] : -ponderacionPeon[i]);
                    break;
                case CABALLO:
                    valorBlancas += (pieza.esBlanca ? ponderacionCaballo[flip[i]] : -ponderacionCaballo[i]);
                    break;
                case ALFIL:
                    valorBlancas += (pieza.esBlanca ? ponderacionAlfil[flip[i]] : -ponderacionAlfil[i]);
                    break;
                case TORRE:
                    valorBlancas += (pieza.esBlanca ? ponderacionTorre[flip[i]] : -ponderacionTorre[i]);
                    break;
                case DAMA:
                    valorBlancas += (pieza.esBlanca ? ponderacionDama[flip[i]] : -ponderacionDama[i]);
                    break;
                case REY:
                    valorBlancas += (pieza.esBlanca ? ponderacionRey[flip[i]] : -ponderacionRey[i]);
                    break;
            }
            if (pieza.esBlanca) valorBlancas += pieza.valor;
            else valorNegras += pieza.valor;
        }

        return valorBlancas + valorNegras;

    }

//    public int maxi(int nivel, EstadoTablero estado, Pieza[] tablero) throws CloneNotSupportedException, IOException {
//
//        if (nivel == 0) return evaluar(tablero);
//
//        int eval = -1_000_000;
//
//
//        var movimientos = new Generador().generarMovimientos(tablero, estado);
//
//        if (movimientos.isEmpty()) {
//            if (reyEnJaque(tablero, estado) != NO_JAQUE) return -MATE;
//            else return AHOGADO;
//        }
//
//        var estadoLocal = estado.clone();
//
//        for (int i = 0; i < movimientos.size(); i++) {
//            var mov = movimientos.get(i);
//            Utilidades.actualizarTablero(tablero, estadoLocal, mov);
//
//            estadoLocal.turnoBlanco = !estadoLocal.turnoBlanco;
//
//            int evaluacion = mini(nivel - 1, estadoLocal.clone(), tablero);
//
//            if (evaluacion > eval)
//                eval = evaluacion;
//
//            estadoLocal.turnoBlanco = !estadoLocal.turnoBlanco;
//            revertirMovimiento(mov, estadoLocal, tablero);
//            estadoLocal = estado.clone();
//        }
//        return eval;
//    }

//    public int[] search(int n) throws CloneNotSupportedException, InterruptedException, ExecutionException, IOException {
//
//        var estadoOriginal = estadoTablero.clone();
//        int valoracion = estadoOriginal.turnoBlanco ? -1000 : 1000;
//        int pos = 0;
//
//        ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(10);
//        List<Callable<Integer>> callables = new ArrayList<>();
//
//        var movimientos = new Generador().generarMovimientos(tablero, estadoTablero);
//
//        for (int i = 0; i < movimientos.size(); i++) {
//            var mov = movimientos.get(i);
//            Utilidades.actualizarTablero(tablero, estadoTablero, mov);
//            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
//            var arregloCopia = Arrays.copyOf(tablero, tablero.length);
//
//            var clon = estadoTablero.clone();
//            callables.add(() -> estadoOriginal.turnoBlanco ? mini(n, clon, arregloCopia) : maxi(n, clon, arregloCopia));
//
//            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
//            revertirMovimiento(mov, estadoTablero, tablero);
//            estadoTablero = estadoOriginal.clone();
//
//        }
//
//        estadoTablero = estadoOriginal.clone();
//
//        var resultados = WORKER_THREAD_POOL.invokeAll(callables);
//        awaitTerminationAfterShutdown(WORKER_THREAD_POOL);
//
//
//        for (int i = 0; i < resultados.size(); i++) {
//            var eval = resultados.get(i).get();
//
//            if (estadoOriginal.turnoBlanco) {
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
//
//
////       for (int i = 0; i < movimientos.size() ; i++) {
////
////            var mov = movimientos.get(i);
////
////            Utilidades.actualizarTablero(tablero, estadoTablero, mov);
////            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
////
////            var estadoLocal = (EstadoTablero) estadoTablero.clone();
////
////            int eval = estadoOriginal.turnoBlanco ? mini(n,estadoLocal,tablero) : maxi(n,estadoLocal,tablero);
////
////            if(estadoOriginal.turnoBlanco){
////                if(eval > valoracion){
////                   valoracion = eval;
////                   pos = i;
////                }
////            }
////            else{
////                 if(eval < valoracion){
////                   valoracion = eval;
////                   pos = i;
////                }
////            }
////           estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
////           revertirMovimiento(mov,estadoTablero,tablero);
////           estadoTablero =  estadoOriginal.clone();
////       }
//        return movimientos.get(pos);
//    }

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
