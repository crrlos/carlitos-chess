package com.wolf.carlitos.Piezas;

public class Peon implements Pieza {

    private final boolean esBlanco;

    public Peon(boolean bando) {
        this.esBlanco = bando;
    }

    @Override
    public boolean EsBlanca() {
        return esBlanco;
    }

    @Override
    public String Nombre() {
        return esBlanco ? "P" : "p";
    }

    @Override
    public int Valor() {
        return esBlanco ? 1 : -1;
    }
}
