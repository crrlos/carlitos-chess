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
    public List<int[]> ObtenerMovimientos(Pieza[][] tablero, int[] posicion) {
        int filaOrigen = posicion[0];
        int columnaOrigen = posicion[1];
        Pieza posicionActual;
        var lista = new ArrayList<int[]>();

        var f = filaOrigen + 1;
        var c = columnaOrigen + 1;
        while(f < 8 && c < 8){
            posicionActual = tablero[f][c];
            
            if(posicionActual != null)
                if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                    break;
            
            lista.add( new int[]{filaOrigen,columnaOrigen,f,c});
            f++; c++;
        }

         f = filaOrigen -1;
         c = columnaOrigen -1;
        while(f >= 0 && c >=0){
            
            posicionActual = tablero[f][c];
            
            if(posicionActual != null)
                if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                    break;
            
            lista.add( new int[]{filaOrigen,columnaOrigen,f,c});
            f--;c--;
        }

         f = filaOrigen +1;
         c = columnaOrigen -1;
        while(f < 8 && c >=0){
            posicionActual = tablero[f][c];
            
            if(posicionActual != null)
                if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                    break;
            
            lista.add( new int[]{filaOrigen,columnaOrigen,f,c});
            f++;c--;
        }

         f = filaOrigen -1;
         c = columnaOrigen +1;
        while(f >= 0 && c < 8){
            posicionActual = tablero[f][c];
            
            if(posicionActual != null)
                if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                    break;
            
            lista.add( new int[]{filaOrigen,columnaOrigen,f,c});
            f--;c++;
        }
        System.out.print("Alfil " + esBlanco + " genero: ");
        for(var pos: lista){
            System.out.print(Juego.juego.ConvertirANotacion(pos));
        }
        System.out.println("");
        return MovimientosValidos(lista, tablero, esBlanco);
    }

    

    @Override
    public boolean EsBlanca() {
        return esBlanco;
    }

    @Override
    public String Nombre() {
        return esBlanco ? "A" : "a";
    }


    
}