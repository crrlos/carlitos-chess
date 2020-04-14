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
    public boolean EsBlanca() {
        return esBlanco;
    }

    @Override
    public String Nombre() {
        return esBlanco ? "R" : "r";
    }

    @Override
    public int Valor() {
        return esBlanco ? 10 : -10;
    }

}
