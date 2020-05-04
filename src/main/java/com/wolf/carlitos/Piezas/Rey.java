package com.wolf.carlitos.Piezas;



/**
 * Rey
 */
public class Rey  implements Pieza {

    private final boolean esBlanco;

    public Rey(boolean bando) {
        this.esBlanco = bando;
    }
    @Override
    public boolean esBlanca() {
        return esBlanco;
    }

    @Override
    public String nombre() {
        return esBlanco ? "R" : "r";
    }

    @Override
    public int valor() {
        return esBlanco ? 20_000 : -20_000;
    }

}
