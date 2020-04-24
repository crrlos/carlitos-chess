/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.Peon;
import com.wolf.carlitos.Piezas.Pieza;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author carlos
 */
public class search {
    public static List<int[]> secuencia =  new ArrayList<>();
    private final Pieza[][] tablero;
    private EstadoTablero estadoTablero;

    public search(Pieza[][] tablero, EstadoTablero estado) throws CloneNotSupportedException {
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
            comprobar(movimientos, secuencia);
        }
        
        for(var mov : movimientos){
            
            secuencia.add(mov);
            
            Utilidades.actualizarTablero(tablero, estadoTablero, mov);
            
            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
            
            
            perftSearch(deep - 1, estadoTablero.clone(),acumulador,false);
            
            revertirMovimiento(mov[0],mov[1],mov[2],mov[3],estado.turnoBlanco,estadoTablero);
            
            estadoTablero =  estado.clone(); //vuelve a ser el original
           
          if(reset){
               System.out.println(Utilidades.convertirANotacion(mov) + " " + acumulador.contadorPerft);
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

         
       
     }
    
    
    private void revertirMovimiento(int fi, int ci, int fd, int cd, boolean turnoBlanco, EstadoTablero estado){
        
       
        var pieza = tablero[fd][cd];
        
        
        var iterator = estadoTablero.trayectorias.listIterator(estado.trayectorias.size());
        
        while(iterator.hasPrevious()){
            var ele =  iterator.previous();
            if(ele.pieza == pieza){
                iterator.remove();
            }
        }
        
        
        
        switch(estado.tipoMovimiento){
            case 0: //mov
                tablero[fi][ci] = tablero[fd][cd];
                break;
            case 1: //al paso
                tablero[fi][cd] = estadoTablero.piezaALPaso;
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
                    estadoTablero.posicionReyBlanco[0] = fi;
                    estadoTablero.posicionReyBlanco[1] =ci;
                }else{
                    estadoTablero.posicionReyNegro[0] =fi;
                    estadoTablero.posicionReyNegro[1] = ci;
                }
                
                break;
            case 100:
                tablero[fi][ci] = tablero[fd][cd];
                if(turnoBlanco){
                    estadoTablero.posicionReyBlanco[0] = fi;
                    estadoTablero.posicionReyBlanco[1] =ci;
                }else{
                    estadoTablero.posicionReyNegro[0] =fi;
                    estadoTablero.posicionReyNegro[1] = ci;
                }
                
              
        }
       
        tablero[fd][cd] = estado.piezaCapturada;


        //quitar pieza de listas de ataque
        for (int i = 0; i < estadoTablero.trayectorias.size(); i++) {
            var t =  estadoTablero.trayectorias.get(i);
            t.piezasAtacadas.remove(pieza);
        }

    }
    public int mini(int nivel, EstadoTablero estado) throws CloneNotSupportedException, IOException{
        
        
        
        if(nivel == 0) return 0;
        
        int eval = 1000;
        var movimientos = Generador.generarMovimientos(tablero, estado);
        
        estadoTablero = estado.clone();
        
        for(var mov : movimientos){
            
           Utilidades.actualizarTablero(tablero, estadoTablero, mov);
            
            estadoTablero.turnoBlanco = !estado.turnoBlanco;
            
            int evaluacion = maxi(nivel -1,estadoTablero.clone());
            
            if(evaluacion < eval)
                eval = evaluacion;
            
            revertirMovimiento(mov[0],mov[1],mov[2],mov[3],estado.turnoBlanco,estadoTablero);
            estadoTablero = (EstadoTablero) estado.clone();
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
            
            estadoTablero.turnoBlanco = !estado.turnoBlanco;
            
            int evaluacion = mini(nivel -1,estadoTablero.clone());
            
            if(evaluacion > eval)
                eval = evaluacion;
            
            revertirMovimiento(mov[0],mov[1],mov[2],mov[3],estado.turnoBlanco,estadoTablero);
            estadoTablero = (EstadoTablero) estado.clone();
        }
        return eval;
    }
    public int[] search(int n) throws CloneNotSupportedException, IOException{
       
       var estadoOriginal = estadoTablero.clone();//copia original
       int valoracion = estadoOriginal.turnoBlanco ? -1000 : 1000;
       int pos = 0;
       
       var movimientos = Generador.generarMovimientos(tablero, estadoOriginal);
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
           
           revertirMovimiento(mov[0],mov[1],mov[2],mov[3],estadoOriginal.turnoBlanco,estadoLocal);
           estadoTablero =  estadoOriginal.clone(); //vuelve a ser el original
       }
       return movimientos.get(pos);
   }
    private void comprobar(List<int[]> movimientos, List<int[]> secuencia){
        var movimientosCopia = movimientos.stream().map( e -> {
        
            return new int[]{e[0],e[1],e[2],e[3]};
        
        }).collect(Collectors.toList());
        
        var secuenciaCopia = secuencia.stream().map( e -> {
        
            return Utilidades.convertirANotacion(e);
        
        }).collect(Collectors.toList());
        
        Hilos.comparar(movimientos, secuenciaCopia, estadoTablero);
        
        
    }

    public void setSecuencia(List<int[]> secuencia) {
        this.secuencia = secuencia;
    }
    
    
}
