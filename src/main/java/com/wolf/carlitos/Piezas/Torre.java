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
    public boolean EsBlanca() {
        return esBlanco;
    }

    @Override
    public String Nombre() {
        return esBlanco ? "T" : "t";
    }

    @Override
    public int Valor() {
        return esBlanco ? 5 : -5;
    }

}
