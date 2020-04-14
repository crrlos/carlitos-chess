
package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.Pieza;
import java.util.HashMap;


public  class EstadoTablero  implements Cloneable{
    
    public static HashMap<String,String> map = new HashMap<>();
    
    public  boolean DEBUG = false;
    public  boolean EnroqueCBlanco = true;
    public  boolean EnroqueLBlanco = true;
    public  boolean EnroqueCNegro = true;
    public  boolean EnroqueLNegro = true;
    public  boolean AlPaso;
    public  boolean TurnoBlanco = true;
    
    public  Pieza PiezaALPaso;
    public  Pieza PiezaCapturada;
    
    public  int TipoMovimiento = -1;
    public  int capturas = 0;
    
    public int[] PosicionReyBlanco = new int[]{0,4};
    public int[] PosicionReyNegro  = new int[]{7,4};
    
    @Override
    public EstadoTablero clone() throws
                   CloneNotSupportedException 
    { 
        return (EstadoTablero)super.clone(); 
    } 
}
