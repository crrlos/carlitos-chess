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

/**
 *
 * @author carlos
 */
public class Search {
    
    private final Pieza[][] tablero;
    private EstadoTablero estadoTablero;

    public Search(Pieza[][] tablero, EstadoTablero estado) {
        this.tablero = tablero;
        this.estadoTablero = estado;
    }
    
    private void ActualizarTablero(int[] movimiento){
        
        int filaInicio = movimiento[0];
        int colInicio = movimiento[1];
        int filaFinal = movimiento[2];
        int colFinal = movimiento[3];
        
        
        estadoTablero.PiezaCapturada = null;
        estadoTablero.TipoMovimiento = -1;

        var pieza = tablero[filaInicio][colInicio];

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
            estadoTablero.capturas++;
       }
       
       
       tablero[filaFinal][colFinal] = pieza;
       tablero[filaInicio][colInicio] = null;
       
       estadoTablero.AlPaso = false;
       
       if(estadoTablero.TipoMovimiento == -1)
            estadoTablero.TipoMovimiento = 0;
       
       
      
    }
    
    public void perftSearch(int deep, EstadoTablero estado, Acumulador acumulador, boolean reset) throws CloneNotSupportedException{
        
        if(deep == 0) {
            acumulador.contador++;
            acumulador.contadorPerft++;
            return;
        }
         estadoTablero = estado.clone();
        
        var movimientos = Generador.generarMovimientos(tablero, estadoTablero);
        
        for(var mov : movimientos){
            
            ActualizarTablero(mov);
            
            estadoTablero.TurnoBlanco = !estadoTablero.TurnoBlanco;
            
            
            perftSearch(deep - 1, estadoTablero.clone(),acumulador,false);
            
            RevertirMovimiento(mov[0],mov[1],mov[2],mov[3],estado.TurnoBlanco,estadoTablero);
            
            estadoTablero =  estado.clone(); //vuelve a ser el original
           
          if(reset){
               System.out.println(Utilidades.ConvertirANotacion(mov) + " " + acumulador.contadorPerft);
                acumulador.contadorPerft = 0;
          }
        }
        
    }
    
    class Acumulador{
        int contadorPerft;
        int contador;
    }
     public void perft(int deep) throws CloneNotSupportedException, IOException{
         var acumulador = new Acumulador();
         perftSearch(deep, estadoTablero,acumulador,true);
         System.out.println(acumulador.contador);
       
     }
    
    
      private void RevertirMovimiento(int fi, int ci, int fd, int cd,boolean turnoBlanco, EstadoTablero estado){
        
       
        
        
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
}
