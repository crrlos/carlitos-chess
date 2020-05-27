package com.wolf.carlitos;


import static com.wolf.carlitos.Bitboard.add;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Tablero.color;
import static com.wolf.carlitos.Tablero.tablero;


public class Juego {

    private Tablero tab = new Tablero();

    public Juego() {
        Tablero.estados.push(POSICION_INICIAL);
    }

    public void establecerPosicion(String... movimientos) {
        tab.setHistoria(movimientos);
    }

    public void setFen(String fen) {
       tab.setFen(fen);
    }

    public void perft(int n) {
        var search = new Search(tablero, color);
        search.perft(n);
    }

    public String mover(int n){
        var search = new Search(tablero, color);
        return Utilidades.convertirANotacion(search.search(n));
    }

    public void evaluarPosicion() {
       // System.out.println(Evaluar.evaluar(0));
    }
}