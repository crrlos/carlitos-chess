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
import static com.wolf.carlitos.Constantes.*;

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
        estadoTablero.tipoMovimiento = NO_ASIGNADO;

        if (pieza instanceof Peon) {

            if (abs(inicio - destino) == 16) {
                estadoTablero.alPaso = true;
                estadoTablero.piezaALPaso = pieza;

                tablero[destino] = pieza;
                tablero[inicio] = null;
                estadoTablero.tipoMovimiento = MOVIMIENTO_NORMAL;
                return;
            }
            if (estadoTablero.alPaso) {
                if (abs(destino - inicio) == 7  || abs(destino - inicio) == 9) {
                    if (tablero[destino] == estadoTablero.piezaALPaso) {

                        // aqui se remueve la pieza al paso
                        tablero[inicio]= null;
                        estadoTablero.tipoMovimiento = AL_PASO;
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
                estadoTablero.tipoMovimiento = PROMOCION;
            }

        } else if (pieza instanceof Rey) {
            // en los enroques solo se mueven las torres por ser el movimiento especial
            if (abs(inicio - destino) == 2) {
                if (pieza.esBlanca()) {
                    if (destino == G1) {
                        tablero[F1] = tablero[H1];
                        tablero[H1] = null;
                    } else {
                        tablero[D1] = tablero[A1];
                        tablero[A1] = null;
                    }
                } else {
                    if (destino == G8) {
                        tablero[F8] = tablero[H8];
                        tablero[H8] = null;
                    } else {
                        tablero[D8] = tablero[A8];
                        tablero[A8] = null;
                    }
                }
                estadoTablero.tipoMovimiento = ENROQUE;
            } else {
                estadoTablero.tipoMovimiento = MOVIMIENTO_REY;
            }
            if (pieza.esBlanca()) {
                estadoTablero.enroqueCBlanco = estadoTablero.enroqueLBlanco = false;
                estadoTablero.posicionReyBlanco = destino;
            } else {
                estadoTablero.enroqueCNegro = estadoTablero.enroqueLNegro = false;
                estadoTablero.posicionReyNegro = destino;
            }

        } else if (pieza instanceof Torre) {

            switch (inicio){
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

        if (estadoTablero.tipoMovimiento == NO_ASIGNADO) {
            estadoTablero.tipoMovimiento = MOVIMIENTO_NORMAL;
        }
    }

    public static int reyEnJaque(Pieza[] tablero, EstadoTablero estado) {
        var blanco = estado.turnoBlanco;
        var posicionRey = blanco ? estado.posicionReyBlanco : estado.posicionReyNegro;

        int result;

        if ((result = ataqueFilaColumna(posicionRey, tablero, blanco)) != 0) return result;
        if ((result = ataqueDiagonal(posicionRey,tablero, blanco)) != 0) return result;
        if ((result = ataqueCaballo(posicionRey, tablero, blanco)) != 0) return result;
       // if ((result = ataquePeon(posicionRey, tablero, blanco)) != 0) return result;

        return 0;
    }
    private static int ataquePeon(int posicion, Pieza[] tablero, boolean blanco) {
            throw  new IllegalStateException("no implementado");

    }

    private static int ataqueFilaColumna(int posicion, Pieza[] tablero, boolean blanco) {

        var movimientosTorre = Generador.movimientosTorre.get(posicion);

        for (int i = 0; i < movimientosTorre.size(); i++) {

            for (int j = 0; j < movimientosTorre.get(i).size(); j++) {
                var m = movimientosTorre.get(i);
                var posicionActual = tablero[m.get(j)];
                if(posicionActual != null)
                    if(posicionActual.esBlanca() != blanco
                            && (posicionActual instanceof  Torre || posicionActual instanceof Dama)){
                        return m.get(i);
                    }

            }

        }
        return 0;
    }

    private static int ataqueDiagonal(int posicion, Pieza[] tablero, boolean blanco) {

        var movimientosAlfil = Generador.movimientosAlfil.get(posicion);

        for (int i = 0; i < movimientosAlfil.size(); i++) {

            for (int j = 0; j < movimientosAlfil.get(i).size(); j++) {
                var m = movimientosAlfil.get(i);
                var posicionActual = tablero[m.get(j)];
                if(posicionActual != null)
                    if(posicionActual.esBlanca() != blanco
                            && (posicionActual instanceof  Alfil || posicionActual instanceof Dama)){
                        return m.get(i);
                    }

            }

        }
        return 0;
    }

    private static int ataqueCaballo(int posicion, Pieza[] tablero, boolean blanco) {

        var movimientosCaballo = Generador.movimientosCaballo.get(posicion);

        for (int j = 0; j < movimientosCaballo.size(); j++) {
            var m = movimientosCaballo.get(j);
            var posicionActual = tablero[m];
            if(posicionActual != null)
                if(posicionActual.esBlanca() != blanco
                        && (posicionActual instanceof Caballo)){
                    return m;
                }

        }
        return 0;
    }

    public static boolean movimientoValido(int[] movimiento, Pieza[] tablero, EstadoTablero estado) {

        int inicio = movimiento[0];
        int destino = movimiento[1];

        Pieza piezaActual = tablero[inicio];
        Pieza piezaDestino = tablero[destino];

        if(piezaActual instanceof  Rey){
            var diferencia = abs(destino - (estado.turnoBlanco ? estado.posicionReyNegro : estado.posicionReyBlanco));
            if(diferencia == 1 || diferencia == 7 || diferencia == 8 || diferencia == 9) return  false;
        }

        tablero[inicio] = null;
        tablero[destino] = piezaActual;

        boolean tomaAlPaso = false;
        int posicionPaso = 0;

        if (piezaActual instanceof Peon && estado.alPaso) {

            if(estado.turnoBlanco){
                if(destino - inicio == 7){
                    posicionPaso = inicio - 1;
                }else if(destino - inicio == 9){
                    posicionPaso = inicio + 1;
                }
            }else{
                if(destino - inicio == -7){
                    posicionPaso = inicio + 1;
                }else if(destino - inicio == -9){
                    posicionPaso = inicio - 1;
                }
            }

            if (tablero[posicionPaso] == estado.piezaALPaso) {
                tomaAlPaso = true;
                tablero[posicionPaso] = null;
            }

        }

        var jaque = reyEnJaque(tablero, estado);

        if (piezaActual instanceof Peon && estado.alPaso && tomaAlPaso) {
            tablero[posicionPaso] = estado.piezaALPaso;
        }

        tablero[destino] = piezaDestino;
        tablero[inicio] = piezaActual;

        return jaque == 0;
    }

}
