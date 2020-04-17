/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.Alfil;
import com.wolf.carlitos.Piezas.Caballo;
import com.wolf.carlitos.Piezas.Dama;
import com.wolf.carlitos.Piezas.Peon;
import com.wolf.carlitos.Piezas.Pieza;
import com.wolf.carlitos.Piezas.Rey;
import com.wolf.carlitos.Piezas.Torre;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author carlos
 */
public class Search {
    private List<int[]> secuencia =  new ArrayList<>();
    private final Pieza[][] tablero;
    private EstadoTablero estadoTablero;

    public Search(Pieza[][] tablero, EstadoTablero estado) throws CloneNotSupportedException {
        this.tablero = tablero;
        this.estadoTablero = estado.clone();
    }
    private void actualizarTrayectorias(Pieza pieza, int[] movimiento) {

        int filaInicio = movimiento[0];
        int colInicio = movimiento[1];
        int filaFinal = movimiento[2];
        int colFinal = movimiento[3];

        //calculos con trayectorias
        var ubicacionRey = !estadoTablero.TurnoBlanco
                ? estadoTablero.PosicionReyBlanco
                : estadoTablero.PosicionReyNegro;

        //posicion de la pieza
        int x1 = filaFinal;
        int y1 = colFinal;

        //posicion del rey
        int x2 = ubicacionRey[0];
        int y2 = ubicacionRey[1];
        

        // si la pendiente es 1 es trayectoria diagonal
        if ((pieza instanceof Dama || pieza instanceof Alfil) && (x2 -x1) != 0) {
            
            var pendiente = false;
            pendiente = Math.abs((double)(y2 - y1) / (x2 - x1)) == 1;
            
           
            
            if (pendiente) {
                var trayectoria = new Trayectoria(pieza,x1, y1);
                estadoTablero.trayectorias.add(trayectoria);
                
                var jaque = true;
                //si hay pieza amiga terminar
                if (x1 < x2 && y1 > y2) {
                    //IA
                    for (int i = x1 + 1; i < x2; i++) {
                        var p = tablero[i][y1 - (i - x1)];
                        if (p != null) {
                            if (p.EsBlanca() != pieza.EsBlanca()) {
                                jaque = false;
                                trayectoria.piezasAtacadas++;
                            } else {
                                jaque = false;
                                break;
                            }
                        }
                    }
                } else if (x1 < x2 && y1 < y2) {
                    //DA
                    for (int i = x1 + 1; i < x2; i++) {
                        var p = tablero[i][y1 + (i - x1)];
                        if (p != null) {
                            if (p.EsBlanca() != pieza.EsBlanca()) {
                                jaque = false;
                               
                                trayectoria.piezasAtacadas++;
                            } else {
                                jaque = false;
                                break;
                            }
                        }
                    }
                } else if (x1 > x2 && y1 < y2) {
                    //DAB
                    for (int i = x1 - 1; i > x2; i--) {
                        var p = tablero[i][y1 + (x1 - i)];
                        if (p != null) {
                            if (p.EsBlanca() != pieza.EsBlanca()) {
                                jaque = false;
                                
                                trayectoria.piezasAtacadas++;
                            } else {
                                jaque = false;
                                break;
                            }
                        }
                    }
                }
                else if (x1 > x2 && y1 > y2) {
                    //IAB
                    for (int i = x1 - 1; i > x2; i--) {
                        var p = tablero[i][y1 - (x1 - i)];
                        if (p != null) {
                            if (p.EsBlanca() != pieza.EsBlanca()) {
                                jaque = false;
                               
                                trayectoria.piezasAtacadas++;
                            } else {
                                jaque = false;
                                break;
                            }
                        }
                    }
                } 
                estadoTablero.reyEnJaque = jaque;
                if(jaque){
                    estadoTablero.piezaJaque = pieza;
                }

            }
        }

//        if ((pieza instanceof Torre || pieza instanceof Dama) && (x1 == x2 || y1 == y2)) {
//            //trayectoria recta
//            var trayectoria =new Trayectoria(x1, y1, x2, y2, true);
//                Juego.estadoTablero.trayectorias.put(pieza, trayectoria);
//                tablero[y2][x2].setTrayectoria(pieza);
//                
//            //Juego.ImprimirPosicicion();
//            
//            if (x1 < x2) {//derecha
//                for (int i = x1 + 1; i < x2; i++) {
//                    var p = tablero[y1][i];
//                    if (p != null) {
//                        if (p.EsBlanca() != pieza.EsBlanca()) {
//                            p.setTrayectoria(pieza);
//                            trayectoria.piezas.add(p);
//                        } else {
//                            break;
//                        }
//                    }
//                }
//            } else if (x1 > x2) {
//                //izquierda
//                for (int i = x1 - 1; i > x2; i--) {
//                    var p = tablero[y1][i];
//                    if (p != null) {
//                        if (p.EsBlanca() != pieza.EsBlanca()) {
//                            p.setTrayectoria(pieza);
//                            trayectoria.piezas.add(p);
//                        } else {
//                            break;
//                        }
//                    }
//                }
//            } else if (y1 > y2) {
//                for (int i = y1 - 1; i > y2; i--) {
//                    var p = tablero[i][x1];
//                    if (p != null) {
//                        if (p.EsBlanca() != pieza.EsBlanca()) {
//                            p.setTrayectoria(pieza);
//                            trayectoria.piezas.add(p);
//                        } else {
//                            break;
//                        }
//                    }
//                }
//            } else if (y1 < y2) {
//                //arriba
//                for (int i = y1 + 1; i < y2; i++) {
//                    var p = tablero[i][x1];
//                    if (p != null) {
//                        if (p.EsBlanca() != pieza.EsBlanca()) {
//                            p.setTrayectoria(pieza);
//                            trayectoria.piezas.add(p);
//                        } else {
//                            break;
//                        }
//                    }
//                }
//            }
//        }
        //fin calculos con trayectorias
    }
    private void ActualizarTablero(int[] movimiento){
        
        int filaInicio = movimiento[0];
        int filaFinal = 0;
        int colInicio = movimiento[1];
        try{
        filaFinal = movimiento[2];
        }catch(Exception es){
        
            System.out.println("excepcion");
            Utilidades.ImprimirPosicicion(tablero);
        
        
        }
        int colFinal = movimiento[3];
        
        var pieza = tablero[filaInicio][colInicio];
        
        estadoTablero.reyEnJaque = false;
        
        actualizarTrayectorias(pieza, movimiento);
        
        estadoTablero.PiezaCapturada = null;
        estadoTablero.TipoMovimiento = -1;
        
        if(pieza instanceof Peon){

            if(Math.abs(filaInicio - filaFinal) == 2){
                estadoTablero.AlPaso = true;
                estadoTablero.PiezaALPaso = pieza;
                
                tablero[filaFinal][colFinal] = pieza;
                tablero[filaInicio][colInicio] = null;
                estadoTablero.TipoMovimiento = 0;
                return;
            }
            if(estadoTablero.AlPaso){
                if(colFinal > colInicio || colFinal < colInicio){
                    if(tablero[filaInicio][colFinal] == estadoTablero.PiezaALPaso){
                        tablero[filaInicio][colFinal] = null;
                        estadoTablero.TipoMovimiento = 1;
                    }
                }
                estadoTablero.AlPaso = false;
            }
            
            if(filaFinal == 7 || filaFinal == 0){
                switch(movimiento[4]){
                    case 1:
                        pieza = new Dama(estadoTablero.TurnoBlanco);
                        break;
                     case 2:
                        pieza = new Torre(estadoTablero.TurnoBlanco);
                        break;
                     case 3:
                        pieza = new Caballo(estadoTablero.TurnoBlanco);
                        break;
                    case 4:
                        pieza = new Alfil(estadoTablero.TurnoBlanco);
                        break;
                }
                estadoTablero.TipoMovimiento = 2;
            }
            
         }
         else
         if(pieza instanceof Rey){
            // en los enroques solo se mueven las torres por ser el movimiento especial
            if(Math.abs(colInicio - colFinal) == 2){
                if(pieza.EsBlanca()){
                    if(colFinal == 6){//enroque corto
                        tablero[0][5] = tablero[0][7];
                        tablero[0][7] = null;
                    }else {//enroque largo
                        tablero[0][3] = tablero[0][0];
                        tablero[0][0] = null;
                    }
                }else{
                    if(colFinal == 6){//enroque corto
                        tablero[7][5] = tablero[7][7];
                        tablero[7][7] = null;
                    }else {//enroque largo
                        tablero[7][3] = tablero[7][0];
                        tablero[7][0] = null;
                    }
                }
                estadoTablero.TipoMovimiento = 3;
            }else{
                estadoTablero.TipoMovimiento = 100;
            }
            if(pieza.EsBlanca()){
                estadoTablero.EnroqueCBlanco = estadoTablero.EnroqueLBlanco = false;
                estadoTablero.PosicionReyBlanco[0] = filaFinal;
                estadoTablero.PosicionReyBlanco[1] = colFinal;
            }
            else{
                estadoTablero.EnroqueCNegro  = estadoTablero.EnroqueLNegro = false;
                estadoTablero.PosicionReyNegro[0] = filaFinal;
                estadoTablero.PosicionReyNegro[1] = colFinal;
            }
            
            
         }
         else
         if(pieza instanceof Torre){
             if(colInicio == 7)
             {
                 if(pieza.EsBlanca())
                     estadoTablero.EnroqueCBlanco = false;
                 else
                     estadoTablero.EnroqueCNegro = false;
             }else if(colInicio == 0)
                 if(pieza.EsBlanca())
                     estadoTablero.EnroqueLBlanco = false;
                 else
                     estadoTablero.EnroqueLNegro = false;
         }
         
       if(tablero[filaFinal][colFinal] != null){
            estadoTablero.PiezaCapturada = tablero[filaFinal][colFinal];
            
            if(estadoTablero.PiezaCapturada instanceof Torre){
                if(colFinal == 7){
                    if(estadoTablero.TurnoBlanco){
                        estadoTablero.EnroqueCNegro = false;
                    }else{
                        estadoTablero.EnroqueCBlanco = false;
                    }
                }else if(colFinal == 0){
                     if(estadoTablero.TurnoBlanco){
                        estadoTablero.EnroqueLNegro = false;
                    }else{
                        estadoTablero.EnroqueLBlanco = false;
                    }
                }
            }
       }
       
       
       tablero[filaFinal][colFinal] = pieza;
       tablero[filaInicio][colInicio] = null;
       
       estadoTablero.AlPaso = false;
       
       if(estadoTablero.TipoMovimiento == -1)
            estadoTablero.TipoMovimiento = 0;
       
       
      
    }
    
    private void perftSearch(int deep, EstadoTablero estado, Acumulador acumulador, boolean reset) throws CloneNotSupportedException{
        
        if(deep == 0) {
            acumulador.contador++;
            acumulador.contadorPerft++;
            return;
        }
        
        estadoTablero = estado.clone();
        
        var movimientos = Generador.generarMovimientos(tablero, estadoTablero);
        
        //comprobar(movimientos, secuencia);
        
        for(var mov : movimientos){
            
            secuencia.add(mov);
            
            ActualizarTablero(mov);
            
            estadoTablero.TurnoBlanco = !estadoTablero.TurnoBlanco;
            
            
            perftSearch(deep - 1, estadoTablero.clone(),acumulador,false);
            
            RevertirMovimiento(mov[0],mov[1],mov[2],mov[3],estado.TurnoBlanco,estadoTablero);
            
            estadoTablero =  estado.clone(); //vuelve a ser el original
           
          if(reset){
               System.out.println(Utilidades.ConvertirANotacion(mov) + " " + acumulador.contadorPerft);
                acumulador.contadorPerft = 0;
          }
          secuencia.remove(secuencia.size() -1);
        }
        
    }
    
    class Acumulador{
        int contadorPerft;
        int contador;
    }
     public void perft(int deep) throws CloneNotSupportedException, IOException{
         var acumulador = new Acumulador();
         var tinicio =  System.currentTimeMillis();
         perftSearch(deep, estadoTablero,acumulador,true);
         System.out.println(acumulador.contador);
         
         var tfin = (System.currentTimeMillis() - tinicio) / 1000;
         
         //System.out.println("tiempo: " + tfin+ " segundos");
         
        // System.out.println("Velocidad: " + acumulador.contador / tfin + " n/s");
         
       
     }
    
    
    private void RevertirMovimiento(int fi, int ci, int fd, int cd,boolean turnoBlanco, EstadoTablero estado){
        
       
        var pieza = tablero[fd][cd];
        
        
        var iterator = estadoTablero.trayectorias.listIterator(estado.trayectorias.size());
        
        while(iterator.hasPrevious()){
            var ele =  iterator.previous();
            if(ele.pieza == pieza){
                iterator.remove();
            }
        }
        
        
        
        switch(estado.TipoMovimiento){
            case 0: //mov
                tablero[fi][ci] = tablero[fd][cd];
                break;
            case 1: //al paso
                tablero[fi][cd] = new Peon(!turnoBlanco);
                tablero[fi][ci] = tablero[fd][cd];
                break;
            case 2: //promocion
                tablero[fi][ci] = new Peon(turnoBlanco);
                break;
            case 3: //enroque
                tablero[fi][ci] = tablero[fd][cd];
                tablero[fd][cd] = null;
                if(cd > ci) {
                    tablero[fi][7] = tablero[fi][cd -1];
                    tablero[fi][cd - 1] = null;
                }
                else {
                    tablero[fi][0] = tablero[fi][cd + 1];
                    tablero[fi][cd + 1] = null;
                }
                
                if(turnoBlanco){
                    estadoTablero.PosicionReyBlanco[0] = fi;
                    estadoTablero.PosicionReyBlanco[1] =ci;
                }else{
                    estadoTablero.PosicionReyNegro[0] =fi;
                    estadoTablero.PosicionReyNegro[1] = ci;
                }
                
                break;
            case 100:
                tablero[fi][ci] = tablero[fd][cd];
                if(turnoBlanco){
                    estadoTablero.PosicionReyBlanco[0] = fi;
                    estadoTablero.PosicionReyBlanco[1] =ci;
                }else{
                    estadoTablero.PosicionReyNegro[0] =fi;
                    estadoTablero.PosicionReyNegro[1] = ci;
                }
                
              
        }
       
        tablero[fd][cd] = estado.PiezaCapturada;
    }
    public int Mini(int nivel,EstadoTablero estado) throws CloneNotSupportedException, IOException{
        
        
        
        if(nivel == 0) return 0;
        
        int eval = 1000;
        var movimientos = Generador.generarMovimientos(tablero, estado);
        
        estadoTablero = estado.clone();
        
        for(var mov : movimientos){
            
            ActualizarTablero(mov);
            
            estadoTablero.TurnoBlanco = !estado.TurnoBlanco;
            
            int evaluacion = Maxi(nivel -1,estadoTablero.clone());
            
            if(evaluacion < eval)
                eval = evaluacion;
            
            RevertirMovimiento(mov[0],mov[1],mov[2],mov[3],estado.TurnoBlanco,estadoTablero);
            estadoTablero = (EstadoTablero) estado.clone();
        }
       
        return eval;
    }
    
    
    public int Maxi(int nivel, EstadoTablero estado) throws CloneNotSupportedException, IOException{
        
        if(nivel == 0) return 0;
        
        int eval = -1000;
        
        
        var movimientos = Generador.generarMovimientos(tablero, estado);
        
        estadoTablero = estado.clone();
        
        for(var mov : movimientos){
           
            ActualizarTablero(mov);
            
            estadoTablero.TurnoBlanco = !estado.TurnoBlanco;
            
            int evaluacion = Mini(nivel -1,estadoTablero.clone());
            
            if(evaluacion > eval)
                eval = evaluacion;
            
            RevertirMovimiento(mov[0],mov[1],mov[2],mov[3],estado.TurnoBlanco,estadoTablero);
            estadoTablero = (EstadoTablero) estado.clone();
        }
        return eval;
    }
    public int[] search(int n) throws CloneNotSupportedException, IOException{
       
       var estadoOriginal = estadoTablero.clone();//copia original
       int valoracion = estadoOriginal.TurnoBlanco ? -1000 : 1000;
       int pos = 0;
       
       var movimientos = Generador.generarMovimientos(tablero, estadoOriginal);
       for (int i = 0; i < movimientos.size() ; i++) {
           
            var mov = movimientos.get(i);
            
            ActualizarTablero(mov);//modifica el original
            
            
            estadoTablero.TurnoBlanco = !estadoTablero.TurnoBlanco;
            
            var estadoLocal = (EstadoTablero) estadoTablero.clone();
            
            int eval = estadoOriginal.TurnoBlanco ? Mini(n,estadoLocal) : Maxi(n,estadoLocal);
            
            if(estadoOriginal.TurnoBlanco){
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
           
           RevertirMovimiento(mov[0],mov[1],mov[2],mov[3],estadoOriginal.TurnoBlanco,estadoLocal);
           estadoTablero =  estadoOriginal.clone(); //vuelve a ser el original
       }
       return movimientos.get(pos);
   }
    private void comprobar(List<int[]> movimientos, List<int[]> secuencia){
        var movimientosCopia = movimientos.stream().map( e -> {
        
            return new int[]{e[0],e[1],e[2],e[3]};
        
        }).collect(Collectors.toList());
        
        var secuenciaCopia = secuencia.stream().map( e -> {
        
            return Utilidades.ConvertirANotacion(e);
        
        }).collect(Collectors.toList());
        
        Hilos.comparar(movimientos, secuenciaCopia, estadoTablero);
        
        
    }

    public void setSecuencia(List<int[]> secuencia) {
        this.secuencia = secuencia;
    }
    
    
}
