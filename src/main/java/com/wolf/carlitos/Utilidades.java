/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;



import java.time.Period;
import java.util.HashMap;
import java.util.Objects;

import static java.lang.Math.abs;
import static com.wolf.carlitos.Constantes.*;

/**
 * @author carlos
 */
public class Utilidades {

    private static final HashMap<String, Integer> casillaPosicion = new HashMap<>();
    private static final HashMap<Integer, String> posicionCasilla = new HashMap<>();


    static {
        int correlativo = 0;

        for (int i = 1; i <= 8; i++) {
            for (char c : "abcdefgh".toCharArray()) {
                casillaPosicion.put(String.valueOf(c) + i, correlativo);
                posicionCasilla.put(correlativo++, String.valueOf(c) + i);
            }
        }


    }

    public static void imprimirPosicicion(int[] tablero,int[] color) {

        for (int i = 64; i > 0; i -= 8) {
            System.out.println("+---+---+---+---+---+---+---+---+");
            for (int j = i - 8; j < (i - 8) + 8; j++) {
                System.out.print("| " + (color[j] == BLANCO ? String.valueOf(PIEZAS[tablero[j]]) : String.valueOf(PIEZAS[tablero[j]]).toLowerCase()) + " ");
            }
            System.out.print("|");

            System.out.println();
        }
        System.out.println("+---+---+---+---+---+---+---+---+");
    }

    public static int convertirAPosicion(String movimiento) {
        int posicion = 0;

        var inicio = movimiento.substring(0, 2);
        var destino = movimiento.substring(2, 4);

        posicion |= casillaPosicion.get(inicio) << 6;
        posicion |= casillaPosicion.get(destino);


        if (movimiento.length() == 5) {
            switch (movimiento.charAt(4)) {
                case 'q':
                    posicion |= 1 << 12;
                    break;
                case 'r':
                    posicion |= 2 << 12;
                    break;
                case 'n':
                    posicion |= 3 << 12;
                    break;
                case 'b':
                    posicion |= 4 << 12;
                    break;
            }
        }

        return posicion;
    }

    public static String convertirANotacion(int movimiento) {

        var mov = posicionCasilla.get(movimiento >> 6 & 0b111111) + posicionCasilla.get(movimiento & 0b111111);

        if (movimiento >> 12 > 0) {
            switch (movimiento >> 7) {
                case 1:
                    return mov + "q";
                case 2:
                    return mov + "r";
                case 3:
                    return mov + "n";
                case 4:
                    return mov + "b";
            }
        }
        return mov;
    }
//position startpos moves e2e4 e7e5 g1f3 g8f6 f1e2 f8e7 e1g1 e8g8
    public static long actualizarTablero(int[] tablero,int[]color, long estadoTablero, int movimiento) {

        int inicio = movimiento >> 6 & 0b111111;
        int destino = movimiento & 0b111111;

        var pieza = tablero[inicio];

        // pieza capturada
        estadoTablero =  estadoTablero & 0b111111_111111_111_11_000_1_111111_1_1_11_11L | (long)NOPIEZA << 13;
        // color captura
        estadoTablero =  estadoTablero & 0b111111_111111_111_00_111_1_111111_1_1_11_11L | (long)NOCOLOR << 16;
        // tipo movimiento
        estadoTablero =  estadoTablero & 0b111111_111111_000_11_111_1_111111_1_1_11_11L | (long)NO_ASIGNADO << 18;



        if (pieza  == PEON) {

            if (abs(inicio - destino) == 16) {

                // al paso = true
                estadoTablero |= 0b1_00_000;

                // pieza al paso
                int posicionPiezaAlPAso = destino + (esTurnoBlanco(estadoTablero) ? -8 : 8);
                estadoTablero =  estadoTablero & 0b111111_111111_111_11_111_1_000000_1_1_11_11L | (long)posicionPiezaAlPAso << 6;

                // color al paso
                estadoTablero =  estadoTablero & 0b111111_111111_111_11_111_0_111111_1_1_11_11L | (long)color[inicio] << 12;

                tablero[destino] = pieza;
                tablero[inicio] = NOPIEZA;

                color[destino] = color[inicio];
                color[inicio] = NOCOLOR;

                // tipo de movimiento
                estadoTablero = setTipoMovimiento(estadoTablero,MOVIMIENTO_NORMAL);

                return estadoTablero;
            }
            if (alPaso(estadoTablero)) {
                if (abs(destino - inicio) == 7 || abs(destino - inicio) == 9) {
                    var posicionAlPaso = destino + (esTurnoBlanco(estadoTablero) ? - 8 : 8);
                    if (destino == (estadoTablero >> 6 & 0b111111)) {

                        // aqui se remueve la pieza al paso
                        tablero[posicionAlPaso] = NOPIEZA;
                        // color al paso
                        estadoTablero =  estadoTablero & 0b111111_111111_111_11_111_0_111111_1_1_11_11L | (long)color[posicionAlPaso] << 12;

                        color[posicionAlPaso] = NOCOLOR;

                        estadoTablero = setTipoMovimiento(estadoTablero,AL_PASO);
                    }
                }
                // al paso = false
                estadoTablero  &=  0b111111_111111_111_11_111_1_111111_0_1_11_11L;
            }

            if (destino <= H1 || destino >= A8) {
                switch (movimiento >> 12 & 0b111) {
                    case 1:
                        tablero[destino] = DAMA;
                        color[destino] = esTurnoBlanco(estadoTablero) ?BLANCO:NEGRO;
                        break;
                    case 2:
                        tablero[destino] = TORRE;
                        color[destino] =esTurnoBlanco(estadoTablero) ?BLANCO:NEGRO;
                        break;
                    case 3:
                        tablero[destino] = CABALLO;
                        color[destino] =esTurnoBlanco(estadoTablero) ?BLANCO:NEGRO;
                        break;
                    case 4:
                        tablero[destino] = ALFIL;
                        color[destino] =esTurnoBlanco(estadoTablero) ?BLANCO:NEGRO;
                        break;
                }
               estadoTablero = setTipoMovimiento(estadoTablero,PROMOCION);
            }

        } else if (pieza == REY) {
            // en los enroques solo se mueven las torres por ser el movimiento especial
            if (abs(inicio - destino) == 2) {
                if (color[inicio] == BLANCO) {
                    if (destino == G1) {
                        tablero[F1] = tablero[H1];
                        tablero[H1] = NOPIEZA;
                        color[F1] = color[H1];
                        color[H1] = NOCOLOR;
                    } else {
                        tablero[D1] = tablero[A1];
                        tablero[A1] = NOPIEZA;
                        color[D1] = color[A1];
                        color[A1] = NOCOLOR;
                    }
                } else {
                    if (destino == G8) {
                        tablero[F8] = tablero[H8];
                        tablero[H8] = NOPIEZA;
                        color[F8] = color[H8];
                        color[H8] = NOCOLOR;
                    } else {
                        tablero[D8] = tablero[A8];
                        tablero[A8] = NOPIEZA;
                        color[D8] = color[A8];
                        color[A8] = NOCOLOR;
                    }
                }
                estadoTablero = setTipoMovimiento(estadoTablero,ENROQUE);
            } else {
                estadoTablero = setTipoMovimiento(estadoTablero,MOVIMIENTO_REY);
            }
            if (esTurnoBlanco(estadoTablero)) {
                // enroques blancos false
                estadoTablero &= 0b111111_111111_111_11_111_1_111111_1_1_11_00L;
                // posicion rey blanco
                estadoTablero = estadoTablero & 0b111111_000000_111_11_111_1_111111_1_1_11_11L | (long)destino << 21;
            } else {
                // enroques negros false
                estadoTablero &= 0b111111_111111_111_11_111_1_111111_1_1_00_11L;
                // posicion rey negro
                estadoTablero = estadoTablero & 0b000000_111111_111_11_111_1_111111_1_1_11_11L | (long)destino << 27;
            }

        } else if (pieza == TORRE) {

            switch (inicio) {
                case H8:
                    estadoTablero &= 0b111111_111111_111_11_111_1_111111_1_1_10_11L;
                    break;
                case A8:
                    estadoTablero &= 0b111111_111111_111_11_111_1_111111_1_1_01_11L;
                    break;
                case H1:
                    estadoTablero &= 0b111111_111111_111_11_111_1_111111_1_1_11_10L;
                    break;
                case A1:
                    estadoTablero &= 0b111111_111111_111_11_111_1_111111_1_1_11_01L;
                    break;
            }

        }

        // pieza capturada
        estadoTablero =  estadoTablero & 0b111111_111111_111_11_000_1_111111_1_1_11_11L | (long)tablero[destino] << 13;
        // color captura
        estadoTablero =  estadoTablero & 0b111111_111111_111_00_111_1_111111_1_1_11_11L | (long)color[destino] << 16;

        if (tablero[destino] == TORRE) {
            switch (destino) {
                case H8:
                    estadoTablero &= 0b111111_111111_111_11_111_1_111111_1_1_10_11L;
                    break;
                case A8:
                    estadoTablero &= 0b111111_111111_111_11_111_1_111111_1_1_01_11L;
                    break;
                case H1:
                    estadoTablero &= 0b111111_111111_111_11_111_1_111111_1_1_11_10L;
                    break;
                case A1:
                    estadoTablero &= 0b111111_111111_111_11_111_1_111111_1_1_11_01L;
                    break;

            }

        }

        tablero[destino] = tablero[inicio];
        tablero[inicio] = NOPIEZA;
        color[destino] = color[inicio];
        color[inicio] = NOCOLOR;


        // al paso  = false
        estadoTablero  &=  0b111111_111111_111_11_111_1_111111_0_1_11_11L;

        if ((estadoTablero >> 18 & 0b111) == NO_ASIGNADO) {
            estadoTablero = setTipoMovimiento(estadoTablero,MOVIMIENTO_NORMAL);
        }
        return  estadoTablero;
    }

    static boolean alPaso(long estadoTablero) {
        return (estadoTablero & 0b000000_000000_000_00_000_0_000000_1_0_00_00L) > 0;
    }

    private static long setTipoMovimiento(long estadoTablero, int movimiento) {
        return estadoTablero & 0b111111_111111_000_11_111_1_111111_1_1_11_11L | (long)movimiento << 18;
    }

    public static boolean esTurnoBlanco(long estadoTablero) {
        return (estadoTablero & 0b000000_000000_000_00_000_0_000000_0_1_00_00L) > 0;
    }

    public static int posicionRey(long estado, int desplazamiento){
        return (int) (estado >> desplazamiento & 0b111111);
    }
    public static int reyEnJaque(int[]pieza, int[] color, long estado) {
        var blanco = esTurnoBlanco(estado);

        var posicionRey = blanco ? posicionRey(estado,21) : posicionRey(estado,27);

        int result;

        if ((result = ataqueFilaColumna(posicionRey, pieza,color, blanco)) != NO_JAQUE) return result;
        if ((result = ataqueDiagonal(posicionRey, pieza,color, blanco)) != NO_JAQUE) return result;
        if ((result = ataqueCaballo(posicionRey, pieza,color, blanco)) != NO_JAQUE) return result;
        if ((result = ataquePeon(posicionRey, pieza,color, blanco)) != NO_JAQUE) return result;

        return NO_JAQUE;
    }
    public static boolean esCasillaBlanca(int i) {
        boolean filaImpar = (i / 8 + 1) % 2 != 0;

        return filaImpar == ((i + 1) % 2 == 0);
    }
    private static int ataquePeon(int posicion, int[] pieza, int[] color, boolean blanco) {
        if(blanco){
            var destino = posicion + 9;
            if(destino < 64){
                int posicionActual = pieza[destino];
                if(posicionActual != NOPIEZA && pieza[destino] == PEON && !(color[destino] == BLANCO) && esCasillaBlanca(posicion) == esCasillaBlanca(destino)){
                    return destino;
                }
            }
            destino = posicion + 7;
            if(destino < 64){
                var posicionActual = pieza[destino];
                if(posicionActual != NOPIEZA && posicionActual == PEON && !(color[destino] == BLANCO) && esCasillaBlanca(posicion) == esCasillaBlanca(destino)){
                    return destino;
                }
            }
        }else{
            var destino = posicion - 9;
            if(destino >= 0){
                var posicionActual = pieza[destino];
                if(posicionActual != NOPIEZA && pieza[destino] == PEON && color[destino] == BLANCO && esCasillaBlanca(posicion) == esCasillaBlanca(destino)){
                    return destino;
                }
            }
            destino = posicion - 7;
            if(destino >= 0){
                var posicionActual = pieza[destino];
                if(posicionActual != NOPIEZA && pieza[destino] == PEON && color[destino] == BLANCO && esCasillaBlanca(posicion) == esCasillaBlanca(destino)){
                    return destino;
                }
            }
        }
    return NO_JAQUE;
    }

    private static int ataqueFilaColumna(int posicion, int[] pieza,int[] color, boolean blanco) {

        var movimientosTorre = Generador.movimientosTorre.get(posicion);

        for (int i = 0; i < movimientosTorre.size(); i++) {

            var m = movimientosTorre.get(i);

            for (int j = 0; j < m.size(); j++) {
                int posicionActual = pieza[m.get(j)];
                if (posicionActual != NOPIEZA) {
                    if (color[m.get(j)] == BLANCO != blanco
                            && (posicionActual == TORRE || posicionActual == DAMA)) {
                        return m.get(j);
                    } else break;
                }


            }

        }
        return NO_JAQUE;
    }

    private static int ataqueDiagonal(int posicion, int[] pieza,int[] color, boolean blanco) {

        var movimientosAlfil = Generador.movimientosAlfil.get(posicion);

        for (int i = 0; i < movimientosAlfil.size(); i++) {
            var m = movimientosAlfil.get(i);

            for (int j = 0; j < m.size(); j++) {
                int posicionActual = pieza[m.get(j)];
                if (posicionActual != NOPIEZA) {

                    if (color[m.get(j)] == BLANCO != blanco
                            && (posicionActual == ALFIL || posicionActual == DAMA)) {
                        return m.get(j);
                    } else break;
                }

            }

        }
        return NO_JAQUE;
    }

    private static int ataqueCaballo(int posicion, int[] pieza,int[] color, boolean blanco) {

        var movimientosCaballo = Generador.movimientosCaballo.get(posicion);

        for (int j = 0; j < movimientosCaballo.size(); j++) {
            var m = movimientosCaballo.get(j);
            int posicionActual = pieza[m];
            if (posicionActual != NOPIEZA)
                if (color[m] == BLANCO != blanco
                        && (posicionActual == CABALLO)) {
                    return m;
                }

        }
        return NO_JAQUE;
    }

    public static boolean movimientoValido(int movimiento, int[] tablero,int[]color, long estado) {

        int inicio = movimiento >> 6 & 0b111111;
        int destino = movimiento & 0b111111;

        int piezaActual = tablero[inicio];
        int piezaDestino = tablero[destino];
        int colorDestino = color[destino];

        tablero[inicio] = NOPIEZA;
        tablero[destino] = piezaActual;

        color[destino] = color[inicio];
        color[inicio] = NOCOLOR;

        boolean tomaAlPaso = false;
        int posicionPaso = 0;

        if (piezaActual == PEON && alPaso(estado)) {

            if (esTurnoBlanco(estado)) {
                if (destino - inicio == 7) {
                    posicionPaso = inicio - 1;
                } else if (destino - inicio == 9) {
                    posicionPaso = inicio + 1;
                }
            } else {
                if (destino - inicio == -7) {
                    posicionPaso = inicio + 1;
                } else if (destino - inicio == -9) {
                    posicionPaso = inicio - 1;
                }
            }

            if (tablero[posicionPaso] == (estado >> 6 & 0b111111)) {
                tomaAlPaso = true;
                tablero[posicionPaso] = NOPIEZA;
            }

        }
        if (piezaActual == REY) {
            if (esTurnoBlanco(estado)) {
                estado = estado & 0b111111_000000_111_11_111_1_111111_1_1_11_11L | (long)destino << 21;
            } else {
                estado = estado & 0b000000_111111_111_11_111_1_111111_1_1_11_11L | (long)destino << 27;
            }
        }

        var jaque = reyEnJaque(tablero,color, estado);

        if (piezaActual == PEON && alPaso(estado) && tomaAlPaso) {
            tablero[posicionPaso] = (int) (estado >> 6 & 0b111111);
        }

        tablero[destino] = piezaDestino;
        tablero[inicio] = piezaActual;

        color[inicio] = color[destino];
        color[destino] = colorDestino;

        return jaque == NO_JAQUE;
    }

    public static  int casillaANumero(String casilla){
        int columna = "abcdefgh".indexOf(casilla.charAt(0));
        int fila = "12345678".indexOf(casilla.charAt(1));
        return  columna + 8 * fila;
    }


}
