
package com.wolf.carlitos;


import java.util.Arrays;

import static com.wolf.carlitos.Ataque.*;
import static com.wolf.carlitos.Bitboard.*;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Pieza.bitboard;
import static com.wolf.carlitos.Pieza.valorPiezas;
import static com.wolf.carlitos.Utilidades.convertirAPosicion;
import static java.lang.Long.bitCount;
import static java.lang.Long.numberOfTrailingZeros;
import static java.lang.Math.abs;

public class Tablero {

    private static class StateStack {
        private final int[] estados;
        private int pos;

        StateStack() {
            estados = new int[1024];
            pos = 0;
        }

        public void push(int estado) {
            estados[pos++] = estado;
        }

        public int pop() {
            return estados[--pos];
        }

        public int lastElement() {
            return estados[pos - 1];
        }

        public void clear() {
            pos = 0;
        }

    }

    private static class ZobristStack {
        private final long[] zobrist;
        private int pos;

        ZobristStack() {
            zobrist = new long[1024];
            pos = 0;
        }

        public void push(long estado) {
            zobrist[pos++] = estado;
        }

        public void pop() {
            --pos;
        }

        public long lastElement() {
            return zobrist[pos - 1];
        }

        public long firtElement() {
            return zobrist[0];
        }

        public void clear() {
            pos = 0;
        }

    }

    public int[] tablero = new int[64];
    public int[] color = new int[64];
    private final StateStack estados = new StateStack();
    private final ZobristStack zobristKeys = new ZobristStack();

    // valores aleatorios para generar clave zobrist
    public static long[][][] claveCasilla = new long[2][6][64];
    public static long[] claveAlPaso = new long[16];
    public static long[] claveEnroque = new long[4];
    public static long claveLadoNegro;
    public static int[] direccionAlPaso = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 1, 2, 3, 4, 5, 6, 7,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            8, 9, 10, 11, 12, 13, 14, 15
    };

    static {
        int posicion = 0;
        // llenar numeros aleatorios [color][pieza][casilla]
        for (int i = 0; i < claveCasilla.length; i++) {
            for (int j = 0; j < claveCasilla[i].length; j++) {
                for (int k = 0; k < claveCasilla[i][j].length; k++) {
                    claveCasilla[i][j][k] = ALEATORIOS[posicion++];
                }
            }
        }
        // llenar arreglo de aleatorios para casillas al paso
        for (int i = 0; i < 16; i++) {
            claveAlPaso[i] = ALEATORIOS[posicion++];
        }
        // llenar arreglo de aleatorios para enroques QKqk
        for (int i = 0; i < 4; i++) {
            claveEnroque[i] = ALEATORIOS[posicion++];
        }

        claveLadoNegro = ALEATORIOS[posicion];


    }


    Tablero() {
        setFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public boolean casillaAtacada(int posicion, int colorContrario) {

        if ((ataqueCaballo[posicion] & bitboard[colorContrario][CABALLO]) != 0) return true;

        /* ATAQUE TORRE/DAMA */
        long casillasOcupadas = casillasOcupadas();
        long piezasAtacadas = casillasOcupadas & maskAtaqueTorre[posicion];
        int index = (int) ((piezasAtacadas * rookMagics[posicion]) >>> rook_shift[posicion]);
        long attackSet = Ataque.ataqueTorre[posicion][index];
        if ((attackSet & (bitboard[colorContrario][TORRE] | bitboard[colorContrario][DAMA])) != 0) return true;
        /* FIN ATAQUE TORRE/DAMA */

        /* ATAQUE ALFIL/DAMA */
        piezasAtacadas = casillasOcupadas & maskAtaqueAlfil[posicion];
        index = (int) ((piezasAtacadas * bishopMagics[posicion]) >>> bishop_shift[posicion]);
        attackSet = Ataque.ataqueAlfil[posicion][index];
        if ((attackSet & (bitboard[colorContrario][ALFIL] | bitboard[colorContrario][DAMA])) != 0) return true;
        /* FIN ATAQUE ALFIL/DAMA */


        if ((ataquePeon[colorContrario ^ 1][posicion] & bitboard[colorContrario][PEON]) != 0) return true;


        return (ataqueRey[posicion] & bitboard[colorContrario][REY]) != 0;
    }

    public void takeBack(Movimiento movimiento) {

        int estado = estados.pop();
        zobristKeys.pop();
        int inicio = movimiento.inicio;
        int destino = movimiento.destino;

        boolean turnoBlanco = esTurnoBlanco();

        switch ((estado >> POSICION_TIPO_MOVIMIENTO & 0b111)) {

            case MOVIMIENTO_NORMAL:
            case MOVIMIENTO_REY:
                update(turnoBlanco, tablero[destino], destino, inicio);
                tablero[inicio] = tablero[destino];
                color[inicio] = color[destino];
                break;
            case AL_PASO:
                int posicionPaso = turnoBlanco ? destino - 8 : destino + 8;

                tablero[posicionPaso] = PEON;
                color[posicionPaso] = esTurnoBlanco() ? NEGRO : BLANCO;

                add(!esTurnoBlanco(), PEON, posicionPaso);
                update(turnoBlanco, tablero[destino], destino, inicio);

                tablero[inicio] = tablero[destino];
                color[inicio] = color[destino];

                break;
            case PROMOCION:
                tablero[inicio] = PEON;
                color[inicio] = turnoBlanco ? BLANCO : NEGRO;
                remove(turnoBlanco, tablero[destino], destino);
                add(turnoBlanco, tablero[inicio], inicio);
                break;
            case ENROQUE:

                update(turnoBlanco, tablero[destino], destino, inicio);

                tablero[inicio] = tablero[destino];
                tablero[destino] = NOPIEZA;
                color[inicio] = color[destino];
                color[destino] = NOCOLOR;

                if (destino == G1 || destino == G8) {

                    update(turnoBlanco, TORRE, turnoBlanco ? F1 : F8, turnoBlanco ? H1 : H8);

                    tablero[turnoBlanco ? H1 : H8] = tablero[turnoBlanco ? F1 : F8];
                    tablero[turnoBlanco ? F1 : F8] = NOPIEZA;

                    color[turnoBlanco ? H1 : H8] = color[turnoBlanco ? F1 : F8];
                    color[turnoBlanco ? F1 : F8] = NOCOLOR;

                } else if (destino == C1 || destino == C8) {

                    update(turnoBlanco, TORRE, turnoBlanco ? D1 : D8, turnoBlanco ? A1 : A8);

                    tablero[turnoBlanco ? A1 : A8] = tablero[turnoBlanco ? D1 : D8];
                    tablero[turnoBlanco ? D1 : D8] = NOPIEZA;

                    color[turnoBlanco ? A1 : A8] = color[turnoBlanco ? D1 : D8];
                    color[turnoBlanco ? D1 : D8] = NOCOLOR;
                }

                break;

        }

        tablero[destino] = (estado >> POSICION_PIEZA_CAPTURADA & 0b111);
        if (tablero[destino] != NOPIEZA) {
            add(!turnoBlanco, tablero[destino], destino);
        }

        color[destino] = tablero[destino] == NOPIEZA ? NOCOLOR : esTurnoBlanco() ? NEGRO : BLANCO;

    }

    public void makeMove(Movimiento movimiento) {

        long zobrist = zobristKeys.lastElement();
        int estado = estados.lastElement();

        zobrist ^= claveLadoNegro;

        // se cambia el turno
        estado ^= 16;

        int inicio = movimiento.inicio;
        int destino = movimiento.destino;

        var pieza = tablero[inicio];

        estado = colocarValor(NOPIEZA, POSICION_PIEZA_CAPTURADA, MASK_LIMPIAR_PIEZA_CAPTURADA, estado);
        estado = colocarValor(MOVIMIENTO_NORMAL, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO, estado);

        if (pieza == PEON) {

            if (alPaso(destino)) {

                var posicionPeonAlPaso = destino + (esTurnoBlanco() ? -8 : 8);

                remove(!esTurnoBlanco(), PEON, posicionPeonAlPaso);

                // aqui se remueve la pieza al paso
                tablero[posicionPeonAlPaso] = NOPIEZA;
                color[posicionPeonAlPaso] = NOCOLOR;
                zobrist ^= claveCasilla[colorContrario()][PEON][posicionPeonAlPaso];

                estado = colocarValor(AL_PASO, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO, estado);

                // al paso = false
                estado &= MASK_LIMPIAR_AL_PASO;
                zobrist ^= claveAlPaso[direccionAlPaso[destino]];

            } else if (destino <= H1 || destino >= A8) {

                if (tablero[destino] != NOPIEZA) {

                    estado = colocarValor(tablero[destino], POSICION_PIEZA_CAPTURADA, MASK_LIMPIAR_PIEZA_CAPTURADA, estado);

                    if (tablero[destino] == TORRE) {
                        switch (destino) {
                            case H1:
                                if ((estado & 1) > 0) {
                                    // si hay enroque corto, quitarlo.
                                    zobrist ^= claveEnroque[0];
                                    estado ^= 1;
                                }
                                break;
                            case A1:
                                if ((estado & 2) > 0) {
                                    zobrist ^= claveEnroque[1];
                                    estado ^= 2;
                                }
                                break;
                            case H8:
                                if ((estado & 4) > 0) {
                                    zobrist ^= claveEnroque[2];
                                    estado ^= 4;
                                }
                                break;
                            case A8:
                                if ((estado & 8) > 0) {
                                    zobrist ^= claveEnroque[3];
                                    estado ^= 8;
                                }
                                break;

                        }

                    }
                    // remover pieza capturada
                    remove(!esTurnoBlanco(), tablero[destino], destino);
                    zobrist ^= claveCasilla[colorContrario()][tablero[destino]][destino];
                }
                switch (movimiento.promocion) {
                    case 1:
                        tablero[destino] = DAMA;
                        break;
                    case 2:
                        tablero[destino] = TORRE;
                        break;
                    case 3:
                        tablero[destino] = CABALLO;
                        break;
                    case 4:
                        tablero[destino] = ALFIL;
                        break;
                }
                // agregar pieza promovida
                add(esTurnoBlanco(), tablero[destino], destino);
                zobrist ^= claveCasilla[miColor()][tablero[destino]][destino];
                // remover el peon porque se  promovió
                remove(esTurnoBlanco(), PEON, inicio);
                zobrist ^= claveCasilla[miColor()][PEON][inicio];


                color[destino] = esTurnoBlanco() ? BLANCO : NEGRO;
                estado = colocarValor(PROMOCION, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO, estado);

                tablero[inicio] = NOPIEZA;
                color[inicio] = NOCOLOR;

                // si hay al paso removerlo de zobrist
                if ((estado & 2016) > 0) {
                    zobrist ^= claveAlPaso[direccionAlPaso[estado >> 5 & 63]];
                }

                estado &= MASK_LIMPIAR_AL_PASO;

                estados.push(estado);
                zobristKeys.push(zobrist);
                return;

            } else if (abs(inicio - destino) == 16) {

                int posicionPiezaAlPAso = destino + (esTurnoBlanco() ? -8 : 8);
                if ((estado & 2016) > 0) {
                    // se remueve al paso anterior
                    zobrist ^= claveAlPaso[direccionAlPaso[estado >> 5 & 63]];
                }
                estado = estado & MASK_LIMPIAR_AL_PASO | posicionPiezaAlPAso << POSICION_PIEZA_AL_PASO;

                update(esTurnoBlanco(), PEON, inicio, destino);

                // se mueve el peon
                zobrist ^= claveCasilla[miColor()][PEON][inicio];
                zobrist ^= claveCasilla[miColor()][PEON][destino];

                // se agrega al paso
                zobrist ^= claveAlPaso[direccionAlPaso[posicionPiezaAlPAso]];

                tablero[destino] = pieza;
                tablero[inicio] = NOPIEZA;

                color[destino] = color[inicio];
                color[inicio] = NOCOLOR;

                estados.push(estado);
                zobristKeys.push(zobrist);
                return;
            }

        } else if (pieza == REY) {
            // en los enroques solo se mueven las torres por ser el movimiento especial
            if (abs(inicio - destino) == 2) {
                if (color[inicio] == BLANCO) {
                    if (destino == G1) {
                        update(esTurnoBlanco(), TORRE, H1, F1);
                        // mover torre de h1 a f1
                        zobrist ^= claveCasilla[miColor()][TORRE][H1];
                        zobrist ^= claveCasilla[miColor()][TORRE][F1];

                        tablero[F1] = tablero[H1];
                        tablero[H1] = NOPIEZA;
                        color[F1] = color[H1];
                        color[H1] = NOCOLOR;
                    } else {
                        update(esTurnoBlanco(), TORRE, A1, D1);
                        // mover torre de a1 a d1
                        zobrist ^= claveCasilla[miColor()][TORRE][A1];
                        zobrist ^= claveCasilla[miColor()][TORRE][D1];
                        tablero[D1] = tablero[A1];
                        tablero[A1] = NOPIEZA;
                        color[D1] = color[A1];
                        color[A1] = NOCOLOR;
                    }
                } else {
                    if (destino == G8) {
                        update(esTurnoBlanco(), TORRE, H8, F8);
                        zobrist ^= claveCasilla[miColor()][TORRE][H8];
                        zobrist ^= claveCasilla[miColor()][TORRE][F8];
                        tablero[F8] = tablero[H8];
                        tablero[H8] = NOPIEZA;
                        color[F8] = color[H8];
                        color[H8] = NOCOLOR;
                    } else {
                        update(esTurnoBlanco(), TORRE, A8, D8);
                        zobrist ^= claveCasilla[miColor()][TORRE][A8];
                        zobrist ^= claveCasilla[miColor()][TORRE][D8];
                        tablero[D8] = tablero[A8];
                        tablero[A8] = NOPIEZA;
                        color[D8] = color[A8];
                        color[A8] = NOCOLOR;
                    }
                }
                estado = colocarValor(ENROQUE, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO, estado);
            } else {
                estado = colocarValor(MOVIMIENTO_REY, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO, estado);
            }
            if (esTurnoBlanco()) {
                // enroques blancos false
                if ((estado & 1) > 0) {
                    zobrist ^= claveEnroque[0];
                }
                if ((estado & 2) > 0) {
                    zobrist ^= claveEnroque[1];
                }
                estado &= MASK_LIMPIAR_ENROQUES_BLANCOS;
            } else {
                // enroques negros false
                if ((estado & 4) > 0) {
                    zobrist ^= claveEnroque[2];
                }
                if ((estado & 8) > 0) {
                    zobrist ^= claveEnroque[3];
                }

                estado &= MASK_LIMPIAR_ENROQUES_NEGROS;


            }

        } else if (pieza == TORRE) {

            switch (inicio) {
                case H1:
                    if ((estado & 1) > 0) {
                        // si hay enroque corto, quitarlo.
                        zobrist ^= claveEnroque[0];
                        estado ^= 1;
                    }
                    break;
                case A1:
                    if ((estado & 2) > 0) {
                        zobrist ^= claveEnroque[1];
                        estado ^= 2;
                    }
                    break;
                case H8:
                    if ((estado & 4) > 0) {
                        zobrist ^= claveEnroque[2];
                        estado ^= 4;
                    }
                    break;
                case A8:
                    if ((estado & 8) > 0) {
                        zobrist ^= claveEnroque[3];
                        estado ^= 8;
                    }
                    break;
            }

        }
        estado = colocarValor(tablero[destino], POSICION_PIEZA_CAPTURADA, MASK_LIMPIAR_PIEZA_CAPTURADA, estado);

        if (tablero[destino] != NOPIEZA) {
            remove(!esTurnoBlanco(), tablero[destino], destino);
            zobrist ^= claveCasilla[colorContrario()][tablero[destino]][destino];
        }

        if (tablero[destino] == TORRE) {
            switch (destino) {
                case H1:
                    if ((estado & 1) > 0) {
                        // si hay enroque corto, quitarlo.
                        zobrist ^= claveEnroque[0];
                        estado ^= 1;
                    }
                    break;
                case A1:
                    if ((estado & 2) > 0) {
                        zobrist ^= claveEnroque[1];
                        estado ^= 2;
                    }
                    break;
                case H8:
                    if ((estado & 4) > 0) {
                        zobrist ^= claveEnroque[2];
                        estado ^= 4;
                    }
                    break;
                case A8:
                    if ((estado & 8) > 0) {
                        zobrist ^= claveEnroque[3];
                        estado ^= 8;
                    }
                    break;

            }

        }
        update(esTurnoBlanco(), tablero[inicio], inicio, destino);

        zobrist ^= claveCasilla[miColor()][tablero[inicio]][inicio];
        zobrist ^= claveCasilla[miColor()][tablero[inicio]][destino];

        tablero[destino] = tablero[inicio];
        color[destino] = color[inicio];

        tablero[inicio] = NOPIEZA;
        color[inicio] = NOCOLOR;

        if ((estado & 2016) > 0) {
            zobrist ^= claveAlPaso[direccionAlPaso[estado >> 5 & 63]];
        }

        estado &= MASK_LIMPIAR_AL_PASO;
        estados.push(estado);
        zobristKeys.push(zobrist);
    }

    public long casillasOcupadas() {
        return
                bitboard[BLANCO][PEON] |
                        bitboard[BLANCO][CABALLO] |
                        bitboard[BLANCO][ALFIL] |
                        bitboard[BLANCO][TORRE] |
                        bitboard[BLANCO][DAMA] |
                        bitboard[BLANCO][REY] |
                        bitboard[NEGRO][PEON] |
                        bitboard[NEGRO][CABALLO] |
                        bitboard[NEGRO][ALFIL] |
                        bitboard[NEGRO][TORRE] |
                        bitboard[NEGRO][DAMA] |
                        bitboard[NEGRO][REY];

    }

    public long piezasAmigas(int color) {
        return
                bitboard[color][PEON] |
                        bitboard[color][CABALLO] |
                        bitboard[color][ALFIL] |
                        bitboard[color][TORRE] |
                        bitboard[color][DAMA] |
                        bitboard[color][REY];
    }

    public long piezasEnemigas(int color) {
        color ^= 1;
        return
                bitboard[color][PEON] |
                        bitboard[color][CABALLO] |
                        bitboard[color][ALFIL] |
                        bitboard[color][TORRE] |
                        bitboard[color][DAMA];
    }

    private int colocarValor(int valor, int posicion, int mascara, int estado) {
        return estado & mascara | valor << posicion;
    }

    public int miColor() {
        return (estados.lastElement() >>> 4 & 0b1) ^ 1;
    }

    public boolean enJaque() {
        int posicionRey = numberOfTrailingZeros(bitboard[miColor()][REY]);
        return casillaAtacada(posicionRey, colorContrario());
    }

    public boolean contrarioEnJaque() {
        int posicionRey = numberOfTrailingZeros(bitboard[colorContrario()][REY]);
        return casillaAtacada(posicionRey, miColor());
    }

    public boolean alPaso(int destino) {

        if ((destino >= A3 && destino <= H3 && miColor() == NEGRO) || (destino >= A6 && destino <= H6 && miColor() == BLANCO))
            return (estados.lastElement() >> POSICION_PIEZA_AL_PASO & 0b111111) == destino;

        return false;
    }

    public boolean esTurnoBlanco() {
        return (estados.lastElement() & 0b1_00_00) > 0;
    }

    public int colorContrario() {
        return estados.lastElement() >>> 4 & 0b1;
    }

    public int getPiezaEnPosicion(int posicion, int color) {
        long maskPosicion = 1L << posicion;
        for (int i = 0; i < bitboard[color].length; i++) {
            if ((bitboard[color][i] & maskPosicion) != 0) return i;
        }
        throw new IllegalStateException("no se encontró la pieza");
    }

    public void setFen(String fen) {
        int estado = 0;
        long zobrist = 0;

        tablero = new int[64];
        color = new int[64];
        bitboard = new long[2][6];

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
                    var posicion = Utilidades.casillaANumero(ops[3]);
                    estado |= posicion << POSICION_PIEZA_AL_PASO;
                }

            } else {
                iniciarFen(filas[i], 7 - i);
            }

        }


        for (int i = 0; i < 64; i++) {
            if (tablero[i] == NOPIEZA) continue;
            zobrist ^= claveCasilla[color[i]][tablero[i]][i];
        }

        // agregar enroques disponibles
        if ((estado & 1) > 0) zobrist ^= claveEnroque[0];
        if ((estado & 2) > 0) zobrist ^= claveEnroque[1];
        if ((estado & 4) > 0) zobrist ^= claveEnroque[2];
        if ((estado & 8) > 0) zobrist ^= claveEnroque[3];
        // agregar al paso, si hay
        if ((estado & 2016) > 0) zobrist ^= claveAlPaso[direccionAlPaso[estado >> 5 & 63]];

        // si es el turno del jugador negro agregar clave
        if ((estado >> 4 & 1) == 0) zobrist ^= claveLadoNegro;

        estados.clear();
        zobristKeys.clear();

        // establecer valores iniciales
        estados.push(estado);
        zobristKeys.push(zobrist);

    }

    public void setHistoria(String... movimientos) {
        for (var movimiento : movimientos) {
            makeMove(convertirAPosicion(movimiento));
        }
    }

    private void iniciarFen(String fila, int i) {

        int indexInicio = 8 * i;


        for (char c : fila.toCharArray()) {
            if (c == 'k') {
                tablero[indexInicio] = REY;
                color[indexInicio] = NEGRO;
                add(false, REY, indexInicio);
            }
            if (c == 'q') {
                tablero[indexInicio] = DAMA;
                color[indexInicio] = NEGRO;
                add(false, DAMA, indexInicio);
            }
            if (c == 'r') {
                tablero[indexInicio] = TORRE;
                color[indexInicio] = NEGRO;
                add(false, TORRE, indexInicio);
            }
            if (c == 'b') {
                tablero[indexInicio] = ALFIL;
                color[indexInicio] = NEGRO;
                add(false, ALFIL, indexInicio);
            }
            if (c == 'n') {
                tablero[indexInicio] = CABALLO;
                color[indexInicio] = NEGRO;
                add(false, CABALLO, indexInicio);
            }
            if (c == 'p') {
                tablero[indexInicio] = PEON;
                color[indexInicio] = NEGRO;
                add(false, PEON, indexInicio);
            }

            if (c == 'K') {
                tablero[indexInicio] = REY;
                color[indexInicio] = BLANCO;
                add(true, REY, indexInicio);
            }
            if (c == 'Q') {
                tablero[indexInicio] = DAMA;
                color[indexInicio] = BLANCO;
                add(true, DAMA, indexInicio);
            }
            if (c == 'R') {
                tablero[indexInicio] = TORRE;
                color[indexInicio] = BLANCO;
                add(true, TORRE, indexInicio);
            }
            if (c == 'B') {
                tablero[indexInicio] = ALFIL;
                color[indexInicio] = BLANCO;
                add(true, ALFIL, indexInicio);
            }
            if (c == 'N') {
                tablero[indexInicio] = CABALLO;
                color[indexInicio] = BLANCO;
                add(true, CABALLO, indexInicio);
            }
            if (c == 'P') {
                tablero[indexInicio] = PEON;
                color[indexInicio] = BLANCO;
                add(true, PEON, indexInicio);
            }

            if (Character.isDigit(c)) {
                indexInicio += Integer.parseInt(String.valueOf(c));
            } else {
                indexInicio++;
            }
        }

    }

    public int getEstado() {
        return estados.lastElement();
    }

    public long getZobrist() {
        return zobristKeys.lastElement();
    }

    public void validarKey() {

        long k = 0;

        for (int i = 0; i < 64; i++) {
            if (tablero[i] == NOPIEZA) continue;

            k ^= claveCasilla[color[i]][tablero[i]][i];

        }

        // agregar enroques disponibles
        if ((estados.lastElement() & 1) > 0) k ^= claveEnroque[0];
        if ((estados.lastElement() & 2) > 0) k ^= claveEnroque[1];
        if ((estados.lastElement() & 4) > 0) k ^= claveEnroque[2];
        if ((estados.lastElement() & 8) > 0) k ^= claveEnroque[3];
        // agregar al paso, si hay
        if ((estados.lastElement() & 2016) > 0) k ^= claveAlPaso[direccionAlPaso[estados.lastElement() >> 5 & 63]];
        if ((estados.lastElement() >> 4 & 1) == 0) k ^= claveLadoNegro;

        //System.out.println(Integer.toBinaryString(estados.lastElement()));
        //throw new IllegalStateException("clave no coincide con el estado");

        if (k != getZobrist()) throw new IllegalStateException("no coinciden");

    }

    public void doNull() {
        estados.push(getEstado() ^ 16);
        zobristKeys.push(getZobrist() ^ claveLadoNegro);
    }

    public void takeBackNull() {
        estados.pop();
        zobristKeys.pop();
    }

    public int gameMaterial(int color){
        return
                bitCount(bitboard[color][PEON]) * valorPiezas[PEON] +
                        bitCount(bitboard[color][CABALLO]) * valorPiezas[CABALLO] +
                        bitCount(bitboard[color][ALFIL]) * valorPiezas[ALFIL] +
                        bitCount(bitboard[color][TORRE]) * valorPiezas[TORRE] +
                        bitCount(bitboard[color][DAMA]) * valorPiezas[DAMA];
    }
}
