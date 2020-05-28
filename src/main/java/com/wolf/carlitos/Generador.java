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

    Generador(Tablero tablero){
        this.tab = tablero;
    }
    private Tablero tab;
    private boolean reyEnJaque;
    private boolean turnoBlanco;
    private final Movimientos movimientos = new Movimientos();
    private final Respuesta respuesta = new Respuesta();

    public Respuesta generarMovimientos(int nivel) {

        this.movimientos.iniciar(nivel);
        this.turnoBlanco = tab.esTurnoBlanco();
        reyEnJaque = tab.reyEnJaque();

        int bando = tab.miColor();

        for (long squares = bitboard[bando][PEON]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDePeon(tab.getEstado(), square);
        }

        for (long squares = bitboard[bando][CABALLO]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeCaballo(tab.getEstado(), square);
        }
        for (long squares = bitboard[bando][ALFIL]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeAlfil(tab.getEstado(), square);
        }
        for (long squares = bitboard[bando][TORRE]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeTorre(tab.getEstado(), square);
        }
        for (long squares = bitboard[bando][DAMA]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeDama(tab.getEstado(), square);
        }

        for (long squares = bitboard[bando][REY]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);
            movimientosDeRey(tab.getEstado(), square);
        }

        reyEnJaque = false;

        respuesta.movimientosGenerados = this.movimientos.getMovimientos();
        respuesta.cantidadDeMovimientos = this.movimientos.getPosicionFinal();
        return respuesta;
    }

    public Respuesta generarCapturas(int[] pieza, int[] color, int nivel) {

        movimientos.iniciar(nivel);

        int contrario = tab.colorContrario();
        int miColor = tab.miColor();

        long piezasEnemigas = tab.piezasEnemigas(miColor);
        long piezasAmigas = tab.piezasAmigas(miColor);
        long casillasOcupadas = tab.casillasOcupadas();

        for (long squares = bitboard[miColor][PEON]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            int pos;
            for (int i = 1; i < offsetMailBox[PEON].length; i++) {

                int dir = offsetMailBox[PEON][i];
                pos = direccion[square];
                if (mailBox[pos + (contrario == BLANCO ? -dir : dir)] != -1) {
                    pos += (contrario == BLANCO ? -dir : dir);
                    if (pieza[mailBox[pos]] != NOPIEZA) {
                        if (color[mailBox[pos]] == contrario && pieza[mailBox[pos]] != REY) {
                            if (movimientoValido(square << 6 | mailBox[pos],  tab.getEstado()))
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

                if (movimientoValido(square << 6 | destino,tab.getEstado()))
                    movimientos.add(square << 6 | destino);

            }
        }
        for (long squares = bitboard[miColor][REY]; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long piezasAtacadas = ataqueRey[square] & piezasEnemigas;

            for (long squa = piezasAtacadas; squa != 0; squa = remainder(squa)) {
                int destino = next(squa);
                if (movimientoValido(square << 6 | destino, tab.getEstado()))
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

                if (movimientoValido(square << 6 | destino,  tab.getEstado()))
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

                if (movimientoValido(square << 6 | destino, tab.getEstado()))
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
                if (movimientoValido(square << 6 | destino,tab.getEstado()))
                    movimientos.add(square << 6 | destino);
            }


        }


        respuesta.movimientosGenerados = this.movimientos.getMovimientos();
        respuesta.cantidadDeMovimientos = this.movimientos.getPosicionFinal();

        return respuesta;
    }

    private void movimientosDeTorre( int estado, int posicion) {

        int miColor = turnoBlanco ? BLANCO : NEGRO;

        long casillasOcupadas = tab.casillasOcupadas();

        long maskedBlockers = maskAtaqueTorre[posicion] & casillasOcupadas;

        int index = (int) ((maskedBlockers * rookMagics[posicion]) >>> (64 - bitCount(maskAtaqueTorre[posicion])));

        long attackSet = Ataque.ataqueTorre[posicion][index];

        attackSet = attackSet & ~(tab.piezasAmigas(miColor) | bitboard[tab.colorContrario()][REY]);

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

        int miColor = tab.esTurnoBlanco() ? BLANCO : NEGRO;

        long movimientosCaballo = ataqueCaballo[posicion] & ~(tab.piezasAmigas(miColor) | bitboard[tab.colorContrario()][REY]);

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

            if (movimientoValido(posicion << 6 | square,estado))
                movimientos.add(posicion << 6 | square);

        }

    }

    private void movimientosDeAlfil(int estado, int posicion) {

        int miColor = turnoBlanco ? BLANCO : NEGRO;

        long casillasOcupadas = tab.casillasOcupadas();

        long maskedBlockers = maskAtaqueAlfil[posicion] & casillasOcupadas;

        int index = (int) ((maskedBlockers * bishopMagics[posicion]) >>> (64 - bitCount(maskAtaqueAlfil[posicion])));

        long attackSet = Ataque.ataqueAlfil[posicion][index];

        attackSet = attackSet & ~(tab.piezasAmigas(miColor) | bitboard[tab.colorContrario()][REY]);

        for (long squares = attackSet; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            if (movimientoValido(posicion << 6 | square,estado))
                movimientos.add(posicion << 6 | square);
        }

    }

    private void movimientosDeRey(int estado, int posicion) {


        long movimientosRey = ataqueRey[posicion] & ~(tab.piezasAmigas(tab.miColor()));

        for (long squares = movimientosRey; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            if (movimientoValido(posicion << 6 | square,estado))
                movimientos.add(posicion << 6 | square);
        }


        if ((estado & 0b1111) == 0 || reyEnJaque) return;

        if (turnoBlanco) {
            if ((estado & 0b11) > 0) {

                if ((estado & 1) > 0) {
                    if ((0b11 << F1 & tab.casillasOcupadas()) == 0
                            && !tab.casillaAtacada(F1, tab.colorContrario())
                            && !tab.casillaAtacada(G1, tab.colorContrario())) {
                        movimientos.add(E1 << 6 | G1);
                    }
                }

                if ((estado & 2) > 0) {

                    if ((0b111 << B1 & tab.casillasOcupadas()) == 0
                            && !tab.casillaAtacada(D1, tab.colorContrario())
                            && !tab.casillaAtacada(C1, tab.colorContrario())) {
                        movimientos.add(E1 << 6 | C1);
                    }
                }

            }
        } else {
            if ((estado & 0b11_00) > 0) {

                if ((estado & 4) > 0) {
                    if ((0b11L << F8 & tab.casillasOcupadas()) == 0
                            && !tab.casillaAtacada(F8, tab.colorContrario())
                            && !tab.casillaAtacada(G8, tab.colorContrario())) {
                        movimientos.add(E8 << 6 | G8);
                    }
                }

                if ((estado & 8) > 0) {

                    if ((0b111L << B8 & tab.casillasOcupadas()) == 0
                            && !tab.casillaAtacada(D8, tab.colorContrario())
                            && !tab.casillaAtacada(C8, tab.colorContrario())) {
                        movimientos.add(E8 << 6 | C8);
                    }
                }

            }
        }

    }

    private void movimientosDePeon(int estado, int posicion) {

        long casillasOcupadas = tab.casillasOcupadas();

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

        long attackSet = ataquePeon[tab.miColor()][posicion];

        for (long squares = attackSet; squares != 0; squares = remainder(squares)) {
            int square = next(squares);

            long moveMask = 1L << square;
            if ((moveMask & casillasOcupadas) != 0) {
                moveMask = moveMask & ~(tab.piezasAmigas(tab.miColor()) | bitboard[tab.colorContrario()][REY]);
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
            } else if (tab.alPaso(square)) {
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

    private  boolean movimientoValido(int movimiento, int estado) {
        int inicio = movimiento >> 6 & 0b111111;
        int destino = movimiento & 0b111111;

        int piezaDestino = NOPIEZA;
        int piezaInicio = tab.getPiezaEnPosicion(inicio,tab.miColor());

        if ((tab.casillasOcupadas() & 1L << destino) != 0) {
            piezaDestino = tab.getPiezaEnPosicion(destino, tab.colorContrario());
            remove(!tab.esTurnoBlanco(), piezaDestino, destino);
        }
        update(tab.esTurnoBlanco(), piezaInicio, inicio, destino);


        int posicionRey = numberOfTrailingZeros(bitboard[tab.miColor()][REY]);
        var jaque = tab.casillaAtacada(posicionRey, tab.colorContrario());


        if (piezaDestino != NOPIEZA) {
            add(!tab.esTurnoBlanco(), piezaDestino, destino);
        }
        update(tab.esTurnoBlanco(), piezaInicio, destino, inicio);

        return !jaque;
    }

}
