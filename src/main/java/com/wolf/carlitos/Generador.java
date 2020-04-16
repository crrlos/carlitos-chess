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

    public static List<int[]> generarMovimientos(Pieza[][] tablero, EstadoTablero estado) {

        var movimientos = new ArrayList<int[]>();

        int fila = 0;
        int columna = 0;

        for (var f : tablero) {
            for (var pieza : f) {

                if (pieza != null && pieza.EsBlanca() == estado.TurnoBlanco) {
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
        boolean horizontal = true;
        boolean vertical = true;
        
        
        var posicionRey = estado.TurnoBlanco?  
                estado.PosicionReyBlanco : 
                estado.PosicionReyNegro;
        
        
        if(estado.reyEnJaque){
            
            
            
        }
        
        
        Pieza posicionActual;
        Pieza pieza;
        
        var lista = new ArrayList<int[]>();
        pieza = tablero[fila][columna];

        var i = fila + 1;

        if (vertical) {
            while (i < 8) {
                posicionActual = tablero[i][columna];

                if (posicionActual != null) {
                    if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                        break;
                    }
                }

                lista.add(new int[]{fila, columna, i, columna});

                if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
                    break;
                }

                i++;
            }
            i = fila - 1;
            while (i >= 0) {
                posicionActual = tablero[i][columna];

                if (posicionActual != null) {
                    if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                        break;
                    }
                }

                lista.add(new int[]{fila, columna, i, columna});

                if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
                    break;
                }

                i--;
            }
        }
        i = columna + 1;
        if(horizontal){
            while (i < 8) {
            posicionActual = tablero[fila][i];

            if (posicionActual != null) {
                if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, fila, i});

            if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
                break;
            }
            i++;
        }
        i = columna - 1;
        while (i >= 0) {
            posicionActual = tablero[fila][i];

            if (posicionActual != null) {
                if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, fila, i});

            if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
                break;
            }

            i--;
        }
        }
        return new Base(estado).MovimientosValidos(lista, tablero, pieza.EsBlanca());
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
                if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
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
                if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
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
                if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
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
                if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
                break;
            }

            f--;
            c++;
        }

        var i = fila + 1;

        while (i < 8) {
            posicionActual = tablero[i][columna];

            if (posicionActual != null) {
                if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, i, columna});

            if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
                break;
            }

            i++;
        }
        i = fila - 1;
        while (i >= 0) {
            posicionActual = tablero[i][columna];

            if (posicionActual != null) {
                if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, i, columna});

            if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
                break;
            }

            i--;
        }
        i = columna + 1;
        while (i < 8) {
            posicionActual = tablero[fila][i];

            if (posicionActual != null) {
                if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, fila, i});

            if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
                break;
            }
            i++;
        }
        i = columna - 1;
        while (i >= 0) {
            posicionActual = tablero[fila][i];

            if (posicionActual != null) {
                if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, fila, i});

            if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
                break;
            }

            i--;
        }
        return new Base(estado).MovimientosValidos(lista, tablero, pieza.EsBlanca());
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
                    if (posicionActual.EsBlanca() != pieza.EsBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila + 2, columna + 1});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila + 2, columna + 1});
                }
            }
            if (columna - 1 >= 0) {
                posicionActual = tablero[fila + 2][columna - 1];
                if (posicionActual != null) {
                    if (posicionActual.EsBlanca() != pieza.EsBlanca() && !(posicionActual instanceof Rey)) {
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
                    if (posicionActual.EsBlanca() != pieza.EsBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila - 2, columna + 1});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila - 2, columna + 1});
                }
            }
            if (columna - 1 >= 0) {
                posicionActual = tablero[fila - 2][columna - 1];
                if (posicionActual != null) {
                    if (posicionActual.EsBlanca() != pieza.EsBlanca() && !(posicionActual instanceof Rey)) {
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
                    if (posicionActual.EsBlanca() != pieza.EsBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila + 1, columna - 2});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila + 1, columna - 2});
                }
            }
            if (fila - 1 >= 0) {
                posicionActual = tablero[fila - 1][columna - 2];
                if (posicionActual != null) {
                    if (posicionActual.EsBlanca() != pieza.EsBlanca() && !(posicionActual instanceof Rey)) {
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
                    if (posicionActual.EsBlanca() != pieza.EsBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila + 1, columna + 2});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila + 1, columna + 2});
                }
            }
            if (fila - 1 >= 0) {
                posicionActual = tablero[fila - 1][columna + 2];
                if (posicionActual != null) {
                    if (posicionActual.EsBlanca() != pieza.EsBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila - 1, columna + 2});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila - 1, columna + 2});
                }
            }
        }

        return new Base(estado).MovimientosValidos(lista, tablero, pieza.EsBlanca());

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
                if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
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
                if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
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
                if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
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
                if (posicionActual.EsBlanca() == pieza.EsBlanca() || posicionActual instanceof Rey) {
                    break;
                }
            }

            lista.add(new int[]{fila, columna, f, c});

            if (posicionActual != null && posicionActual.EsBlanca() != pieza.EsBlanca()) {
                break;
            }

            f--;
            c++;
        }
        return new Base(estado).MovimientosValidos(lista, tablero, pieza.EsBlanca());

    }

    private static List<int[]> movimientosDeRey(Pieza[][] tablero, EstadoTablero estado, int fila, int columna) {

        var base = new Base(estado);

        var lista = new ArrayList<int[]>();

        Pieza pieza = tablero[fila][columna];

        if (fila + 1 < 8) {

            if (tablero[fila + 1][columna] == null || tablero[fila + 1][columna].EsBlanca() != pieza.EsBlanca()) {
                lista.add(new int[]{fila, columna, fila + 1, columna});
            }

            if (columna + 1 < 8) {
                if (tablero[fila + 1][columna + 1] == null || tablero[fila + 1][columna + 1].EsBlanca() != pieza.EsBlanca()) {
                    lista.add(new int[]{fila, columna, fila + 1, columna + 1});
                }
            }

            if (columna - 1 >= 0) {
                if (tablero[fila + 1][columna - 1] == null || tablero[fila + 1][columna - 1].EsBlanca() != pieza.EsBlanca()) {
                    lista.add(new int[]{fila, columna, fila + 1, columna - 1});
                }
            }

        }
        if (fila - 1 >= 0) {

            if (tablero[fila - 1][columna] == null || tablero[fila - 1][columna].EsBlanca() != pieza.EsBlanca()) {
                lista.add(new int[]{fila, columna, fila - 1, columna});
            }

            if (columna + 1 < 8) {
                if (tablero[fila - 1][columna + 1] == null || tablero[fila - 1][columna + 1].EsBlanca() != pieza.EsBlanca()) {
                    lista.add(new int[]{fila, columna, fila - 1, columna + 1});
                }
            }

            if (columna - 1 >= 0) {
                if (tablero[fila - 1][columna - 1] == null || tablero[fila - 1][columna - 1].EsBlanca() != pieza.EsBlanca()) {
                    lista.add(new int[]{fila, columna, fila - 1, columna - 1});
                }
            }

        }

        if (columna + 1 < 8) {
            if (tablero[fila][columna + 1] == null || tablero[fila][columna + 1].EsBlanca() != pieza.EsBlanca()) {
                lista.add(new int[]{fila, columna, fila, columna + 1});
            }

        }
        if (columna - 1 >= 0) {
            if (tablero[fila][columna - 1] == null || tablero[fila][columna - 1].EsBlanca() != pieza.EsBlanca()) {
                lista.add(new int[]{fila, columna, fila, columna - 1});
            }

        }

        if (!base.ReyEnJaque(tablero, pieza.EsBlanca())) {
            if (estado.EnroqueCBlanco && pieza.EsBlanca()) {
                if (columna == 4) {
                    if (tablero[fila][columna + 1] == null && tablero[fila][columna + 2] == null) {
                        tablero[fila][columna + 1] = tablero[fila][columna];//camino del rey
                        tablero[fila][columna] = null;
                        estado.PosicionReyBlanco[1] = columna + 1;
                        if (!base.ReyEnJaque(tablero, pieza.EsBlanca())) {
                            tablero[fila][columna + 2] = tablero[fila][columna + 1];
                            tablero[fila][columna + 1] = null;
                            estado.PosicionReyBlanco[1] = columna + 2;
                            if (!base.ReyEnJaque(tablero, pieza.EsBlanca())) {
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
                estado.PosicionReyBlanco[1] = columna;
            } else if (estado.EnroqueLBlanco && pieza.EsBlanca()) {
                if (columna == 4) {
                    if (tablero[fila][columna - 1] == null && tablero[fila][columna - 2] == null && tablero[fila][columna - 3] == null) {
                        tablero[fila][columna - 1] = tablero[fila][columna];
                        tablero[fila][columna] = null;
                        //estado.PosicionReyBlanco[0] = fila;
                        estado.PosicionReyBlanco[1] = columna - 1;
                        if (!base.ReyEnJaque(tablero, pieza.EsBlanca())) {
                            tablero[fila][columna - 2] = tablero[fila][columna - 1];
                            tablero[fila][columna - 1] = null;
                            // estado.PosicionReyBlanco[0] = fila;
                            estado.PosicionReyBlanco[1] = columna - 2;
                            if (!base.ReyEnJaque(tablero, pieza.EsBlanca())) {
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
                estado.PosicionReyBlanco[1] = columna;
            } else //enroque
            if (estado.EnroqueCNegro && !pieza.EsBlanca()) {
                if (columna == 4) {
                    if (tablero[fila][columna + 1] == null && tablero[fila][columna + 2] == null) {
                        tablero[fila][columna + 1] = tablero[fila][columna];
                        tablero[fila][columna] = null;
                        //estado.PosicionReyNegro[0] = fila;
                        estado.PosicionReyNegro[1] = columna + 1;
                        if (!base.ReyEnJaque(tablero, pieza.EsBlanca())) {
                            tablero[fila][columna + 2] = tablero[fila][columna + 1];
                            tablero[fila][columna + 1] = null;
                            estado.PosicionReyNegro[0] = fila;
                            estado.PosicionReyNegro[1] = columna + 2;
                            if (!base.ReyEnJaque(tablero, pieza.EsBlanca())) {
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
                estado.PosicionReyNegro[1] = columna;
            } else if (estado.EnroqueLNegro && !pieza.EsBlanca()) {
                if (columna == 4) {
                    if (tablero[fila][columna - 1] == null && tablero[fila][columna - 2] == null && tablero[fila][columna - 3] == null) {
                        tablero[fila][columna - 1] = tablero[fila][columna];
                        tablero[fila][columna] = null;
                        //estado.PosicionReyNegro[0] = fila;
                        estado.PosicionReyNegro[1] = columna - 1;
                        if (!base.ReyEnJaque(tablero, pieza.EsBlanca())) {
                            tablero[fila][columna - 2] = tablero[fila][columna - 1];
                            tablero[fila][columna - 1] = null;
                            //estado.PosicionReyNegro[0] = fila;
                            estado.PosicionReyNegro[1] = columna - 2;
                            if (!base.ReyEnJaque(tablero, pieza.EsBlanca())) {
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
                estado.PosicionReyNegro[1] = columna;
            }
        }
        return base.MovimientosValidosRey(lista, tablero, pieza.EsBlanca());
    }

    private static List<int[]> movimientosDePeon(Pieza[][] tablero, EstadoTablero estado, int fila, int columna) {
        int filaDestino;

        Pieza posicionActual;
        Pieza pieza = tablero[fila][columna];

        var lista = new ArrayList<int[]>();

        //avance dos casillas
        if (fila == (pieza.EsBlanca() ? 1 : 6)) {
            if (tablero[pieza.EsBlanca() ? 2 : 5][columna] == null && tablero[pieza.EsBlanca() ? 3 : 4][columna] == null) {
                lista.add(new int[]{fila, columna, pieza.EsBlanca() ? 3 : 4, columna});
            }
        }

        //avance una casilla
        filaDestino = fila + (pieza.EsBlanca() ? 1 : -1);
        if (tablero[filaDestino][columna] == null) {
            if (filaDestino == (pieza.EsBlanca() ? 7 : 0)) {
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
                if (posicionActual.EsBlanca() != pieza.EsBlanca() && !(posicionActual instanceof Rey)) {
                    if (filaDestino == (pieza.EsBlanca() ? 7 : 0)) {
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 1});
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 2});
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 3});
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 4});
                    } else {
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1});
                    }
                }
            } else if (estado.AlPaso && fila == (pieza.EsBlanca() ? 4 : 3)) {
                if (tablero[fila][columna - 1] == estado.PiezaALPaso) {
                    lista.add(new int[]{fila, columna, filaDestino, columna - 1});
                }
            }
        }
        if (columna < 7) {
            posicionActual = tablero[filaDestino][columna + 1];
            if (posicionActual != null) {
                if (posicionActual.EsBlanca() != pieza.EsBlanca() && !(posicionActual instanceof Rey)) {
                    if (filaDestino == (pieza.EsBlanca() ? 7 : 0)) {
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 1});
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 2});
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 3});
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 4});
                    } else {
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1});
                    }
                }
            } else if (estado.AlPaso && fila == (pieza.EsBlanca() ? 4 : 3)) {
                if (tablero[fila][columna + 1] == estado.PiezaALPaso) {
                    lista.add(new int[]{fila, columna, filaDestino, columna + 1});
                }
            }
        }

        return new Base(estado).MovimientosValidos(lista, tablero, pieza.EsBlanca());
    }

}
