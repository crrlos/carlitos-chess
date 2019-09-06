package app;

import java.util.ArrayList;
import java.util.List;

import app.Piezas.Alfil;
import app.Piezas.Caballo;
import app.Piezas.Dama;
import app.Piezas.Peon;
import app.Piezas.Pieza;
import app.Piezas.Rey;
import app.Piezas.Torre;

public class Juego {
    public Pieza[][] tablero;
    public String casillAlPaso;
    public boolean alPaso;
    public boolean enroqueCortoBlanco = true;
    public boolean enroqueLargoBlanco = true;
    public boolean enroqueCortoNegro = true;
    public boolean enroqueLargoNegro = true;
    public boolean turnoBlanco = true;
    private String filas = "12345678";
    private String columnas = "abcdefgh";
    
   public Juego(){
        this.tablero = new Pieza[8][8];
        for(int i = 0 ; i < 8; i++){
            tablero[1][i] = new Peon(true);
            tablero[6][i] = new Peon(false);
        }
        tablero[0][0] = new Torre(true);
        tablero[0][7] = new Torre(true);
        tablero[7][0] = new Torre(false);
        tablero[7][7] = new Torre(false);   

        tablero[0][1] = new Caballo(true);
        tablero[0][6] = new Caballo(true);
        tablero[7][1] = new Caballo(false);
        tablero[7][6] = new Caballo(false);   

        tablero[0][2] = new Alfil(true);
        tablero[0][5] = new Alfil(true);
        tablero[7][2] = new Alfil(false);
        tablero[7][5] = new Alfil(false);   

        tablero[0][3] = new Dama(true);
        tablero[7][3] = new Dama(false);   

        tablero[0][4] = new Rey(true);
        tablero[7][4] = new Rey(false);   
    
    }
    public void ImprimirPosicicion(){
        for (int i = 7; i >=0 ; i--) {
            for (int j = 0; j < 8 ; j++) {
                var pieza = tablero[i][j];
                System.out.print(pieza != null ? pieza.Nombre() : " ");
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
    private String ConvertirANotacion(int[] movimiento){
        var mov = columnas.charAt(movimiento[1]) + "" + filas.charAt(movimiento[0]) +
                  columnas.charAt(movimiento[3]) + "" + filas.charAt(movimiento[2]);
        if(movimiento.length == 5){
            switch(movimiento[4]){
                case 1: return mov += "=Q";
                case 2: return mov += "=T";
                case 3: return mov += "=C";
                case 4: return mov += "=A";
            }
        }
        return mov;
    }
    public void MovimientosValidos(){
        List<int[]> movimientos = new ArrayList<int[]>();
        for (int i = 7; i >=0 ; i--) {
            for (int j = 0; j < 8 ; j++) {
                var pieza = tablero[i][j];
                
                if(pieza instanceof Peon || pieza instanceof Torre || pieza instanceof Caballo || pieza instanceof Alfil){
                     
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
        for(var mov : movimientos){
            System.out.print(ConvertirANotacion(mov) + " ");
        }
    }
    private void ActualizarTablero(String movimiento){
        int colInicio = columnas.indexOf(movimiento.substring(0,1));
        int filaInicio = filas.indexOf(movimiento.substring(1, 2));
        int colFinal = columnas.indexOf(movimiento.substring(2, 3));
        int filaFinal = filas.indexOf(movimiento.substring(3, 4));

        var pieza = this.tablero[filaInicio][colInicio];

         if(pieza instanceof Peon){

            if(Math.abs(filaInicio - filaFinal) == 2){
                casillAlPaso = movimiento.substring(2, 3) + (filaFinal + (!pieza.EsBlanca() ? 2 : 0));
                alPaso = true;
                this.tablero[filaFinal][colFinal] = pieza;
                this.tablero[filaInicio][colInicio] = null;
                return;
            }

            if(!casillAlPaso.isEmpty()){ 
                if(movimiento.substring(2, 4).equals(casillAlPaso)){
                    this.tablero[filaFinal - (pieza.EsBlanca() ? 1 : -1)][colFinal] = null;
                }
                
            }
            // Todo: promoción del peón
         }

         if(pieza instanceof Rey){
            // en los enroques solo se mueven las torres por ser el movimiento especial
            if(Math.abs(colInicio - colFinal) == 2){
                if(pieza.EsBlanca()){
                    enroqueCortoBlanco = enroqueLargoBlanco = false;
                    if(colFinal == 6){//enroque corto
                        tablero[0][5] = tablero[0][7];
                        tablero[0][7] = null;
                    }else {//enroque largo
                        tablero[0][3] = tablero[0][0];
                        tablero[0][0] = null;
                    }
                }else{
                    enroqueCortoNegro = enroqueLargoNegro = false;
                    if(colFinal == 6){//enroque corto
                        tablero[7][5] = tablero[7][7];
                        tablero[7][7] = null;
                    }else {//enroque largo
                        tablero[7][3] = tablero[7][0];
                        tablero[7][0] = null;
                    }
                }
            }


         }

       this.tablero[filaFinal][colFinal] = pieza;
       this.tablero[filaInicio][colInicio] = null;
       alPaso = false;
    }
}