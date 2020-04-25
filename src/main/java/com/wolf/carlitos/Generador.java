/*
 * To change pieza license header, choose License Headers in Project Properties.
 * To change pieza template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carlos
 */
public class Generador {
    enum Trayectoria {Recta, Diagonal, Ninguna}

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

    public static List<int[]> movimientosDeTorre(Pieza[][] tablero, EstadoTablero estado, int fila, int columna) {

        var lista = new ArrayList<int[]>();

        Pieza posicionActual;
        Pieza pieza;

        pieza = tablero[fila][columna];

        var piezaJaque = Utilidades.reyEnJaque(tablero,estado);


        if(false)
        if (piezaJaque != null) {

            var posicionRey = estado.turnoBlanco ? estado.posicionReyBlanco : estado.posicionReyNegro;

            Trayectoria trayectoria = null;

            if (tablero[piezaJaque[0]][piezaJaque[1]] instanceof Peon ||
                    tablero[piezaJaque[0]][piezaJaque[1]] instanceof Caballo) {
                trayectoria = Trayectoria.Ninguna;

            } else if (piezaJaque[0] == posicionRey[0] || piezaJaque[1] == posicionRey[1]) {
                trayectoria = Trayectoria.Recta;

            } else {
                trayectoria = Trayectoria.Diagonal;

            }


            int puntoX = 0;
            int puntoY = 0;

            int x1 = piezaJaque[0];
            int y1 = piezaJaque[1];

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


            switch (trayectoria) {
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
                    // torre puede capturar pieza que ataca
                    if (fila - y1 == 0) {//misma fila
                        lista.add(new int[]{fila, columna, fila, x1});
                    } else if (columna - x1 == 0) {
                        lista.add(new int[]{fila, columna, y1, columna});
                    }
                    //si torre puede bloquear
                    if (x1 - x2 != 0 && columna >= Math.min(x1, x2) && columna <= Math.max(x1, x2)) {
                        if (!(tablero[y1][columna] instanceof Rey)) {
                            // TODO mejorar o revisar este paso
                            if (lista.size() > 0)
                            {
                                if (!(lista.get(0)[2] == y1 && lista.get(0)[3] == columna))
                                    lista.add(new int[]{fila, columna, y1, columna});
                            }
                            else
                                lista.add(new int[]{fila, columna, y1, columna});
                        }
                    } else if (y1 - y2 != 0 && fila >= Math.min(y1, y2) && fila <= Math.max(y1, y2)) {
                        if (!(tablero[fila][x2] instanceof Rey)) {
                            if (lista.size() > 0)
                            {
                                if (!(lista.get(0)[2] == y1 && lista.get(0)[3] == columna))
                                    lista.add(new int[]{fila, columna, fila, x2});
                            }
                            else
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

        boolean vertical;
        boolean horizontal;

//        int filaPrueba = fila < 7 && tablero[fila + 1][columna] == null ? fila + 1 :
//                fila > 0 && tablero[fila - 1][columna] == null ? fila - 1: fila;
//
//        int columnaPrueba = columna < 7 && tablero[fila][columna + 1] == null ? columna + 1 :
//                columna > 0 && tablero[fila][columna - 1] == null ? columna - 1: columna;
//
//
//        vertical = Utilidades.movimientoValido(new int[]{fila,columna,filaPrueba,columna},tablero,estado);
//        horizontal = Utilidades.movimientoValido(new int[]{fila,columna,fila,columnaPrueba},tablero,estado);

        vertical = horizontal = true;
        int i;

        if(vertical){
            i = fila + 1;
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
        if(horizontal){
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
        }
//        if(lista.stream().map(Utilidades::convertirANotacion).filter(s -> s.equals("h3f3")).count() > 0
//        && !estado.turnoBlanco){
//            Utilidades.imprimirPosicicion(tablero);
//            System.out.println();
//        }
        //return lista;
       return new Base(estado).MovimientosValidos(lista,tablero,estado.turnoBlanco);
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

        var lista = new ArrayList<int[]>();

        Pieza posicionActual;
        Pieza pieza = tablero[fila][columna];



        int filaDestino;





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
