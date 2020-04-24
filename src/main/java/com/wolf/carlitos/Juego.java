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
   
   public void establecerPosicion(String... movimientos){
        for (var movimiento : movimientos) {
            secuencia.add(Utilidades.convertirAPosicion(movimiento));
            Utilidades.actualizarTablero(tablero, estadoTablero, Utilidades.convertirAPosicion(movimiento));
            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
        }
    }
    public void setFen(String linea){
        tablero = new Pieza[8][8];
        String[] filas = linea.replace("fen ", "").split("/");

        for (int i = 0; i < filas.length; i++) {

            if (i == 7) {
                String[] ops = filas[i].split(" ");
                iniciarFen(ops[0], 7 - i);

                estadoTablero.turnoBlanco = ops[1].equals("w");
                estadoTablero.enroqueLBlanco = ops[2].contains("Q");
                estadoTablero.enroqueCNegro = ops[2].contains("k");
                estadoTablero.enroqueCBlanco = ops[2].contains("K");
                estadoTablero.enroqueLNegro = ops[2].contains("q");

            } else {
                iniciarFen(filas[i], 7 - i);
            }

        }

        System.out.println("fen procesado");
        Utilidades.imprimirPosicicion(tablero);
    }
    private  void iniciarFen(String fila, int i) {

        int j = 0;

        for (char c : fila.toCharArray()) {
            if (c == 'k') {
                tablero[i][j] = new Rey(false);
            }
            if (c == 'q') {
                tablero[i][j] = new Dama(false);
            }
            if (c == 'r') {
                tablero[i][j] = new Torre(false);
            }
            if (c == 'b') {
                tablero[i][j] = new Alfil(false);
            }
            if (c == 'n') {
                tablero[i][j] = new Caballo(false);
            }
            if (c == 'p') {
                tablero[i][j] = new Peon(false);
            }

            if (c == 'K') {
                tablero[i][j] = new Rey(true);
            }
            if (c == 'Q') {
                tablero[i][j] = new Dama(true);
            }
            if (c == 'R') {
                tablero[i][j] = new Torre(true);
            }
            if (c == 'B') {
                tablero[i][j] = new Alfil(true);
            }
            if (c == 'N') {
                tablero[i][j] = new Caballo(true);
            }
            if (c == 'P') {
                tablero[i][j] = new Peon(true);
            }

            if (Character.isDigit(c)) {
                j += Integer.parseInt(String.valueOf(c));
            } else {
                j++;
            }
        }

    }
  
   public void perft(int n) throws CloneNotSupportedException, IOException{
       var search = new search(tablero, estadoTablero);
       search.setSecuencia(secuencia);
       search.perft(n);
       
   }
   public String mover(int n) throws CloneNotSupportedException, IOException{
       var search = new search(tablero, estadoTablero);
       return Utilidades.convertirANotacion(search.search(n));
   }
}