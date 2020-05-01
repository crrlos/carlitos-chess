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

import static com.wolf.carlitos.Constantes.*;


public class Juego {
    public  Pieza[] tablero;
    public  EstadoTablero estadoTablero;
    public  List<int[]> secuencia =  new ArrayList<>();

    
   public Juego(){
        estadoTablero = new  EstadoTablero();
        tablero = new Pieza[64];

//       tablero[0] = new Torre(true);
//       tablero[1] = new Caballo(true);
//       tablero[2] = new Alfil(true);
       tablero[D1] = new Dama(true);

       tablero[E1] = new Rey(true);
//       tablero[5] = new Alfil(true);
//       tablero[6] = new Caballo(true);
//       tablero[7] = new Torre(true);
//
//       tablero[56] = new Torre(false);
//       tablero[57] = new Caballo(false);
//       tablero[58] = new Alfil(false);
//       tablero[59] = new Dama(false);

        tablero[E8] = new Rey(false);
//       tablero[61] = new Alfil(false);
//       tablero[62] = new Caballo(false);
//       tablero[63] = new Torre(false);
//
//       for(int i = 0 ; i < 8; i++){
//            tablero[i + 8]= new Peon(true);
//            tablero[i + 48]  = new Peon(false);
//        }

       estadoTablero.enroqueLBlanco = estadoTablero.enroqueCBlanco = estadoTablero.enroqueCNegro = estadoTablero.enroqueLNegro
               =false;
    }
   
   public void establecerPosicion(String... movimientos){
        for (var movimiento : movimientos) {
            secuencia.add(Utilidades.convertirAPosicion(movimiento));
            Utilidades.actualizarTablero(tablero, estadoTablero, Utilidades.convertirAPosicion(movimiento));
            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
        }
    }
    public void setFen(String linea){
        tablero = new Pieza[64];
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
       
         int indexInicio = 8 * i;
       

        for (char c : fila.toCharArray()) {
            if (c == 'k') {
                tablero[indexInicio] = new Rey(false);
                estadoTablero.posicionReyNegro = indexInicio;
            }
            if (c == 'q') {
                tablero[indexInicio] = new Dama(false);
            }
            if (c == 'r') {
                tablero[indexInicio] = new Torre(false);
            }
            if (c == 'b') {
                tablero[indexInicio] = new Alfil(false);
            }
            if (c == 'n') {
                tablero[indexInicio] = new Caballo(false);
            }
            if (c == 'p') {
                tablero[indexInicio] = new Peon(false);
            }

            if (c == 'K') {
                tablero[indexInicio] = new Rey(true);
                estadoTablero.posicionReyBlanco = indexInicio;
            }
            if (c == 'Q') {
                tablero[indexInicio] = new Dama(true);
            }
            if (c == 'R') {
                tablero[indexInicio] = new Torre(true);
            }
            if (c == 'B') {
                tablero[indexInicio] = new Alfil(true);
            }
            if (c == 'N') {
                tablero[indexInicio] = new Caballo(true);
            }
            if (c == 'P') {
                tablero[indexInicio] = new Peon(true);
            }

            if (Character.isDigit(c)) {
                indexInicio += Integer.parseInt(String.valueOf(c));
            } else {
                indexInicio++;
            }
        }

    }

   public void perft(int n) throws CloneNotSupportedException, IOException{
       var search = new Search(tablero, estadoTablero);
       //search.setSecuencia(secuencia);
       search.perft(n);

   }
//   public String mover(int n) throws CloneNotSupportedException, IOException{
//       var search = new search(tablero, estadoTablero);
//       return Utilidades.convertirANotacion(search.search(n));
//   }
}