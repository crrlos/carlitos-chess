package com.wolf.carlitos.Piezas;

/**
 * Caballo
 */
public class Caballo implements Pieza {

    private final boolean esBlanco;

    public Caballo(boolean bando) {
        this.esBlanco = bando;
    }

    @Override
    public boolean EsBlanca() {
        return esBlanco;
    }

    @Override
    public String Nombre() {
        return esBlanco ? "C" : "c";
    }

    @Override
    public int Valor() {
        return esBlanco ? 3 : -3;
    }

}
