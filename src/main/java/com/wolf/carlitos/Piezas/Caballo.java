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
    public boolean esBlanca() {
        return esBlanco;
    }

    @Override
    public String nombre() {
        return esBlanco ? "C" : "c";
    }

    @Override
    public int valor() {
        return esBlanco ? 320 : -320;
    }

}
