package com.wolf.carlitos;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.wolf.carlitos.Constantes.*;


public class Juego {
    public Pieza[] tablero;
    public EstadoTablero estadoTablero;
    public List<int[]> secuencia = new ArrayList<>();


    public Juego() {
        estadoTablero = new EstadoTablero();
        tablero = new Pieza[64];

        tablero[A1] = new Pieza(true,TORRE);
        tablero[B1] = new Pieza(true,CABALLO);
        tablero[C1] = new Pieza(true,ALFIL);
        tablero[D1] = new Pieza(true,DAMA);

        tablero[E1] = new Pieza(true,REY);
        tablero[F1] = new Pieza(true,ALFIL);
        tablero[G1] = new Pieza(true,CABALLO);
        tablero[H1] = new Pieza(true,TORRE);

        tablero[A8] = new Pieza(false,TORRE);
        tablero[B8] = new Pieza(false,CABALLO);
        tablero[C8] = new Pieza(false,ALFIL);
        tablero[D8] = new Pieza(false,DAMA);

        tablero[E8] = new Pieza(false,REY);
        tablero[F8] = new Pieza(false,ALFIL);
        tablero[G8] = new Pieza(false,CABALLO);
        tablero[H8] = new Pieza(false,TORRE);

        for (int i = 0; i < 8; i++) {
            tablero[i + 8] = new Pieza(true,PEON);
            tablero[i + 48] = new Pieza(false,PEON);
        }

    }

    public void establecerPosicion(String... movimientos) {
        for (var movimiento : movimientos) {
            secuencia.add(Utilidades.convertirAPosicion(movimiento));
            Utilidades.actualizarTablero(tablero, estadoTablero, Utilidades.convertirAPosicion(movimiento));
            estadoTablero.turnoBlanco = !estadoTablero.turnoBlanco;
        }
    }

    public void setFen(String linea) {
        tablero = new Pieza[64];
        String[] filas = linea.split("/");

        for (int i = 0; i < filas.length; i++) {

            if (i == 7) {
                String[] ops = filas[i].split(" ");
                iniciarFen(ops[0], 7 - i);

                estadoTablero.turnoBlanco = ops[1].equals("w");
                estadoTablero.enroqueLBlanco = ops[2].contains("Q");
                estadoTablero.enroqueCNegro = ops[2].contains("k");
                estadoTablero.enroqueCBlanco = ops[2].contains("K");
                estadoTablero.enroqueLNegro = ops[2].contains("q");

                if (!ops[3].contains("-")) {
                    var posicion = Utilidades.casillaANumero(ops[3]) + (estadoTablero.turnoBlanco ? -8 : 8);
                    estadoTablero.piezaALPaso = tablero[posicion];
                    estadoTablero.alPaso = true;
                }

            } else {
                iniciarFen(filas[i], 7 - i);
            }

        }

        System.out.println("fen procesado");
        Utilidades.imprimirPosicicion(tablero);
    }

    private void iniciarFen(String fila, int i) {

        int indexInicio = 8 * i;


        for (char c : fila.toCharArray()) {
            if (c == 'k') {
                tablero[indexInicio] = new Pieza(false,REY);
                estadoTablero.posicionReyNegro = indexInicio;
            }
            if (c == 'q') {
                tablero[indexInicio] = new Pieza(false,DAMA);
            }
            if (c == 'r') {
                tablero[indexInicio] = new Pieza(false,TORRE);
            }
            if (c == 'b') {
                tablero[indexInicio] = new Pieza(false,ALFIL);
            }
            if (c == 'n') {
                tablero[indexInicio] = new Pieza(false,CABALLO);
            }
            if (c == 'p') {
                tablero[indexInicio] = new Pieza(false,PEON);
            }

            if (c == 'K') {
                tablero[indexInicio] = new Pieza(true,REY);
                estadoTablero.posicionReyBlanco = indexInicio;
            }
            if (c == 'Q') {
                tablero[indexInicio] = new Pieza(true,DAMA);
            }
            if (c == 'R') {
                tablero[indexInicio] = new Pieza(true,TORRE);
            }
            if (c == 'B') {
                tablero[indexInicio] = new Pieza(true,ALFIL);
            }
            if (c == 'N') {
                tablero[indexInicio] = new Pieza(true,CABALLO);
            }
            if (c == 'P') {
                tablero[indexInicio] = new Pieza(true,PEON);
            }

            if (Character.isDigit(c)) {
                indexInicio += Integer.parseInt(String.valueOf(c));
            } else {
                indexInicio++;
            }
        }

    }

    public void perft(int n) throws CloneNotSupportedException, IOException {
        var search = new Search(tablero, estadoTablero);
        //search.setSecuencia(secuencia);
        search.perft(n);

    }
   public String mover(int n) throws CloneNotSupportedException, IOException, InterruptedException, ExecutionException {
       var search = new Search(tablero, estadoTablero);
       return Utilidades.convertirANotacion(search.search(n));
   }
}