package com.wolf.carlitos;

public class Pieza {

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

    public static int[] valorPiezas = new int[]
            {100, 320, 330, 500, 900, 10000, 0};

    public static long[][] piezas = new long[2][6];

}