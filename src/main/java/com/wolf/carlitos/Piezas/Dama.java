package com.wolf.carlitos.Piezas;

/**
 * Dama
 */
public class Dama implements Pieza {

    private final boolean esBlanco;

    public Dama(boolean bando) {
        this.esBlanco = bando;
    }

    @Override
    public boolean esBlanca() {
        return esBlanco;
    }

    @Override
    public String nombre() {
        return esBlanco ? "D" : "d";
    }

    @Override
    public int valor() {
        return esBlanco ? 900 : -900;
    }
}
