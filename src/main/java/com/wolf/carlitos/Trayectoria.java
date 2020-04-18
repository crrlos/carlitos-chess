/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.Pieza;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carlos
 */
public class Trayectoria {
    
    public static enum TRAYECTORIA {Diagonal, Recta, Ninguna}
    
    public Pieza pieza;
    public List<Pieza> piezasAtacadas = new ArrayList<>();
    public int[] posicion = new int[2];
    public TRAYECTORIA trayectoria;
    

    public Trayectoria(Pieza p,int x, int y,TRAYECTORIA trayectoria) {
        this.pieza = p;
        posicion[0] = x;
        posicion[1] = y; 
        this.trayectoria = trayectoria;
    }
    
    
    
}
