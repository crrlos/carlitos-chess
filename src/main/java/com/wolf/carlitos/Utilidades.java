/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.Alfil;
import com.wolf.carlitos.Piezas.Caballo;
import com.wolf.carlitos.Piezas.Dama;
import com.wolf.carlitos.Piezas.Peon;
import com.wolf.carlitos.Piezas.Pieza;
import com.wolf.carlitos.Piezas.Rey;
import com.wolf.carlitos.Piezas.Torre;

import java.util.HashMap;

import static java.lang.Math.abs;
import static com.wolf.carlitos.Piezas.Casillas.*;

/**
 * @author carlos
 */
public class Utilidades {

    private  static  final HashMap<String,Integer> casillaPosicion = new HashMap<>();
    private  static  final HashMap<Integer,String> posicionCasilla = new HashMap<>();


    static {
        int correlativo = 0;

        for (int i = 1; i <= 8; i++) {
            for(char c : "abcdefgh".toCharArray()){
                casillaPosicion.put(String.valueOf(c) + i, correlativo);
                posicionCasilla.put(correlativo++, String.valueOf(c) + i);
            }
        }


    }

    public static void imprimirPosicicion(Pieza[] tablero) {

        for (int i = 64; i > 0; i -= 8) {
            System.out.println("+---+---+---+---+---+---+---+---+");
            for (int j = i - 8; j < (i - 8) + 8; j++) {
                var pieza = tablero[j];
                System.out.print("| " + (pieza != null ? pieza.nombre() : " ") + " ");
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

        var inicio = movimiento.substring(0,2);
        var destino = movimiento.substring(2,4);

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

    public static void actualizarTablero(Pieza[] tablero, EstadoTablero estadoTablero, int[] movimiento) {

        int inicio = movimiento[0];
        int destino = movimiento[1];


        var pieza = tablero[inicio];

        estadoTablero.piezaCapturada = null;
        estadoTablero.tipoMovimiento = -1;

        if (pieza instanceof Peon) {

            if (abs(inicio - destino) == 16) {
                estadoTablero.alPaso = true;
                estadoTablero.piezaALPaso = pieza;

                tablero[destino] = pieza;
                tablero[inicio] = null;
                estadoTablero.tipoMovimiento = 0;
                return;
            }
            if (estadoTablero.alPaso) {
                if (abs(destino - inicio) == 7  || abs(destino - inicio) == 9) {
                    if (tablero[destino] == estadoTablero.piezaALPaso) {

                        // aqui se remueve la pieza al paso
                        tablero[inicio]= null;
                        estadoTablero.tipoMovimiento = 1;
                    }
                }

                estadoTablero.alPaso = false;
            }

            if (destino >= A1 && destino <= H1 || destino >= A8 && destino <= H8) {
                switch (movimiento[2]) {
                    case 1:
                        pieza = new Dama(estadoTablero.turnoBlanco);
                        break;
                    case 2:
                        pieza = new Torre(estadoTablero.turnoBlanco);
                        break;
                    case 3:
                        pieza = new Caballo(estadoTablero.turnoBlanco);
                        break;
                    case 4:
                        pieza = new Alfil(estadoTablero.turnoBlanco);
                        break;
                }
                estadoTablero.tipoMovimiento = 2;
            }

        } else if (pieza instanceof Rey) {
            // en los enroques solo se mueven las torres por ser el movimiento especial
            if (abs(inicio - destino) == 2) {
                if (pieza.esBlanca()) {
                    if (destino == G1) {//enroque corto
                        tablero[F1] = tablero[H1];
                        tablero[H1] = null;
                    } else {//enroque largo
                        tablero[D1] = tablero[A1];
                        tablero[A1] = null;
                    }
                } else {
                    if (destino == G8) {//enroque corto
                        tablero[F1] = tablero[H8];
                        tablero[H8] = null;
                    } else {//enroque largo
                        tablero[D8] = tablero[A8];
                        tablero[A8] = null;
                    }
                }
                estadoTablero.tipoMovimiento = 3;
            } else {
                estadoTablero.tipoMovimiento = 100;
            }
            if (pieza.esBlanca()) {
                estadoTablero.enroqueCBlanco = estadoTablero.enroqueLBlanco = false;
                estadoTablero.posicionReyBlanco = destino;
            } else {
                estadoTablero.enroqueCNegro = estadoTablero.enroqueLNegro = false;
                estadoTablero.posicionReyNegro = destino;
            }

        } else if (pieza instanceof Torre) {

            if (inicio == H8) {
                estadoTablero.enroqueCNegro = false;
            } else if (inicio == A8) {
                estadoTablero.enroqueLNegro = false;
            } else if (inicio == H1) {
                estadoTablero.enroqueCBlanco = false;
            } else if (inicio == A1) {
                estadoTablero.enroqueLBlanco = false;
            }
        }

        estadoTablero.piezaCapturada = tablero[destino];
        if (estadoTablero.piezaCapturada instanceof Torre) {

            switch (destino){
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
        tablero[inicio] = null;

        estadoTablero.alPaso = false;

        if (estadoTablero.tipoMovimiento == -1) {
            estadoTablero.tipoMovimiento = 0;
        }
    }

//    public static int[] reyEnJaque(Pieza[][] tablero, EstadoTablero estado) {
//        var blanco = estado.turnoBlanco;
//        var posicionRey = blanco ? estado.posicionReyBlanco : estado.posicionReyNegro;
//
//        int[] result;
//
//        if ((result = ataqueFilaColumna(posicionRey[0], posicionRey[1], tablero, blanco)) != null) return result;
//        if ((result = ataqueDiagonal(posicionRey[0], posicionRey[1], tablero, blanco)) != null) return result;
//        if ((result = ataqueCaballo(posicionRey[0], posicionRey[1], tablero, blanco)) != null) return result;
//        if ((result = ataquePeon(posicionRey[0], posicionRey[1], tablero, blanco)) != null) return result;
//
//        return null;
//    }

    private static int[] ataqueRey(int fila, int columna, Pieza[][] tablero, boolean blanco) {

        if (columna + 1 < 8)
            if (tablero[fila][columna + 1] != null && (tablero[fila][columna + 1] instanceof Rey))
                return new int[]{fila, columna + 1};
        if (columna - 1 >= 0)
            if (tablero[fila][columna - 1] != null && (tablero[fila][columna - 1] instanceof Rey))
                return new int[]{fila, columna - 1};

        if (fila + 1 < 8) {
            if (tablero[fila + 1][columna] != null && (tablero[fila + 1][columna] instanceof Rey))
                return new int[]{fila + 1, columna};
            if (columna + 1 < 8)
                if (tablero[fila + 1][columna + 1] != null && (tablero[fila + 1][columna + 1] instanceof Rey))
                    return new int[]{fila + 1, columna + 1};
            if (columna - 1 >= 0)
                if (tablero[fila + 1][columna - 1] != null && (tablero[fila + 1][columna - 1] instanceof Rey))
                    return new int[]{fila + 1, columna - 1};
        }
        if (fila - 1 >= 0) {
            if (tablero[fila - 1][columna] != null && (tablero[fila - 1][columna] instanceof Rey))
                return new int[]{fila - 1, columna};
            if (columna + 1 < 8)
                if (tablero[fila - 1][columna + 1] != null && (tablero[fila - 1][columna + 1] instanceof Rey))
                    return new int[]{fila - 1, columna + 1};
            if (columna - 1 >= 0)
                if (tablero[fila - 1][columna - 1] != null && (tablero[fila - 1][columna - 1] instanceof Rey))
                    return new int[]{fila - 1, columna - 1};
        }

        return null;
    }

    private static int[] ataquePeon(int fila, int columna, Pieza[][] tablero, boolean blanco) {
        Pieza pieza;
        boolean condicion;

        fila = fila + (blanco ? 1 : -1);
        condicion = blanco ? fila < 8 : fila >= 0;

        if (condicion) {
            if (columna + 1 < 8)
                if ((pieza = tablero[fila][columna + 1]) != null) {
                    if (pieza.esBlanca() != blanco && pieza instanceof Peon)
                        return new int[]{fila, columna + 1};

                }
            if (columna - 1 >= 0)
                if ((pieza = tablero[fila][columna - 1]) != null) {
                    if (pieza.esBlanca() != blanco && pieza instanceof Peon)
                        return new int[]{fila, columna - 1};
                }
        }

        return null;
    }

    private static int[] ataqueFilaColumna(int fila, int columna, Pieza[][] tablero, boolean blanco) {

        var i = fila + 1;
        Pieza posicionActual;
        while (i < 8) {

            posicionActual = tablero[i][columna];
            if (posicionActual != null)
                if (posicionActual.esBlanca() != blanco && (posicionActual instanceof Torre || posicionActual instanceof Dama))
                    return new int[]{i, columna};
                else break;
            i++;
        }

        i = fila - 1;
        while (i >= 0) {

            posicionActual = tablero[i][columna];
            if (posicionActual != null)
                if (posicionActual.esBlanca() != blanco && (posicionActual instanceof Torre || posicionActual instanceof Dama))
                    return new int[]{i, columna};
                else break;
            i--;
        }
        i = columna + 1;
        while (i < 8) {

            posicionActual = tablero[fila][i];
            if (posicionActual != null)
                if (posicionActual.esBlanca() != blanco && (posicionActual instanceof Torre || posicionActual instanceof Dama))
                    return new int[]{fila, i};
                else break;
            i++;
        }
        i = columna - 1;
        while (i >= 0) {

            posicionActual = tablero[fila][i];
            if (posicionActual != null)
                if (posicionActual.esBlanca() != blanco && (posicionActual instanceof Torre || posicionActual instanceof Dama))
                    return new int[]{fila, i};
                else break;
            i--;
        }

        return null;
    }

    private static int[] ataqueDiagonal(int fila, int columna, Pieza[][] tablero, boolean blanco) {

        var f = fila + 1;
        var c = columna + 1;
        Pieza posicionActual;
        while (f < 8 && c < 8) {
            posicionActual = tablero[f][c];
            if (posicionActual != null) {
                if (posicionActual.esBlanca() != blanco && (posicionActual instanceof Dama || posicionActual instanceof Alfil))
                    return new int[]{f, c};
                else break;
            }
            ++f;
            ++c;
        }

        f = fila - 1;
        c = columna - 1;
        while (f >= 0 && c >= 0) {
            posicionActual = tablero[f][c];
            if (posicionActual != null) {
                if (posicionActual.esBlanca() != blanco && (posicionActual instanceof Dama || posicionActual instanceof Alfil))
                    return new int[]{f, c};
                else break;
            }
            --f;
            --c;
        }

        f = fila + 1;
        c = columna - 1;
        while (f < 8 && c >= 0) {
            posicionActual = tablero[f][c];
            if (posicionActual != null) {
                if (posicionActual.esBlanca() != blanco && (posicionActual instanceof Dama || posicionActual instanceof Alfil))
                    return new int[]{f, c};
                else break;
            }
            ++f;
            --c;
        }

        f = fila - 1;
        c = columna + 1;
        while (f >= 0 && c < 8) {
            posicionActual = tablero[f][c];
            if (posicionActual != null) {
                if (posicionActual.esBlanca() != blanco && (posicionActual instanceof Dama || posicionActual instanceof Alfil))
                    return new int[]{f, c};
                else break;
            }
            --f;
            ++c;
        }

        return null;
    }

    private static int[] ataqueCaballo(int fila, int columna, Pieza[][] tablero, boolean blanco) {
        Pieza posicionActual;

        if (fila + 2 < 8) {
            if (columna + 1 < 8) {
                posicionActual = tablero[fila + 2][columna + 1];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != blanco && posicionActual instanceof Caballo)
                        return new int[]{fila + 2, columna + 1};
                }

            }
            if (columna - 1 >= 0) {
                posicionActual = tablero[fila + 2][columna - 1];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != blanco && posicionActual instanceof Caballo)
                        return new int[]{fila + 2, columna - 1};
                }
            }
        }

        if (fila - 2 >= 0) {
            if (columna + 1 < 8) {
                posicionActual = tablero[fila - 2][columna + 1];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != blanco && posicionActual instanceof Caballo)
                        return new int[]{fila - 2, columna + 1};
                }
            }
            if (columna - 1 >= 0) {
                posicionActual = tablero[fila - 2][columna - 1];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != blanco && posicionActual instanceof Caballo)
                        return new int[]{fila - 2, columna - 1};
                }
            }
        }

        if (columna - 2 >= 0) {
            if (fila + 1 < 8) {
                posicionActual = tablero[fila + 1][columna - 2];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != blanco && posicionActual instanceof Caballo)
                        return new int[]{fila + 1, columna - 2};
                }
            }
            if (fila - 1 >= 0) {
                posicionActual = tablero[fila - 1][columna - 2];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != blanco && posicionActual instanceof Caballo)
                        return new int[]{fila - 1, columna - 2};
                }
            }
        }
        if (columna + 2 < 8) {
            if (fila + 1 < 8) {
                posicionActual = tablero[fila + 1][columna + 2];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != blanco && posicionActual instanceof Caballo)
                        return new int[]{fila + 1, columna + 2};
                }
            }
            if (fila - 1 >= 0) {
                posicionActual = tablero[fila - 1][columna + 2];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != blanco && posicionActual instanceof Caballo)
                        return new int[]{fila - 1, columna + 2};
                }
            }
        }
        return null;
    }

//    public static boolean movimientoValido(int[] movimiento, Pieza[][] tablero, EstadoTablero estado) {
//        var fo = movimiento[0];
//        var co = movimiento[1];
//        var fd = movimiento[2];
//        var cd = movimiento[3];
//
//        Pieza piezaActual = tablero[fo][co];
//        Pieza piezaDestino = tablero[fd][cd];
//
//        tablero[fo][co] = null;
//        tablero[fd][cd] = piezaActual;
//
//        boolean tomaAlPaso = false;
//
//        if (piezaActual instanceof Peon && estado.alPaso) {
//
//            if (tablero[fo][cd] == estado.piezaALPaso) {
//                tomaAlPaso = true;
//                tablero[fo][cd] = null;
//            }
//
//        }
//
//        var jaque = reyEnJaque(tablero, estado);
//
//        if (piezaActual instanceof Peon && estado.alPaso && tomaAlPaso) {
//            tablero[fo][cd] = estado.piezaALPaso;
//        }
//
//        tablero[fd][cd] = piezaDestino;
//        tablero[fo][co] = piezaActual;
//        return jaque == null;
//    }
}
