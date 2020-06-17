package com.wolf.carlitos;

import static com.wolf.carlitos.Constantes.*;

public class Pieza {

    public static final int NORTE = 10;
    public static final int SUR = -10;
    public static final int ESTE = 1;
    public static final int OESTE = -1;
    public static final int NORESTE = NORTE + ESTE;
    public static final int NOROESTE = NORTE + OESTE;
    public static final int SURESTE = SUR + ESTE;
    public static final int SUROESTE = SUR + OESTE;

    public static int[] piezas = new int[]{
            PEON, CABALLO, ALFIL, TORRE, DAMA, REY
    };

    public static int[] valorPiezas = new int[]{
            100, 320, 330, 500, 900, 10000, 0
    };

    public static boolean[] slider = new boolean[]{
            false, false, true, true, true, false
    };

}