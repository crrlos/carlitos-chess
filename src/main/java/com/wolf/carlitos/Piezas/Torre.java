package com.wolf.carlitos.Piezas;

/**
 * Torre
 */
public class Torre implements Pieza {

    private final boolean esBlanco;

    public Torre(boolean bando) {
        this.esBlanco = bando;
    }

    @Override
    public boolean esBlanca() {
        return esBlanco;
    }

    @Override
    public String nombre() {
        return esBlanco ? "T" : "t";
    }

    @Override
    public int valor() {
        return esBlanco ? 500 : -500;
    }

}
