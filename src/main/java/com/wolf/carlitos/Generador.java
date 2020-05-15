/*
 * To change pieza license header, choose License Headers in Project Properties.
 * To change pieza template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import java.util.*;

//import static com.wolf.carlitos.Utilidades.movimientoValido;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Tablero.*;
import static com.wolf.carlitos.Utilidades.*;
import static java.lang.Math.abs;

/**
 * @author carlos
 */
public class Generador {

    private boolean reyEnJaque;
    private Movimientos movimientos;

    private static final int[][] direccionesVertical = new int[][]{
            {8, -8},
            {NORTE, SUR}
    };
    private static final int[][] direccionesHorizontal = new int[][]{
            {1, -1},
            {ESTE, OESTE}
    };

    private static final int[][] direccionesDiagonal1 = new int[][]{
            {9, -9},
            {NORESTE, SUROESTE}
    };
    private static final int[][] direccionesDiagonal2 = new int[][]{
            {7, -7},
            {NOROESTE, SURESTE}
    };

    private  static  final  int[] direccionesMail = new int[]{NORESTE, SUROESTE, NOROESTE, SURESTE};
    private  static  final  int[] direccionesLocal = new int[]{9, -9, 7, -7};


    public static final HashMap<Integer, List<List<Integer>>> movimientosAlfil = new HashMap<>();
    public static final HashMap<Integer, List<List<Integer>>> movimientosTorre = new HashMap<>();
    public static final HashMap<Integer, List<Integer>> movimientosCaballo = new HashMap<>();

    static {

        llenarMovimientosDeAlfil();
        llenarMovimientosDeTorre();
        llenarMovimientosDeCaballo();

    }

    private static void llenarMovimientosDeCaballo() {
        for (int i = 0; i < 64; i++) {
            var movimientos = new ArrayList<Integer>();
            var colorInicio = esCasillaBlanca(i);

            if (i + 6 < 64 && colorInicio != esCasillaBlanca(i + 6))
                movimientos.add(i + 6);
            if (i + 10 < 64 && colorInicio != esCasillaBlanca(i + 10))
                movimientos.add(i + 10);
            if (i + 15 < 64 && colorInicio != esCasillaBlanca(i + 15))
                movimientos.add(i + 15);
            if (i + 17 < 64 && colorInicio != esCasillaBlanca(i + 17))
                movimientos.add(i + 17);

            if (i - 6 >= 0 && colorInicio != esCasillaBlanca(i - 6))
                movimientos.add(i - 6);
            if (i - 10 >= 0 && colorInicio != esCasillaBlanca(i - 10))
                movimientos.add(i - 10);
            if (i - 15 >= 0 && colorInicio != esCasillaBlanca(i - 15))
                movimientos.add(i - 15);
            if (i - 17 >= 0 && colorInicio != esCasillaBlanca(i - 17))
                movimientos.add(i - 17);

            movimientosCaballo.put(i, movimientos);
        }
    }

    private static void llenarMovimientosDeTorre() {
        for (int i = 0; i < 64; i++) {
            var movimientos = new ArrayList<List<Integer>>(4);

            var movimientosInterna = new ArrayList<Integer>();
            int base = i + 8;

            while (base < 64) {
                movimientosInterna.add(base);
                base += 8;
            }
            movimientos.add(0, movimientosInterna);

            movimientosInterna = new ArrayList<>();
            base = i - 8;
            while (base >= 0) {
                movimientosInterna.add(base);
                base -= 8;
            }

            movimientos.add(1, movimientosInterna);

            movimientosInterna = new ArrayList<>();


            int residuo = i / 8;

            int fin = 7 + (8 * residuo);

            for (int j = i + 1; j <= fin; j++) {
                movimientosInterna.add(j);
            }
            movimientos.add(2, movimientosInterna);

            residuo = i / 8;

            fin = 8 * residuo;

            movimientosInterna = new ArrayList<Integer>();

            for (int j = i - 1; j >= fin; j--) {
                movimientosInterna.add(j);
            }
            movimientos.add(3, movimientosInterna);

            movimientosTorre.put(i, movimientos);
        }
    }

    private static void llenarMovimientosDeAlfil() {


        for (int i = 0; i < 64; i++) {

            boolean colorInicio = esCasillaBlanca(i);

            var movimientos = new ArrayList<List<Integer>>(4);

            var movimientosInterna = new ArrayList<Integer>();

            int base = i + 9;

            while (base < 64 && colorInicio == esCasillaBlanca(base)) {
                movimientosInterna.add(base);
                base += 9;
            }
            movimientos.add(0, movimientosInterna);

            movimientosInterna = new ArrayList<Integer>();
            base = i - 9;
            while (base >= 0 && colorInicio == esCasillaBlanca(base)) {
                movimientosInterna.add(base);
                base -= 9;
            }
            movimientos.add(1, movimientosInterna);

            movimientosInterna = new ArrayList<Integer>();
            base = i + 7;
            while (base < 64 && colorInicio == esCasillaBlanca(base)) {
                movimientosInterna.add(base);
                base += 7;
            }
            movimientos.add(2, movimientosInterna);


            movimientosInterna = new ArrayList<Integer>();
            base = i - 7;
            while (base >= 0 && colorInicio == esCasillaBlanca(base)) {
                movimientosInterna.add(base);
                base -= 7;
            }
            movimientos.add(3, movimientosInterna);


            movimientosAlfil.put(i, movimientos);

        }
    }


    public void generarMovimientos(int[] pieza, int[] color, int estado, Movimientos movimientos) {

        this.movimientos = movimientos;

        int posicionRey = posicionRey(estado, esTurnoBlanco(estado) ? POSICION_REY_BLANCO : POSICION_REY_NEGRO);

        reyEnJaque = Tablero.casillaAtacada(posicionRey, pieza, color, esTurnoBlanco(estado) ? NEGRO : BLANCO);

        for (int i = 0; i < pieza.length; i++) {

            var piezaActual = pieza[i];

            if (piezaActual != NOPIEZA && color[i] == BLANCO == esTurnoBlanco(estado)) {
                switch (piezaActual) {
                    case PEON:
                        movimientosDePeon(pieza, color, estado, i);
                        break;
                    case CABALLO:
                        movimientosDeCaballo(pieza, color, estado, i);
                        break;
                    case ALFIL:
                        movimientosDeAlfil(pieza, color, estado, i);
                        break;
                    case TORRE:
                        movimientosDeTorre(pieza, color, estado, i);
                        break;
                    case DAMA:
                        movimientosDeDama(pieza, color, estado, i);
                        break;
                    case REY:
                        movimientosDeRey(pieza, color, estado, i);
                        break;
                }
            }

        }
        reyEnJaque = false;
    }

    public void movimientosDeTorre(int[] tablero, int[] color, int estado, int posicion) {


        if (!reyEnJaque) {

            boolean verticalSinValidar = false;
            boolean horizontalSinValidar = false;

            for (int i = 0; i < direccionesVertical[0].length; i++) {
                int dir = direccionesVertical[0][i];
                if (mailBox[direccion[posicion] + direccionesVertical[1][i]] != -1
                        && (tablero[posicion + dir] == NOPIEZA
                        || (color[posicion + dir] != color[posicion]
                        && tablero[posicion + dir] != REY))
                        && movimientoValido(posicion << 6 | posicion + dir, tablero, color, estado)) {
                    verticalSinValidar = true;
                    break;
                }
            }

            for (int i = 0; i < direccionesHorizontal[0].length; i++) {
                int dir = direccionesHorizontal[0][i];
                if (mailBox[direccion[posicion] + direccionesHorizontal[1][i]] != -1
                        && (tablero[posicion + dir] == NOPIEZA
                        || (color[posicion + dir] != color[posicion]
                        && tablero[posicion + dir] != REY))
                        && movimientoValido(posicion << 6 | posicion + dir, tablero, color, estado)) {
                    horizontalSinValidar = true;
                    break;
                }
            }

            int pos;

            if (verticalSinValidar) {

                for (int i = 0; i < direccionesVertical[0].length; i++) {
                    pos = posicion;
                    int dir = direccionesVertical[0][i];
                    while (mailBox[direccion[pos] + direccionesVertical[1][i]] != -1) {
                        pos += dir;

                        if (tablero[pos] == NOPIEZA) {
                            movimientos.add(posicion << 6 | pos);
                            continue;
                        }

                        if (color[pos] == color[posicion] || tablero[pos] == REY) break;

                        movimientos.add(posicion << 6 | pos);
                        break;

                    }

                }
            }

            if (horizontalSinValidar) {

                for (int i = 0; i < direccionesHorizontal[0].length; i++) {
                    pos = posicion;
                    int dir = direccionesHorizontal[0][i];
                    while (mailBox[direccion[pos] + direccionesHorizontal[1][i]] != -1) {
                        pos += dir;

                        if (tablero[pos] == NOPIEZA) {
                            movimientos.add(posicion << 6 | pos);
                            continue;
                        }

                        if (color[pos] == color[posicion] || tablero[pos] == REY) break;

                        movimientos.add(posicion << 6 | pos);
                        break;

                    }

                }

            }

            return;
        }

        int[] direccionesMail = new int[]{ESTE, OESTE, NORTE, SUR};
        int[] direccionesLocal = new int[]{1, -1, 8, -8};

        for (int i = 0; i < direccionesMail.length; i++) {

            int dir = direccionesMail[i];
            int dirLocal = direccionesLocal[i];
            int pos = posicion;
            while (mailBox[direccion[pos] + dir] != -1) {
                pos += dirLocal;

                if (tablero[pos] == NOPIEZA) {
                    if(movimientoValido(posicion << 6 | pos, tablero, color, estado))
                        movimientos.add(posicion << 6 | pos);

                    continue;
                }

                if (color[pos] == color[posicion] || tablero[pos] == REY) break;

                if (movimientoValido(posicion << 6 | pos, tablero, color, estado))
                    movimientos.add(posicion << 6 | pos);

                break;

            }


        }

    }

    private void movimientosDeDama(int[] tablero, int[] color, int estado, int posicion) {
        movimientosDeAlfil(tablero, color, estado, posicion);
        movimientosDeTorre(tablero, color, estado, posicion);
    }

    private void movimientosDeCaballo(int[] tablero, int[] color, int estado, int posicion) {
        int posicionActual;

        var movimientosCaballo = Generador.movimientosCaballo.get(posicion);

        for (int i = 0; i < movimientosCaballo.size(); i++) {
            var mov = movimientosCaballo.get(i);
            posicionActual = tablero[mov];

            if (posicionActual == NOPIEZA || (color[mov] != color[posicion] && !(posicionActual == REY))) {
                validarYAgregar(tablero, color, estado, posicion, mov);
            }

        }


    }

    public void movimientosDeAlfil(int[] tablero, int[] color, int estado, int posicion) {


        if (!reyEnJaque) {

            boolean diagonal1SinValidar = false;
            boolean diagonal2SinValidar = false;

            for (int i = 0; i < direccionesDiagonal1[0].length; i++) {
                int dir = direccionesDiagonal1[0][i];
                if (mailBox[direccion[posicion] + direccionesDiagonal1[1][i]] != -1
                        && (tablero[posicion + dir] == NOPIEZA
                        || (color[posicion + dir] != color[posicion]
                        && tablero[posicion + dir] != REY))
                        && movimientoValido(posicion << 6 | posicion + dir, tablero, color, estado)) {
                    diagonal1SinValidar = true;
                    break;
                }
            }

            for (int i = 0; i < direccionesDiagonal2[0].length; i++) {
                int dir = direccionesDiagonal2[0][i];
                if (mailBox[direccion[posicion] + direccionesDiagonal2[1][i]] != -1
                        && (tablero[posicion + dir] == NOPIEZA
                        || (color[posicion + dir] != color[posicion]
                        && tablero[posicion + dir] != REY))
                        && movimientoValido(posicion << 6 | posicion + dir, tablero, color, estado)) {
                    diagonal2SinValidar = true;
                    break;
                }
            }

            int pos;

            if (diagonal1SinValidar) {

                for (int i = 0; i < direccionesDiagonal1[0].length; i++) {
                    pos = posicion;
                    int dir = direccionesDiagonal1[0][i];
                    while (mailBox[direccion[pos] + direccionesDiagonal1[1][i]] != -1) {
                        pos += dir;

                        if (tablero[pos] == NOPIEZA) {
                            movimientos.add(posicion << 6 | pos);
                            continue;
                        }

                        if (color[pos] == color[posicion] || tablero[pos] == REY) break;

                        movimientos.add(posicion << 6 | pos);
                        break;

                    }

                }
            }

            if (diagonal2SinValidar) {

                for (int i = 0; i < direccionesDiagonal2[0].length; i++) {
                    pos = posicion;
                    int dir = direccionesDiagonal2[0][i];
                    while (mailBox[direccion[pos] + direccionesDiagonal2[1][i]] != -1) {
                        pos += dir;

                        if (tablero[pos] == NOPIEZA) {
                            movimientos.add(posicion << 6 | pos);
                            continue;
                        }

                        if (color[pos] == color[posicion] || tablero[pos] == REY) break;

                        movimientos.add(posicion << 6 | pos);
                        break;

                    }

                }

            }

            return;
        }

        for (int i = 0; i < direccionesMail.length; i++) {

            int dir = direccionesMail[i];
            int dirLocal = direccionesLocal[i];
            int pos = posicion;
            while (mailBox[direccion[pos] + dir] != -1) {
                pos += dirLocal;

                if (tablero[pos] == NOPIEZA) {
                    if(movimientoValido(posicion << 6 | pos, tablero, color, estado))
                        movimientos.add(posicion << 6 | pos);

                    continue;
                }

                if (color[pos] == color[posicion] || tablero[pos] == REY) break;

                if (movimientoValido(posicion << 6 | pos, tablero, color, estado))
                    movimientos.add(posicion << 6 | pos);

                break;

            }


        }


    }

    private void movimientosDeRey(int[] tablero, int[] color, int estado, int posicion) {

        int pos;

        for (int i = 0; i < offsetMailBox[TORRE].length; i++) {
            int mailOffset = offsetMailBox[TORRE][i];

            if(mailBox[direccion[posicion] + mailOffset] != -1){

                pos = posicion + offset64[TORRE][i];

                if((tablero[pos] == NOPIEZA || color[pos] != color[posicion])  && movimientoValido(posicion << 6 | pos,tablero,color,estado))
                        movimientos.add(posicion << 6 | pos);

            }
        }

        for (int i = 0; i < offsetMailBox[ALFIL].length; i++) {
            int mailOffset = offsetMailBox[ALFIL][i];

            if(mailBox[direccion[posicion] + mailOffset] != -1){

                pos = posicion + offset64[ALFIL][i];

                if((tablero[pos] == NOPIEZA || color[pos] != color[posicion])  && movimientoValido(posicion << 6 | pos,tablero,color,estado))
                    movimientos.add(posicion << 6 | pos);

            }
        }

        if ((estado & 0b1111) == 0 || reyEnJaque) return;

        if (esTurnoBlanco(estado)) {
            if ((estado & 0b000000_000000_000_000_000000_0_00_11) > 0) {

                    if ((estado & 1) > 0) {
                        if ((tablero[F1] & tablero[G1]) == NOPIEZA
                                && !casillaAtacada(F1, tablero, color, colorContrario(estado))
                                && !casillaAtacada(G1, tablero, color, colorContrario(estado))) {
                            movimientos.add(E1 << 6 | G1);
                        }
                    }

                    if ((estado & 2) > 0) {

                        if ((tablero[D1] & tablero[C1] & tablero[B1]) == NOPIEZA
                                && !casillaAtacada(D1, tablero, color, colorContrario(estado))
                                && !casillaAtacada(C1, tablero, color, colorContrario(estado))) {
                            movimientos.add(E1 << 6 | C1);
                        }
                    }

            }
        } else {
            if ((estado & 0b000000_000000_000_000_000000_0_11_00) > 0) {

                if ((estado & 4) > 0) {
                    if ((tablero[F8] & tablero[G8]) == NOPIEZA
                            && !casillaAtacada(F8, tablero, color, colorContrario(estado))
                            && !casillaAtacada(G8, tablero, color, colorContrario(estado))) {
                        movimientos.add(E8 << 6 | G8);
                    }
                }

                if ((estado & 8) > 0) {

                    if ((tablero[D8] & tablero[C8] & tablero[B8]) == NOPIEZA
                            && !casillaAtacada(D8, tablero, color, colorContrario(estado))
                            && !casillaAtacada(C8, tablero, color, colorContrario(estado))) {
                        movimientos.add(E8 << 6 | C8);
                    }
                }

            }
        }

    }

    private void movimientosDePeon(int[] tablero, int[] color, int estado, int posicion) {

        var turnoBlanco = esTurnoBlanco(estado);
        int pieza = tablero[posicion];

        if (posicion >= (turnoBlanco ? A2 : A7) && posicion <= (turnoBlanco ? H2 : H7)) {
            if (tablero[posicion + (turnoBlanco ? 8 : -8)] == NOPIEZA && tablero[posicion + (turnoBlanco ? 16 : -16)] == NOPIEZA) {
                validarYAgregar(tablero, color, estado, posicion, posicion + (turnoBlanco ? 16 : -16));
            }
        }

        var destino = posicion + (turnoBlanco ? 8 : -8);

        //avance una casilla
        if (tablero[destino] == NOPIEZA) {
            var m = posicion << 6 | destino;
            if (destino <= H1 || destino >= A8 && destino <= H8) {
                if (movimientoValido(m, tablero, color, estado)) {
                    movimientos.add(m | 1 << 12);
                    movimientos.add(m | 2 << 12);
                    movimientos.add(m | 3 << 12);
                    movimientos.add(m | 4 << 12);
                }

            } else {
                validarYAgregar(tablero, color, estado, posicion, destino);
            }
        }
        avanceDiagonal(tablero, color, estado, posicion, turnoBlanco, pieza, 9);
        avanceDiagonal(tablero, color, estado, posicion, turnoBlanco, pieza, 7);


    }

    private void avanceDiagonal(int[] tablero, int[] color, int estado, int posicion, boolean turnoBlanco, int pieza, int i) {
        int destino;
        int posicionActual;
        destino = posicion + (turnoBlanco ? i : -i);
        if ((destino >= 0 && destino < 64) && esCasillaBlanca(destino) == esCasillaBlanca(posicion)) {
            posicionActual = tablero[destino];
            var m = posicion << 6 | destino;
            if (posicionActual != NOPIEZA) {
                if (color[destino] == BLANCO != (color[posicion] == BLANCO) && !(posicionActual == REY)) {
                    if (destino >= A8 || destino <= H1) {
                        if (movimientoValido(m, tablero, color, estado)) {
                            movimientos.add(m | 1 << 12);
                            movimientos.add(m | 2 << 12);
                            movimientos.add(m | 3 << 12);
                            movimientos.add(m | 4 << 12);
                        }
                    } else {
                        validarYAgregar(tablero, color, estado, posicion, destino);
                    }
                }
            } else if (alPaso(estado, destino) && posicion >= (turnoBlanco ? A5 : A4) && posicion <= (turnoBlanco ? H5 : H4)) {
                if (destino == (estado >> POSICION_PIEZA_AL_PASO & 0b111111)) {
                    validarYAgregar(tablero, color, estado, posicion, destino);
                }
            }
        }
    }

    private void validarYAgregar(int[] tablero, int[] color, int estado, int posicion, int i) {
        var mm = posicion << 6 | i;
        if (movimientoValido(mm, tablero, color, estado))
            movimientos.add(mm);
    }
}
