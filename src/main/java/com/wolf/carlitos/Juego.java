package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.Alfil;
import com.wolf.carlitos.Piezas.Caballo;
import com.wolf.carlitos.Piezas.Dama;
import com.wolf.carlitos.Piezas.Peon;
import com.wolf.carlitos.Piezas.Pieza;
import com.wolf.carlitos.Piezas.Rey;
import com.wolf.carlitos.Piezas.Torre;
import java.io.IOException;



public class Juego {
    public  Pieza[][] tablero;
    public  EstadoTablero estadoTablero;

    
   public Juego(){
        estadoTablero = new  EstadoTablero();
        tablero = new Pieza[8][8];
        
        tablero[0][0] =  new Torre(true);
        tablero[0][7] =  new Torre(true);
        tablero[7][0] =  new Torre(false);
        tablero[7][7] =  new Torre(false);   
        
        tablero[0][1] =  new Caballo(true);
        tablero[0][6] =  new Caballo(true);
        tablero[7][1] =  new Caballo(false);
        tablero[7][6] =  new Caballo(false);   

        tablero[0][2] =  new Alfil(true);
        tablero[0][5] =  new Alfil(true);
        tablero[7][2] =  new Alfil(false);
        tablero[7][5] =  new Alfil(false);   

        tablero[0][3] =  new Dama(true);
        tablero[7][3] =  new Dama(false);   

        tablero[0][4] =  new Rey(true);
        tablero[7][4] =  new Rey(false);   

        for(int i = 0 ; i < 8; i++){
            tablero[1][i] = new Peon(true);
            tablero[6][i] = new Peon(false);
        }
    
    }
   public   void ImprimirPosicicion(){
        for (int i = 7; i >=0 ; i--) {
            for (int j = 0; j < 8 ; j++) {
                var pieza = tablero[i][j];
                System.out.print(pieza != null ? pieza.Nombre() : " ");
                System.out.print("|");
            }
            System.out.println();
        }
        System.out.println("");
    }
   public void EstablecerPosicion(String... movimientos){
        for (var movimiento : movimientos) {
            
            ActualizarTablero(movimiento);
            estadoTablero.TurnoBlanco  = !estadoTablero.TurnoBlanco;
        }
    }
   private void ActualizarTablero(String movimiento){
        
        int colInicio = "abcdefgh".indexOf(movimiento.substring(0,1));
        int filaInicio = "12345678".indexOf(movimiento.substring(1, 2));
        int colFinal =  "abcdefgh".indexOf(movimiento.substring(2, 3));
        int filaFinal = "12345678".indexOf(movimiento.substring(3, 4));
        
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
                switch(movimiento.charAt(4)){
                    case 'q':
                        pieza = new Dama(estadoTablero.TurnoBlanco);
                        break;
                     case 'r':
                        pieza = new Torre(estadoTablero.TurnoBlanco);
                        break;
                     case 'n':
                        pieza = new Caballo(estadoTablero.TurnoBlanco);
                        break;
                    case 'b':
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
       }
       
       
       tablero[filaFinal][colFinal] = pieza;
       tablero[filaInicio][colInicio] = null;
       
       estadoTablero.AlPaso = false;
       
       if(estadoTablero.TipoMovimiento == -1)
            estadoTablero.TipoMovimiento = 0;
       
       
      
    }
   public void perft(int n) throws CloneNotSupportedException, IOException{
       var search = new Search(tablero, estadoTablero);
       search.perft(n);
       
   }
   public String mover(int n) throws CloneNotSupportedException, IOException{
       var search = new Search(tablero, estadoTablero);
       return Utilidades.ConvertirANotacion(search.search(n));
   }
}