package com.wolf.carlitos;

import static com.wolf.carlitos.Constantes.*;

public class Pieza {

    public boolean esBlanca;
    public String nombre;
    public int valor;
    public int tipo;

    public Pieza(boolean esBlanca, int tipo) {
        this.esBlanca = esBlanca;
        this.tipo = tipo;
        switch (tipo) {
            case PEON:
                valor = esBlanca ? 100 : -100;
                nombre = esBlanca ? "P" : "p";
                break;
            case CABALLO:
                valor = esBlanca ? 320 : -320;
                nombre = esBlanca ? "C" : "c";
                break;
            case ALFIL:
                valor = esBlanca ? 330 : -330;
                nombre = esBlanca ? "A" : "a";
                break;
            case TORRE:
                valor = esBlanca ? 500 : -500;
                nombre = esBlanca ? "T" : "t";
                break;
            case DAMA:
                valor = esBlanca ? 900 : -900;
                nombre = esBlanca ? "D" : "d";
                break;
            case REY:
                valor = esBlanca ? 10000 : -10000;
                nombre = esBlanca ? "R" : "r";
                break;

        }
    }

}