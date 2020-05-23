/*
 * To change pieza license header, choose License Headers in Project Properties.
 * To change pieza template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import static com.wolf.carlitos.Ataque._bishopMagics;
import static com.wolf.carlitos.Ataque._rookMagics;
import static com.wolf.carlitos.Bitboard.*;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Tablero.*;
import static com.wolf.carlitos.Utilidades.*;
import static java.lang.Long.bitCount;

/**
 * @author carlos
 */
public class Generador {

    static class Movimientos {
        private final int[][] movimientosPorNivel = new int[200][256];
        private int[] current;
        private int posicion;

        public void iniciar(int nivel) {
            if (nivel < 0) {
                nivel = 10 + nivel * -1;
            }
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

    public static class Respuesta {
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

        reyEnJaque = casillaAtacada(posicionRey, pieza, color, turnoBlanco ? NEGRO : BLANCO);

        int bando = turnoBlanco ? BLANCO : NEGRO;

        for (long squares = piezas[bando][PEON]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDePeon(pieza, color, estado, square);
        }

        for (long squares = piezas[bando][CABALLO]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeCaballo(pieza, color, estado, square);
        }
        for (long squares = piezas[bando][ALFIL]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeAlfil(pieza, color, estado, square);
        }
        for (long squares = piezas[bando][TORRE]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeTorre(pieza, color, estado, square);
        }
        for (long squares = piezas[bando][DAMA]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeDama(pieza, color, estado, square);
        }

        for (long squares = piezas[bando][REY]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeRey(pieza, color, estado, square);
        }

        reyEnJaque = false;

        respuesta.movimientosGenerados = this.movimientos.getMovimientos();
        respuesta.cantidadDeMovimientos = this.movimientos.getPosicionFinal();
        return respuesta;
    }
    public Respuesta generarCapturas(int[] pieza, int[] color, int estado, int nivel){

        movimientos.iniciar(nivel);

        int contrario = colorContrario(estado);
        int miColor = esTurnoBlanco(estado) ? BLANCO :NEGRO;

        long piezasEnemigas = piezasEnemigas(miColor);
        long piezasAmigas = piezasAmigas(miColor);
        long casillasOcupadas = casillasOcupadas();

        for (long squares = piezas[miColor][PEON]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            int pos;
            for (int i = 1; i < offsetMailBox[PEON].length; i++) {

                int dir = offsetMailBox[PEON][i];
                pos = square;
                if (mailBox[direccion[pos] + (contrario == BLANCO ? -dir : dir)] != -1) {
                    pos += (contrario == BLANCO ? -offset64[PEON][i] : offset64[PEON][i]);
                    if (pieza[pos] != NOPIEZA) {
                        if (color[pos] == contrario && pieza[pos] != REY){
                            if(movimientoValido(square << 6 | pos,pieza,color,estado))
                                movimientos.add(square << 6 | pos);
                        }

                    }

                }

            }

        }
        for (long squares = piezas[miColor][CABALLO]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = ataqueCaballo[square] & piezasEnemigas;

            for (long squa = piezasAtacadas ; squa != 0; squa = remainder(squa)){
                int destino = next(squa);

                if(movimientoValido(square << 6 | destino,pieza,color,estado))
                    movimientos.add(square << 6 | destino);

            }
        }
        for (long squares = piezas[miColor][REY]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = ataqueRey[square] & piezasEnemigas;

            for (long squa = piezasAtacadas ; squa != 0; squa = remainder(squa)){
                int destino = next(squa);

                int ec = turnoBlanco ?
                        estado & MASK_LIMPIAR_POSICION_REY_BLANCO | destino << POSICION_REY_BLANCO :
                        estado & MASK_LIMPIAR_POSICION_REY_NEGRO | destino << POSICION_REY_NEGRO;

                if(movimientoValido(square << 6 | destino,pieza,color,ec))
                    movimientos.add(square << 6 | destino);

            }

        }
        for (long squares = piezas[miColor][TORRE]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = ataqueTorre[square] & casillasOcupadas;
            int index = (int) ((piezasAtacadas * _rookMagics[square]) >>> (64 -  bitCount(ataqueTorre[square])));

            long attackSet = Ataque.ataqueTorre[square][index];

            long ataque = casillasOcupadas & attackSet;
            ataque =  ataque & ~piezasAmigas;

            for (long squa = ataque ; squa != 0; squa = remainder(squa)){
                int destino = next(squa);

                if(movimientoValido(square << 6 | destino,pieza,color,estado))
                    movimientos.add(square << 6 | destino);
            }

        }
        for (long squares = piezas[miColor][ALFIL]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = ataqueAlfil[square] & casillasOcupadas;
            int index = (int) ((piezasAtacadas * _bishopMagics[square]) >>> (64 -  bitCount(ataqueAlfil[square])));

            long attackSet = Ataque.ataqueAlfil[square][index];

            long ataque = casillasOcupadas & attackSet;
            ataque =  ataque & ~piezasAmigas;

            for (long squa = ataque ; squa != 0; squa = remainder(squa)){
                int destino = next(squa);

                if(movimientoValido(square << 6 | destino,pieza,color,estado))
                    movimientos.add(square << 6 | destino);
            }

        }
        for (long squares = piezas[miColor][DAMA]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = ataqueTorre[square] & casillasOcupadas;
            int index = (int) ((piezasAtacadas * _rookMagics[square]) >>> (64 -  bitCount(ataqueTorre[square])));
            long attackSetTorre = Ataque.ataqueTorre[square][index];

            piezasAtacadas = ataqueAlfil[square] & casillasOcupadas;
            index = (int) ((piezasAtacadas * _bishopMagics[square]) >>> (64 -  bitCount(ataqueAlfil[square])));
            long attackSetAlfil = Ataque.ataqueAlfil[square][index];

            long ataque = casillasOcupadas & (attackSetTorre | attackSetAlfil);
            ataque =  ataque & ~piezasAmigas;

            for (long squa = ataque ; squa != 0; squa = remainder(squa)){
                int destino = next(squa);
                if(movimientoValido(square << 6 | destino,pieza,color,estado))
                    movimientos.add(square << 6 | destino);
            }



        }


        respuesta.movimientosGenerados = this.movimientos.getMovimientos();
        respuesta.cantidadDeMovimientos = this.movimientos.getPosicionFinal();

        return respuesta;
    }

    private void movimientosDeTorre(int[] tablero, int[] color, int estado, int posicion) {

        int miColor = turnoBlanco ? BLANCO :NEGRO;

        long casillasOcupadas = casillasOcupadas();

        long maskedBlockers = ataqueTorre[posicion] & casillasOcupadas;

        int index = (int) ((maskedBlockers * _rookMagics[posicion]) >>> (64 - bitCount(ataqueTorre[posicion])));

        long attackSet = Ataque.ataqueTorre[posicion][index];

        attackSet = attackSet & ~(piezasAmigas(miColor) | piezas[colorContrario(estado)][REY] );

        for(long squares = attackSet; squares != 0; squares = remainder(squares)){
            int square = next(squares);

            if(movimientoValido(posicion << 6 | square,tablero,color,estado))
                movimientos.add(posicion << 6 | square);
        }


    }

    private void movimientosDeDama(int[] tablero, int[] color, int estado, int posicion) {
        movimientosDeAlfil(tablero, color, estado, posicion);
        movimientosDeTorre(tablero, color, estado, posicion);
    }

    private void movimientosDeCaballo(int[] tablero, int[] color, int estado, int posicion) {

       int miColor = esTurnoBlanco(estado) ? BLANCO : NEGRO;

       long movimientosCaballo = ataqueCaballo[posicion] & ~(piezasAmigas(miColor) | piezas[colorContrario(estado)][REY]);

       boolean validar = false;

       // se quita el caballo de esta posición, si el rey no queda en jaque se omite la validación de los movimientos
        remove(turnoBlanco, CABALLO, posicion);
        if (reyEnJaque(tablero, color, estado)) validar = true;
        add(turnoBlanco, CABALLO, posicion);


        for (long squares = movimientosCaballo; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            if(!validar) {
                movimientos.add(posicion << 6 | square);
                continue;
            }

            if (movimientoValido(posicion << 6 | square, tablero, color, estado))
                movimientos.add(posicion << 6 | square);

        }

    }

    private void movimientosDeAlfil(int[] tablero, int[] color, int estado, int posicion) {

        int miColor = turnoBlanco ? BLANCO :NEGRO;

        long casillasOcupadas = casillasOcupadas();

        long maskedBlockers = ataqueAlfil[posicion] & casillasOcupadas;

        int index = (int) ((maskedBlockers * _bishopMagics[posicion]) >>> (64 - bitCount(ataqueAlfil[posicion])));

        long attackSet = Ataque.ataqueAlfil[posicion][index];

        attackSet = attackSet & ~(piezasAmigas(miColor) | piezas[colorContrario(estado)][REY]);

        for(long squares = attackSet; squares != 0; squares = remainder(squares)){
            int square = next(squares);

            if(movimientoValido(posicion << 6 | square,tablero,color,estado))
                movimientos.add(posicion << 6 | square);
        }

    }

    private void movimientosDeRey(int[] tablero, int[] color, int estado, int posicion) {


        long movimientosRey = ataqueRey[posicion] & ~(piezasAmigas(miColor(estado)));

        for (long squares = movimientosRey; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            if (movimientoValido(posicion << 6 | square, tablero, color, estado))
                movimientos.add(posicion << 6 | square);
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

                remove(!turnoBlanco, PEON, posicionPiezaALPaso);

                validarYAgregar(tablero, color, estado, posicion, destino);

                add(!turnoBlanco, PEON, posicionPiezaALPaso);

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
