package com.wolf.carlitos.Piezas;

/**
 * Alfil
 */
public class Alfil implements Pieza {

    private final boolean esBlanco;

    public Alfil(boolean bando) {
        this.esBlanco = bando;
    }

    @Override
    public boolean esBlanca() {
        return esBlanco;
    }

    @Override
    public String nombre() {
        return esBlanco ? "A" : "a";
    }

    @Override
    public int valor() {
        return esBlanco ? 3 : -3;
    }

}
