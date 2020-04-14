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
    public boolean EsBlanca() {
        return esBlanco;
    }

    @Override
    public String Nombre() {
        return esBlanco ? "D" : "d";
    }

    @Override
    public int Valor() {
        return esBlanco ? 9 : -9;
    }
}
