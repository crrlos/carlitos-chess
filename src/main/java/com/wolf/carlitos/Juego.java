package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.Alfil;
import com.wolf.carlitos.Piezas.Caballo;
import com.wolf.carlitos.Piezas.Dama;
import com.wolf.carlitos.Piezas.Peon;
import com.wolf.carlitos.Piezas.Pieza;
import com.wolf.carlitos.Piezas.Rey;
import com.wolf.carlitos.Piezas.Torre;
import java.util.ArrayList;
import java.util.Random;



public class Juego {
    public static Juego juego;
    public Pieza[][] tablero;
    public boolean turnoBlanco = true;
    private final String filas = "12345678";
    private final String columnas = "abcdefgh";
    
    public Pieza[] piezasBlancas;
    public Pieza[] piezasNegras;
    public int[] posicionReyNegro;
    public int[] posicionReyBlanco;

    
   public Juego(){
       this.piezasBlancas = new Pieza[16];
       this.piezasNegras = new Pieza[16];
       this.tablero = new Pieza[8][8];
        
        tablero[0][0] = piezasBlancas[0] = new Torre(true);
        tablero[0][7] = piezasBlancas[1] = new Torre(true);
        tablero[7][0] = new Torre(false);
        tablero[7][7] = new Torre(false);   
        
        tablero[0][1] = piezasBlancas[2] = new Caballo(true);
        tablero[0][6] = piezasBlancas[3] = new Caballo(true);
        tablero[7][1] = new Caballo(false);
        tablero[7][6] = new Caballo(false);   

        tablero[0][2] = piezasBlancas[4] = new Alfil(true);
        tablero[0][5] = piezasBlancas[5] = new Alfil(true);
        tablero[7][2] = new Alfil(false);
        tablero[7][5] = new Alfil(false);   

        tablero[0][3] = piezasBlancas[6] = new Dama(true);
        tablero[7][3] = new Dama(false);   

        tablero[0][4] = new Rey(true);
        tablero[7][4] = new Rey(false);   

        for(int i = 0 ; i < 8; i++){
            tablero[1][i] = new Peon(true);
            tablero[6][i] = new Peon(false);
        }
        
       Juego.juego = this;
    
    }
    public void ImprimirPosicicion(){
        for (int i = 7; i >=0 ; i--) {
            for (int j = 0; j < 8 ; j++) {
                var pieza = tablero[i][j];
                System.out.print(pieza != null ? pieza.Nombre() : " ");
                System.out.print("|");
            }
            System.out.println();
        }
    }
    public void EstablecerPosicion(String... movimientos){
        for (var movimiento : movimientos) {
            ActualizarTablero(movimiento);
            turnoBlanco = !turnoBlanco;
        }
    }
    public  String ConvertirANotacion(int[] movimiento){
        var mov = columnas.charAt(movimiento[1]) + "" + filas.charAt(movimiento[0]) +
                  columnas.charAt(movimiento[3]) + "" + filas.charAt(movimiento[2]);
        if(movimiento.length == 5){
            switch(movimiento[4]){
                case 1: return mov + "q";
                case 2: return mov + "r";
                case 3: return mov + "n";
                case 4: return mov + "b";
            }
        }
        return mov;
    }
    public void MovimientosValidos(){
        var movimientos = new ArrayList<int[]>();
        for (int i = 7; i >=0 ; i--) {
            for (int j = 0; j < 8 ; j++) {
                var pieza = tablero[i][j];
                
                if(pieza instanceof Rey || pieza instanceof Peon || pieza instanceof Torre || pieza instanceof Caballo || pieza instanceof Alfil || pieza instanceof Dama){
                     
                     if(turnoBlanco){
                         if(pieza.EsBlanca())
                          movimientos.addAll(pieza.ObtenerMovimientos(tablero, new int[]{i,j}));
                     }else{
                        if(!pieza.EsBlanca())
                        movimientos.addAll(pieza.ObtenerMovimientos(tablero, new int[]{i,j}));
                     }
                    
                }
            }
            
        }
        Random r = new Random();
int low = 0;
int high = movimientos.size() - 1;
int rango = high -low;
int result = rango == 0 ? 0 : r.nextInt(rango) + low;
    System.out.println("bestmove " + ConvertirANotacion(movimientos.get(result)));
    }
    private void ActualizarTablero(String movimiento){
        int colInicio = columnas.indexOf(movimiento.substring(0,1));
        int filaInicio = filas.indexOf(movimiento.substring(1, 2));
        int colFinal = columnas.indexOf(movimiento.substring(2, 3));
        int filaFinal = filas.indexOf(movimiento.substring(3, 4));

        var pieza = this.tablero[filaInicio][colInicio];

         if(pieza instanceof Peon){

            if(Math.abs(filaInicio - filaFinal) == 2){
                EstadoTablero.AlPaso = true;
                EstadoTablero.PiezaALPaso = pieza;
                
                this.tablero[filaFinal][colFinal] = pieza;
                this.tablero[filaInicio][colInicio] = null;
                return;
            }
            if(EstadoTablero.AlPaso){
                if(colFinal > colInicio || colFinal < colInicio){
                    if(tablero[filaInicio][colFinal] == EstadoTablero.PiezaALPaso){
                        tablero[filaInicio][colFinal] = null;
                    }
                }
                EstadoTablero.AlPaso = false;
            }
            
            if(filaFinal == 7 || filaFinal == 0){
                switch(movimiento.charAt(4)){
                    case 'q':
                        pieza = new Dama(turnoBlanco);
                        break;
                     case 'r':
                        pieza = new Torre(turnoBlanco);
                        break;
                     case 'n':
                        pieza = new Caballo(turnoBlanco);
                        break;
                    case 'b':
                        pieza = new Alfil(turnoBlanco);
                        break;
                }
            }
            
         }

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
            }
            if(pieza.EsBlanca())
                EstadoTablero.EnroqueCBlanco = EstadoTablero.EnroqueLBlanco = false;
            else
                EstadoTablero.EnroqueCNegro  = EstadoTablero.EnroqueLNegro = false;
         }
         
         if(pieza instanceof Torre){
             if(colInicio == 7)
             {
                 if(pieza.EsBlanca())
                     EstadoTablero.EnroqueCBlanco = false;
                 else
                     EstadoTablero.EnroqueCNegro = false;
             }else if(colInicio == 0)
                 if(pieza.EsBlanca())
                     EstadoTablero.EnroqueLBlanco = false;
                 else
                     EstadoTablero.EnroqueLNegro = false;
         }

       this.tablero[filaFinal][colFinal] = pieza;
       this.tablero[filaInicio][colInicio] = null;
       EstadoTablero.AlPaso = false;
    }
}