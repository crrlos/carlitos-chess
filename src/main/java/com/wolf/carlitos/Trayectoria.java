/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.Pieza;

/**
 *
 * @author carlos
 */
public class Trayectoria {
    
    public Pieza pieza;
    public int piezasAtacadas;
    public int[] posicion = new int[2];
    

    public Trayectoria(Pieza p,int x, int y) {
        this.pieza = p;
        posicion[0] = x;
        posicion[1] = y; 
    }
    
    
    
}
