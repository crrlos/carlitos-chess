package app.Piezas;

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

        var lista = new ArrayList<int[]>();

        var f = filaOrigen + 1;
        var c = columnaOrigen + 1;
        while(f < 8 && c < 8){

            if(tablero[f][c] !=null){
                if(tablero[f][c].EsBlanca() != this.EsBlanca() && !(tablero[f][c] instanceof Rey)){
                    if(!ReyEnJaque(tablero,esBlanco))
                        lista.add( new int[]{filaOrigen,columnaOrigen,f,c});
                }
                break;
            }else{
                if(!ReyEnJaque(tablero,esBlanco))
                    lista.add( new int[]{filaOrigen,columnaOrigen,f,c});
            }
            f++; c++;
        }

         f = filaOrigen -1;
         c = columnaOrigen -1;
        while(f >= 0 && c >=0){
            
            if(tablero[f][c] !=null){
                if(tablero[f][c].EsBlanca() != this.EsBlanca() && !(tablero[f][c] instanceof Rey)){
                    if(!ReyEnJaque(tablero,esBlanco))
                    lista.add( new int[]{filaOrigen,columnaOrigen,f,c});
                }
                break;
            }else{
                if(!ReyEnJaque(tablero,esBlanco))
                lista.add( new int[]{filaOrigen,columnaOrigen,f,c});
            }
            f--;c--;
        }

         f = filaOrigen +1;
         c = columnaOrigen -1;
        while(f < 8 && c >=0){
            
            if(tablero[f][c] !=null){
                if(tablero[f][c].EsBlanca() != this.EsBlanca() && !(tablero[f][c] instanceof Rey)){
                    if(!ReyEnJaque(tablero,esBlanco))
                    lista.add( new int[]{filaOrigen,columnaOrigen,f,c});
                }
                break;
            }else{
                if(!ReyEnJaque(tablero,esBlanco))
                lista.add( new int[]{filaOrigen,columnaOrigen,f,c});
            }
            f++;c--;
        }

         f = filaOrigen -1;
         c = columnaOrigen +1;
        while(f >= 0 && c < 8){
            if(tablero[f][c] !=null){
                if(tablero[f][c].EsBlanca() != this.EsBlanca() && !(tablero[f][c] instanceof Rey)){
                    if(!ReyEnJaque(tablero,esBlanco))
                    lista.add( new int[]{filaOrigen,columnaOrigen,f,c});
                }
                break;
            }else{
                if(!ReyEnJaque(tablero,esBlanco))
                lista.add( new int[]{filaOrigen,columnaOrigen,f,c});
            }
            f--;c++;
        }
        
        return lista;
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