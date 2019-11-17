
package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.Pieza;

public  class EstadoTablero  implements Cloneable{
    public static int contador = 0;
    public static int capturas = 0;
    public  boolean EnroqueCBlanco = true;
    public  boolean EnroqueLBlanco = true;
    public  boolean EnroqueCNegro = true;
    public  boolean EnroqueLNegro = true;
    public  boolean AlPaso;
    public boolean TurnoBlanco = true;
    public  Pieza PiezaALPaso;
    public  Pieza PiezaCapturada;
    public  int TipoMovimiento = -1;
    public int[] PosicionReyBlanco = new int[]{0,4};
    public int[] PosicionReyNegro  = new int[]{7,4};
    @Override
    public EstadoTablero clone() throws
                   CloneNotSupportedException 
    { 
        return (EstadoTablero)super.clone(); 
    } 
}
