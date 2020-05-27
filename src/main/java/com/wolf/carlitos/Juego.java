package com.wolf.carlitos;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.wolf.carlitos.Bitboard.add;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Pieza.piezas;
import static com.wolf.carlitos.Tablero.esTurnoBlanco;
import static com.wolf.carlitos.Tablero.hacerMovimiento;
import static com.wolf.carlitos.Utilidades.convertirAPosicion;


public class Juego {

    public static int[] tablero = new int[64];
    public static  int[] color = new int[64];


    public List<Movimiento> secuencia = new ArrayList<>();


    public Juego() {
        Tablero.estados.push(POSICION_INICIAL);

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
            hacerMovimiento(tablero, color, convertirAPosicion(movimiento));
        }

    }

    public void setFen(String fen) {
        int estado = 0;

        tablero = new int[64];
        color = new int[64];
        piezas = new long[2][6];

        Arrays.fill(tablero, NOPIEZA);
        Arrays.fill(color, NOCOLOR);

        String[] filas = fen.split("/");

        for (int i = 0; i < filas.length; i++) {

            if (i == 7) {
                String[] ops = filas[i].split(" ");
                iniciarFen(ops[0], 7 - i);

                // turno
                estado |= (ops[1].equals("w") ? 1 : 0) << 4;

                int enroques = 0;

                if (ops[2].contains("K")) enroques |= 1;
                if (ops[2].contains("Q")) enroques |= 2;
                if (ops[2].contains("k")) enroques |= 4;
                if (ops[2].contains("q")) enroques |= 8;

                estado |= enroques;


                if (!ops[3].contains("-")) {
                    var posicion = Utilidades.casillaANumero(ops[3]) + (esTurnoBlanco() ? -8 : 8);
                    estado |= posicion << POSICION_PIEZA_AL_PASO;
                }

            } else {
                iniciarFen(filas[i], 7 - i);
            }

        }

        Tablero.estados.clear();
        Tablero.estados.push(estado);
        System.out.println("fen procesado");
        Utilidades.imprimirBinario(estado);
        Utilidades.imprimirPosicicion(tablero, color);
    }

    private void iniciarFen(String fila, int i) {

        int indexInicio = 8 * i;


        for (char c : fila.toCharArray()) {
            if (c == 'k') {
                tablero[indexInicio] = REY;
                color[indexInicio] = NEGRO;
                add(false,REY,indexInicio);
            }
            if (c == 'q') {
                tablero[indexInicio] = DAMA;
                color[indexInicio] = NEGRO;
                add(false,DAMA,indexInicio);
            }
            if (c == 'r') {
                tablero[indexInicio] = TORRE;
                color[indexInicio] = NEGRO;
                add(false,TORRE,indexInicio);
            }
            if (c == 'b') {
                tablero[indexInicio] = ALFIL;
                color[indexInicio] = NEGRO;
                add(false,ALFIL,indexInicio);
            }
            if (c == 'n') {
                tablero[indexInicio] = CABALLO;
                color[indexInicio] = NEGRO;
                add(false,CABALLO,indexInicio);
            }
            if (c == 'p') {
                tablero[indexInicio] = PEON;
                color[indexInicio] = NEGRO;
                add(false,PEON,indexInicio);
            }

            if (c == 'K') {
                tablero[indexInicio] = REY;
                color[indexInicio] = BLANCO;
                add(true,REY,indexInicio);
            }
            if (c == 'Q') {
                tablero[indexInicio] = DAMA;
                color[indexInicio] = BLANCO;
                add(true,DAMA,indexInicio);
            }
            if (c == 'R') {
                tablero[indexInicio] = TORRE;
                color[indexInicio] = BLANCO;
                add(true,TORRE,indexInicio);
            }
            if (c == 'B') {
                tablero[indexInicio] = ALFIL;
                color[indexInicio] = BLANCO;
                add(true,ALFIL,indexInicio);
            }
            if (c == 'N') {
                tablero[indexInicio] = CABALLO;
                color[indexInicio] = BLANCO;
                add(true,CABALLO,indexInicio);
            }
            if (c == 'P') {
                tablero[indexInicio] = PEON;
                color[indexInicio] = BLANCO;
                add(true,PEON,indexInicio);
            }

            if (Character.isDigit(c)) {
                indexInicio += Integer.parseInt(String.valueOf(c));
            } else {
                indexInicio++;
            }
        }

    }

    public void perft(int n) {
        var search = new Search(tablero, color);
        search.perft(n);

    }

    public String mover(int n){
        var search = new Search(tablero, color);
        return Utilidades.convertirANotacion(search.search(n));
    }

    public void evaluarPosicion() {
       // System.out.println(Evaluar.evaluar(0));
    }
}