package app.Piezas;

import java.util.ArrayList;
import java.util.List;

/**
 * Torre
 */
public class Torre extends Base implements Pieza{
    public boolean esBlanco;
    public Torre(boolean bando){
        this.esBlanco = bando;
    }
    @Override
    public List<int[]> ObtenerMovimientos(Pieza[][] tablero, int[] posicion) {
        int filaOrigen = posicion[0];
        int columnaOrigen = posicion[1];
        Pieza posicionActual;
        var lista = new ArrayList<int[]>();


        var i = filaOrigen + 1;

        while(i < 8){

            posicionActual = tablero[i][columnaOrigen];
            if( posicionActual == null){
                
                    lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
            }else{
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey)){
                    
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
                
                    lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
            }else{
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey)){
                    
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
                
                    lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
            }else{
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey)){
                    
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
                
                    lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
            }else{
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey)){
                    
                        lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
                }
                 break;
            }
            i--;
        }

        return MovimientosValidos(lista, tablero,esBlanco);
    }

    @Override
    public boolean EsBlanca() {
        return esBlanco;
    }
    @Override
    public String Nombre() {
        return esBlanco ? "T" : "t";
    }
    
}