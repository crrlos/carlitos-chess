package com.wolf.carlitos.Piezas;

import com.wolf.carlitos.EstadoTablero;
import com.wolf.carlitos.Juego;
import java.util.ArrayList;
import java.util.List;

/**
 * Rey
 */
public class Rey extends Base implements Pieza {

    public boolean esBlanco;

    public Rey(boolean bando) {
        this.esBlanco = bando;
    }

    @Override
    public List<int[]> ObtenerMovimientos(int fila, int columna) {
        var tablero = Juego.tablero;

        var lista = new ArrayList<int[]>();

        if (fila + 1 < 8) {

            if (tablero[fila + 1][columna] == null || tablero[fila + 1][columna].EsBlanca() != this.EsBlanca()) {
                lista.add(new int[]{fila, columna, fila + 1, columna});
            }

            if (columna + 1 < 8) {
                if (tablero[fila + 1][columna + 1] == null || tablero[fila + 1][columna + 1].EsBlanca() != this.EsBlanca()) {
                    lista.add(new int[]{fila, columna, fila + 1, columna + 1});
                }
            }

            if (columna - 1 >= 0) {
                if (tablero[fila + 1][columna - 1] == null || tablero[fila + 1][columna - 1].EsBlanca() != this.EsBlanca()) {
                    lista.add(new int[]{fila, columna, fila + 1, columna - 1});
                }
            }

        }
        if (fila - 1 >= 0) {

            if (tablero[fila - 1][columna] == null || tablero[fila - 1][columna].EsBlanca() != this.EsBlanca()) {
                lista.add(new int[]{fila, columna, fila - 1, columna});
            }

            if (columna + 1 < 8) {
                if (tablero[fila - 1][columna + 1] == null || tablero[fila - 1][columna + 1].EsBlanca() != this.EsBlanca()) {
                    lista.add(new int[]{fila, columna, fila - 1, columna + 1});
                }
            }

            if (columna - 1 >= 0) {
                if (tablero[fila - 1][columna - 1] == null || tablero[fila - 1][columna - 1].EsBlanca() != this.EsBlanca()) {
                    lista.add(new int[]{fila, columna, fila - 1, columna - 1});
                }
            }

        }

        if (columna + 1 < 8) {
            if (tablero[fila][columna + 1] == null || tablero[fila][columna + 1].EsBlanca() != this.EsBlanca()) {
                lista.add(new int[]{fila, columna, fila, columna + 1});
            }

        }
        if (columna - 1 >= 0) {
            if (tablero[fila][columna - 1] == null || tablero[fila][columna - 1].EsBlanca() != this.EsBlanca()) {
                lista.add(new int[]{fila, columna, fila, columna - 1});
            }

        }

        if (!ReyEnJaque(tablero, esBlanco)) {
            if (Juego.estadoTablero.EnroqueCBlanco && esBlanco) {
                if (columna == 4) {
                    if (tablero[fila][columna + 1] == null && tablero[fila][columna + 2] == null) {
                        tablero[fila][columna + 1] = tablero[fila][columna];//camino del rey
                        tablero[fila][columna] = null;
                        //Juego.estadoTablero.PosicionReyBlanco[0] = fila;
                        Juego.estadoTablero.PosicionReyBlanco[1] = columna + 1;
                        if (!ReyEnJaque(tablero, esBlanco)) {
                            tablero[fila][columna + 2] = tablero[fila][columna + 1];
                            tablero[fila][columna + 1] = null;
                            //Juego.estadoTablero.PosicionReyBlanco[0] = fila;
                            Juego.estadoTablero.PosicionReyBlanco[1] = columna + 2;
                            if (!ReyEnJaque(tablero, esBlanco)) {
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
                Juego.estadoTablero.PosicionReyBlanco[0] = fila;
                Juego.estadoTablero.PosicionReyBlanco[1] = columna;
            }

            if (Juego.estadoTablero.EnroqueLBlanco && esBlanco) {
                if (columna == 4) {
                    if (tablero[fila][columna - 1] == null && tablero[fila][columna - 2] == null && tablero[fila][columna - 3] == null) {
                        tablero[fila][columna - 1] = tablero[fila][columna];
                        tablero[fila][columna] = null;
                        //Juego.estadoTablero.PosicionReyBlanco[0] = fila;
                        Juego.estadoTablero.PosicionReyBlanco[1] = columna - 1;
                        if (!ReyEnJaque(tablero, esBlanco)) {
                            tablero[fila][columna - 2] = tablero[fila][columna - 1];
                            tablero[fila][columna - 1] = null;
                            // Juego.estadoTablero.PosicionReyBlanco[0] = fila;
                            Juego.estadoTablero.PosicionReyBlanco[1] = columna - 2;
                            if (!ReyEnJaque(tablero, esBlanco)) {
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
                Juego.estadoTablero.PosicionReyBlanco[0] = fila;
                Juego.estadoTablero.PosicionReyBlanco[1] = columna;
            }
            //enroque
            if (Juego.estadoTablero.EnroqueCNegro && !esBlanco) {
                if (columna == 4) {
                    if (tablero[fila][columna + 1] == null && tablero[fila][columna + 2] == null) {
                        tablero[fila][columna + 1] = tablero[fila][columna];
                        tablero[fila][columna] = null;
                        //Juego.estadoTablero.PosicionReyNegro[0] = fila;
                        Juego.estadoTablero.PosicionReyNegro[1] = columna + 1;
                        if (!ReyEnJaque(tablero, esBlanco)) {
                            tablero[fila][columna + 2] = tablero[fila][columna + 1];
                            tablero[fila][columna + 1] = null;
                            Juego.estadoTablero.PosicionReyNegro[0] = fila;
                            Juego.estadoTablero.PosicionReyNegro[1] = columna + 2;
                            if (!ReyEnJaque(tablero, esBlanco)) {
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
                Juego.estadoTablero.PosicionReyNegro[0] = fila;
                Juego.estadoTablero.PosicionReyNegro[1] = columna;
            }

            if (Juego.estadoTablero.EnroqueLNegro && !esBlanco) {
                if (columna == 4) {
                    if (tablero[fila][columna - 1] == null && tablero[fila][columna - 2] == null && tablero[fila][columna - 3] == null) {
                        tablero[fila][columna - 1] = tablero[fila][columna];
                        tablero[fila][columna] = null;
                        //Juego.estadoTablero.PosicionReyNegro[0] = fila;
                        Juego.estadoTablero.PosicionReyNegro[1] = columna - 1;
                        if (!ReyEnJaque(tablero, esBlanco)) {
                            tablero[fila][columna - 2] = tablero[fila][columna - 1];
                            tablero[fila][columna - 1] = null;
                            //Juego.estadoTablero.PosicionReyNegro[0] = fila;
                            Juego.estadoTablero.PosicionReyNegro[1] = columna - 2;
                            if (!ReyEnJaque(tablero, esBlanco)) {
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
                Juego.estadoTablero.PosicionReyNegro[0] = fila;
                Juego.estadoTablero.PosicionReyNegro[1] = columna;
            }
        }

        return this.MovimientosValidos(lista, tablero, esBlanco);
    }

    @Override
    public List<int[]> MovimientosValidos(List<int[]> movimientos, Pieza[][] tablero, boolean blanco) {
        var iterator = movimientos.iterator();
        while (iterator.hasNext()) {
            var movimiento = iterator.next();
            var fo = movimiento[0];
            var co = movimiento[1];
            var fd = movimiento[2];
            var cd = movimiento[3];
            Pieza piezaActual = tablero[fo][co];
            Pieza piezaDestino = tablero[fd][cd];

            tablero[fo][co] = null;
            tablero[fd][cd] = piezaActual;
            if (blanco) {
                Juego.estadoTablero.PosicionReyBlanco[0] = fd;
                Juego.estadoTablero.PosicionReyBlanco[1] = cd;
            } else {
                Juego.estadoTablero.PosicionReyNegro[0] = fd;
                Juego.estadoTablero.PosicionReyNegro[1] = cd;
            }

            var jaque = ReyEnJaque(tablero, blanco);

            if (blanco) {
                Juego.estadoTablero.PosicionReyBlanco[0] = fo;
                Juego.estadoTablero.PosicionReyBlanco[1] = co;
            } else {
                Juego.estadoTablero.PosicionReyNegro[0] = fo;
                Juego.estadoTablero.PosicionReyNegro[1] = co;
            }

            tablero[fd][cd] = piezaDestino;
            tablero[fo][co] = piezaActual;
            //Juego.ImprimirPosicicion();
            if (jaque) {
                iterator.remove();
            }
        }
        return movimientos;
    }

    @Override
    public boolean EsBlanca() {
        return esBlanco;
    }

    @Override
    public String Nombre() {
        return esBlanco ? "R" : "r";
    }

    @Override
    public int Valor() {
        return esBlanco ? 10 : -10;
    }

}
