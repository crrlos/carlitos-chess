package com.wolf.carlitos.Piezas;

import com.wolf.carlitos.Juego;
import java.util.ArrayList;
import java.util.List;

/**
 * Dama
 */
public class Dama extends Base  implements Pieza{
    public boolean esBlanco;
    public Dama(boolean bando){
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