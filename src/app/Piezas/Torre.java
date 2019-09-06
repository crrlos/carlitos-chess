package app.Piezas;

import java.util.ArrayList;
import java.util.List;

/**
 * Torre
 */
public class Torre implements Pieza{
    public boolean esBlanco;
    public Torre(boolean bando){
        this.esBlanco = bando;
    }
    @Override
    public List<int[]> ObtenerMovimientos(Pieza[][] tablero, int[] posicion) {
        int filaOrigen = posicion[0];
        int columnaOrigen = posicion[1];

        var lista = new ArrayList<int[]>();
        var i = filaOrigen +1;

        while(i < 8){
            if(tablero[i][columnaOrigen] == null){
                lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
            }else{
                if(tablero[i][columnaOrigen].EsBlanca() != this.EsBlanca()){
                    lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
                }
                 break;
            }
            ++i;
        }
        i = filaOrigen -1;
        while(i >=0 ){
            if(tablero[i][columnaOrigen] == null){
                lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
            }else{
                if(tablero[i][columnaOrigen].EsBlanca() != this.EsBlanca()){
                    lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
                }
                break;
            }
            --i;
        }
        i = columnaOrigen + 1;
        while(i <8  ){
            if(tablero[filaOrigen][columnaOrigen] == null){
                lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen,i});
            }else{
                if(tablero[filaOrigen][columnaOrigen].EsBlanca() != this.EsBlanca()){
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen,i});
                }
                break;
            }
            ++i;
        }
        i = columnaOrigen - 1;
        while(i >= 0  ){
            if(tablero[filaOrigen][columnaOrigen] == null){
                lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen,i});
            }else{
                if(tablero[filaOrigen][columnaOrigen].EsBlanca() != this.EsBlanca()){
                    lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen,i});
                }
                break;
            }
            --i;
        }
        return lista;
    }

    @Override
    public boolean AtacaCasilla(Pieza[][] tablero, int[] posicion, int[] casilla) {
        return false;
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