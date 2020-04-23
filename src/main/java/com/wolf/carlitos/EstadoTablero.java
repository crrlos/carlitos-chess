
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
    public  boolean reyEnJaque;
    
    public  Pieza piezaALPaso;
    public  Pieza piezaCapturada;
    public Pieza piezaJaque;
    
    public  int tipoMovimiento = -1;
    
    public int[] posicionReyBlanco = new int[]{0,4};
    public int[] posicionReyNegro = new int[]{7,4};
    
    
    public List<Trayectoria> trayectorias =  new ArrayList<>();
    
    
    @Override
    public EstadoTablero clone() throws
                   CloneNotSupportedException 
    {
        var estadoTablero = (EstadoTablero)super.clone();
        estadoTablero.trayectorias = new ArrayList<>();

        trayectorias.forEach(t -> {

            Trayectoria tra = new Trayectoria(t.pieza,t.posicion[0],t.posicion[1],t.trayectoria);

            tra.piezasAtacadas = new ArrayList<Pieza>();
            tra.piezasAtacadas.addAll(t.piezasAtacadas);

            estadoTablero.trayectorias.add(tra);

        });
        return estadoTablero;
    } 
}
