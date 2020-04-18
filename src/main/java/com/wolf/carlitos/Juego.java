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



public class Juego {
    public  Pieza[][] tablero;
    public  EstadoTablero estadoTablero;
    public  List<int[]> secuencia =  new ArrayList<>();

    
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
   
   public void EstablecerPosicion(String... movimientos){
        for (var movimiento : movimientos) {
            secuencia.add(Utilidades.convertirAPosicion(movimiento));
            Utilidades.actualizarTablero(tablero, estadoTablero, Utilidades.convertirAPosicion(movimiento));
            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
        }
    }
  
   public void perft(int n) throws CloneNotSupportedException, IOException{
       var search = new Search(tablero, estadoTablero);
       search.setSecuencia(secuencia);
       search.perft(n);
       
   }
   public String mover(int n) throws CloneNotSupportedException, IOException{
       var search = new Search(tablero, estadoTablero);
       return Utilidades.ConvertirANotacion(search.search(n));
   }
}