package com.wolf.carlitos.Piezas;

public class Peon implements Pieza {

    private final boolean esBlanco;

    public Peon(boolean bando) {
        this.esBlanco = bando;
    }

    @Override
    public boolean esBlanca() {
        return esBlanco;
    }

    @Override
    public String nombre() {
        return esBlanco ? "P" : "p";
    }

    @Override
    public int valor() {
        return esBlanco ? 100 : -100;
    }
}
