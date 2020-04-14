package com.wolf.carlitos.Piezas;

import com.wolf.carlitos.EstadoTablero;
import com.wolf.carlitos.Juego;
import java.util.ArrayList;
import java.util.List;

public class Peon extends Base implements Pieza {

    public boolean esBlanco;

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
