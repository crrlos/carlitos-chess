package com.wolf.carlitos;

import static com.wolf.carlitos.Utilidades.convertirANotacion;

public class Engine {

    private final Posicion posicion;
    private final Search search;

    public Engine() {
        posicion = new Posicion();
        search = new Search(posicion);
    }

    public void setMovesHistory(String... movs) {
        posicion.setHistory(movs);
    }

    public void setFen(String fen) {
        posicion.setFen(fen);
    }

    public void perft(int n) {
        var perft = new Perft(posicion);
        perft.perft(n);
    }

    public String move(int depth) {
        return convertirANotacion(search.search(depth));
    }
}