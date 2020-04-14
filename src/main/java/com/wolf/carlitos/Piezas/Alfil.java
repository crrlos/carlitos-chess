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
    public boolean EsBlanca() {
        return esBlanco;
    }

    @Override
    public String Nombre() {
        return esBlanco ? "A" : "a";
    }

    @Override
    public int Valor() {
        return esBlanco ? 3 : -3;
    }

}
