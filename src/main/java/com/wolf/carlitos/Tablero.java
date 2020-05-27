
package com.wolf.carlitos;


import java.util.Arrays;

import static com.wolf.carlitos.Ataque.*;
import static com.wolf.carlitos.Bitboard.*;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Pieza.piezas;
import static com.wolf.carlitos.Utilidades.convertirAPosicion;
import static java.lang.Long.bitCount;
import static java.lang.Long.numberOfTrailingZeros;
import static java.lang.Math.abs;

public class Tablero {

    static class Stack{
        private final int[] estados;
        private int pos;

        Stack(){
            estados = new int[1024];
            pos = 0;
        }
        public void push(int estado){
            estados[pos++] = estado;
        }
        public int pop(){
            return estados[--pos];
        }
        public int lastElement(){
            return estados[pos -1];
        }
        public void clear()
        {
            pos = 0;
        }

    }

    public  int[] tablero = new int[64];
    public  int[] color = new int[64];
    private final Stack estados = new Stack();

    Tablero(){
        setFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        estados.push(POSICION_INICIAL);
    }

    public  boolean casillaAtacada(int posicion, int colorContrario) {

        if ((ataqueCaballo[posicion] & piezas[colorContrario][CABALLO]) != 0) return true;

        /* ATAQUE TORRE/DAMA */
        long casillasOcupadas = casillasOcupadas();
        long piezasAtacadas = casillasOcupadas & maskAtaqueTorre[posicion];
        int bits = bitCount(maskAtaqueTorre[posicion]);
        int desplazamiento = 64 - bits;
        int index = (int) ((piezasAtacadas * _rookMagics[posicion]) >>> desplazamiento);
        long attackSet = Ataque.ataqueTorre[posicion][index];
        if ((attackSet & (piezas[colorContrario][TORRE] | piezas[colorContrario][DAMA])) != 0) return true;
        /* FIN ATAQUE TORRE/DAMA */

        /* ATAQUE ALFIL/DAMA */
        piezasAtacadas = casillasOcupadas & maskAtaqueAlfil[posicion];
        bits = bitCount(maskAtaqueAlfil[posicion]);
        desplazamiento = 64 - bits;
        index = (int) ((piezasAtacadas * _bishopMagics[posicion]) >>> desplazamiento);
        attackSet = Ataque.ataqueAlfil[posicion][index];
        if ((attackSet & (piezas[colorContrario][ALFIL] | piezas[colorContrario][DAMA])) != 0) return true;
        /* FIN ATAQUE ALFIL/DAMA */


        if((ataquePeon[colorContrario ^ 1][posicion] & piezas[colorContrario][PEON] )!= 0) return true;


        return (ataqueRey[posicion] & piezas[colorContrario][REY]) != 0;
    }
    public  void revertirMovimiento(Movimiento movimiento, int[] tablero, int[] color) {

        int estado = estados.pop();
        estado ^= 0b10000;

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
    public  void hacerMovimiento(int[] tablero, int[] color, Movimiento movimiento) {

        int estado = estados.lastElement();

        int inicio = movimiento.inicio;
        int destino = movimiento.destino;

        var pieza = tablero[inicio];

        estado = colocarValor(NOPIEZA, POSICION_PIEZA_CAPTURADA, MASK_LIMPIAR_PIEZA_CAPTURADA,estado);
        estado = colocarValor(MOVIMIENTO_NORMAL, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO,estado);

        if (pieza == PEON) {

            if (alPaso(destino)) {

                var posicionAlPaso = destino + (esTurnoBlanco() ? -8 : 8);

                remove(!esTurnoBlanco(), PEON, posicionAlPaso);

                // aqui se remueve la pieza al paso
                tablero[posicionAlPaso] = NOPIEZA;
                color[posicionAlPaso] = NOCOLOR;

                estado = colocarValor(AL_PASO, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO,estado);

                // al paso = false
                estado &= MASK_LIMPIAR_AL_PASO;
            } else if (destino <= H1 || destino >= A8) {

                if (tablero[destino] != NOPIEZA) {

                    estado = colocarValor(tablero[destino], POSICION_PIEZA_CAPTURADA, MASK_LIMPIAR_PIEZA_CAPTURADA,estado);

                    if (tablero[destino] == TORRE) {
                        switch (destino) {
                            case H8:
                                estado &= MASK_LIMPIAR_ENROQUES_NEGROS | 8;
                                break;
                            case A8:
                                estado &= MASK_LIMPIAR_ENROQUES_NEGROS | 4;
                                break;
                            case H1:
                                estado &= MASK_LIMPIAR_ENROQUES_BLANCOS | 2;
                                break;
                            case A1:
                                estado &= MASK_LIMPIAR_ENROQUES_BLANCOS | 1;
                                break;

                        }

                    }
                    // remover pieza capturada
                    remove(!esTurnoBlanco(), tablero[destino], destino);
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
                // remover el peon porque se va a promover
                remove(esTurnoBlanco(), PEON, inicio);


                color[destino] = esTurnoBlanco() ? BLANCO : NEGRO;
                 estado = colocarValor(PROMOCION, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO,estado);

                tablero[inicio] = NOPIEZA;
                color[inicio] = NOCOLOR;

                estado &= MASK_LIMPIAR_AL_PASO;

                estado ^= 0b10000;

                estados.push(estado);
                return;

            } else if (abs(inicio - destino) == 16) {

                int posicionPiezaAlPAso = destino + (esTurnoBlanco() ? -8 : 8);
                estado = estado & MASK_LIMPIAR_AL_PASO | posicionPiezaAlPAso << POSICION_PIEZA_AL_PASO;

                update(esTurnoBlanco(), PEON, inicio, destino);

                tablero[destino] = pieza;
                tablero[inicio] = NOPIEZA;

                color[destino] = color[inicio];
                color[inicio] = NOCOLOR;

                estado ^= 0b10000;

                estados.push(estado);
                return;
            }

        } else if (pieza == REY) {
            // en los enroques solo se mueven las torres por ser el movimiento especial
            if (abs(inicio - destino) == 2) {
                if (color[inicio] == BLANCO) {
                    if (destino == G1) {
                        update(esTurnoBlanco(), TORRE, H1, F1);
                        tablero[F1] = tablero[H1];
                        tablero[H1] = NOPIEZA;
                        color[F1] = color[H1];
                        color[H1] = NOCOLOR;
                    } else {
                        update(esTurnoBlanco(), TORRE, A1, D1);
                        tablero[D1] = tablero[A1];
                        tablero[A1] = NOPIEZA;
                        color[D1] = color[A1];
                        color[A1] = NOCOLOR;
                    }
                } else {
                    if (destino == G8) {
                        update(esTurnoBlanco(), TORRE, H8, F8);
                        tablero[F8] = tablero[H8];
                        tablero[H8] = NOPIEZA;
                        color[F8] = color[H8];
                        color[H8] = NOCOLOR;
                    } else {
                        update(esTurnoBlanco(), TORRE, A8, D8);
                        tablero[D8] = tablero[A8];
                        tablero[A8] = NOPIEZA;
                        color[D8] = color[A8];
                        color[A8] = NOCOLOR;
                    }
                }
                estado = colocarValor(ENROQUE, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO,estado);
            } else {
                estado = colocarValor(MOVIMIENTO_REY, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO,estado);
            }
            if (esTurnoBlanco()) {
                // enroques blancos false
                estado &= MASK_LIMPIAR_ENROQUES_BLANCOS;
                // posicion rey blanco
                estado = estado & MASK_LIMPIAR_POSICION_REY_BLANCO | destino << POSICION_REY_BLANCO;
            } else {
                // enroques negros false
                estado &= MASK_LIMPIAR_ENROQUES_NEGROS;
                // posicion rey negro
                estado = estado & MASK_LIMPIAR_POSICION_REY_NEGRO | destino << POSICION_REY_NEGRO;
            }

        } else if (pieza == TORRE) {

            switch (inicio) {
                case H8:
                    estado &= MASK_LIMPIAR_ENROQUES_NEGROS | 8;
                    break;
                case A8:
                    estado &= MASK_LIMPIAR_ENROQUES_NEGROS | 4;
                    break;
                case H1:
                    estado &= MASK_LIMPIAR_ENROQUES_BLANCOS | 2;
                    break;
                case A1:
                    estado &= MASK_LIMPIAR_ENROQUES_BLANCOS | 1;
                    break;
            }

        }
        estado = colocarValor(tablero[destino], POSICION_PIEZA_CAPTURADA, MASK_LIMPIAR_PIEZA_CAPTURADA,estado);

        if (tablero[destino] != NOPIEZA)
            remove(!esTurnoBlanco(), tablero[destino], destino);

        if (tablero[destino] == TORRE) {
            switch (destino) {
                case H8:
                    estado &= MASK_LIMPIAR_ENROQUES_NEGROS | 8;
                    break;
                case A8:
                    estado &= MASK_LIMPIAR_ENROQUES_NEGROS | 4;
                    break;
                case H1:
                    estado &= MASK_LIMPIAR_ENROQUES_BLANCOS | 2;
                    break;
                case A1:
                    estado &= MASK_LIMPIAR_ENROQUES_BLANCOS | 1;
                    break;

            }

        }
        update(esTurnoBlanco(), tablero[inicio], inicio, destino);

        tablero[destino] = tablero[inicio];
        color[destino] = color[inicio];

        tablero[inicio] = NOPIEZA;
        color[inicio] = NOCOLOR;

        estado &= MASK_LIMPIAR_AL_PASO;
        estado ^= 0b10000;
        estados.push(estado);
    }
    public  long casillasOcupadas(){
        return
                piezas[BLANCO][PEON] |
                piezas[BLANCO][CABALLO] |
                piezas[BLANCO][ALFIL] |
                piezas[BLANCO][TORRE] |
                piezas[BLANCO][DAMA] |
                piezas[BLANCO][REY] |
                piezas[NEGRO][PEON] |
                piezas[NEGRO][CABALLO] |
                piezas[NEGRO][ALFIL] |
                piezas[NEGRO][TORRE] |
                piezas[NEGRO][DAMA] |
                piezas[NEGRO][REY];

    }
    public  long piezasAmigas(int color){
        return
                piezas[color][PEON] |
                piezas[color][CABALLO] |
                piezas[color][ALFIL] |
                piezas[color][TORRE] |
                piezas[color][DAMA] |
                piezas[color][REY];
    }
    public  long piezasEnemigas(int color){
        color ^= 1;
        return
                piezas[color][PEON] |
                piezas[color][CABALLO] |
                piezas[color][ALFIL] |
                piezas[color][TORRE] |
                piezas[color][DAMA];
    }
    private   int colocarValor(int valor, int posicion, int mascara, int estado) {
        return  estado & mascara | valor << posicion;
    }
    public  int miColor() {
        return (estados.lastElement() >>> 4 & 0b1) ^ 1;
    }
    public  boolean reyEnJaque() {
        int miColor = miColor();
        int posicionRey = numberOfTrailingZeros(piezas[miColor][REY]);
        return casillaAtacada(posicionRey, colorContrario());
    }
    public  boolean alPaso(int destino) {

        if ((destino >= A3 && destino <= H3 && miColor() == NEGRO) || (destino >= A6 && destino <= H6 && miColor() == BLANCO))
            return (estados.lastElement() >> POSICION_PIEZA_AL_PASO & 0b111111) == destino;

        return false;
    }
    public  boolean esTurnoBlanco() {
        return (estados.lastElement() & 0b000000_000000_000_000_000000_1_00_00) > 0;
    }
    public  int colorContrario() {
        return estados.lastElement() >>> 4 & 0b1;
    }
    public  int getPiezaEnPosicion(int posicion, int color){
        long maskPosicion = 1L << posicion;
        for (int i = 0; i < piezas[color].length; i++) {
            if ((piezas[color][i] & maskPosicion) != 0) return i;
        }
        throw new IllegalStateException("no se encontró la pieza");
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

        estados.clear();
        estados.push(estado);
    }
    public void setHistoria(String... movimientos){
        for (var movimiento : movimientos) {
            hacerMovimiento(tablero, color, convertirAPosicion(movimiento));
        }
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
    public int getEstado(){
        return estados.lastElement();
    }
}
