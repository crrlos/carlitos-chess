package com.wolf.carlitos;

public class Constantes {

    public static final int A1 = 0;
    public static final int B1 = 1;
    public static final int C1 = 2;
    public static final int D1 = 3;
    public static final int E1 = 4;
    public static final int F1 = 5;
    public static final int G1 = 6;
    public static final int H1 = 7;

    public static final int A2 = 8;
    public static final int B2 = 9;
    public static final int C2 = 10;
    public static final int D2 = 11;
    public static final int E2 = 12;
    public static final int F2 = 13;
    public static final int G2 = 14;
    public static final int H2 = 15;

    public static final int A3 = 16;
    public static final int B3 = 17;
    public static final int C3 = 18;
    public static final int D3 = 19;
    public static final int E3 = 20;
    public static final int F3 = 21;
    public static final int G3 = 22;
    public static final int H3 = 23;

    public static final int A4 = 24;
    public static final int B4 = 25;
    public static final int C4 = 26;
    public static final int D4 = 27;
    public static final int E4 = 28;
    public static final int F4 = 29;
    public static final int G4 = 30;
    public static final int H4 = 31;

    public static final int A5 = 32;
    public static final int B5 = 33;
    public static final int C5 = 34;
    public static final int D5 = 35;
    public static final int E5 = 36;
    public static final int F5 = 37;
    public static final int G5 = 38;
    public static final int H5 = 39;

    public static final int A6 = 40;
    public static final int B6 = 41;
    public static final int C6 = 42;
    public static final int D6 = 43;
    public static final int E6 = 44;
    public static final int F6 = 45;
    public static final int G6 = 46;
    public static final int H6 = 47;

    public static final int A7 = 48;
    public static final int B7 = 49;
    public static final int C7 = 50;
    public static final int D7 = 51;
    public static final int E7 = 52;
    public static final int F7 = 53;
    public static final int G7 = 54;
    public static final int H7 = 55;

    public static final int A8 = 56;
    public static final int B8 = 57;
    public static final int C8 = 58;
    public static final int D8 = 59;
    public static final int E8 = 60;
    public static final int F8 = 61;
    public static final int G8 = 62;
    public static final int H8 = 63;

    public static final int ENROQUE = 0;
    public static final int MOVIMIENTO_NORMAL = 1;
    public static final int AL_PASO = 2;
    public static final int PROMOCION = 3;
    public static final int MOVIMIENTO_REY = 4;
    public static final int NO_ASIGNADO = 5;

    public static final int NO_JAQUE = 1001;

    public static final int MATE = 1_000_000;
    public static final int AHOGADO = 0;

    public static final int PEON = 0;
    public static final int CABALLO = 1;
    public static final int ALFIL = 2;
    public static final int TORRE = 3;
    public static final int DAMA = 4;
    public static final int REY = 5;
    public static final int NOPIEZA = 6;

    public static final int BLANCO = 0;
    public static final int NEGRO = 1;
    public static final int NOCOLOR = 2;

    public static char[] PIEZAS = {'P', 'N', 'B', 'R', 'Q', 'K', ' '};

    public static final int ENDGAME_MATERIAL = 1500; // eight pawns plus two minor pieces

    public static final int POSICION_TIPO_MOVIMIENTO = 14;
    public static final int POSICION_PIEZA_CAPTURADA = 11;
    public static final int POSICION_REY_BLANCO = 17;
    public static final int POSICION_REY_NEGRO = 23;
    public static final int POSICION_PIEZA_AL_PASO = 5;

    public static final int MASK_LIMPIAR_PIEZA_CAPTURADA = 0b111111_111111_111_000_111111_1_11_11;
    public static final int MASK_LIMPIAR_TIPO_MOVIMIENTO = 0b111111_111111_000_111_111111_1_11_11;
    public static final int MASK_LIMPIAR_AL_PASO = 0b111111_111111_111_111_000000_1_11_11;
    public static final int MASK_LIMPIAR_ENROQUES_BLANCOS = 0b111111_111111_111_111_111111_1_11_00;
    public static final int MASK_LIMPIAR_ENROQUES_NEGROS = 0b111111_111111_111_111_111111_1_00_11;
    public static final int MASK_LIMPIAR_POSICION_REY_BLANCO = 0b111111_000000_111_111_111111_1_11_11;
    public static final int MASK_LIMPIAR_POSICION_REY_NEGRO = 0b000000_111111_111_111_111111_1_11_11;

    public static final int POSICION_INICIAL = 0b111100_000100_000_000_000000_1_11_11;

    public static final int INFINITO = 10_000_000;

    public static final int BETA = 10;
    public static final int ALFA = 11;
    public static final int EXACT = 12;
    public static final int NOENTRY = -1;

    public static final int PV_SORT_SCORE = 150_000;
    public static final int KILLER_SORT_SCORE = 140_000;
    public static final int CAPTURE_MOVE_SORT = 100_000;


    public static final String INITIAL_POS = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";


}
