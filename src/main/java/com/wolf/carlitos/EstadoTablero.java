
package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.Pieza;
import java.util.ArrayList;
import java.util.List;


public  class EstadoTablero  implements Cloneable{
    
    public  boolean enroqueCBlanco = true;
    public  boolean enroqueLBlanco = true;
    public  boolean enroqueCNegro = true;
    public  boolean enroqueLNegro = true;
    public  boolean alPaso;
    public  boolean turnoBlanco = true;
    
    public  Pieza piezaALPaso;
    public  Pieza piezaCapturada;
    
    public  int tipoMovimiento = -1;
    
    public int[] posicionReyBlanco = new int[]{0,4};
    public int[] posicionReyNegro = new int[]{7,4};

    @Override
    public EstadoTablero clone() throws
                   CloneNotSupportedException 
    {
        return (EstadoTablero)super.clone();
    } 
}
