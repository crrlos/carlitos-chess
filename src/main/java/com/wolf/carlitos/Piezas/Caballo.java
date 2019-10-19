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
    public List<int[]> ObtenerMovimientos(Pieza[][] tablero, int[] posicion) {
        int filaOrigen = posicion[0];
        int columnaOrigen = posicion[1];
        Pieza posicionActual;

        var lista = new ArrayList<int[]>();

        if(filaOrigen + 2 < 8){
            if(columnaOrigen + 1 < 8){
                posicionActual = tablero[filaOrigen + 2][columnaOrigen+1];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                        lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen +2,columnaOrigen +1});
                }
                else
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen +2,columnaOrigen +1});
            }
            if(columnaOrigen - 1 >= 0){
            posicionActual = tablero[filaOrigen + 2][columnaOrigen-1];
            if(posicionActual != null){
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen +2,columnaOrigen -1});
            }
            else
                lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen +2,columnaOrigen -1});
        }
        }

        if(filaOrigen - 2 >=0 ){
            if(columnaOrigen + 1 < 8){
                posicionActual = tablero[filaOrigen - 2][columnaOrigen+1];
                if(posicionActual != null){
                    if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                        lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen -2,columnaOrigen +1});
                }
                else
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen -2,columnaOrigen +1});
            }
            if(columnaOrigen - 1 >= 0){
                posicionActual = tablero[filaOrigen - 2][columnaOrigen-1];
            if(posicionActual != null){
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen -2,columnaOrigen -1});
            }
            else
                lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen -2,columnaOrigen -1});
        }
        }

        if(columnaOrigen - 2 >= 0){
            if(filaOrigen + 1 < 8){
                posicionActual = tablero[filaOrigen + 1][columnaOrigen -2];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                        lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen +1,columnaOrigen -2});
                }
                else
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen +1,columnaOrigen -2});
            }
            if(filaOrigen - 1 >=0 ){
                posicionActual =tablero[filaOrigen - 1][columnaOrigen- 2];
            if(posicionActual != null){
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen -1,columnaOrigen -2});
            }
            else
                lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen -1,columnaOrigen -2});
        }
        }
        if(columnaOrigen + 2 < 8){
            if(filaOrigen + 1 < 8){
                posicionActual = tablero[filaOrigen + 1][columnaOrigen +2];
                if(posicionActual != null){
                    if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                         lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen +1,columnaOrigen +2});
                }
                else
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen +1,columnaOrigen +2});
            }
            if(filaOrigen - 1 >=0 ){
                posicionActual = tablero[filaOrigen - 1][columnaOrigen+ 2];
            if(posicionActual != null){
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen -1,columnaOrigen +2});
            }
            else
                lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen -1,columnaOrigen +2});
        }
        }
        System.out.print("Caballo " + esBlanco + " genero: ");
        lista.forEach((pos) -> {
            System.out.print(Juego.juego.ConvertirANotacion(pos) + " ");
        });
        System.out.println("");
        var mValidos =  MovimientosValidos(lista, tablero, esBlanco);
        System.out.print("Caballo " + esBlanco + " genero: ");
        mValidos.forEach((pos) -> {
            System.out.print(Juego.juego.ConvertirANotacion(pos) + " ");
        });
        System.out.println("");
            return mValidos;
    }


    @Override
    public boolean EsBlanca() {
        return esBlanco;
    }

    @Override
    public String Nombre() {
        return esBlanco ? "C" : "c";
    }

    
}