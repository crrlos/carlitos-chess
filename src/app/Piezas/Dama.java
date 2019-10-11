package app.Piezas;

import java.util.ArrayList;
import java.util.List;

/**
 * Dama
 */
public class Dama extends Base  implements Pieza{
    public boolean esBlanco;
    public Dama(boolean bando){
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

        var i = filaOrigen +1;

        while(i < 8){

            posicionActual = tablero[i][columnaOrigen];
            if( posicionActual == null){
                if(!ReyEnJaque(tablero,esBlanco))
                    lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
            }else{
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey)){
                    if(!ReyEnJaque(tablero,esBlanco))
                        lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
                }
                 break;
            }
            i++;
        }
        i = filaOrigen - 1;
        while(i >= 0 ){
            posicionActual = tablero[i][columnaOrigen];
            if( posicionActual == null){
                if(!ReyEnJaque(tablero,esBlanco))
                    lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
            }else{
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey)){
                    if(!ReyEnJaque(tablero,esBlanco))
                        lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
                }
                 break;
            }
            i--;
        }
        i = columnaOrigen + 1;
        while(i < 8  ){
            posicionActual = tablero[filaOrigen][i];
            if( posicionActual == null){
                if(!ReyEnJaque(tablero,esBlanco))
                    lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
            }else{
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey)){
                    if(!ReyEnJaque(tablero,esBlanco))
                        lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
                }
                 break;
            }
            i++;
        }
        i = columnaOrigen - 1;
        while(i >= 0  ){
            posicionActual = tablero[filaOrigen][i];
            if( posicionActual == null){
                if(!ReyEnJaque(tablero,esBlanco))
                    lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
            }else{
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey)){
                    if(!ReyEnJaque(tablero,esBlanco))
                        lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
                }
                 break;
            }
            i--;
        }
        return lista;
    }
    @Override
    public boolean EsBlanca() {
        return esBlanco;
    }

    @Override
    public String Nombre() {
        return esBlanco ? "D" : "d";
    }
}