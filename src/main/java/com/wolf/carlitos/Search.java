/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.*;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static com.wolf.carlitos.Ponderaciones.*;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Utilidades.actualizarTablero;
import static com.wolf.carlitos.Utilidades.reyEnJaque;
import static java.lang.Math.abs;

/**
 * @author carlos
 */
class Acumulador {
    long contadorPerft;
    long contador;
}

public class Search {
    public static List<int[]> secuencia = new ArrayList<>();
    private final Pieza[] tablero;
    private EstadoTablero estadoTablero;


    public Search(Pieza[] tablero, EstadoTablero estado) throws CloneNotSupportedException {
        this.tablero = tablero;
        this.estadoTablero = estado.clone();
    }


    private void perftSearch(int deep, EstadoTablero estado, Acumulador acumulador, boolean reset) throws CloneNotSupportedException {

        if (deep == 0) {
            acumulador.contador++;
            acumulador.contadorPerft++;
            return;
        }

        estadoTablero = estado.clone();

        var movimientos = new Generador().generarMovimientos(tablero, estadoTablero);

        if (Config.debug) {
            //comprobar(movimientos, secuencia);
        }

        for (var mov : movimientos) {

            secuencia.add(mov);

            actualizarTablero(tablero, estadoTablero, mov);

            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;

            perftSearch(deep - 1, estadoTablero.clone(), acumulador, false);

            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;

            revertirMovimiento(mov, estadoTablero,tablero);

            estadoTablero = estado.clone(); //vuelve a ser el original

            if (reset) {
                System.out.println(Utilidades.convertirANotacion(mov) + " " + acumulador.contadorPerft);
                acumulador.contadorPerft = 0;
            }
            secuencia.remove(secuencia.size() - 1);
        }

    }

    public void perft(int deep) throws CloneNotSupportedException, IOException {
        var acumulador = new Acumulador();
        var tinicio = System.currentTimeMillis();
        perftSearch(deep, estadoTablero, acumulador, true);
        System.out.println(acumulador.contador);

        var tfin = (System.currentTimeMillis() - tinicio) / 1000;

    }


    private void revertirMovimiento(int[] movimiento, EstadoTablero estado,Pieza[] tablero) {

        int inicio = movimiento[0];
        int destino = movimiento[1];

        boolean turnoBlanco = estado.turnoBlanco;

        switch (estado.tipoMovimiento) {

            case MOVIMIENTO_NORMAL:
            case MOVIMIENTO_REY:
                tablero[inicio] = tablero[destino];
                break;
            case AL_PASO:
                int posicionPaso = estado.turnoBlanco ? destino - 8 : destino + 8;

                tablero[posicionPaso] = estado.piezaALPaso;
                tablero[inicio] = tablero[destino];

                break;
            case PROMOCION:
                tablero[inicio] = new Peon(turnoBlanco);
                break;
            case ENROQUE:

                tablero[inicio] = tablero[destino];
                tablero[destino] = null;

                if (destino == G1 || destino == G8) {
                    tablero[turnoBlanco ? H1 : H8] = tablero[turnoBlanco ? F1 : F8];
                    tablero[turnoBlanco ? F1 : F8] = null;
                }

                if (destino == C1 || destino == C8) {
                    tablero[turnoBlanco ? A1 : A8] = tablero[turnoBlanco ? D1 : D8];
                    tablero[turnoBlanco ? D1 : D8] = null;
                }

                break;

        }

        tablero[destino] = estado.piezaCapturada;
    }

    public int mini(int nivel, EstadoTablero estado, Pieza[] tablero) throws CloneNotSupportedException, IOException {


        if (nivel == 0) return evaluar(tablero);

        int eval = 1_000_000;
        var movimientos = new Generador().generarMovimientos(tablero, estado);

        if (movimientos.isEmpty()) {
            if (reyEnJaque(tablero, estado) != NO_JAQUE) return MATE;
            else return AHOGADO;
        }


        var estadoLocal = estado.clone();

        for (var mov : movimientos) {

            Utilidades.actualizarTablero(tablero, estadoLocal, mov);

            estadoLocal.turnoBlanco = !estadoLocal.turnoBlanco;

            int evaluacion = maxi(nivel - 1, estadoLocal.clone(), tablero);

            if (evaluacion < eval)
                eval = evaluacion;
            estadoLocal.turnoBlanco = !estadoLocal.turnoBlanco;
            revertirMovimiento(mov, estadoLocal,tablero);
            estadoLocal = estado.clone();
        }

        return eval;
    }

    private int evaluar(Pieza[] tablero) {

        int valorBlancas = 0;
        int valorNegras = 0;

        for (int i = 0; i < 64; i++) {
            var pieza = tablero[i];
            if (pieza != null)
                if (pieza.esBlanca()) {
                    if (pieza instanceof Peon) valorBlancas += ponderacionPeon[flip[i]];
                    else if (pieza instanceof Caballo) valorBlancas += ponderacionCaballo[flip[i]];
                    else if (pieza instanceof Alfil) valorBlancas += ponderacionAlfil[flip[i]];
                    else if (pieza instanceof Dama) valorBlancas += ponderacionDama[flip[i]];
                    else if (pieza instanceof Torre) valorBlancas += ponderacionTorre[flip[i]];
                    else if (pieza instanceof Rey) valorBlancas += ponderacionRey[flip[i]];

                    valorBlancas += pieza.valor();
                } else {
                    if (pieza instanceof Peon) valorNegras -= ponderacionPeon[i];
                    else if (pieza instanceof Caballo) valorNegras -= ponderacionCaballo[i];
                    else if (pieza instanceof Alfil) valorNegras -= ponderacionAlfil[i];
                    else if (pieza instanceof Dama) valorNegras -= ponderacionDama[i];
                    else if (pieza instanceof Torre) valorNegras -= ponderacionTorre[i];
                    else if (pieza instanceof Rey) valorNegras -= ponderacionRey[i];

                    valorNegras += pieza.valor();
                }
        }
        return valorBlancas + valorNegras;

    }

    public int maxi(int nivel, EstadoTablero estado, Pieza[] tablero) throws CloneNotSupportedException, IOException {

        if (nivel == 0) return evaluar(tablero);

        int eval = -1_000_000;


        var movimientos = new Generador().generarMovimientos(tablero, estado);

        if (movimientos.isEmpty()) {
            if (reyEnJaque(tablero, estado) != NO_JAQUE) return MATE;
            else return AHOGADO;
        }

        var estadoLocal = estado.clone();

        for (var mov : movimientos) {

            Utilidades.actualizarTablero(tablero, estadoLocal, mov);

            estadoLocal.turnoBlanco = !estadoLocal.turnoBlanco;

            int evaluacion = mini(nivel - 1, estadoLocal.clone(), tablero);

            if (evaluacion > eval)
                eval = evaluacion;

            estadoLocal.turnoBlanco = !estadoLocal.turnoBlanco;
            revertirMovimiento(mov, estadoLocal,tablero);
            estadoLocal = estado.clone();
        }
        return eval;
    }

    public int[] search(int n) throws CloneNotSupportedException, InterruptedException, ExecutionException {

        var estadoOriginal = estadoTablero.clone();
        int valoracion = estadoOriginal.turnoBlanco ? -1000 : 1000;
        int pos = 0;

        ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(10);
        List<Callable<Integer>> callables = new ArrayList<>();

        var movimientos = new Generador().generarMovimientos(tablero, estadoTablero);

        for (int i = 0; i < movimientos.size(); i++) {
            var mov = movimientos.get(i);
            Utilidades.actualizarTablero(tablero, estadoTablero, mov);
            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
            var arregloCopia = Arrays.copyOf(tablero, tablero.length);

            var clon = estadoTablero.clone();
            callables.add(() -> estadoOriginal.turnoBlanco ? mini(n - 1, clon, arregloCopia) : maxi(n - 1, clon, arregloCopia));

            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
            revertirMovimiento(mov,estadoTablero,tablero);
            estadoTablero = estadoOriginal.clone();

        }

        estadoTablero = estadoOriginal.clone();

        var resultados = WORKER_THREAD_POOL.invokeAll(callables);

        awaitTerminationAfterShutdown(WORKER_THREAD_POOL);





        for (int i = 0; i < resultados.size(); i++) {
            var eval = resultados.get(i).get();

            if (estadoOriginal.turnoBlanco) {
                if (eval > valoracion) {
                    valoracion = eval;
                    pos = i;
                }
            } else {
                if (eval < valoracion) {
                    valoracion = eval;
                    pos = i;
                }
            }

        }


//       for (int i = 0; i < movimientos.size() ; i++) {
//
//            var mov = movimientos.get(i);
//
//            Utilidades.actualizarTablero(tablero, estadoTablero, mov);
//            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
//
//            var estadoLocal = (EstadoTablero) estadoTablero.clone();
//
//            int eval = estadoOriginal.turnoBlanco ? mini(n - 1,estadoLocal) : maxi(n - 1,estadoLocal);
//
//            if(estadoOriginal.turnoBlanco){
//                if(eval > valoracion){
//                   valoracion = eval;
//                   pos = i;
//                }
//            }
//            else{
//                 if(eval < valoracion){
//                   valoracion = eval;
//                   pos = i;
//                }
//            }
//           estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
//           revertirMovimiento(mov,estadoTablero);
//           estadoTablero =  estadoOriginal.clone();
//       }
        return movimientos.get(pos);
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
