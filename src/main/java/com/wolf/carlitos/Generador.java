/*
 * To change pieza license header, choose License Headers in Project Properties.
 * To change pieza template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.*;
import com.wolf.carlitos.Trayectoria.TRAYECTORIA;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carlos
 */
public class Generador {

    public static List<int[]> generarMovimientos(Pieza[][] tablero, EstadoTablero estado) {

        var movimientos = new ArrayList<int[]>();

        int fila = 0;
        int columna = 0;

        for (var f : tablero) {
            for (var pieza : f) {

                if (pieza != null && pieza.esBlanca() == estado.turnoBlanco) {
                    var movs = pieza instanceof Dama ? movimientosDeDama(tablero, estado, fila, columna)
                            : pieza instanceof Torre ? movimientosDeTorre(tablero, estado, fila, columna)
                                    : pieza instanceof Alfil ? movimientosDeAlfil(tablero, estado, fila, columna)
                                            : pieza instanceof Caballo ? movimientosDeCaballo(tablero, estado, fila, columna)
                                                    : pieza instanceof Peon ? movimientosDePeon(tablero, estado, fila, columna)
                                                            : movimientosDeRey(tablero, estado, fila, columna);
                    movimientos.addAll(movs);
                }
                columna++;
            }
            fila++;
            columna = 0;
        }
        return movimientos;

    }

    private static List<int[]> movimientosDeTorre(Pieza[][] tablero, EstadoTablero estado, int fila, int columna) {


//        if(tablero[7][4] instanceof Rey && tablero[6][5] instanceof Dama && tablero[5][0] instanceof Torre
//        && estado.reyEnJaque){
//           Utilidades.ImprimirPosicicion(tablero);
//        }
//        Utilidades.ImprimirPosicicion(tablero);

        boolean horizontal = true;
        boolean vertical = true;
        var lista = new ArrayList<int[]>();

        var posicionRey = estado.turnoBlanco
                ? estado.posicionReyBlanco
                : estado.posicionReyNegro;

        int[] posicionPiezaAtaque = null;
        if (estado.reyEnJaque) {

            int puntoX = 0;
            int puntoY = 0;

            TRAYECTORIA tipoTrayectoria = TRAYECTORIA.Ninguna;
            //buscar posicion de jaque
            var iterator = estado.trayectorias.listIterator(estado.trayectorias.size());
            while (iterator.hasPrevious()) {
                var trayectoria = iterator.previous();
                if (trayectoria.pieza == estado.piezaJaque) {
                    posicionPiezaAtaque = trayectoria.posicion;
                    tipoTrayectoria = trayectoria.trayectoria;
                }
            }

            int x1 = posicionPiezaAtaque[0];
            int y1 = posicionPiezaAtaque[1];

            int x2 = posicionRey[0];
            int y2 = posicionRey[1];

            //invertir en el tablero se guarda [fila][columna] o sea [y][x]
            //en matematica seria x = columna, y = fila
            int temp;

            temp = x1;
            x1 = y1;
            y1 = temp;

            temp = x2;
            x2 = y2;
            y2 = temp;


            switch (tipoTrayectoria) {
                case Diagonal:

                    //se invierte para que cuadre la formula de los puntos
                    //punto x1 < x2
                    if (x1 > x2) {

                        temp = x1;
                        x1 = x2;
                        x2 = temp;
                        temp = y1;
                        y1 = y2;
                        y2 = temp;

                    }

                    int constante = (x1 * (y2 - y1) - y1 * (x2 - x1)) / Math.abs(y2 - y1);
                    boolean x;
                    boolean c;
                    x = -(y2 - y1) > 0;
                    c = constante > 0;
                    constante = Math.abs(constante);
                    if (!x && !c) {
                        puntoX = fila + constante;
                        puntoY = columna + constante;
                    } else if (!x && c) {
                        puntoX = fila + constante;
                        puntoY = columna - constante;
                    } else if (x && !c) {
                        puntoX = -fila + constante;
                        puntoY = -columna + constante;
                    }
                    if (Math.min(x1, x2) <= puntoX && puntoX <= Math.max(x1, x2)) {
                        if (!(tablero[fila][puntoX] instanceof Rey)) {
                            lista.add(new int[]{fila, columna, fila, puntoX});
                        }

                    }
                    if (Math.min(y1, y2) <= puntoY && puntoY <= Math.max(y1, y2)) {
                        if (!(tablero[puntoY][columna] instanceof Rey)) {
                            lista.add(new int[]{fila, columna, puntoY, columna});
                        }
                    }
                    break;
                case Recta:

                    if (x1 - x2 != 0 && columna >= Math.min(x1, x2) && columna <= Math.max(x1, x2)) {
                        if (!(tablero[y1][columna] instanceof Rey)) {
                            lista.add(new int[]{fila, columna, y1, columna});
                        }
                    } else if (y1 - y2 != 0 && fila >= Math.min(y1, y2) && fila <= Math.max(y1, y2)) {
                        if (!(tablero[fila][x2] instanceof Rey)) {
                            lista.add(new int[]{fila, columna, fila, x2});
                        }
                    }
                    break;
                case Ninguna:
                    if (x1 - columna == 0) {
                        lista.add(new int[]{fila, columna, y1, columna});
                    } else if (y1 - fila == 0) {
                        lista.add(new int[]{fila, columna, fila, x1});
                    }
                    break;
                default:
                    break;
            }
            //validar movimientos generados
            var ite = lista.iterator();

            while (ite.hasNext()) {
                var mov = ite.next();

                if (mov[2] > fila) {//arriba
                    for (int i = fila + 1; i < mov[2]; i++) {
                        if (tablero[i][columna] != null) {
                            ite.remove();
                            break;
                        }
                    }

                } else if (mov[2] < fila) {//abajo
                    for (int i = fila - 1; i > mov[2]; i--) {
                        if (tablero[i][columna] != null) {
                            ite.remove();
                            break;
                        }
                    }
                } else if (mov[3] > columna) {//derecha
                    for (int i = columna + 1; i < mov[3]; i++) {
                        if (tablero[fila][i] != null) {
                            ite.remove();
                            break;
                        }
                    }
                } else if (mov[3] < columna) {//izquierda
                    for (int i = columna - 1; i > mov[3]; i--) {
                        if (tablero[fila][i] != null) {
                            ite.remove();
                            break;
                        }
                    }
                }
            }

            return lista;

        }

        Pieza posicionActual;
        Pieza pieza;

        pieza = tablero[fila][columna];

        boolean estaClavada = false;

        for (int i = 0; i < estado.trayectorias.size(); i++) {
            var trayectoria = estado.trayectorias.get(i);
            if (trayectoria.pieza.esBlanca() != pieza.esBlanca()
                    && trayectoria.piezasAtacadas.size() == 1
                    && trayectoria.trayectoria == TRAYECTORIA.Diagonal
                    && trayectoria.piezasAtacadas.contains(pieza)) {
                estaClavada = true;
                break;
            }
        }
        if(estaClavada) return lista;


        Trayectoria resultado = null;


        for (int i = 0; i < estado.trayectorias.size(); i++) {
            var trayectoria = estado.trayectorias.get(i);

            if (trayectoria.pieza.esBlanca() != pieza.esBlanca()
                    && trayectoria.piezasAtacadas.contains(pieza)) {
                resultado = trayectoria;
                break;
            }
        }


        if(resultado != null){

           int x = resultado.posicion[0];
           int y = resultado.posicion[1];

           if(x == fila)
               vertical = false;

           if(y == columna)
               horizontal = false;
        }


        var i = fila + 1;

        if (vertical) {
            while (i < 8) {
                posicionActual = tablero[i][columna];

                if (posicionActual != null) {
                    if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                        break;
                    }
                }

                lista.add(new int[]{fila, columna, i, columna});

                if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                    break;
                }

                i++;
            }
            i = fila - 1;
            while (i >= 0) {
                posicionActual = tablero[i][columna];

                if (posicionActual != null) {
                    if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                        break;
                    }
                }

                lista.add(new int[]{fila, columna, i, columna});

                if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                    break;
                }

                i--;
            }
        }
        i = columna + 1;
        if (horizontal) {
            while (i < 8) {
                posicionActual = tablero[fila][i];

                if (posicionActual != null) {
                    if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                        break;
                    }
                }

                lista.add(new int[]{fila, columna, fila, i});

                if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                    break;
                }
                i++;
            }
            i = columna - 1;
            while (i >= 0) {
                posicionActual = tablero[fila][i];

                if (posicionActual != null) {
                    if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                        break;
                    }
                }

                lista.add(new int[]{fila, columna, fila, i});

                if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                    break;
                }

                i--;
            }
        }
        return lista;
    }

    private static List<int[]> movimientosDeDama(Pieza[][] tablero, EstadoTablero estado, int fila, int columna) {
        Pieza posicionActual;
        Pieza pieza;
        var lista = new ArrayList<int[]>();
        pieza = tablero[fila][columna];
        var f = fila + 1;
        var c = columna + 1;
        while (f < 8 && c < 8) {
            posicionActual = tablero[f][c];

            if (posicionActual != null) {
                if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                break;
            }

            f++;
            c++;
        }

        f = fila - 1;
        c = columna - 1;
        while (f >= 0 && c >= 0) {

            posicionActual = tablero[f][c];

            if (posicionActual != null) {
                if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                break;
            }

            f--;
            c--;
        }

        f = fila + 1;
        c = columna - 1;
        while (f < 8 && c >= 0) {
            posicionActual = tablero[f][c];

            if (posicionActual != null) {
                if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                break;
            }

            f++;
            c--;
        }

        f = fila - 1;
        c = columna + 1;
        while (f >= 0 && c < 8) {
            posicionActual = tablero[f][c];

            if (posicionActual != null) {
                if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                break;
            }

            f--;
            c++;
        }

        var i = fila + 1;

        while (i < 8) {
            posicionActual = tablero[i][columna];

            if (posicionActual != null) {
                if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, i, columna});

            if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                break;
            }

            i++;
        }
        i = fila - 1;
        while (i >= 0) {
            posicionActual = tablero[i][columna];

            if (posicionActual != null) {
                if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, i, columna});

            if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                break;
            }

            i--;
        }
        i = columna + 1;
        while (i < 8) {
            posicionActual = tablero[fila][i];

            if (posicionActual != null) {
                if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, fila, i});

            if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                break;
            }
            i++;
        }
        i = columna - 1;
        while (i >= 0) {
            posicionActual = tablero[fila][i];

            if (posicionActual != null) {
                if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, fila, i});

            if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                break;
            }

            i--;
        }
        return new Base(estado).MovimientosValidos(lista, tablero, pieza.esBlanca());
    }

    private static List<int[]> movimientosDeCaballo(Pieza[][] tablero, EstadoTablero estado, int fila, int columna) {
        Pieza posicionActual;
        Pieza pieza;

        var lista = new ArrayList<int[]>();

        pieza = tablero[fila][columna];

        if (fila + 2 < 8) {
            if (columna + 1 < 8) {
                posicionActual = tablero[fila + 2][columna + 1];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila + 2, columna + 1});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila + 2, columna + 1});
                }
            }
            if (columna - 1 >= 0) {
                posicionActual = tablero[fila + 2][columna - 1];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila + 2, columna - 1});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila + 2, columna - 1});
                }
            }
        }

        if (fila - 2 >= 0) {
            if (columna + 1 < 8) {
                posicionActual = tablero[fila - 2][columna + 1];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila - 2, columna + 1});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila - 2, columna + 1});
                }
            }
            if (columna - 1 >= 0) {
                posicionActual = tablero[fila - 2][columna - 1];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila - 2, columna - 1});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila - 2, columna - 1});
                }
            }
        }

        if (columna - 2 >= 0) {
            if (fila + 1 < 8) {
                posicionActual = tablero[fila + 1][columna - 2];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila + 1, columna - 2});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila + 1, columna - 2});
                }
            }
            if (fila - 1 >= 0) {
                posicionActual = tablero[fila - 1][columna - 2];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila - 1, columna - 2});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila - 1, columna - 2});
                }
            }
        }
        if (columna + 2 < 8) {
            if (fila + 1 < 8) {
                posicionActual = tablero[fila + 1][columna + 2];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila + 1, columna + 2});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila + 1, columna + 2});
                }
            }
            if (fila - 1 >= 0) {
                posicionActual = tablero[fila - 1][columna + 2];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila - 1, columna + 2});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila - 1, columna + 2});
                }
            }
        }

        return new Base(estado).MovimientosValidos(lista, tablero, pieza.esBlanca());

    }

    private static List<int[]> movimientosDeAlfil(Pieza[][] tablero, EstadoTablero estado, int fila, int columna) {

        Pieza posicionActual;
        Pieza pieza = tablero[fila][columna];
        var lista = new ArrayList<int[]>();

        var f = fila + 1;
        var c = columna + 1;
        while (f < 8 && c < 8) {
            posicionActual = tablero[f][c];

            if (posicionActual != null) {
                if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                break;
            }

            f++;
            c++;
        }

        f = fila - 1;
        c = columna - 1;
        while (f >= 0 && c >= 0) {

            posicionActual = tablero[f][c];

            if (posicionActual != null) {
                if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                break;
            }

            f--;
            c--;
        }

        f = fila + 1;
        c = columna - 1;
        while (f < 8 && c >= 0) {
            posicionActual = tablero[f][c];

            if (posicionActual != null) {
                if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                break;
            }

            f++;
            c--;
        }

        f = fila - 1;
        c = columna + 1;
        while (f >= 0 && c < 8) {
            posicionActual = tablero[f][c];

            if (posicionActual != null) {
                if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                break;
            }

            f--;
            c++;
        }
        return new Base(estado).MovimientosValidos(lista, tablero, pieza.esBlanca());

    }

    private static List<int[]> movimientosDeRey(Pieza[][] tablero, EstadoTablero estado, int fila, int columna) {

        var base = new Base(estado);

        var lista = new ArrayList<int[]>();

        Pieza pieza = tablero[fila][columna];

        if (fila + 1 < 8) {

            if (tablero[fila + 1][columna] == null || tablero[fila + 1][columna].esBlanca() != pieza.esBlanca()) {
                lista.add(new int[]{fila, columna, fila + 1, columna});
            }

            if (columna + 1 < 8) {
                if (tablero[fila + 1][columna + 1] == null || tablero[fila + 1][columna + 1].esBlanca() != pieza.esBlanca()) {
                    lista.add(new int[]{fila, columna, fila + 1, columna + 1});
                }
            }

            if (columna - 1 >= 0) {
                if (tablero[fila + 1][columna - 1] == null || tablero[fila + 1][columna - 1].esBlanca() != pieza.esBlanca()) {
                    lista.add(new int[]{fila, columna, fila + 1, columna - 1});
                }
            }

        }
        if (fila - 1 >= 0) {

            if (tablero[fila - 1][columna] == null || tablero[fila - 1][columna].esBlanca() != pieza.esBlanca()) {
                lista.add(new int[]{fila, columna, fila - 1, columna});
            }

            if (columna + 1 < 8) {
                if (tablero[fila - 1][columna + 1] == null || tablero[fila - 1][columna + 1].esBlanca() != pieza.esBlanca()) {
                    lista.add(new int[]{fila, columna, fila - 1, columna + 1});
                }
            }

            if (columna - 1 >= 0) {
                if (tablero[fila - 1][columna - 1] == null || tablero[fila - 1][columna - 1].esBlanca() != pieza.esBlanca()) {
                    lista.add(new int[]{fila, columna, fila - 1, columna - 1});
                }
            }

        }

        if (columna + 1 < 8) {
            if (tablero[fila][columna + 1] == null || tablero[fila][columna + 1].esBlanca() != pieza.esBlanca()) {
                lista.add(new int[]{fila, columna, fila, columna + 1});
            }

        }
        if (columna - 1 >= 0) {
            if (tablero[fila][columna - 1] == null || tablero[fila][columna - 1].esBlanca() != pieza.esBlanca()) {
                lista.add(new int[]{fila, columna, fila, columna - 1});
            }

        }

        if (!base.ReyEnJaque(tablero, pieza.esBlanca())) {
            if (estado.enroqueCBlanco && pieza.esBlanca()) {
                if (columna == 4) {
                    if (tablero[fila][columna + 1] == null && tablero[fila][columna + 2] == null) {
                        tablero[fila][columna + 1] = tablero[fila][columna];//camino del rey
                        tablero[fila][columna] = null;
                        estado.posicionReyBlanco[1] = columna + 1;
                        if (!base.ReyEnJaque(tablero, pieza.esBlanca())) {
                            tablero[fila][columna + 2] = tablero[fila][columna + 1];
                            tablero[fila][columna + 1] = null;
                            estado.posicionReyBlanco[1] = columna + 2;
                            if (!base.ReyEnJaque(tablero, pieza.esBlanca())) {
                                lista.add(new int[]{fila, columna, fila, columna + 2});
                                tablero[fila][columna] = tablero[fila][columna + 2];
                                tablero[fila][columna + 2] = null;
                            } else {
                                tablero[fila][columna] = tablero[fila][columna + 2];
                                tablero[fila][columna + 2] = null;
                            }
                        } else {
                            tablero[fila][columna] = tablero[fila][columna + 1];
                            tablero[fila][columna + 1] = null;
                        }
                    }
                }
                estado.posicionReyBlanco[1] = columna;
            }
            if (estado.enroqueLBlanco && pieza.esBlanca()) {
                if (columna == 4) {
                    if (tablero[fila][columna - 1] == null && tablero[fila][columna - 2] == null && tablero[fila][columna - 3] == null) {
                        tablero[fila][columna - 1] = tablero[fila][columna];
                        tablero[fila][columna] = null;
                        //estado.PosicionReyBlanco[0] = fila;
                        estado.posicionReyBlanco[1] = columna - 1;
                        if (!base.ReyEnJaque(tablero, pieza.esBlanca())) {
                            tablero[fila][columna - 2] = tablero[fila][columna - 1];
                            tablero[fila][columna - 1] = null;
                            // estado.PosicionReyBlanco[0] = fila;
                            estado.posicionReyBlanco[1] = columna - 2;
                            if (!base.ReyEnJaque(tablero, pieza.esBlanca())) {
                                lista.add(new int[]{fila, columna, fila, columna - 2});
                                tablero[fila][columna] = tablero[fila][columna - 2];
                                tablero[fila][columna - 2] = null;
                            } else {
                                tablero[fila][columna] = tablero[fila][columna - 2];
                                tablero[fila][columna - 2] = null;
                            }
                        } else {
                            tablero[fila][columna] = tablero[fila][columna - 1];
                            tablero[fila][columna - 1] = null;
                        }
                    }
                }
                estado.posicionReyBlanco[1] = columna;
            }
            //enroque
            if (estado.enroqueCNegro && !pieza.esBlanca()) {
                if (columna == 4) {
                    if (tablero[fila][columna + 1] == null && tablero[fila][columna + 2] == null) {
                        tablero[fila][columna + 1] = tablero[fila][columna];
                        tablero[fila][columna] = null;
                        //estado.PosicionReyNegro[0] = fila;
                        estado.posicionReyNegro[1] = columna + 1;
                        if (!base.ReyEnJaque(tablero, pieza.esBlanca())) {
                            tablero[fila][columna + 2] = tablero[fila][columna + 1];
                            tablero[fila][columna + 1] = null;
                            estado.posicionReyNegro[0] = fila;
                            estado.posicionReyNegro[1] = columna + 2;
                            if (!base.ReyEnJaque(tablero, pieza.esBlanca())) {
                                lista.add(new int[]{fila, columna, fila, columna + 2});
                                tablero[fila][columna] = tablero[fila][columna + 2];
                                tablero[fila][columna + 2] = null;
                            } else {
                                tablero[fila][columna] = tablero[fila][columna + 2];
                                tablero[fila][columna + 2] = null;
                            }
                        } else {
                            tablero[fila][columna] = tablero[fila][columna + 1];
                            tablero[fila][columna + 1] = null;
                        }
                    }
                }
                estado.posicionReyNegro[1] = columna;
            }
            if (estado.enroqueLNegro && !pieza.esBlanca()) {
                if (columna == 4) {
                    if (tablero[fila][columna - 1] == null && tablero[fila][columna - 2] == null && tablero[fila][columna - 3] == null) {
                        tablero[fila][columna - 1] = tablero[fila][columna];
                        tablero[fila][columna] = null;
                        //estado.PosicionReyNegro[0] = fila;
                        estado.posicionReyNegro[1] = columna - 1;
                        if (!base.ReyEnJaque(tablero, pieza.esBlanca())) {
                            tablero[fila][columna - 2] = tablero[fila][columna - 1];
                            tablero[fila][columna - 1] = null;
                            //estado.PosicionReyNegro[0] = fila;
                            estado.posicionReyNegro[1] = columna - 2;
                            if (!base.ReyEnJaque(tablero, pieza.esBlanca())) {
                                lista.add(new int[]{fila, columna, fila, columna - 2});
                                tablero[fila][columna] = tablero[fila][columna - 2];
                                tablero[fila][columna - 2] = null;
                            } else {
                                tablero[fila][columna] = tablero[fila][columna - 2];
                                tablero[fila][columna - 2] = null;
                            }
                        } else {
                            tablero[fila][columna] = tablero[fila][columna - 1];
                            tablero[fila][columna - 1] = null;
                        }
                    }
                }
                estado.posicionReyNegro[1] = columna;
            }
        }
        return base.MovimientosValidosRey(lista, tablero, pieza.esBlanca());
    }

    private static List<int[]> movimientosDePeon(Pieza[][] tablero, EstadoTablero estado, int fila, int columna) {
        int filaDestino;

        Pieza posicionActual;
        Pieza pieza = tablero[fila][columna];

        var lista = new ArrayList<int[]>();

        //avance dos casillas
        if (fila == (pieza.esBlanca() ? 1 : 6)) {
            if (tablero[pieza.esBlanca() ? 2 : 5][columna] == null && tablero[pieza.esBlanca() ? 3 : 4][columna] == null) {
                lista.add(new int[]{fila, columna, pieza.esBlanca() ? 3 : 4, columna});
            }
        }

        //avance una casilla
        filaDestino = fila + (pieza.esBlanca() ? 1 : -1);
        if (tablero[filaDestino][columna] == null) {
            if (filaDestino == (pieza.esBlanca() ? 7 : 0)) {
                lista.add(new int[]{fila, columna, filaDestino, columna, 1});
                lista.add(new int[]{fila, columna, filaDestino, columna, 2});
                lista.add(new int[]{fila, columna, filaDestino, columna, 3});
                lista.add(new int[]{fila, columna, filaDestino, columna, 4});
            } else {
                lista.add(new int[]{fila, columna, filaDestino, columna});
            }
        }
        //avance diagonal
        if (columna > 0) {
            posicionActual = tablero[filaDestino][columna - 1];
            if (posicionActual != null) {
                if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                    if (filaDestino == (pieza.esBlanca() ? 7 : 0)) {
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 1});
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 2});
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 3});
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 4});
                    } else {
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1});
                    }
                }
            } else if (estado.alPaso && fila == (pieza.esBlanca() ? 4 : 3)) {
                if (tablero[fila][columna - 1] == estado.piezaALPaso) {
                    lista.add(new int[]{fila, columna, filaDestino, columna - 1});
                }
            }
        }
        if (columna < 7) {
            posicionActual = tablero[filaDestino][columna + 1];
            if (posicionActual != null) {
                if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                    if (filaDestino == (pieza.esBlanca() ? 7 : 0)) {
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 1});
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 2});
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 3});
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 4});
                    } else {
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1});
                    }
                }
            } else if (estado.alPaso && fila == (pieza.esBlanca() ? 4 : 3)) {
                if (tablero[fila][columna + 1] == estado.piezaALPaso) {
                    lista.add(new int[]{fila, columna, filaDestino, columna + 1});
                }
            }
        }

        return new Base(estado).MovimientosValidos(lista, tablero, pieza.esBlanca());
    }

}
