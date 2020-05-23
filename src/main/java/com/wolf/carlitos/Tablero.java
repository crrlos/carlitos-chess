
package com.wolf.carlitos;


import static com.wolf.carlitos.Bitboard.*;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Juego.color;
import static com.wolf.carlitos.Juego.tablero;
import static com.wolf.carlitos.Utilidades.*;
import static java.lang.Long.bitCount;
import static java.lang.Long.numberOfTrailingZeros;
import static java.lang.Math.abs;
import static com.wolf.carlitos.Ataque.*;

public class Tablero {

    public static final int NORTE = 10;
    public static final int SUR = -10;
    public static final int ESTE = 1;
    public static final int OESTE = -1;
    public static final int NORESTE = NORTE + ESTE;
    public static final int NOROESTE = NORTE + OESTE;
    public static final int SURESTE = SUR + ESTE;
    public static final int SUROESTE = SUR + OESTE;

    public static final int norte = 8;
    public static final int sur = -8;
    public static final int este = 1;
    public static final int oeste = -1;
    public static final int noreste = norte + este;
    public static final int noroeste = norte + oeste;
    public static final int sureste = sur + este;
    public static final int suroeste = sur + oeste;

    public static final int[][] offsetMailBox = new int[][]
            {
                    {NORTE, NORESTE, NOROESTE},
                    {8, 12, 19, 21, -8, -12, -19, -21},
                    {SURESTE, NORESTE, SUROESTE, NOROESTE},
                    {NORTE, SUR, ESTE, OESTE},
                    {NORTE, SUR, ESTE, OESTE, SURESTE, NORESTE, SUROESTE, NOROESTE}
            };
    public static final int[][] offset64 = new int[][]
            {
                    {norte, noreste, noroeste},
                    {6, 10, 15, 17, -6, -10, -15, -17},
                    {sureste, noreste, suroeste, noroeste},
                    {norte, sur, este, oeste},
                    {norte, sur, este, oeste, sureste, noreste, suroeste, noroeste}
            };

    public static int[] valorPiezas = new int[]
            {100, 320, 330, 500, 900, 10000, 0};

    public static final int[] mailBox = new int[]{
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -0, -0, -0, -0, -0, -0, -0, -0, -1,
            -1, -0, -0, -0, -0, -0, -0, -0, -0, -1,
            -1, -0, -0, -0, -0, -0, -0, -0, -0, -1,
            -1, -0, -0, -0, -0, -0, -0, -0, -0, -1,
            -1, -0, -0, -0, -0, -0, -0, -0, -0, -1,
            -1, -0, -0, -0, -0, -0, -0, -0, -0, -1,
            -1, -0, -0, -0, -0, -0, -0, -0, -0, -1,
            -1, -0, -0, -0, -0, -0, -0, -0, -0, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
    };

    public static final int[] direccion = new int[]{
            21, 22, 23, 24, 25, 26, 27, 28,
            31, 32, 33, 34, 35, 36, 37, 38,
            41, 42, 43, 44, 45, 46, 47, 48,
            51, 52, 53, 54, 55, 56, 57, 58,
            61, 62, 63, 64, 65, 66, 67, 68,
            71, 72, 73, 74, 75, 76, 77, 78,
            81, 82, 83, 84, 85, 86, 87, 88,
            91, 92, 93, 94, 95, 96, 97, 98,
    };

    public static long[][] piezas = new long[2][6];
    public static long[] ataqueCaballo = new long[64];
    public static long[] ataqueTorre = new long[64];
    public static long[] ataqueAlfil = new long[64];
    public static long[] ataqueRey = new long[64];
    public static long[][] ataquePeon = new long[2][64];


    static {


        for (int j = 0; j < 64; j++) {
            long ataquesCaballo = 0;
            long ataquesTorre = 0;
            long ataquesAlfil = 0;
            long ataquesRey = 0;
            long ataquesPeonBlanco = 0;
            long ataquesPeonNegro = 0;
            for (int i = 0; i < offsetMailBox[CABALLO].length; i++) {
                int mailOffset = offsetMailBox[CABALLO][i];
                if (mailBox[direccion[j] + mailOffset] != -1) {

                    int pos = j + offset64[CABALLO][i];
                    ataquesCaballo |= 1L << pos;

                }
            }
            ataqueCaballo[j] = ataquesCaballo;

            for (int i = 0; i < offsetMailBox[TORRE].length; i++) {

                int dir = offsetMailBox[TORRE][i];
                int pos = j;
                while (mailBox[direccion[pos] + dir] != -1) {
                    pos += offset64[TORRE][i];
                    if (mailBox[direccion[pos] + offsetMailBox[TORRE][i]] == -1) break;
                    ataquesTorre |= 1L << pos;

                }
            }
            ataqueTorre[j] = ataquesTorre;

            for (int i = 0; i < offsetMailBox[ALFIL].length; i++) {

                int dir = offsetMailBox[ALFIL][i];
                int pos = j;
                while (mailBox[direccion[pos] + dir] != -1) {
                    pos += offset64[ALFIL][i];
                    if (mailBox[direccion[pos] + offsetMailBox[ALFIL][i]] == -1) break;
                    ataquesAlfil |= 1L << pos;
                }

            }
            ataqueAlfil[j] = ataquesAlfil;

            for (int i = 0; i < offsetMailBox[DAMA].length; i++) {
                int dir = offsetMailBox[DAMA][i];
                int pos = j;
                if (mailBox[direccion[pos] + dir] != -1) {
                    pos += offset64[DAMA][i];
                    ataquesRey |= 1L << pos;
                }
            }

            ataqueRey[j] = ataquesRey;

                for (int i = 1 ; i < offsetMailBox[PEON].length;i++){

                    if(mailBox[direccion[j] + offsetMailBox[PEON][i]] != -1){
                        int pos = j + offset64[PEON][i];
                        ataquesPeonBlanco |= 1L << pos;
                    }
                    if(mailBox[direccion[j] - offsetMailBox[PEON][i]] != -1){
                        int pos = j - offset64[PEON][i];
                        ataquesPeonNegro |= 1L << pos;
                    }

                }

                ataquePeon[BLANCO][j] = ataquesPeonBlanco;
                ataquePeon[NEGRO][j] = ataquesPeonNegro;


        }
    }


    public static boolean casillaAtacada(int posicion, int[] tablero, int[] color, int colorContrario) {

        if ((ataqueCaballo[posicion] & piezas[colorContrario][CABALLO]) != 0) return true;

        /* ATAQUE TORRE/DAMA */
        long casillasOcupadas = casillasOcupadas();
        long piezasAtacadas = casillasOcupadas & ataqueTorre[posicion];
        int bits = bitCount(ataqueTorre[posicion]);
        int desplazamiento = 64 - bits;
        int index = (int) ((piezasAtacadas * _rookMagics[posicion]) >>> desplazamiento);
        long attackSet = Ataque.ataqueTorre[posicion][index];
        if ((attackSet & (piezas[colorContrario][TORRE] | piezas[colorContrario][DAMA])) != 0) return true;
        /* FIN ATAQUE TORRE/DAMA */

        /* ATAQUE ALFIL/DAMA */
        piezasAtacadas = casillasOcupadas & ataqueAlfil[posicion];
        bits = bitCount(ataqueAlfil[posicion]);
        desplazamiento = 64 - bits;
        index = (int) ((piezasAtacadas * _bishopMagics[posicion]) >>> desplazamiento);
        attackSet = Ataque.ataqueAlfil[posicion][index];
        if ((attackSet & (piezas[colorContrario][ALFIL] | piezas[colorContrario][DAMA])) != 0) return true;
        /* FIN ATAQUE ALFIL/DAMA */


        if((ataquePeon[colorContrario ^ 1][posicion] & piezas[colorContrario][PEON] )!= 0) return true;


        return (ataqueRey[posicion] & piezas[colorContrario][REY]) != 0;
    }

    public static void revertirMovimiento(int movimiento, int estado, int[] tablero, int[] color) {

        estado ^= 0b10000;

        int inicio = movimiento >> 6 & 0b111111;
        int destino = movimiento & 0b111111;

        boolean turnoBlanco = esTurnoBlanco(estado);

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
                color[posicionPaso] = esTurnoBlanco(estado) ? NEGRO : BLANCO;

                add(!esTurnoBlanco(estado), PEON, posicionPaso);
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

        color[destino] = tablero[destino] == NOPIEZA ? NOCOLOR : esTurnoBlanco(estado) ? NEGRO : BLANCO;

    }

    public static int hacerMovimiento(int[] tablero, int[] color, int estado, int movimiento) {

        int inicio = movimiento >> 6 & 0b111111;
        int destino = movimiento & 0b111111;

        var pieza = tablero[inicio];

        estado = colocarValor(estado, NOPIEZA, POSICION_PIEZA_CAPTURADA, MASK_LIMPIAR_PIEZA_CAPTURADA);
        estado = colocarValor(estado, MOVIMIENTO_NORMAL, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO);

        if (pieza == PEON) {

            if (alPaso(estado, destino)) {

                var posicionAlPaso = destino + (esTurnoBlanco(estado) ? -8 : 8);

                remove(!esTurnoBlanco(estado), PEON, posicionAlPaso);

                // aqui se remueve la pieza al paso
                tablero[posicionAlPaso] = NOPIEZA;
                color[posicionAlPaso] = NOCOLOR;

                estado = colocarValor(estado, AL_PASO, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO);

                // al paso = false
                estado &= MASK_LIMPIAR_AL_PASO;
            } else if (destino <= H1 || destino >= A8) {

                if (tablero[destino] != NOPIEZA) {

                    estado = colocarValor(estado, tablero[destino], POSICION_PIEZA_CAPTURADA, MASK_LIMPIAR_PIEZA_CAPTURADA);

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
                    remove(!esTurnoBlanco(estado), tablero[destino], destino);
                }
                switch (movimiento >> 12 & 0b111) {
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
                add(esTurnoBlanco(estado), tablero[destino], destino);
                // remover el peon porque se va a promover
                remove(esTurnoBlanco(estado), PEON, inicio);


                color[destino] = esTurnoBlanco(estado) ? BLANCO : NEGRO;
                estado = colocarValor(estado, PROMOCION, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO);

                tablero[inicio] = NOPIEZA;
                color[inicio] = NOCOLOR;

                estado &= MASK_LIMPIAR_AL_PASO;

                estado ^= 0b10000;


                return estado;

            } else if (abs(inicio - destino) == 16) {

                int posicionPiezaAlPAso = destino + (esTurnoBlanco(estado) ? -8 : 8);
                estado = estado & MASK_LIMPIAR_AL_PASO | posicionPiezaAlPAso << POSICION_PIEZA_AL_PASO;

                update(esTurnoBlanco(estado), PEON, inicio, destino);

                tablero[destino] = pieza;
                tablero[inicio] = NOPIEZA;

                color[destino] = color[inicio];
                color[inicio] = NOCOLOR;

                estado ^= 0b10000;

                return estado;
            }

        } else if (pieza == REY) {
            // en los enroques solo se mueven las torres por ser el movimiento especial
            if (abs(inicio - destino) == 2) {
                if (color[inicio] == BLANCO) {
                    if (destino == G1) {
                        update(esTurnoBlanco(estado), TORRE, H1, F1);
                        tablero[F1] = tablero[H1];
                        tablero[H1] = NOPIEZA;
                        color[F1] = color[H1];
                        color[H1] = NOCOLOR;
                    } else {
                        update(esTurnoBlanco(estado), TORRE, A1, D1);
                        tablero[D1] = tablero[A1];
                        tablero[A1] = NOPIEZA;
                        color[D1] = color[A1];
                        color[A1] = NOCOLOR;
                    }
                } else {
                    if (destino == G8) {
                        update(esTurnoBlanco(estado), TORRE, H8, F8);
                        tablero[F8] = tablero[H8];
                        tablero[H8] = NOPIEZA;
                        color[F8] = color[H8];
                        color[H8] = NOCOLOR;
                    } else {
                        update(esTurnoBlanco(estado), TORRE, A8, D8);
                        tablero[D8] = tablero[A8];
                        tablero[A8] = NOPIEZA;
                        color[D8] = color[A8];
                        color[A8] = NOCOLOR;
                    }
                }
                estado = colocarValor(estado, ENROQUE, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO);
            } else {
                estado = colocarValor(estado, MOVIMIENTO_REY, POSICION_TIPO_MOVIMIENTO, MASK_LIMPIAR_TIPO_MOVIMIENTO);
            }
            if (esTurnoBlanco(estado)) {
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
        estado = colocarValor(estado, tablero[destino], POSICION_PIEZA_CAPTURADA, MASK_LIMPIAR_PIEZA_CAPTURADA);

        if (tablero[destino] != NOPIEZA)
            remove(!esTurnoBlanco(estado), tablero[destino], destino);

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

        update(esTurnoBlanco(estado), tablero[inicio], inicio, destino);

        tablero[destino] = tablero[inicio];
        color[destino] = color[inicio];

        tablero[inicio] = NOPIEZA;
        color[inicio] = NOCOLOR;

        estado &= MASK_LIMPIAR_AL_PASO;
        estado ^= 0b10000;
        return estado;
    }

    public static long casillasOcupadas(){
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
    public static long piezasAmigas(int color){
        return
                piezas[color][PEON] |
                piezas[color][CABALLO] |
                piezas[color][ALFIL] |
                piezas[color][TORRE] |
                piezas[color][DAMA] |
                piezas[color][REY];
    }

    public static long piezasEnemigas(int color){
        color ^= 1;
        return
                piezas[color][PEON] |
                piezas[color][CABALLO] |
                piezas[color][ALFIL] |
                piezas[color][TORRE] |
                piezas[color][DAMA];
    }


}
