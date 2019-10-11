package app.Piezas;

import java.util.ArrayList;
import java.util.List;

/**
 * Rey
 */
public class Rey implements Pieza{
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


        return lista;
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