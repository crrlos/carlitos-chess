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

        return MovimientosValidos(lista, tablero);
    }

    private List<int[]> MovimientosValidos(List<int[]> movimientos, Pieza[][] tablero){

        var iterator = movimientos.iterator();
        while(iterator.hasNext()){
            if(!MovimientoValido(iterator.next(),tablero))
            {
                iterator.remove();
            }
        }
        return movimientos; 
    }
    private boolean MovimientoValido(int[] movimiento,Pieza [][] tablero){
        var fo = movimiento[0];
        var co = movimiento[1];
        var fd = movimiento[2];
        var cd = movimiento[3];

        Pieza piezaActual  = tablero[fo][co];
        Pieza piezaDestino = tablero[fd][cd];

        tablero[fo][co] = null;
        tablero[fd][cd] = piezaActual;

        var jaque = ReyEnJaque(tablero,esBlanco);

        tablero[fd][cd] = piezaDestino;
        tablero[fo][co] = piezaActual;

        return !jaque;
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