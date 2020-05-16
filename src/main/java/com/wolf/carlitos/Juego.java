package com.wolf.carlitos;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutionException;

import static com.wolf.carlitos.Bitboard.add;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Tablero.hacerMovimiento;
import static com.wolf.carlitos.Tablero.piezas;
import static com.wolf.carlitos.Utilidades.*;


public class Juego {

    public static int[] tablero = new int[64];
    public static  int[] color = new int[64];

    public int estadoTablero;
    public List<Integer> secuencia = new ArrayList<>();


    public Juego() {
        estadoTablero = POSICION_INICIAL;

        for (int i = 0; i < 64; i++) {
            tablero[i] = NOPIEZA;
            color[i] = NOCOLOR;
        }

        for (int i = 0; i < 16; i++) {
            color[i] = BLANCO;
            color[i + 48] = NEGRO;

        }
        tablero[A1] = TORRE;
        add(true, TORRE, A1);
        tablero[B1] = CABALLO;
        add(true, CABALLO, B1);
        tablero[C1] = ALFIL;
        add(true, ALFIL, C1);
        tablero[D1] = DAMA;
        add(true, DAMA, D1);
        tablero[E1] = REY;
        add(true, REY, E1);
        tablero[F1] = ALFIL;
        add(true, ALFIL, F1);
        tablero[G1] = CABALLO;
        add(true, CABALLO, G1);
        tablero[H1] = TORRE;
        add(true, TORRE, H1);

        tablero[A8] = TORRE;
        add(false, TORRE, A8);
        tablero[B8] = CABALLO;
        add(false, CABALLO, B8);
        tablero[C8] = ALFIL;
        add(false, ALFIL, C8);
        tablero[D8] = DAMA;
        add(false, DAMA, D8);
        tablero[E8] = REY;
        add(false, REY, E8);
        tablero[F8] = ALFIL;
        add(false, ALFIL, F8);
        tablero[G8] = CABALLO;
        add(false, CABALLO, G8);
        tablero[H8] = TORRE;
        add(false, TORRE, H8);

        for (int i = 0; i < 8; i++) {
            tablero[8 + i] = PEON;
            tablero[48 + i] = PEON;

            add(true, PEON, 8 + i);
            add(false, PEON, 48 + i);
        }

        for (int i = 0; i < 16; i++) {
            color[i] = BLANCO;
            color[i + 48] = NEGRO;

        }


    }

    public void establecerPosicion(String... movimientos) {
        for (var movimiento : movimientos) {
            secuencia.add(convertirAPosicion(movimiento));
            estadoTablero = hacerMovimiento(tablero, color, estadoTablero, convertirAPosicion(movimiento));
            estadoTablero ^= 0b10000;
        }

    }

    public void setFen(String fen) {

        tablero = new int[64];
        color = new int[64];
        estadoTablero = 0;

        Arrays.fill(tablero, NOPIEZA);
        Arrays.fill(color, NOCOLOR);

        String[] filas = fen.split("/");

        for (int i = 0; i < filas.length; i++) {

            if (i == 7) {
                String[] ops = filas[i].split(" ");
                iniciarFen(ops[0], 7 - i);

                // turno
                estadoTablero |= (ops[1].equals("w") ? 1 : 0) << 4;

                int enroques = 0;

                if (ops[2].contains("K")) enroques |= 1;
                if (ops[2].contains("Q")) enroques |= 2;
                if (ops[2].contains("k")) enroques |= 4;
                if (ops[2].contains("q")) enroques |= 8;

                estadoTablero |= enroques;


                if (!ops[3].contains("-")) {
                    var posicion = Utilidades.casillaANumero(ops[3]) + (esTurnoBlanco(estadoTablero) ? -8 : 8);
                    estadoTablero |= posicion << POSICION_PIEZA_AL_PASO;
                }

            } else {
                iniciarFen(filas[i], 7 - i);
            }

        }

        System.out.println("fen procesado");
        Utilidades.imprimirBinario(estadoTablero);
        Utilidades.imprimirPosicicion(tablero, color);
    }

    private void iniciarFen(String fila, int i) {

        int indexInicio = 8 * i;


        for (char c : fila.toCharArray()) {
            if (c == 'k') {
                tablero[indexInicio] = REY;
                color[indexInicio] = NEGRO;
                estadoTablero |= indexInicio << POSICION_REY_NEGRO;
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
                estadoTablero |= indexInicio << POSICION_REY_BLANCO;
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

    public void perft(int n) {
        var search = new Search(tablero, color, estadoTablero);
        search.perft(n);

    }

    public String mover(int n) throws InterruptedException, CloneNotSupportedException, ExecutionException {
        var search = new Search(tablero, color, estadoTablero);
        return Utilidades.convertirANotacion(search.search(n));
    }
}