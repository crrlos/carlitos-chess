package com.wolf.carlitos.Piezas;

import com.wolf.carlitos.EstadoTablero;
import com.wolf.carlitos.Juego;
import java.util.ArrayList;
import java.util.List;

public class Peon extends Base implements Pieza {

    public boolean esBlanco;

    public Peon(boolean bando) {
        this.esBlanco = bando;
    }

    @Override
    public List<int[]> ObtenerMovimientos(int fila, int columna) {
        var tablero = Juego.tablero;
        int filaDestino;

        Pieza posicionActual;
        var lista = new ArrayList<int[]>();

        //avance dos casillas
        if (fila == (esBlanco ? 1 : 6)) {
            if (tablero[esBlanco ? 2 : 5][columna] == null && tablero[esBlanco ? 3 : 4][columna] == null) {
                lista.add(new int[]{fila, columna, esBlanco ? 3 : 4, columna});
            }

//            //al paso
//            Juego.estadoTablero.AlPaso = true;
//            Juego.estadoTablero.PiezaALPaso = tablero[fila][columna];
        }

        //avance una casilla
        filaDestino = fila + (esBlanco ? 1 : -1);
        if (tablero[filaDestino][columna] == null) {
            if (filaDestino == (esBlanco ? 7 : 0)) {
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
                if (posicionActual.EsBlanca() != esBlanco && !(posicionActual instanceof Rey)) {
                    if (filaDestino == (esBlanco ? 7 : 0)) {
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 1});
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 2});
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 3});
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 4});
                    } else {
                        lista.add(new int[]{fila, columna, filaDestino, columna - 1});
                    }
                }
            }else if(Juego.estadoTablero.AlPaso && fila == (esBlanco ? 4 : 3)){
                if(tablero[fila][columna - 1] == Juego.estadoTablero.PiezaALPaso){
                    lista.add(new int[]{fila, columna, filaDestino, columna - 1});
                }
            }
        }
        if (columna < 7) {
            posicionActual = tablero[filaDestino][columna + 1];
            if (posicionActual != null) {
                if (posicionActual.EsBlanca() != esBlanco && !(posicionActual instanceof Rey)) {
                    if (filaDestino == (esBlanco ? 7 : 0)) {
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 1});
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 2});
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 3});
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 4});
                    } else {
                        lista.add(new int[]{fila, columna, filaDestino, columna + 1});
                    }
                }
            }else if(Juego.estadoTablero.AlPaso && fila == (esBlanco ? 4 : 3)){
                if(tablero[fila][columna + 1] == Juego.estadoTablero.PiezaALPaso){
                    lista.add(new int[]{fila, columna, filaDestino, columna + 1});
                }
            }
        }

        return MovimientosValidos(lista, tablero, esBlanco);

    }

    @Override
    public boolean EsBlanca() {
        return esBlanco;
    }

    @Override
    public String Nombre() {
        return esBlanco ? "P" : "p";
    }

    @Override
    public int Valor() {
        return esBlanco ? 1 : -1;
    }
}
