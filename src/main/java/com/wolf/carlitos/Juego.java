package com.wolf.carlitos;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Utilidades.*;


public class Juego {
    
    public int[] tablero = new int[64];
    public int [] color = new int[64];
    
    public long estadoTablero;
    public List<Integer> secuencia = new ArrayList<>();


    public Juego() {
        estadoTablero = 0b111100_000100_000_00_000_0_000000_0_1_11_11L;

        for (int i = 0; i < 64; i++) {
            tablero[i] = NOPIEZA;
            color[i] = NOCOLOR;
        }

        tablero[A1] = TORRE;
        tablero[B1] = CABALLO;
        tablero[C1] = ALFIL;
        tablero[D1] = DAMA;
        tablero[E1] = REY;
        tablero[F1] = ALFIL;
        tablero[G1] = CABALLO;
        tablero[H1] = TORRE;

        tablero[A8] = TORRE;
        tablero[B8] = CABALLO;
        tablero[C8] = ALFIL;
        tablero[D8] = DAMA;
        tablero[E8] = REY;
        tablero[F8] = ALFIL;
        tablero[G8] = CABALLO;
        tablero[H8] = TORRE;

        for (int i = 0; i < 8; i++) {
            tablero[8 + i] = PEON;
            tablero[48 + i] = PEON;
        }

        for (int i = 0; i < 16; i++) {
            color[i] = BLANCO;
            color[i + 48] = NEGRO;

        }
    }

    public void establecerPosicion(String... movimientos) {
        for (var movimiento : movimientos) {
            secuencia.add(convertirAPosicion(movimiento));
            estadoTablero = actualizarTablero(tablero,color, estadoTablero, convertirAPosicion(movimiento));
            estadoTablero ^= 0b10000;
        }

    }

    public void setFen(String fen) {

        tablero = new int[64];
        color = new int[64];
        estadoTablero = 0;

        Arrays.fill(tablero,NOPIEZA);
        Arrays.fill(color,NOCOLOR);

        String[] filas = fen.split("/");

        for (int i = 0; i < filas.length; i++) {

            if (i == 7) {
                String[] ops = filas[i].split(" ");
                iniciarFen(ops[0], 7 - i);
                
                // turno
                estadoTablero |= (ops[1].equals("w") ? 1 : 0) << 4;
                
                int enroques = 0;
                
                if(ops[2].contains("K"))  enroques |= 1;
                if(ops[2].contains("Q"))  enroques |= 2;
                if(ops[2].contains("k"))  enroques |= 4;
                if(ops[2].contains("q"))  enroques |= 8;
                
                estadoTablero |= enroques;
                

                if (!ops[3].contains("-")) {
                    var posicion = Utilidades.casillaANumero(ops[3]) + (esTurnoBlanco(estadoTablero) ? -8 : 8);
                    estadoTablero |= posicion << 6;
                    // al paso
                    estadoTablero |= 0b100000;
                }

            } else {
                iniciarFen(filas[i], 7 - i);
            }

        }

        System.out.println("fen procesado");
       Utilidades.imprimirBinario(estadoTablero);
        Utilidades.imprimirPosicicion(tablero,color);
    }

    private void iniciarFen(String fila, int i) {

        int indexInicio = 8 * i;


        for (char c : fila.toCharArray()) {
            if (c == 'k') {
                tablero[indexInicio] = REY;
                color[indexInicio] = NEGRO;
                estadoTablero |=  (long)indexInicio << 27;
            }
            if (c == 'q') {
                tablero[indexInicio] = DAMA;
                color[indexInicio] = NEGRO;
            }
            if (c == 'r') {
                tablero[indexInicio] = TORRE;
                color[indexInicio] = NEGRO;
            }
            if (c == 'b') {
                tablero[indexInicio] = ALFIL;
                color[indexInicio] = NEGRO;
            }
            if (c == 'n') {
                tablero[indexInicio] = CABALLO;
                color[indexInicio] = NEGRO;
            }
            if (c == 'p') {
                tablero[indexInicio] = PEON;
                color[indexInicio] = NEGRO;
            }

            if (c == 'K') {
                tablero[indexInicio] = REY;
                color[indexInicio] = BLANCO;
                estadoTablero |= indexInicio << 21;
            }
            if (c == 'Q') {
                tablero[indexInicio] = DAMA;
                color[indexInicio] = BLANCO;
            }
            if (c == 'R') {
                tablero[indexInicio] = TORRE;
                color[indexInicio] = BLANCO;
            }
            if (c == 'B') {
                tablero[indexInicio] = ALFIL;
                color[indexInicio] = BLANCO;
            }
            if (c == 'N') {
                tablero[indexInicio] = CABALLO;
                color[indexInicio] = BLANCO;
            }
            if (c == 'P') {
                tablero[indexInicio] = PEON;
                color[indexInicio] = BLANCO;
            }

            if (Character.isDigit(c)) {
                indexInicio += Integer.parseInt(String.valueOf(c));
            } else {
                indexInicio++;
            }
        }

    }
    public void perft(int n){
        var search = new Search(tablero,color, estadoTablero);
        search.perft(n);

    }
   public String mover(int n) throws InterruptedException, CloneNotSupportedException, ExecutionException {
       var search = new Search(tablero,color, estadoTablero);
       return Utilidades.convertirANotacion(search.search(n));
   }
}