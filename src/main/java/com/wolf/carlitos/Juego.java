package com.wolf.carlitos;

import static com.wolf.carlitos.Utilidades.convertirANotacion;

public class Juego {

    private final Tablero tablero = new Tablero();

    public void setHistoria(String... movimientos) {
        tablero.setHistoria(movimientos);
    }

    public void setFen(String fen) {
       tablero.setFen(fen);
    }

    public void perft(int n) {
        var perft = new Perft();
        perft.perft(n);
    }

    public String mover(int n){
        var search = new Search(tablero);
        return convertirANotacion(search.search(n));
    }

    public void evaluarPosicion() {
       // System.out.println(Evaluar.evaluar(0));
    }
}