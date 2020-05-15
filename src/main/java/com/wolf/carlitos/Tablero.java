
package com.wolf.carlitos;


import static com.wolf.carlitos.Constantes.*;

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
                    {NORTE, SUR, ESTE, OESTE}
            };
    public static final int[][] offset64 = new int[][]
            {
                    {norte, noreste, noroeste},
                    {6, 10, 15, 17, -6, -10, -15, -17},
                    {sureste, noreste, suroeste, noroeste},
                    {norte, sur, este, oeste},
            };


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


    public static boolean casillaAtacada(int posicion, int[] tablero, int[] color, int colorContrario) {

        int pos;

        for (int i = 0; i < offsetMailBox[TORRE].length; i++) {

            int dir = offsetMailBox[TORRE][i];
            pos = posicion;
            while (mailBox[direccion[pos] + dir] != -1) {
                pos += offset64[TORRE][i];
                if (tablero[pos] != NOPIEZA) {
                    if (color[pos] == colorContrario && (tablero[pos] == TORRE || tablero[pos] == DAMA))
                        return true;
                    else break;
                }

            }

        }

        for (int i = 0; i < offsetMailBox[ALFIL].length; i++) {

            int dir = offsetMailBox[ALFIL][i];
            pos = posicion;
            while (mailBox[direccion[pos] + dir] != -1) {
                pos += offset64[ALFIL][i];
                if (tablero[pos] != NOPIEZA) {
                    if (color[pos] == colorContrario && (tablero[pos] == ALFIL || tablero[pos] == DAMA))
                        return true;
                    else break;
                }

            }

        }

        for (int i = 0; i < offsetMailBox[CABALLO].length; i++) {

            int dir = offsetMailBox[CABALLO][i];
            pos = posicion;
            if (mailBox[direccion[pos] + dir] != -1) {
                pos += offset64[CABALLO][i];
                if (tablero[pos] != NOPIEZA) {
                    if (color[pos] == colorContrario && (tablero[pos] == CABALLO))
                        return true;
                }

            }

        }

        for (int i = 1; i < offsetMailBox[PEON].length; i++) {

            int dir = offsetMailBox[PEON][i];
            pos = posicion;
            if (mailBox[direccion[pos] + (colorContrario == BLANCO ? -dir : dir)] != -1) {
                pos += (colorContrario == BLANCO ? -offset64[PEON][i] : offset64[PEON][i]);
                if (tablero[pos] != NOPIEZA) {
                    if (color[pos] == colorContrario && tablero[pos] == PEON)
                        return true;
                }

            }

        }


        // buscando al rey
        for (int i = 0; i < offsetMailBox[TORRE].length; i++) {

            int dir = offsetMailBox[TORRE][i];
            pos = posicion;
            if (mailBox[direccion[pos] + dir] != -1) {
                pos += offset64[TORRE][i];
                if (tablero[pos] != NOPIEZA) {
                    if (color[pos] == colorContrario && (tablero[pos] == REY))
                        return true;
                }

            }

        }

        for (int i = 0; i < offsetMailBox[ALFIL].length; i++) {

            int dir = offsetMailBox[ALFIL][i];
            pos = posicion;
            if (mailBox[direccion[pos] + dir] != -1) {
                pos += offset64[ALFIL][i];
                if (tablero[pos] != NOPIEZA) {
                    if (color[pos] == colorContrario && (tablero[pos] == REY))
                        return true;
                }

            }

        }

        return false;
    }


}
