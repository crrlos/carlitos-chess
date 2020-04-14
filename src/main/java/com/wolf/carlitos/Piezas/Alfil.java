package com.wolf.carlitos.Piezas;

import com.wolf.carlitos.Juego;
import java.util.ArrayList;
import java.util.List;

/**
 * Alfil
 */
public class Alfil  extends Base implements Pieza{
    public boolean esBlanco;
    public Alfil(boolean bando){
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