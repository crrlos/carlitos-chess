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
            
            Utilidades.actualizarTablero(tablero, estadoTablero, mov);
            
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
            
           Utilidades.actualizarTablero(tablero, estadoTablero, mov);
            
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
           
            Utilidades.actualizarTablero(tablero, estadoTablero, mov);
            
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
            
            Utilidades.actualizarTablero(tablero, estadoTablero, mov);
            
            
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
