/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.Peon;
import com.wolf.carlitos.Piezas.Pieza;
import com.wolf.carlitos.Piezas.Rey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Utilidades.actualizarTablero;
import static java.lang.Math.abs;

/**
 *
 * @author carlos
 */
class Acumulador{
    long contadorPerft;
    long contador;
}
public class Search {
    public static List<int[]> secuencia =  new ArrayList<>();
    private final Pieza[] tablero;
    private EstadoTablero estadoTablero;

    public Search(Pieza[] tablero, EstadoTablero estado) throws CloneNotSupportedException {
        this.tablero = tablero;
        this.estadoTablero = estado.clone();
    }


    private void perftSearch(int deep, EstadoTablero estado, Acumulador acumulador, boolean reset) throws CloneNotSupportedException{

        if(deep == 0) {
            acumulador.contador++;
            acumulador.contadorPerft++;
            return;
        }

        estadoTablero = estado.clone();

        var movimientos = Generador.generarMovimientos(tablero, estadoTablero);

        if(Config.debug){
            //comprobar(movimientos, secuencia);
        }

        for(var mov : movimientos){

            secuencia.add(mov);

            actualizarTablero(tablero, estadoTablero, mov);

            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;

            perftSearch(deep - 1, estadoTablero.clone(),acumulador,false);

            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;

            revertirMovimiento(mov,estadoTablero);

            estadoTablero =  estado.clone(); //vuelve a ser el original

          if(reset){
               System.out.println(Utilidades.convertirANotacion(mov) + " " + acumulador.contadorPerft);
                acumulador.contadorPerft = 0;
          }
          secuencia.remove(secuencia.size() -1);
        }

    }

    public void perft(int deep) throws CloneNotSupportedException, IOException{
         var acumulador = new Acumulador();
         var tinicio =  System.currentTimeMillis();
         perftSearch(deep, estadoTablero,acumulador,true);
         System.out.println(acumulador.contador);

         var tfin = (System.currentTimeMillis() - tinicio) / 1000;

     }


    private void revertirMovimiento(int[] movimiento, EstadoTablero estado){

        int inicio = movimiento[0];
        int destino = movimiento[1];

        boolean turnoBlanco = estado.turnoBlanco;

        switch(estado.tipoMovimiento){

            case MOVIMIENTO_NORMAL:
            case MOVIMIENTO_REY:
                tablero[inicio] = tablero[destino];
                break;
            case AL_PASO:
                int posicionPaso = estadoTablero.turnoBlanco ? destino - 8 : destino + 8;

                tablero[posicionPaso] = estadoTablero.piezaALPaso;
                tablero[inicio] = tablero[destino];

                break;
            case PROMOCION:
                tablero[inicio] = new Peon(turnoBlanco);
                break;
            case ENROQUE:

                tablero[inicio] = tablero[destino];
                tablero[destino] = null;

                if(destino == G1 || destino == G8){
                    tablero[turnoBlanco ? H1 : H8] = tablero[turnoBlanco ? F1 : F8];
                    tablero[turnoBlanco ? F1 : F8] = null;
                }

                if(destino == C1 || destino == C8){
                    tablero[turnoBlanco ? A1 : A8] = tablero[turnoBlanco ? D1 : D8];
                    tablero[turnoBlanco ? D1 : D8] = null;
                }

                break;

        }

        tablero[destino] = estado.piezaCapturada;
    }
    public int mini(int nivel, EstadoTablero estado) throws CloneNotSupportedException, IOException{


        if(nivel == 0) return 0;

        int eval = 1000;
        var movimientos = Generador.generarMovimientos(tablero, estado);

        estadoTablero = estado.clone();

        for(var mov : movimientos){

           Utilidades.actualizarTablero(tablero, estadoTablero, mov);

            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;

            int evaluacion = maxi(nivel -1,estadoTablero.clone());

            if(evaluacion < eval)
                eval = evaluacion;
            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
            revertirMovimiento(mov,estadoTablero);
            estadoTablero =  estado.clone();
        }

        return eval;
    }


    public int maxi(int nivel, EstadoTablero estado) throws CloneNotSupportedException, IOException{

        if(nivel == 0) return 0;

        int eval = -1000;


        var movimientos = Generador.generarMovimientos(tablero, estado);

        estadoTablero = estado.clone();

        for(var mov : movimientos){

            Utilidades.actualizarTablero(tablero, estadoTablero, mov);

            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;

            int evaluacion = mini(nivel -1,estadoTablero.clone());

            if(evaluacion > eval)
                eval = evaluacion;
            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
            revertirMovimiento(mov,estadoTablero);
            estadoTablero =  estado.clone();
        }
        return eval;
    }
    public int[] search(int n) throws CloneNotSupportedException, IOException{

       var estadoOriginal = estadoTablero.clone();//copia original
       int valoracion = estadoOriginal.turnoBlanco ? -1000 : 1000;
       int pos = 0;

       var movimientos = Generador.generarMovimientos(tablero, estadoTablero);
       for (int i = 0; i < movimientos.size() ; i++) {

            var mov = movimientos.get(i);

            Utilidades.actualizarTablero(tablero, estadoTablero, mov);


            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;

            var estadoLocal = (EstadoTablero) estadoTablero.clone();

            int eval = estadoOriginal.turnoBlanco ? mini(n,estadoLocal) : maxi(n,estadoLocal);

            if(estadoOriginal.turnoBlanco){
                if(eval > valoracion){
                   valoracion = eval;
                   pos = i;
                }
            }
            else{
                 if(eval < valoracion){
                   valoracion = eval;
                   pos = i;
                }
            }
           estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
           revertirMovimiento(mov,estadoTablero);
           estadoTablero =  estadoOriginal.clone(); //vuelve a ser el original
       }
       return movimientos.get(pos);
   }
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
