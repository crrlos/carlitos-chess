package com.wolf.carlitos.Piezas;

import com.wolf.carlitos.Juego;
import java.util.ArrayList;
import java.util.List;

/**
 * Caballo
 */
public class Caballo extends Base implements Pieza{
    public boolean esBlanco;
   public Caballo(boolean bando){
        this.esBlanco = bando;
    }


    @Override
    public boolean EsBlanca() {
        return esBlanco;
    }

    @Override
    public String Nombre() {
        return esBlanco ? "C" : "c";
    }
    @Override
    public int Valor() {
        return esBlanco ? 3 : -3;
    }

    
}