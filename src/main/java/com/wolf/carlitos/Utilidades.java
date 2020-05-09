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

    public static int[] convertirAPosicion(String movimiento) {
        int[] posicion;

        if (movimiento.length() == 5) {
            posicion = new int[3];

        } else {
            posicion = new int[2];
        }

        var inicio = movimiento.substring(0, 2);
        var destino = movimiento.substring(2, 4);

        posicion[0] = casillaPosicion.get(inicio);
        posicion[1] = casillaPosicion.get(destino);


        if (movimiento.length() == 5) {
            switch (movimiento.charAt(4)) {
                case 'q':
                    posicion[2] = 1;
                    break;
                case 'r':
                    posicion[2] = 2;
                    break;
                case 'n':
                    posicion[2] = 3;
                    break;
                case 'b':
                    posicion[2] = 4;
                    break;
            }
        }

        return posicion;
    }

    public static String convertirANotacion(int[] movimiento) {

        var mov = posicionCasilla.get(movimiento[0]) + posicionCasilla.get(movimiento[1]);

        if (movimiento.length == 3) {
            switch (movimiento[2]) {
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

    public static void actualizarTablero(int[] tablero,int[]color, EstadoTablero estadoTablero, int[] movimiento) {


        int inicio = movimiento[0];
        int destino = movimiento[1];

        var pieza = tablero[inicio];

        estadoTablero.piezaCapturada = NOPIEZA;
        estadoTablero.colorCaptura = NOCOLOR;
        estadoTablero.tipoMovimiento = NO_ASIGNADO;

        if (pieza  == PEON) {

            if (abs(inicio - destino) == 16) {
                estadoTablero.alPaso = true;
                estadoTablero.piezaALPaso = destino + (estadoTablero.turnoBlanco ? -8 : 8);
                estadoTablero.colorAlPaso = color[inicio];

                tablero[destino] = pieza;
                tablero[inicio] = NOPIEZA;

                color[destino] = color[inicio];
                color[inicio] = NOCOLOR;

                estadoTablero.tipoMovimiento = MOVIMIENTO_NORMAL;
                return;
            }
            if (estadoTablero.alPaso) {
                if (abs(destino - inicio) == 7 || abs(destino - inicio) == 9) {
                    var posicionAlPaso = destino + (estadoTablero.turnoBlanco? - 8 : 8);
                    if (destino == estadoTablero.piezaALPaso) {

                        // aqui se remueve la pieza al paso
                        tablero[posicionAlPaso] = NOPIEZA;
                        estadoTablero.colorAlPaso = color[posicionAlPaso];
                        color[posicionAlPaso] = NOCOLOR;

                        estadoTablero.tipoMovimiento = AL_PASO;
                    }
                }

                estadoTablero.alPaso = false;
            }

            if (destino >= A1 && destino <= H1 || destino >= A8 && destino <= H8) {
                switch (movimiento[2]) {
                    case 1:
                        tablero[destino] = DAMA;
                        color[destino] = estadoTablero.turnoBlanco ?BLANCO:NEGRO;
                        break;
                    case 2:
                        tablero[destino] = TORRE;
                        color[destino] = estadoTablero.turnoBlanco ?BLANCO:NEGRO;
                        break;
                    case 3:
                        tablero[destino] = CABALLO;
                        color[destino] = estadoTablero.turnoBlanco ?BLANCO:NEGRO;
                        break;
                    case 4:
                        tablero[destino] = ALFIL;
                        color[destino] = estadoTablero.turnoBlanco ?BLANCO:NEGRO;
                        break;
                }
                estadoTablero.tipoMovimiento = PROMOCION;
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
                estadoTablero.tipoMovimiento = ENROQUE;
            } else {
                estadoTablero.tipoMovimiento = MOVIMIENTO_REY;
            }
            if (estadoTablero.turnoBlanco) {
                estadoTablero.enroqueCBlanco = estadoTablero.enroqueLBlanco = false;
                estadoTablero.posicionReyBlanco = destino;
            } else {
                estadoTablero.enroqueCNegro = estadoTablero.enroqueLNegro = false;
                estadoTablero.posicionReyNegro = destino;
            }

        } else if (pieza == TORRE) {

            switch (inicio) {
                case H8:
                    estadoTablero.enroqueCNegro = false;
                    break;
                case A8:
                    estadoTablero.enroqueLNegro = false;
                    break;
                case H1:
                    estadoTablero.enroqueCBlanco = false;
                    break;
                case A1:
                    estadoTablero.enroqueLBlanco = false;
                    break;
            }

        }

        estadoTablero.piezaCapturada = tablero[destino];
        estadoTablero.colorCaptura = color[destino];

        if (tablero[destino] == TORRE) {
            switch (destino) {
                case H8:
                    estadoTablero.enroqueCNegro = false;
                    break;
                case A8:
                    estadoTablero.enroqueLNegro = false;
                    break;
                case H1:
                    estadoTablero.enroqueCBlanco = false;
                    break;
                case A1:
                    estadoTablero.enroqueLBlanco = false;

            }

        }

        tablero[destino] = pieza;
        tablero[inicio] = NOPIEZA;
        color[destino] = color[inicio];
        color[inicio] = NOCOLOR;

        estadoTablero.alPaso = false;

        if (estadoTablero.tipoMovimiento == NO_ASIGNADO) {
            estadoTablero.tipoMovimiento = MOVIMIENTO_NORMAL;
        }
    }

    public static int reyEnJaque(int[]pieza, int[] color, EstadoTablero estado) {
        var blanco = estado.turnoBlanco;
        var posicionRey = blanco ? estado.posicionReyBlanco : estado.posicionReyNegro;

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

    public static boolean movimientoValido(int[] movimiento, int[] tablero,int[]color, EstadoTablero estado) {

        int inicio = movimiento[0];
        int destino = movimiento[1];

        int piezaActual = tablero[inicio];
        int piezaDestino = tablero[destino];
        int colorDestino = color[destino];

        tablero[inicio] = NOPIEZA;
        tablero[destino] = piezaActual;

        color[destino] = color[inicio];
        color[inicio] = NOCOLOR;

        boolean tomaAlPaso = false;
        int posicionPaso = 0;

        if (piezaActual == PEON && estado.alPaso) {

            if (estado.turnoBlanco) {
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

            if (tablero[posicionPaso] == estado.piezaALPaso) {
                tomaAlPaso = true;
                tablero[posicionPaso] = NOPIEZA;
            }

        }
        if (piezaActual == REY) {
            if (estado.turnoBlanco) {
                estado.posicionReyBlanco = destino;
            } else {
                estado.posicionReyNegro = destino;
            }
        }

        var jaque = reyEnJaque(tablero,color, estado);

        if (piezaActual == PEON && estado.alPaso && tomaAlPaso) {
            tablero[posicionPaso] = estado.piezaALPaso;
        }
        if (piezaActual== REY) {
            if (estado.turnoBlanco) {
                estado.posicionReyBlanco = inicio;
            } else {
                estado.posicionReyNegro = inicio;
            }
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
