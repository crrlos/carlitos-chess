package com.wolf.carlitos.Piezas;

import com.wolf.carlitos.EstadoTablero;
import com.wolf.carlitos.Juego;
import java.util.ArrayList;
import java.util.List;

/**
 * Rey
 */
public class Rey extends Base implements Pieza{
    public boolean esBlanco;
    public Rey(boolean bando){
        this.esBlanco = bando;
    }
    @Override
    public List<int[]> ObtenerMovimientos(Pieza[][] tablero, int[] posicion) {
        int filaOrigen = posicion[0];
        int columnaOrigen = posicion[1];

        var lista = new ArrayList<int[]>();

        if(filaOrigen + 1 < 8){

            if(tablero[filaOrigen + 1][columnaOrigen] == null)
                lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen});
            else
                if(tablero[filaOrigen + 1][columnaOrigen].EsBlanca() != this.EsBlanca())
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen});
            if(columnaOrigen + 1 < 8)        
            if(tablero[filaOrigen + 1][columnaOrigen + 1] == null)
                lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen + 1});
            else
                if(tablero[filaOrigen + 1][columnaOrigen + 1 ].EsBlanca() != this.EsBlanca())
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen +1});

            if(columnaOrigen - 1 >= 0)        
            if(tablero[filaOrigen + 1][columnaOrigen - 1] == null)
                lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen - 1});
            else
                if(tablero[filaOrigen + 1][columnaOrigen - 1 ].EsBlanca() != this.EsBlanca())
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen -1});
        }
        if(filaOrigen - 1 >= 0){

            if(tablero[filaOrigen - 1][columnaOrigen] == null)
                lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen});
            else
                if(tablero[filaOrigen - 1][columnaOrigen].EsBlanca() != this.EsBlanca())
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen});
            if(columnaOrigen + 1 < 8)        
            if(tablero[filaOrigen - 1][columnaOrigen + 1] == null)
                lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen + 1});
            else
                if(tablero[filaOrigen - 1][columnaOrigen + 1 ].EsBlanca() != this.EsBlanca())
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen +1});

            if(columnaOrigen - 1 >= 0)        
                if(tablero[filaOrigen - 1][columnaOrigen - 1] == null)
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen - 1});
                else
                    if(tablero[filaOrigen - 1][columnaOrigen - 1 ].EsBlanca() != this.EsBlanca())
                        lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen -1});
            
        }

        if(columnaOrigen + 1 < 8){
            if(tablero[filaOrigen ][columnaOrigen + 1] == null)
                lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen,columnaOrigen + 1});
            else
                if(tablero[filaOrigen][columnaOrigen + 1 ].EsBlanca() != this.EsBlanca())
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen,columnaOrigen +1});
        }
        if(columnaOrigen - 1 >= 0){
            if(tablero[filaOrigen ][columnaOrigen - 1] == null)
                lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen,columnaOrigen - 1});
            else
                if(tablero[filaOrigen][columnaOrigen -1 ].EsBlanca() != this.EsBlanca())
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen,columnaOrigen -1});
        }
        
        //enroque
        if(EstadoTablero.EnroqueCBlanco && esBlanco){
            if(tablero[filaOrigen][columnaOrigen + 1] == null &&  tablero[filaOrigen][columnaOrigen + 2] == null){
                tablero[filaOrigen][columnaOrigen + 1] = tablero[filaOrigen][columnaOrigen];
                tablero[filaOrigen][columnaOrigen] = null;
                if(!ReyEnJaque(tablero, esBlanco)){
                    tablero[filaOrigen][columnaOrigen + 2] = tablero[filaOrigen][columnaOrigen + 1];
                    tablero[filaOrigen][columnaOrigen + 1] = null;
                    if(!ReyEnJaque(tablero, esBlanco))
                    {
                        lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen,columnaOrigen + 2});
                        tablero[filaOrigen][columnaOrigen] = tablero[filaOrigen][columnaOrigen + 2];
                        tablero[filaOrigen][columnaOrigen + 2] = null;
                    }
                }
            }
        }
        
        if(EstadoTablero.EnroqueLBlanco && esBlanco){
            if(tablero[filaOrigen][columnaOrigen - 1] == null &&  tablero[filaOrigen][columnaOrigen -2] == null){
                tablero[filaOrigen][columnaOrigen - 1] = tablero[filaOrigen][columnaOrigen];
                tablero[filaOrigen][columnaOrigen] = null;
                if(!ReyEnJaque(tablero, esBlanco)){
                    tablero[filaOrigen][columnaOrigen - 2] = tablero[filaOrigen][columnaOrigen - 1];
                    tablero[filaOrigen][columnaOrigen - 1] = null;
                    if(!ReyEnJaque(tablero, esBlanco))
                    {
                        lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen,columnaOrigen - 2});
                        tablero[filaOrigen][columnaOrigen] = tablero[filaOrigen][columnaOrigen - 2];
                        tablero[filaOrigen][columnaOrigen - 2] = null;
                    }
                }
            }
        }
        //enroque
        if(EstadoTablero.EnroqueCNegro && !esBlanco){
            if(tablero[filaOrigen][columnaOrigen + 1] == null &&  tablero[filaOrigen][columnaOrigen + 2] == null){
                tablero[filaOrigen][columnaOrigen + 1] = tablero[filaOrigen][columnaOrigen];
                tablero[filaOrigen][columnaOrigen] = null;
                if(!ReyEnJaque(tablero, esBlanco)){
                    tablero[filaOrigen][columnaOrigen + 2] = tablero[filaOrigen][columnaOrigen + 1];
                    tablero[filaOrigen][columnaOrigen + 1] = null;
                    if(!ReyEnJaque(tablero, esBlanco))
                    {
                        lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen,columnaOrigen + 2});
                        tablero[filaOrigen][columnaOrigen] = tablero[filaOrigen][columnaOrigen + 2];
                        tablero[filaOrigen][columnaOrigen + 2] = null;
                    }
                }
            }
        }
        
        if(EstadoTablero.EnroqueLNegro && !esBlanco){
            if(tablero[filaOrigen][columnaOrigen - 1] == null &&  tablero[filaOrigen][columnaOrigen -2] == null){
                tablero[filaOrigen][columnaOrigen - 1] = tablero[filaOrigen][columnaOrigen];
                tablero[filaOrigen][columnaOrigen] = null;
                if(!ReyEnJaque(tablero, esBlanco)){
                    tablero[filaOrigen][columnaOrigen - 2] = tablero[filaOrigen][columnaOrigen - 1];
                    tablero[filaOrigen][columnaOrigen - 1] = null;
                    if(!ReyEnJaque(tablero, esBlanco))
                    {
                        lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen,columnaOrigen - 2});
                        tablero[filaOrigen][columnaOrigen] = tablero[filaOrigen][columnaOrigen - 2];
                        tablero[filaOrigen][columnaOrigen - 2] = null;
                    }
                }
            }
        }

System.out.print("Rey " + esBlanco + " genero: ");
        lista.forEach((pos) -> {
            System.out.print(Juego.juego.ConvertirANotacion(pos) + " ");
        });
        System.out.println("");
        var mValidos =  MovimientosValidos(lista, tablero, esBlanco);
        System.out.print("Rey " + esBlanco + " genero: ");
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
        return esBlanco ? "R" : "r";
    }
    
}