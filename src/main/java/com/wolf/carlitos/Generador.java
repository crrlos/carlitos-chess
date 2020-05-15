/*
 * To change pieza license header, choose License Headers in Project Properties.
 * To change pieza template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import javax.print.attribute.standard.Finishings;
import java.util.Arrays;

import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Tablero.*;
import static com.wolf.carlitos.Utilidades.*;

/**
 * @author carlos
 */
public class Generador {

    static class Movimientos {
        private final int[][] movimientosPorNivel = new int[10][256];
        private int[] current;
        private int posicion;

        public void iniciar(int nivel) {
            this.current = movimientosPorNivel[nivel];
            this.posicion = 0;
        }

        public void add(int movimiento) {
            current[posicion++] = movimiento;
        }

        public int[] getMovimientos() {
            return current;
        }

        public int getPosicionFinal() {
            return posicion;
        }

    }

    static class Respuesta {
        public int[] movimientosGenerados;
        public int cantidadDeMovimientos;
    }

    private boolean reyEnJaque;
    private boolean turnoBlanco;
    private final Movimientos movimientos = new Movimientos();
    private final Respuesta respuesta = new Respuesta();

    public Respuesta generarMovimientos(int[] pieza, int[] color, int estado, int nivel) {

        this.movimientos.iniciar(nivel);

        this.turnoBlanco = esTurnoBlanco(estado);

        int posicionRey = posicionRey(estado, turnoBlanco ? POSICION_REY_BLANCO : POSICION_REY_NEGRO);

        reyEnJaque = Tablero.casillaAtacada(posicionRey, pieza, color, turnoBlanco ? NEGRO : BLANCO);

        for (int i = 0; i < pieza.length; i++) {

            var piezaActual = pieza[i];

            if (piezaActual != NOPIEZA && color[i] == BLANCO == turnoBlanco) {
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

        respuesta.movimientosGenerados = this.movimientos.getMovimientos();
        respuesta.cantidadDeMovimientos = this.movimientos.getPosicionFinal();
        return respuesta;
    }

    private void movimientosDeTorre(int[] tablero, int[] color, int estado, int posicion) {


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
                    if (movimientoValido(posicion << 6 | pos, tablero, color, estado))
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
        int pos;

        if (!reyEnJaque) {

            boolean movimientosSinValidar = false;

            tablero[posicion] = NOPIEZA;
            color[posicion] = NOCOLOR;

            if (!casillaAtacada(posicionRey(estado, turnoBlanco ? POSICION_REY_BLANCO : POSICION_REY_NEGRO), tablero, color, colorContrario(estado)))
                movimientosSinValidar = true;

            tablero[posicion] = CABALLO;
            color[posicion] = turnoBlanco ? BLANCO : NEGRO;

            if (movimientosSinValidar) {
                for (int i = 0; i < offsetMailBox[CABALLO].length; i++) {
                    int mailOffset = offsetMailBox[CABALLO][i];

                    if (mailBox[direccion[posicion] + mailOffset] != -1) {

                        pos = posicion + offset64[CABALLO][i];

                        if ((tablero[pos] == NOPIEZA || color[pos] != color[posicion]) && tablero[pos] != REY)
                            movimientos.add(posicion << 6 | pos);
                    }
                }
            }
            return;
        }


        for (int i = 0; i < offsetMailBox[CABALLO].length; i++) {
            int mailOffset = offsetMailBox[CABALLO][i];

            if (mailBox[direccion[posicion] + mailOffset] != -1) {

                pos = posicion + offset64[CABALLO][i];

                if ((tablero[pos] == NOPIEZA || color[pos] != color[posicion]) && movimientoValido(posicion << 6 | pos, tablero, color, estado))
                    movimientos.add(posicion << 6 | pos);

            }
        }
    }

    private void movimientosDeAlfil(int[] tablero, int[] color, int estado, int posicion) {


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

        for (int i = 0; i < offsetMailBox[ALFIL].length; i++) {

            int dir = offsetMailBox[ALFIL][i];
            int dirLocal = offset64[ALFIL][i];
            int pos = posicion;
            while (mailBox[direccion[pos] + dir] != -1) {
                pos += dirLocal;

                if (tablero[pos] == NOPIEZA) {
                    if (movimientoValido(posicion << 6 | pos, tablero, color, estado))
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

        for (int i = 0; i < offsetMailBox[DAMA].length; i++) {
            int mailOffset = offsetMailBox[DAMA][i];

            if (mailBox[direccion[posicion] + mailOffset] != -1) {

                pos = posicion + offset64[DAMA][i];

                int ec = turnoBlanco ?
                        estado & MASK_LIMPIAR_POSICION_REY_BLANCO | pos << POSICION_REY_BLANCO :
                        estado & MASK_LIMPIAR_POSICION_REY_NEGRO | pos << POSICION_REY_NEGRO;


                if ((tablero[pos] == NOPIEZA || color[pos] != color[posicion]) && movimientoValido(posicion << 6 | pos, tablero, color, ec))
                    movimientos.add(posicion << 6 | pos);


            }
        }

        if ((estado & 0b1111) == 0 || reyEnJaque) return;

        if (turnoBlanco) {
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

        if (posicion >= (turnoBlanco ? A2 : A7) && posicion <= (turnoBlanco ? H2 : H7)) {
            int destino = posicion + (turnoBlanco ? 16 : -16);
            if ((tablero[posicion + (turnoBlanco ? 8 : -8)] & tablero[destino]) == NOPIEZA) {
                validarYAgregar(tablero, color, estado, posicion, destino);
            }
        }

        var destino = posicion + (turnoBlanco ? 8 : -8);

        //avance una casilla
        if (tablero[destino] == NOPIEZA) {
            var m = posicion << 6 | destino;
            if (destino <= H1 || destino >= A8) {
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

        avanceDiagonal(tablero, color, estado, posicion, turnoBlanco, offset64[PEON][1]);
        avanceDiagonal(tablero, color, estado, posicion, turnoBlanco, offset64[PEON][2]);

    }

    private void avanceDiagonal(int[] tablero, int[] color, int estado, int posicion, boolean turnoBlanco, int i) {
        int destino;
        int posicionActual;
        destino = posicion + (turnoBlanco ? i : -i);
        if (mailBox[direccion[posicion] + (turnoBlanco ? i + 2 : (-i - 2))] != -1) {
            posicionActual = tablero[destino];
            var m = posicion << 6 | destino;
            if (posicionActual != NOPIEZA) {
                if (color[destino] != color[posicion] && !(posicionActual == REY)) {
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
            } else if (alPaso(estado, destino)) {

                int posicionPiezaALPaso = destino + (turnoBlanco ? -8 : 8);
                tablero[posicionPiezaALPaso] = NOPIEZA;
                color[posicionPiezaALPaso] = NOCOLOR;

                validarYAgregar(tablero, color, estado, posicion, destino);

                tablero[posicionPiezaALPaso] = PEON;
                color[posicionPiezaALPaso] = turnoBlanco ? NEGRO : BLANCO;
            }
        }
    }

    private void validarYAgregar(int[] tablero, int[] color, int estado, int posicion, int i) {
        var mm = posicion << 6 | i;
        if (movimientoValido(mm, tablero, color, estado))
            movimientos.add(mm);
    }
}
