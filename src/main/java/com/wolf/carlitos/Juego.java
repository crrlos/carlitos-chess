package com.wolf.carlitos;

import static com.wolf.carlitos.Utilidades.convertirANotacion;

public class Juego {

    private final Tablero tablero;
    private final Search search;

    public Juego(){
        this.tablero = new Tablero();
        this.search = new Search(tablero);
    }

    public void setHistoria(String... movimientos) {
        tablero.setHistoria(movimientos);
    }

    public void setFen(String fen) {
       tablero.setFen(fen);
    }

    public void perft(int n) {
        search.perft(n);
    }

    public String mover(int n){
        return convertirANotacion(search.search(n));
    }

    public void evaluarPosicion() {
       // System.out.println(Evaluar.evaluar(0));
    }
}