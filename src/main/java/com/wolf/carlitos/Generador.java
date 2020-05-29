/*
 * To change pieza license header, choose License Headers in Project Properties.
 * To change pieza template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import static com.wolf.carlitos.Ataque.*;
import static com.wolf.carlitos.Bitboard.*;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.MailBox.*;
import static com.wolf.carlitos.Pieza.bitboard;
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
        Movimientos() {
            for (int i = 0; i < movimientosPorNivel.length; i++) {
                for (int j = 0; j < movimientosPorNivel[i].length; j++) {
                    movimientosPorNivel[i][j] = new Movimiento();
                }
            }
        }

        public void iniciar(int ply) {
            this.current = movimientosPorNivel[ply];
            this.posicion = 0;
        }

        public void add(int movimiento) {
            current[posicion].inicio = movimiento >>> 6 & 0b111111;
            current[posicion].destino = movimiento & 0b111111;
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

    Generador(Tablero tablero) {
        this.tab = tablero;
    }

    private final Tablero tab;
    private boolean reyEnJaque;
    private boolean turnoBlanco;
    private int estado;
    private int miColor;
    private int colorContrario;
    private long casillasOcupadas;
    private final Movimientos movimientos = new Movimientos();
    private final Respuesta respuesta = new Respuesta();

    private void init(int ply) {
        this.movimientos.iniciar(ply);
        this.turnoBlanco = tab.esTurnoBlanco();
        this.estado = tab.getEstado();
        this.miColor = tab.miColor();
        this.colorContrario = tab.colorContrario();
        this.reyEnJaque = tab.reyEnJaque();
        this.casillasOcupadas = tab.casillasOcupadas();
    }

    public Respuesta generarMovimientos(int ply) {
        init(ply);

        for (long squares = bitboard[miColor][PEON]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDePeon(square);
        }

        for (long squares = bitboard[miColor][CABALLO]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeCaballo(square);
        }
        for (long squares = bitboard[miColor][ALFIL]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeAlfil(square);
        }
        for (long squares = bitboard[miColor][TORRE]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeTorre(square);
        }
        for (long squares = bitboard[miColor][DAMA]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeDama(square);
        }

        for (long squares = bitboard[miColor][REY]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeRey(square);
        }

        this.reyEnJaque = false;

        respuesta.movimientosGenerados = this.movimientos.getMovimientos();
        respuesta.cantidadDeMovimientos = this.movimientos.getPosicionFinal();
        return respuesta;
    }

    public Respuesta generarCapturas(int[] pieza, int[] color, int nivel) {


        init(nivel);

        long piezasEnemigas = tab.piezasEnemigas(miColor);
        long piezasAmigas = tab.piezasAmigas(miColor);

        for (long squares = bitboard[miColor][PEON]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            int pos;
            for (int i = 1; i < offsetMailBox[PEON].length; i++) {

                int dir = offsetMailBox[PEON][i];
                pos = direccion[square];
                if (mailBox[pos + (colorContrario == BLANCO ? -dir : dir)] != -1) {
                    pos += (colorContrario == BLANCO ? -dir : dir);
                    if (pieza[mailBox[pos]] != NOPIEZA) {
                        if (color[mailBox[pos]] == colorContrario && pieza[mailBox[pos]] != REY) {
                            if (movimientoValido(square << 6 | mailBox[pos]))
                                movimientos.add(square << 6 | mailBox[pos]);
                        }

                    }

                }

            }

        }
        for (long squares = bitboard[miColor][CABALLO]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = ataqueCaballo[square] & piezasEnemigas;

            for (long squa = piezasAtacadas; squa != 0; squa = remainder(squa)) {
                int destino = next(squa);

                if (movimientoValido(square << 6 | destino))
                    movimientos.add(square << 6 | destino);

            }
        }
        for (long squares = bitboard[miColor][REY]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = ataqueRey[square] & piezasEnemigas;

            for (long squa = piezasAtacadas; squa != 0; squa = remainder(squa)) {
                int destino = next(squa);
                if (movimientoValido(square << 6 | destino))
                    movimientos.add(square << 6 | destino);

            }

        }
        for (long squares = bitboard[miColor][TORRE]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = maskAtaqueTorre[square] & casillasOcupadas;
            int index = (int) ((piezasAtacadas * rookMagics[square]) >>> (64 - bitCount(maskAtaqueTorre[square])));

            long attackSet = Ataque.ataqueTorre[square][index];

            long ataque = casillasOcupadas & attackSet;
            ataque = ataque & ~piezasAmigas;

            for (long squa = ataque; squa != 0; squa = remainder(squa)) {
                int destino = next(squa);

                if (movimientoValido(square << 6 | destino))
                    movimientos.add(square << 6 | destino);
            }

        }
        for (long squares = bitboard[miColor][ALFIL]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = maskAtaqueAlfil[square] & casillasOcupadas;
            int index = (int) ((piezasAtacadas * bishopMagics[square]) >>> (64 - bitCount(maskAtaqueAlfil[square])));

            long attackSet = Ataque.ataqueAlfil[square][index];

            long ataque = casillasOcupadas & attackSet;
            ataque = ataque & ~piezasAmigas;

            for (long squa = ataque; squa != 0; squa = remainder(squa)) {
                int destino = next(squa);

                if (movimientoValido(square << 6 | destino))
                    movimientos.add(square << 6 | destino);
            }

        }
        for (long squares = bitboard[miColor][DAMA]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = maskAtaqueTorre[square] & casillasOcupadas;
            int index = (int) ((piezasAtacadas * rookMagics[square]) >>> (64 - bitCount(maskAtaqueTorre[square])));
            long attackSetTorre = Ataque.ataqueTorre[square][index];

            piezasAtacadas = maskAtaqueAlfil[square] & casillasOcupadas;
            index = (int) ((piezasAtacadas * bishopMagics[square]) >>> (64 - bitCount(maskAtaqueAlfil[square])));
            long attackSetAlfil = Ataque.ataqueAlfil[square][index];

            long ataque = casillasOcupadas & (attackSetTorre | attackSetAlfil);
            ataque = ataque & ~piezasAmigas;

            for (long squa = ataque; squa != 0; squa = remainder(squa)) {
                int destino = next(squa);
                if (movimientoValido(square << 6 | destino))
                    movimientos.add(square << 6 | destino);
            }


        }


        respuesta.movimientosGenerados = this.movimientos.getMovimientos();
        respuesta.cantidadDeMovimientos = this.movimientos.getPosicionFinal();

        return respuesta;
    }

    private void movimientosDeTorre(int posicion) {

        long maskedBlockers = maskAtaqueTorre[posicion] & casillasOcupadas;

        int index = (int) ((maskedBlockers * rookMagics[posicion]) >>> rook_shift[posicion]);

        long attackSet = Ataque.ataqueTorre[posicion][index];

        attackSet = attackSet & ~(tab.piezasAmigas(miColor) | bitboard[colorContrario][REY]);

        for (long squares = attackSet; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            if (movimientoValido(posicion << 6 | square))
                movimientos.add(posicion << 6 | square);
        }


    }

    private void movimientosDeDama(int posicion) {
        movimientosDeAlfil(posicion);
        movimientosDeTorre(posicion);
    }

    private void movimientosDeCaballo(int posicion) {

        int miColor = turnoBlanco ? BLANCO : NEGRO;

        long movimientosCaballo = ataqueCaballo[posicion] & ~(tab.piezasAmigas(miColor) | bitboard[colorContrario][REY]);

        boolean validar = false;

        // se quita el caballo de esta posición, si el rey no queda en jaque se omite la validación de los movimientos
        remove(turnoBlanco, CABALLO, posicion);
        if (tab.reyEnJaque()) validar = true;
        add(turnoBlanco, CABALLO, posicion);


        for (long squares = movimientosCaballo; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            if (!validar) {
                movimientos.add(posicion << 6 | square);
                continue;
            }

            if (movimientoValido(posicion << 6 | square))
                movimientos.add(posicion << 6 | square);

        }

    }

    private void movimientosDeAlfil(int posicion) {

        long maskedBlockers = maskAtaqueAlfil[posicion] & casillasOcupadas;

        int index = (int) ((maskedBlockers * bishopMagics[posicion]) >>> bishop_shift[posicion]);

        long attackSet = Ataque.ataqueAlfil[posicion][index];

        attackSet = attackSet & ~(tab.piezasAmigas(miColor) | bitboard[colorContrario][REY]);

        for (long squares = attackSet; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            if (movimientoValido(posicion << 6 | square))
                movimientos.add(posicion << 6 | square);
        }

    }

    private void movimientosDeRey(int posicion) {


        long movimientosRey = ataqueRey[posicion] & ~(tab.piezasAmigas(miColor));

        for (long squares = movimientosRey; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            if (movimientoValido(posicion << 6 | square))
                movimientos.add(posicion << 6 | square);
        }


        if ((estado & 0b1111) == 0 || reyEnJaque) return;

        if (turnoBlanco) {
            if ((estado & 0b11) > 0) {

                if ((estado & 1) > 0) {
                    if ((0b11 << F1 & casillasOcupadas) == 0
                            && !tab.casillaAtacada(F1, colorContrario)
                            && !tab.casillaAtacada(G1, colorContrario)) {
                        movimientos.add(E1 << 6 | G1);
                    }
                }

                if ((estado & 2) > 0) {

                    if ((0b111 << B1 & casillasOcupadas) == 0
                            && !tab.casillaAtacada(D1, colorContrario)
                            && !tab.casillaAtacada(C1, colorContrario)) {
                        movimientos.add(E1 << 6 | C1);
                    }
                }

            }
        } else {
            if ((estado & 0b11_00) > 0) {

                if ((estado & 4) > 0) {
                    if ((0b11L << F8 & casillasOcupadas) == 0
                            && !tab.casillaAtacada(F8, colorContrario)
                            && !tab.casillaAtacada(G8, colorContrario)) {
                        movimientos.add(E8 << 6 | G8);
                    }
                }

                if ((estado & 8) > 0) {

                    if ((0b111L << B8 & casillasOcupadas) == 0
                            && !tab.casillaAtacada(D8, colorContrario)
                            && !tab.casillaAtacada(C8, colorContrario)) {
                        movimientos.add(E8 << 6 | C8);
                    }
                }

            }
        }

    }

    private void movimientosDePeon(int posicion) {

        if (posicion >= (turnoBlanco ? A2 : A7) && posicion <= (turnoBlanco ? H2 : H7)) {
            int destino = posicion + (turnoBlanco ? 16 : -16);

            long maskAvance = 1L << posicion + (turnoBlanco ? 8 : -8) | 1L << destino;

            if ((maskAvance & casillasOcupadas) == 0)
                validarYAgregar(posicion, destino);

        }

        var destino = posicion + (turnoBlanco ? 8 : -8);
        long maskAvance = 1L << destino;
        //avance una casilla
        if ((casillasOcupadas & maskAvance) == 0) {
            var m = posicion << 6 | destino;
            if (destino <= H1 || destino >= A8) {
                if (movimientoValido(m)) {
                    movimientos.add(m | 1 << 12);
                    movimientos.add(m | 2 << 12);
                    movimientos.add(m | 3 << 12);
                    movimientos.add(m | 4 << 12);
                }

            } else {
                validarYAgregar(posicion, destino);
            }
        }

        long attackSet = ataquePeon[miColor][posicion];

        for (long squares = attackSet; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long moveMask = 1L << square;
            if ((moveMask & casillasOcupadas) != 0) {
                moveMask = moveMask & ~(tab.piezasAmigas(miColor) | bitboard[colorContrario][REY]);
                if (moveMask != 0) {
                    int m = posicion << 6 | square;
                    if (destino >= A8 || destino <= H1) {
                        if (movimientoValido(m)) {
                            movimientos.add(m | 1 << 12);
                            movimientos.add(m | 2 << 12);
                            movimientos.add(m | 3 << 12);
                            movimientos.add(m | 4 << 12);
                        }
                    } else {
                        validarYAgregar(posicion, square);
                    }
                }
            } else if (tab.alPaso(square)) {
                int posicionPiezaALPaso = square + (turnoBlanco ? -8 : 8);

                remove(!turnoBlanco, PEON, posicionPiezaALPaso);

                validarYAgregar(posicion, square);

                add(!turnoBlanco, PEON, posicionPiezaALPaso);
            }

        }

    }

    private void validarYAgregar(int posicion, int i) {
        var mm = posicion << 6 | i;
        if (movimientoValido(mm))
            movimientos.add(mm);
    }

    private boolean movimientoValido(int movimiento) {
        int inicio = movimiento >> 6 & 0b111111;
        int destino = movimiento & 0b111111;

        int piezaDestino = NOPIEZA;
        int piezaInicio = tab.getPiezaEnPosicion(inicio, miColor);

        if ((casillasOcupadas & 1L << destino) != 0) {
            piezaDestino = tab.getPiezaEnPosicion(destino, colorContrario);
            remove(!turnoBlanco, piezaDestino, destino);
        }
        update(turnoBlanco, piezaInicio, inicio, destino);


        int posicionRey = numberOfTrailingZeros(bitboard[miColor][REY]);
        var jaque = tab.casillaAtacada(posicionRey, colorContrario);


        if (piezaDestino != NOPIEZA) {
            add(!turnoBlanco, piezaDestino, destino);
        }
        update(turnoBlanco, piezaInicio, destino, inicio);

        return !jaque;
    }

}
