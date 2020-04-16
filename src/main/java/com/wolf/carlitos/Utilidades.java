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
public class Utilidades {
    
    public static  void ImprimirPosicicion(Pieza[][] tablero){
        for (int i = 7; i >=0 ; i--) {
            for (int j = 0; j < 8 ; j++) {
                var pieza = tablero[i][j];
                System.out.print(pieza != null ? pieza.Nombre() : " ");
                System.out.print("|");
            }
            System.out.println();
        }
        System.out.println("");
    }
    
     public static String ConvertirANotacion(int[] movimiento){
         
          final String FILAS = "12345678";
          final String COLUMNAS = "abcdefgh";
         
        var mov = COLUMNAS.charAt(movimiento[1]) + "" + FILAS.charAt(movimiento[0]) +
                  COLUMNAS.charAt(movimiento[3]) + "" + FILAS.charAt(movimiento[2]);
        if(movimiento.length == 5){
            switch(movimiento[4]){
                case 1: return mov + "q";
                case 2: return mov + "r";
                case 3: return mov + "n";
                case 4: return mov + "b";
            }
        }
        return mov;
    }
    
}
