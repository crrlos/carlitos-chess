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
