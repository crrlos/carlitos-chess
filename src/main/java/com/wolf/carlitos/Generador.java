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
import static java.lang.Long.numberOfTrailingZeros;

/**
 * @author carlos
 */
public class Generador {

    static class Movimientos {
        private final Movimiento[][] movimientosPorNivel = new Movimiento[200][256];
        private Movimiento[] current;
        private int posicion;

        Movimientos(){
            for (int i = 0; i < movimientosPorNivel.length; i++) {
                for (int j = 0; j < movimientosPorNivel[i].length; j++) {
                    movimientosPorNivel[i][j] = new Movimiento();
                }
            }
        }

        public void iniciar(int nivel) {
            if (nivel < 0) {
                nivel = 10 + nivel * -1;
            }
            this.current = movimientosPorNivel[nivel];
            this.posicion = 0;
        }

        public void add(int movimiento) {
            current[posicion].inicio = movimiento >>> 6 & 0b111111;
            current[posicion].destino = movimiento  & 0b111111;
            current[posicion].promocion = movimiento >> 12 & 0b111;
            posicion++;
        }

        public Movimiento[] getMovimientos() {
            return current;
        }

        public int getPosicionFinal() {
            return posicion;
        }

    }

    public static class Respuesta {
        public Movimiento[] movimientosGenerados;
        public int cantidadDeMovimientos;
    }

    private boolean reyEnJaque;
    private boolean turnoBlanco;
    private final Movimientos movimientos = new Movimientos();
    private final Respuesta respuesta = new Respuesta();

    public Respuesta generarMovimientos(int estado, int nivel) {

        this.movimientos.iniciar(nivel);

        this.turnoBlanco = esTurnoBlanco(estado);

        reyEnJaque = reyEnJaque(estado);

        int bando = turnoBlanco ? BLANCO : NEGRO;

        for (long squares = piezas[bando][PEON]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDePeon(estado, square);
        }

        for (long squares = piezas[bando][CABALLO]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeCaballo(estado, square);
        }
        for (long squares = piezas[bando][ALFIL]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeAlfil(estado, square);
        }
        for (long squares = piezas[bando][TORRE]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeTorre(estado, square);
        }
        for (long squares = piezas[bando][DAMA]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeDama(estado, square);
        }

        for (long squares = piezas[bando][REY]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeRey(estado, square);
        }

        reyEnJaque = false;

        respuesta.movimientosGenerados = this.movimientos.getMovimientos();
        respuesta.cantidadDeMovimientos = this.movimientos.getPosicionFinal();
        return respuesta;
    }

    public Respuesta generarCapturas(int[] pieza, int[] color, int estado, int nivel) {

        movimientos.iniciar(nivel);

        int contrario = colorContrario(estado);
        int miColor = miColor(estado);

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
                        if (color[pos] == contrario && pieza[pos] != REY) {
                            if (movimientoValido(square << 6 | pos,  estado))
                                movimientos.add(square << 6 | pos);
                        }

                    }

                }

            }

        }
        for (long squares = piezas[miColor][CABALLO]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = ataqueCaballo[square] & piezasEnemigas;

            for (long squa = piezasAtacadas; squa != 0; squa = remainder(squa)) {
                int destino = next(squa);

                if (movimientoValido(square << 6 | destino,estado))
                    movimientos.add(square << 6 | destino);

            }
        }
        for (long squares = piezas[miColor][REY]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = ataqueRey[square] & piezasEnemigas;

            for (long squa = piezasAtacadas; squa != 0; squa = remainder(squa)) {
                int destino = next(squa);
                if (movimientoValido(square << 6 | destino, estado))
                    movimientos.add(square << 6 | destino);

            }

        }
        for (long squares = piezas[miColor][TORRE]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = ataqueTorre[square] & casillasOcupadas;
            int index = (int) ((piezasAtacadas * _rookMagics[square]) >>> (64 - bitCount(ataqueTorre[square])));

            long attackSet = Ataque.ataqueTorre[square][index];

            long ataque = casillasOcupadas & attackSet;
            ataque = ataque & ~piezasAmigas;

            for (long squa = ataque; squa != 0; squa = remainder(squa)) {
                int destino = next(squa);

                if (movimientoValido(square << 6 | destino,  estado))
                    movimientos.add(square << 6 | destino);
            }

        }
        for (long squares = piezas[miColor][ALFIL]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = ataqueAlfil[square] & casillasOcupadas;
            int index = (int) ((piezasAtacadas * _bishopMagics[square]) >>> (64 - bitCount(ataqueAlfil[square])));

            long attackSet = Ataque.ataqueAlfil[square][index];

            long ataque = casillasOcupadas & attackSet;
            ataque = ataque & ~piezasAmigas;

            for (long squa = ataque; squa != 0; squa = remainder(squa)) {
                int destino = next(squa);

                if (movimientoValido(square << 6 | destino, estado))
                    movimientos.add(square << 6 | destino);
            }

        }
        for (long squares = piezas[miColor][DAMA]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = ataqueTorre[square] & casillasOcupadas;
            int index = (int) ((piezasAtacadas * _rookMagics[square]) >>> (64 - bitCount(ataqueTorre[square])));
            long attackSetTorre = Ataque.ataqueTorre[square][index];

            piezasAtacadas = ataqueAlfil[square] & casillasOcupadas;
            index = (int) ((piezasAtacadas * _bishopMagics[square]) >>> (64 - bitCount(ataqueAlfil[square])));
            long attackSetAlfil = Ataque.ataqueAlfil[square][index];

            long ataque = casillasOcupadas & (attackSetTorre | attackSetAlfil);
            ataque = ataque & ~piezasAmigas;

            for (long squa = ataque; squa != 0; squa = remainder(squa)) {
                int destino = next(squa);
                if (movimientoValido(square << 6 | destino,estado))
                    movimientos.add(square << 6 | destino);
            }


        }


        respuesta.movimientosGenerados = this.movimientos.getMovimientos();
        respuesta.cantidadDeMovimientos = this.movimientos.getPosicionFinal();

        return respuesta;
    }

    private void movimientosDeTorre( int estado, int posicion) {

        int miColor = turnoBlanco ? BLANCO : NEGRO;

        long casillasOcupadas = casillasOcupadas();

        long maskedBlockers = ataqueTorre[posicion] & casillasOcupadas;

        int index = (int) ((maskedBlockers * _rookMagics[posicion]) >>> (64 - bitCount(ataqueTorre[posicion])));

        long attackSet = Ataque.ataqueTorre[posicion][index];

        attackSet = attackSet & ~(piezasAmigas(miColor) | piezas[colorContrario(estado)][REY]);

        for (long squares = attackSet; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            if (movimientoValido(posicion << 6 | square, estado))
                movimientos.add(posicion << 6 | square);
        }


    }

    private void movimientosDeDama( int estado, int posicion) {
        movimientosDeAlfil(estado, posicion);
        movimientosDeTorre(estado, posicion);
    }

    private void movimientosDeCaballo(int estado, int posicion) {

        int miColor = esTurnoBlanco(estado) ? BLANCO : NEGRO;

        long movimientosCaballo = ataqueCaballo[posicion] & ~(piezasAmigas(miColor) | piezas[colorContrario(estado)][REY]);

        boolean validar = false;

        // se quita el caballo de esta posición, si el rey no queda en jaque se omite la validación de los movimientos
        remove(turnoBlanco, CABALLO, posicion);
        if (reyEnJaque(estado)) validar = true;
        add(turnoBlanco, CABALLO, posicion);


        for (long squares = movimientosCaballo; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            if (!validar) {
                movimientos.add(posicion << 6 | square);
                continue;
            }

            if (movimientoValido(posicion << 6 | square,estado))
                movimientos.add(posicion << 6 | square);

        }

    }

    private void movimientosDeAlfil(int estado, int posicion) {

        int miColor = turnoBlanco ? BLANCO : NEGRO;

        long casillasOcupadas = casillasOcupadas();

        long maskedBlockers = ataqueAlfil[posicion] & casillasOcupadas;

        int index = (int) ((maskedBlockers * _bishopMagics[posicion]) >>> (64 - bitCount(ataqueAlfil[posicion])));

        long attackSet = Ataque.ataqueAlfil[posicion][index];

        attackSet = attackSet & ~(piezasAmigas(miColor) | piezas[colorContrario(estado)][REY]);

        for (long squares = attackSet; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            if (movimientoValido(posicion << 6 | square,estado))
                movimientos.add(posicion << 6 | square);
        }

    }

    private void movimientosDeRey(int estado, int posicion) {


        long movimientosRey = ataqueRey[posicion] & ~(piezasAmigas(miColor(estado)));

        for (long squares = movimientosRey; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            if (movimientoValido(posicion << 6 | square,estado))
                movimientos.add(posicion << 6 | square);
        }


        if ((estado & 0b1111) == 0 || reyEnJaque) return;

        if (turnoBlanco) {
            if ((estado & 0b000000_000000_000_000_000000_0_00_11) > 0) {

                if ((estado & 1) > 0) {
                    if ((0b11 << F1 & casillasOcupadas()) == 0
                            && !casillaAtacada(F1, colorContrario(estado))
                            && !casillaAtacada(G1, colorContrario(estado))) {
                        movimientos.add(E1 << 6 | G1);
                    }
                }

                if ((estado & 2) > 0) {

                    if ((0b111 << B1 & casillasOcupadas()) == 0
                            && !casillaAtacada(D1, colorContrario(estado))
                            && !casillaAtacada(C1, colorContrario(estado))) {
                        movimientos.add(E1 << 6 | C1);
                    }
                }

            }
        } else {
            if ((estado & 0b000000_000000_000_000_000000_0_11_00) > 0) {

                if ((estado & 4) > 0) {
                    if ((0b11L << F8 & casillasOcupadas()) == 0
                            && !casillaAtacada(F8, colorContrario(estado))
                            && !casillaAtacada(G8, colorContrario(estado))) {
                        movimientos.add(E8 << 6 | G8);
                    }
                }

                if ((estado & 8) > 0) {

                    if ((0b111L << B8 & casillasOcupadas()) == 0
                            && !casillaAtacada(D8, colorContrario(estado))
                            && !casillaAtacada(C8, colorContrario(estado))) {
                        movimientos.add(E8 << 6 | C8);
                    }
                }

            }
        }

    }

    private void movimientosDePeon(int estado, int posicion) {

        long casillasOcupadas = casillasOcupadas();

        if (posicion >= (turnoBlanco ? A2 : A7) && posicion <= (turnoBlanco ? H2 : H7)) {
            int destino = posicion + (turnoBlanco ? 16 : -16);

            long maskAvance = 1L << posicion + (turnoBlanco ? 8 : -8) | 1L << destino;

            if ((maskAvance & casillasOcupadas) == 0)
                validarYAgregar(estado, posicion, destino);

        }

        var destino = posicion + (turnoBlanco ? 8 : -8);
        long maskAvance = 1L << destino;
        //avance una casilla
        if ((casillasOcupadas & maskAvance) == 0) {
            var m = posicion << 6 | destino;
            if (destino <= H1 || destino >= A8) {
                if (movimientoValido(m, estado)) {
                    movimientos.add(m | 1 << 12);
                    movimientos.add(m | 2 << 12);
                    movimientos.add(m | 3 << 12);
                    movimientos.add(m | 4 << 12);
                }

            } else {
                validarYAgregar( estado, posicion, destino);
            }
        }

        long attackSet = ataquePeon[miColor(estado)][posicion];

        for (long squares = attackSet; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long moveMask = 1L << square;
            if ((moveMask & casillasOcupadas) != 0) {
                moveMask = moveMask & ~(piezasAmigas(miColor(estado)) | piezas[colorContrario(estado)][REY]);
                if (moveMask != 0) {
                    int m = posicion << 6 | square;
                    if (destino >= A8 || destino <= H1) {
                        if (movimientoValido(m, estado)) {
                            movimientos.add(m | 1 << 12);
                            movimientos.add(m | 2 << 12);
                            movimientos.add(m | 3 << 12);
                            movimientos.add(m | 4 << 12);
                        }
                    } else {
                        validarYAgregar( estado, posicion, square);
                    }
                }
            } else if (alPaso(estado, square)) {
                int posicionPiezaALPaso = square + (turnoBlanco ? -8 : 8);

                remove(!turnoBlanco, PEON, posicionPiezaALPaso);

                validarYAgregar( estado, posicion, square);

                add(!turnoBlanco, PEON, posicionPiezaALPaso);
            }

        }

    }

    private void validarYAgregar(int estado, int posicion, int i) {
        var mm = posicion << 6 | i;
        if (movimientoValido(mm, estado))
            movimientos.add(mm);
    }

    private static boolean movimientoValido(int movimiento, int estado) {
        int inicio = movimiento >> 6 & 0b111111;
        int destino = movimiento & 0b111111;

        int piezaDestino = NOPIEZA;
        int piezaInicio = getPiezaEnPosicion(inicio,miColor(estado));

        if ((casillasOcupadas() & 1L << destino) != 0) {
            piezaDestino = getPiezaEnPosicion(destino, colorContrario(estado));
            remove(!esTurnoBlanco(estado), piezaDestino, destino);
        }
        update(esTurnoBlanco(estado), piezaInicio, inicio, destino);


        int posicionRey = numberOfTrailingZeros(piezas[miColor(estado)][REY]);
        var jaque = casillaAtacada(posicionRey, colorContrario(estado));


        if (piezaDestino != NOPIEZA) {
            add(!esTurnoBlanco(estado), piezaDestino, destino);
        }
        update(esTurnoBlanco(estado), piezaInicio, destino, inicio);

        return !jaque;
    }

}
