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
import com.wolf.carlitos.Trayectoria.TRAYECTORIA;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author carlos
 */
public class Utilidades {

    public static void ImprimirPosicicion(Pieza[][] tablero) {
        for (int i = 7; i >= 0; i--) {
            System.out.println("+---+---+---+---+---+---+---+---+");
            for (int j = 0; j < 8; j++) {
                var pieza = tablero[i][j];
                System.out.print("| " + (pieza != null ? pieza.Nombre() : " ") + " ");
            }
            System.out.print("|");
            System.out.println();
        }

        System.out.println("+---+---+---+---+---+---+---+---+");
    }

    public static int[] convertirAPosicion(String movimiento) {
        int[] posicion;

        if (movimiento.length() == 5) {
            posicion = new int[5];

        } else {
            posicion = new int[4];
        }

        posicion[1] = "abcdefgh".indexOf(movimiento.charAt(0));
        posicion[0] = "12345678".indexOf(movimiento.charAt(1));

        posicion[3] = "abcdefgh".indexOf(movimiento.charAt(2));
        posicion[2] = "12345678".indexOf(movimiento.charAt(3));

        if (movimiento.length() == 5) {
            switch (movimiento.charAt(4)) {
                case 'q':
                    posicion[4] = 1;
                case 'r':
                    posicion[4] = 2;
                case 'n':
                    posicion[4] = 3;
                case 'b':
                    posicion[4] = 4;
            }
        }

        return posicion;
    }

    public static String ConvertirANotacion(int[] movimiento) {

        final String FILAS = "12345678";
        final String COLUMNAS = "abcdefgh";

        var mov = COLUMNAS.charAt(movimiento[1]) + "" + FILAS.charAt(movimiento[0])
                + COLUMNAS.charAt(movimiento[3]) + "" + FILAS.charAt(movimiento[2]);
        if (movimiento.length == 5) {
            switch (movimiento[4]) {
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

    private static void actualizarPosicion(Pieza[][] tablero, EstadoTablero estadoTablero, int[] movimiento) {
        int filaInicio = movimiento[0];
        int filaFinal = 0;
        int colInicio = movimiento[1];
        filaFinal = movimiento[2];

        int colFinal = movimiento[3];

        var pieza = tablero[filaInicio][colInicio];

        estadoTablero.piezaCapturada = null;
        estadoTablero.tipoMovimiento = -1;

        if (pieza instanceof Peon) {

            if (Math.abs(filaInicio - filaFinal) == 2) {
                estadoTablero.alPaso = true;
                estadoTablero.piezaALPaso = pieza;

                tablero[filaFinal][colFinal] = pieza;
                tablero[filaInicio][colInicio] = null;
                estadoTablero.tipoMovimiento = 0;
                return;
            }
            if (estadoTablero.alPaso) {
                if (colFinal > colInicio || colFinal < colInicio) {
                    if (tablero[filaInicio][colFinal] == estadoTablero.piezaALPaso) {
                        tablero[filaInicio][colFinal] = null;
                        estadoTablero.tipoMovimiento = 1;
                    }
                }
                estadoTablero.alPaso = false;
            }

            if (filaFinal == 7 || filaFinal == 0) {
                switch (movimiento[4]) {
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
            if (Math.abs(colInicio - colFinal) == 2) {
                if (pieza.EsBlanca()) {
                    if (colFinal == 6) {//enroque corto
                        tablero[0][5] = tablero[0][7];
                        tablero[0][7] = null;
                    } else {//enroque largo
                        tablero[0][3] = tablero[0][0];
                        tablero[0][0] = null;
                    }
                } else {
                    if (colFinal == 6) {//enroque corto
                        tablero[7][5] = tablero[7][7];
                        tablero[7][7] = null;
                    } else {//enroque largo
                        tablero[7][3] = tablero[7][0];
                        tablero[7][0] = null;
                    }
                }
                estadoTablero.tipoMovimiento = 3;
            } else {
                estadoTablero.tipoMovimiento = 100;
            }
            if (pieza.EsBlanca()) {
                estadoTablero.enroqueCBlanco = estadoTablero.enroqueLBlanco = false;
                estadoTablero.posicionReyBlanco[0] = filaFinal;
                estadoTablero.posicionReyBlanco[1] = colFinal;
            } else {
                estadoTablero.enroqueCNegro = estadoTablero.enroqueLNegro = false;
                estadoTablero.posicionReyNegro[0] = filaFinal;
                estadoTablero.posicionReyNegro[1] = colFinal;
            }

        } else if (pieza instanceof Torre) {
            if (colInicio == 7) {
                if (pieza.EsBlanca()) {
                    estadoTablero.enroqueCBlanco = false;
                } else {
                    estadoTablero.enroqueCNegro = false;
                }
            } else if (colInicio == 0) {
                if (pieza.EsBlanca()) {
                    estadoTablero.enroqueLBlanco = false;
                } else {
                    estadoTablero.enroqueLNegro = false;
                }
            }
        }

        if (tablero[filaFinal][colFinal] != null) {
            estadoTablero.piezaCapturada = tablero[filaFinal][colFinal];

            if (estadoTablero.piezaCapturada instanceof Torre) {
                if (colFinal == 7) {
                    if (estadoTablero.turnoBlanco) {
                        estadoTablero.enroqueCNegro = false;
                    } else {
                        estadoTablero.enroqueCBlanco = false;
                    }
                } else if (colFinal == 0) {
                    if (estadoTablero.turnoBlanco) {
                        estadoTablero.enroqueLNegro = false;
                    } else {
                        estadoTablero.enroqueLBlanco = false;
                    }
                }
            }
        }

        tablero[filaFinal][colFinal] = pieza;
        tablero[filaInicio][colInicio] = null;

        estadoTablero.alPaso = false;

        if (estadoTablero.tipoMovimiento == -1) {
            estadoTablero.tipoMovimiento = 0;
        }
    }

    public static void actualizarTablero(Pieza[][] tablero, EstadoTablero estadoTablero, int[] movimiento) {

        actualizarPosicion(tablero, estadoTablero, movimiento);
        actualizarTrayectorias(movimiento[2], movimiento[3], estadoTablero, tablero, false);
    }

    private static void actualizarTrayectorias(int filaFinal, int colFinal, EstadoTablero estadoTablero,
                                               Pieza[][] tablero, boolean recursivo) {


        var pieza = tablero[filaFinal][colFinal];

        var posicionRey = estadoTablero.turnoBlanco ?
                        estadoTablero.posicionReyNegro:
                        estadoTablero.posicionReyBlanco;

        // buscar trayectoria en esta posicion, si existe y la pieza != pieza, eliminar trayectoria
        // porque fue capturada
        if(!recursivo){

            for(var trayectoria : estadoTablero.trayectorias){
                var posicion = trayectoria.posicion;
                if(posicion[0] == filaFinal && posicion[1] == colFinal){
                    var p =  trayectoria.pieza;
                    if(p != pieza){
                        estadoTablero.trayectorias.remove(p);
                    }
                    break;
                }
            }

        }

        if(!recursivo){
            estadoTablero.reyEnJaque = false;
            estadoTablero.piezaJaque = null;
        }


        // si pieza  es atacada en alguna trayectoria borrar trayectoria
        // y recalcular
        if (!recursivo) {
            var trayectorias = new ArrayList<Trayectoria>();

            for (var trayectoria : estadoTablero.trayectorias) {
                if (trayectoria.piezasAtacadas.contains(pieza)) {
                    trayectorias.add(trayectoria);
                }
            }

            estadoTablero.trayectorias.removeAll(trayectorias);

            for (var trayectoria : trayectorias) {
                var posicion = trayectoria.posicion;
                actualizarTrayectorias(posicion[0], posicion[1], estadoTablero, tablero, true);
            }
        }
        // buscar trayectorias activas, si la pieza está en trayectoria agregarla
        if (!recursivo) {

            for (var trayectoria : estadoTablero.trayectorias) {

                var posicion = trayectoria.posicion;

                int piezaTrayectoriaX = posicion[1];
                int piezaTrayectoriaY = posicion[0];
                int reyX = posicionRey[1];
                int reyY = posicionRey[0];
                int piezaX = colFinal;
                int piezaY = filaFinal;

                boolean piezaEntreTrayectoria =
                        (piezaTrayectoriaY - piezaY) - (piezaY - reyY) == piezaTrayectoriaY - reyY ||
                                (piezaTrayectoriaX - piezaX) - (piezaX - reyX) == piezaTrayectoriaX - reyX;
                if (piezaEntreTrayectoria)
                    if (trayectoria.trayectoria == TRAYECTORIA.Diagonal) {
                        if (reyX - piezaX != 0) {
                            var pendienteTrayectoria = reyY - piezaTrayectoriaY / reyX - piezaTrayectoriaX;
                            var pendientePiezaAlRey = reyY - piezaY / reyX - piezaX;

                            if (pendientePiezaAlRey == pendienteTrayectoria) {
                                trayectoria.piezasAtacadas.add(pieza);
                            }
                        }

                    } else if (trayectoria.trayectoria == TRAYECTORIA.Recta) {

                        if (piezaX == piezaTrayectoriaX && piezaX == reyX
                                || piezaY == piezaTrayectoriaY && piezaY == reyY) {
                                trayectoria.piezasAtacadas.add(pieza);
                        }

                    }
            }

        }


        // recalcular las trayectorias de las piezas contrarias
        if (pieza instanceof Rey) {
            var esBlanco = estadoTablero.turnoBlanco;

            var trayectoriasBandoContrario = new ArrayList<Trayectoria>();

            for(var trayectoria : estadoTablero.trayectorias){
                if(trayectoria.pieza.EsBlanca() != esBlanco)
                    trayectoriasBandoContrario.add(trayectoria);
            }

            estadoTablero.trayectorias.removeAll(trayectoriasBandoContrario);

            for (int i = 0; i < tablero.length; i++) {
                for (int j = 0; j < tablero.length; j++) {
                    var p = tablero[i][j];
                    if (p != null && p.EsBlanca() != esBlanco && !(p instanceof Rey)) {
                        actualizarTrayectorias(i, j, estadoTablero, tablero, true);
                    }
                }
            }
            return;
        }

        if (pieza instanceof Caballo) {

            var coordenadas = new ArrayList<int[]>();

            coordenadas.add(new int[]{filaFinal + 2, colFinal + 1});
            coordenadas.add(new int[]{filaFinal + 2, colFinal - 1});
            coordenadas.add(new int[]{filaFinal - 2, colFinal + 1});
            coordenadas.add(new int[]{filaFinal - 2, colFinal - 1});
            coordenadas.add(new int[]{filaFinal + 1, colFinal - 2});
            coordenadas.add(new int[]{filaFinal + 1, colFinal - 2});
            coordenadas.add(new int[]{filaFinal + 1, colFinal + 2});
            coordenadas.add(new int[]{filaFinal - 1, colFinal + 2});

            var posicionesValidas = new ArrayList<int[]>();

            for(var c : coordenadas){
                if(c[0] >= 0 && c[0] < 8 && c[1] >= 0 && c[1] < 8)
                    posicionesValidas.add(c);
            }

            var jaqueCaballo = false;

            for (var p : posicionesValidas) {
                var piezaAtacada = tablero[p[0]][p[1]];
                if (piezaAtacada != null
                        && piezaAtacada.EsBlanca() != pieza.EsBlanca()
                        && piezaAtacada instanceof Rey) {
                    jaqueCaballo = true;
                    break;
                }
            }

            if (jaqueCaballo) {
                estadoTablero.reyEnJaque = true;
                estadoTablero.piezaJaque = pieza;
                var trayectoria = new Trayectoria(pieza, filaFinal, colFinal, TRAYECTORIA.Ninguna);
                estadoTablero.trayectorias.add(trayectoria);
            }

            return;
        }

        if (pieza instanceof Peon) {

            var esBlanco = pieza.EsBlanca();

            var posiciones = new ArrayList<int[]>();

            posiciones.add(new int[]{filaFinal + (esBlanco ? 1 : -1), colFinal + 1});
            posiciones.add(new int[]{filaFinal + (esBlanco ? 1 : -1), colFinal - 1});

            var posicionesValidas = posiciones.stream()
                    .filter(c -> c[0] >= 0 && c[0] < 8 && c[1] >= 0 && c[1] < 8)
                    .collect(Collectors.toList());

            var jaquePeon = posicionesValidas.stream()
                    .anyMatch(p ->
                    {
                        var piezaAtacada = tablero[p[0]][p[1]];
                        return piezaAtacada != null
                                && piezaAtacada.EsBlanca() != pieza.EsBlanca()
                                && piezaAtacada instanceof Rey;
                    });

            if (jaquePeon) {
                estadoTablero.reyEnJaque = true;
                estadoTablero.piezaJaque = pieza;
                var trayectoria = new Trayectoria(pieza, filaFinal, colFinal, TRAYECTORIA.Ninguna);
                estadoTablero.trayectorias.add(trayectoria);
            }
            return;
        }

        //posicion de la pieza
        int x1 = filaFinal;
        int y1 = colFinal;

        //posicion del rey
        int x2 = posicionRey[0];
        int y2 = posicionRey[1];

        var pendiente = false;

        if ((x2 - x1) != 0) {
            pendiente = Math.abs((double) (y2 - y1) / (x2 - x1)) == 1;
        }

        if ((pieza instanceof Dama || pieza instanceof Alfil) && pendiente) {

            var trayectoria = new Trayectoria(pieza, x1, y1, TRAYECTORIA.Diagonal);
            estadoTablero.trayectorias.add(trayectoria);

            var piezasRecorridas = new ArrayList<Pieza>();


            if (x1 < x2 && y1 > y2) {
                //IA
                for (int i = x1 + 1; i < x2; i++) {
                    var p = tablero[i][y1 - (i - x1)];
                    piezasRecorridas.add(p);
                }
            } else if (x1 < x2 && y1 < y2) {
                //DA
                for (int i = x1 + 1; i < x2; i++) {
//                    try{
//                        var p = tablero[i][y1 + (i - x1)];
//                    }catch(ArrayIndexOutOfBoundsException ex){
//                        Utilidades.ImprimirPosicicion(tablero);;
//                    }
                    var p = tablero[i][y1 + (i - x1)];
                    piezasRecorridas.add(p);
                }
            } else if (x1 > x2 && y1 < y2) {
                //DAB
                for (int i = x1 - 1; i > x2; i--) {
                    var p = tablero[i][y1 + (x1 - i)];
                    piezasRecorridas.add(p);
                }
            } else if (x1 > x2 && y1 > y2) {
                //IAB
                for (int i = x1 - 1; i > x2; i--) {
                    var p = tablero[i][y1 - (x1 - i)];
                    piezasRecorridas.add(p);
                }
            }


            piezasRecorridas.stream()
                    .filter(Objects::nonNull)
                    .forEach(trayectoria.piezasAtacadas::add);


            var jaque = trayectoria.piezasAtacadas.size() == 0;

            if (jaque) {
                estadoTablero.reyEnJaque = true;
                estadoTablero.piezaJaque = pieza;
            }

        }

        var mismaLinea = (x1 - x2 == 0 || y1 - y2 == 0);
        if(false)
        if ((pieza instanceof Torre || pieza instanceof Dama) && mismaLinea) {

            var trayectoria = new Trayectoria(pieza, x1, y1, TRAYECTORIA.Recta);
            estadoTablero.trayectorias.add(trayectoria);

            boolean jaque = true;
            if (x1 - x2 == 0) {
                if (y1 > y2) {
                    //izquierda
                    for (int i = y1 - 1; i > y2; i--) {
                        var p = tablero[x1][i];
                        if (p != null) {
                            if (p.EsBlanca() != pieza.EsBlanca()) {
                                trayectoria.piezasAtacadas.add(p);
                                jaque = false;
                            } else {
                                jaque = false;
                                break;
                            }

                        }
                    }
                } else {
                    //derecha
                    for (int i = y1 + 1; i < y2; i++) {
                        var p = tablero[x1][i];

                        if (p != null) {
                            if (p.EsBlanca() != pieza.EsBlanca()) {
                                trayectoria.piezasAtacadas.add(p);
                                jaque = false;
                            } else {
                                jaque = false;
                                break;
                            }

                        }

                    }
                }
            } else {
                if (x1 > x2) {
                    //abajo
                    for (int i = x1 - 1; i > x2; i--) {
                        var p = tablero[i][y1];
                        if (p != null) {
                            if (p.EsBlanca() != pieza.EsBlanca()) {
                                trayectoria.piezasAtacadas.add(p);
                                jaque = false;
                            } else {
                                jaque = false;
                                break;
                            }

                        }

                    }
                } else {
                    //arriba
                    for (int i = x1 + 1; i < x2; i++) {
                        var p = tablero[i][y1];
                        if (p != null) {
                            if (p.EsBlanca() != pieza.EsBlanca()) {
                                trayectoria.piezasAtacadas.add(p);
                                jaque = false;
                            } else {
                                jaque = false;
                                break;
                            }

                        }

                    }
                }
            }

            if (jaque) {
                estadoTablero.reyEnJaque = true;
                estadoTablero.piezaJaque = pieza;
            }



        }

    }

}
