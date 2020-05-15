/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;


import java.util.HashMap;

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

    public static void imprimirPosicicion(int[] tablero, int[] color) {

        for (int i = 64; i > 0; i -= 8) {
            System.out.println("+---+---+---+---+---+---+---+---+");
            for (int j = i - 8; j < (i - 8) + 8; j++) {
                System.out.print("| " + (color[j] == NOCOLOR ? "." : (color[j] == BLANCO ? String.valueOf(PIEZAS[tablero[j]]) : String.valueOf(PIEZAS[tablero[j]]).toLowerCase())) + " ");
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
            switch (movimiento >> 12 & 0b111) {
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

    public static int hacerMovimiento(int[] tablero, int[] color, int estadoTablero, int movimiento) {

        int inicio = movimiento >> 6 & 0b111111;
        int destino = movimiento & 0b111111;

        var pieza = tablero[inicio];

        estadoTablero = colocarValor(estadoTablero,NOPIEZA,POSICION_PIEZA_CAPTURADA,MASK_LIMPIAR_PIEZA_CAPTURADA);
        estadoTablero = colocarValor(estadoTablero,MOVIMIENTO_NORMAL,POSICION_TIPO_MOVIMIENTO,MASK_LIMPIAR_TIPO_MOVIMIENTO);

        if (pieza == PEON) {

            if (alPaso(estadoTablero,destino)) {

                var posicionAlPaso = destino + (esTurnoBlanco(estadoTablero) ? -8 : 8);

                    // aqui se remueve la pieza al paso
                    tablero[posicionAlPaso] = NOPIEZA;
                    color[posicionAlPaso] = NOCOLOR;

                    estadoTablero = colocarValor(estadoTablero, AL_PASO,POSICION_TIPO_MOVIMIENTO,MASK_LIMPIAR_TIPO_MOVIMIENTO);

                // al paso = false
                estadoTablero &= MASK_LIMPIAR_AL_PASO;
            }
            else if (destino <= H1 || destino >= A8) {

                if (tablero[destino] != NOPIEZA) {

                    estadoTablero = colocarValor(estadoTablero,tablero[destino],POSICION_PIEZA_CAPTURADA,MASK_LIMPIAR_PIEZA_CAPTURADA);

                    if (tablero[destino] == TORRE) {
                        switch (destino) {
                            case H8:
                                estadoTablero &= MASK_LIMPIAR_ENROQUES_NEGROS | 8;
                                break;
                            case A8:
                                estadoTablero &= MASK_LIMPIAR_ENROQUES_NEGROS | 4;
                                break;
                            case H1:
                                estadoTablero &= MASK_LIMPIAR_ENROQUES_BLANCOS | 2;
                                break;
                            case A1:
                                estadoTablero &= MASK_LIMPIAR_ENROQUES_BLANCOS | 1;
                                break;

                        }

                    }
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
                color[destino] = esTurnoBlanco(estadoTablero) ? BLANCO : NEGRO;
                estadoTablero = colocarValor(estadoTablero, PROMOCION,POSICION_TIPO_MOVIMIENTO,MASK_LIMPIAR_TIPO_MOVIMIENTO);

                tablero[inicio] = NOPIEZA;
                color[inicio] = NOCOLOR;

                estadoTablero &= MASK_LIMPIAR_AL_PASO;

                estadoTablero ^= 0b10000;

                return estadoTablero;

            }else if (abs(inicio - destino) == 16) {

                int posicionPiezaAlPAso = destino + (esTurnoBlanco(estadoTablero) ? -8 : 8);
                estadoTablero = estadoTablero & MASK_LIMPIAR_AL_PASO | posicionPiezaAlPAso << POSICION_PIEZA_AL_PASO;

                tablero[destino] = pieza;
                tablero[inicio] = NOPIEZA;

                color[destino] = color[inicio];
                color[inicio] = NOCOLOR;

                estadoTablero ^= 0b10000;

                return estadoTablero;
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
                estadoTablero = colocarValor(estadoTablero, ENROQUE,POSICION_TIPO_MOVIMIENTO,MASK_LIMPIAR_TIPO_MOVIMIENTO);
            } else {
                estadoTablero = colocarValor(estadoTablero, MOVIMIENTO_REY,POSICION_TIPO_MOVIMIENTO,MASK_LIMPIAR_TIPO_MOVIMIENTO);
            }
            if (esTurnoBlanco(estadoTablero)) {
                // enroques blancos false
                estadoTablero &= MASK_LIMPIAR_ENROQUES_BLANCOS;
                // posicion rey blanco
                estadoTablero = estadoTablero & MASK_LIMPIAR_POSICION_REY_BLANCO |  destino << POSICION_REY_BLANCO;
            } else {
                // enroques negros false
                estadoTablero &= MASK_LIMPIAR_ENROQUES_NEGROS;
                // posicion rey negro
                estadoTablero = estadoTablero & MASK_LIMPIAR_POSICION_REY_NEGRO | destino << POSICION_REY_NEGRO;
            }

        } else if (pieza == TORRE) {

            switch (inicio) {
                case H8:
                    estadoTablero &= MASK_LIMPIAR_ENROQUES_NEGROS | 8;
                    break;
                case A8:
                    estadoTablero &= MASK_LIMPIAR_ENROQUES_NEGROS | 4;
                    break;
                case H1:
                    estadoTablero &= MASK_LIMPIAR_ENROQUES_BLANCOS | 2;
                    break;
                case A1:
                    estadoTablero &= MASK_LIMPIAR_ENROQUES_BLANCOS | 1;
                    break;
            }

        }
        estadoTablero = colocarValor(estadoTablero,tablero[destino],POSICION_PIEZA_CAPTURADA,MASK_LIMPIAR_PIEZA_CAPTURADA);

        if (tablero[destino] == TORRE) {
            switch (destino) {
                case H8:
                    estadoTablero &= MASK_LIMPIAR_ENROQUES_NEGROS | 8;
                    break;
                case A8:
                    estadoTablero &= MASK_LIMPIAR_ENROQUES_NEGROS | 4;
                    break;
                case H1:
                    estadoTablero &= MASK_LIMPIAR_ENROQUES_BLANCOS | 2;
                    break;
                case A1:
                    estadoTablero &= MASK_LIMPIAR_ENROQUES_BLANCOS | 1;
                    break;

            }

        }


        tablero[destino] = tablero[inicio];
        color[destino] = color[inicio];
        tablero[inicio] = NOPIEZA;
        color[inicio] = NOCOLOR;

        estadoTablero &= MASK_LIMPIAR_AL_PASO;
        estadoTablero ^= 0b10000;
        return estadoTablero;
    }

    static boolean alPaso(int estadoTablero, int destino) {

        if(destino >= A3 && destino <= H3 || destino >= A6 && destino <= H6)
            return (estadoTablero >> POSICION_PIEZA_AL_PASO & 0b111111) == destino;

        return  false;
    }

    private static int colocarValor(int estadoTablero, int valor, int posicion, int mascara) {
        return estadoTablero & mascara | valor << posicion;
    }

    public static boolean esTurnoBlanco(int estadoTablero) {
        return (estadoTablero & 0b000000_000000_000_000_000000_1_00_00) > 0;
    }

    public static int  posicionRey(int estado, int desplazamiento) {
        return  estado >> desplazamiento & 0b111111;
    }

    public static int reyEnJaque(int[] pieza, int[] color, int estado) {
        var blanco = esTurnoBlanco(estado);

        var posicionRey = blanco ? posicionRey(estado, POSICION_REY_BLANCO) : posicionRey(estado, POSICION_REY_NEGRO);

        int result;

        if ((result = ataqueFilaColumna(posicionRey, pieza, color, blanco)) != NO_JAQUE) return result;
        if ((result = ataqueDiagonal(posicionRey, pieza, color, blanco)) != NO_JAQUE) return result;
        if ((result = ataqueCaballo(posicionRey, pieza, color, blanco)) != NO_JAQUE) return result;
        if ((result = ataquePeon(posicionRey, pieza, color, blanco)) != NO_JAQUE) return result;

        return NO_JAQUE;
    }

    public static boolean esCasillaBlanca(int i) {
        boolean filaImpar = (i / 8 + 1) % 2 != 0;

        return filaImpar == ((i + 1) % 2 == 0);
    }

    private static int ataquePeon(int posicion, int[] pieza, int[] color, boolean blanco) {
        if (blanco) {
            var destino = posicion + 9;
            if (destino < 64) {
                int posicionActual = pieza[destino];
                if (posicionActual != NOPIEZA && pieza[destino] == PEON && !(color[destino] == BLANCO) && esCasillaBlanca(posicion) == esCasillaBlanca(destino)) {
                    return destino;
                }
            }
            destino = posicion + 7;
            if (destino < 64) {
                var posicionActual = pieza[destino];
                if (posicionActual != NOPIEZA && posicionActual == PEON && !(color[destino] == BLANCO) && esCasillaBlanca(posicion) == esCasillaBlanca(destino)) {
                    return destino;
                }
            }
        } else {
            var destino = posicion - 9;
            if (destino >= 0) {
                var posicionActual = pieza[destino];
                if (posicionActual != NOPIEZA && pieza[destino] == PEON && color[destino] == BLANCO && esCasillaBlanca(posicion) == esCasillaBlanca(destino)) {
                    return destino;
                }
            }
            destino = posicion - 7;
            if (destino >= 0) {
                var posicionActual = pieza[destino];
                if (posicionActual != NOPIEZA && pieza[destino] == PEON && color[destino] == BLANCO && esCasillaBlanca(posicion) == esCasillaBlanca(destino)) {
                    return destino;
                }
            }
        }
        return NO_JAQUE;
    }

    private static int ataqueFilaColumna(int posicion, int[] pieza, int[] color, boolean blanco) {

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

    private static int ataqueDiagonal(int posicion, int[] pieza, int[] color, boolean blanco) {

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

    private static int ataqueCaballo(int posicion, int[] pieza, int[] color, boolean blanco) {

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

    public static boolean movimientoValido(int movimiento, int[] tablero, int[] color, int estado) {

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
        int posicionPiezaALPaso = 0;

        if (piezaActual == PEON && alPaso(estado,destino)) {
            posicionPiezaALPaso = destino + (esTurnoBlanco(estado) ? -8 : 8);
            tomaAlPaso = true;
            tablero[posicionPiezaALPaso] = NOPIEZA;

        }
        if (piezaActual == REY) {
            if (esTurnoBlanco(estado)) {
                estado = estado & MASK_LIMPIAR_POSICION_REY_BLANCO | destino << POSICION_REY_BLANCO;
            } else {
                estado = estado & MASK_LIMPIAR_POSICION_REY_NEGRO | destino << POSICION_REY_NEGRO;
            }
        }

        //var jaque = reyEnJaque(tablero, color, estado);

        var jaque = Tablero.casillaAtacada(posicionRey(estado, (esTurnoBlanco(estado) ? POSICION_REY_BLANCO : POSICION_REY_NEGRO)),tablero,color, esTurnoBlanco(estado) ? NEGRO : BLANCO );

        if (tomaAlPaso) {
            tablero[posicionPiezaALPaso] = PEON;
            color[posicionPiezaALPaso] = esTurnoBlanco(estado) ? NEGRO :BLANCO;
        }

        tablero[destino] = piezaDestino;
        tablero[inicio] = piezaActual;

        color[inicio] = color[destino];
        color[destino] = colorDestino;

        //return jaque == NO_JAQUE;
        return  !jaque;
    }

    public static int casillaANumero(String casilla) {
        int columna = "abcdefgh".indexOf(casilla.charAt(0));
        int fila = "12345678".indexOf(casilla.charAt(1));
        return columna + 8 * fila;
    }

    public static void imprimirBinario(int estadoTablero){
        String formateado = Integer.toBinaryString(estadoTablero);
        String formato = "";
        formato = formateado.substring(formateado.length() - 2);
        formato = formateado.substring(formateado.length() - 4, formateado.length() -2) + "_" + formato;
        formato = formateado.substring(formateado.length() - 5, formateado.length() -4) + "_" + formato;
        formato = formateado.substring(formateado.length() - 11, formateado.length() -5) + "_" + formato;
        formato = formateado.substring(formateado.length() - 14, formateado.length() -11) + "_" + formato;
        formato = formateado.substring(formateado.length() - 17, formateado.length() -14) + "_" + formato;
        formato = formateado.substring(formateado.length() - 23, formateado.length() -17) + "_" + formato;
        formato = formateado.substring(0, formateado.length() -23) + "_" + formato;

        System.out.println(formato);
    }


}
