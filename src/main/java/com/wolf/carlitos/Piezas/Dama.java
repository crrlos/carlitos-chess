package com.wolf.carlitos.Piezas;

import com.wolf.carlitos.Juego;
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
            posicionActual = tablero[f][c];
            
            if(posicionActual != null)
                if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                    break;
            
            lista.add( new int[]{filaOrigen,columnaOrigen,f,c});
            
            if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
            
            
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
            
            if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
            
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
            
            if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
            
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
            
            if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
            
            f--;c++;
        }

        var i = filaOrigen +1;

        while(i < 8){
            posicionActual = tablero[i][columnaOrigen];
            
            if(posicionActual != null)
                    if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                        break;
            
            lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
            
            if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
            
            i++;
        }
        i = filaOrigen - 1;
        while(i >= 0 ){
            posicionActual = tablero[i][columnaOrigen];
            
            if(posicionActual != null)
                    if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                        break;
            
            lista.add( new int[]{filaOrigen,columnaOrigen,i,columnaOrigen});
            
            if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
            
            i--;
        }
        i = columnaOrigen + 1;
        while(i < 8  ){
            posicionActual = tablero[filaOrigen][i];
            
            if(posicionActual != null)
                    if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                        break;
            
            lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen,i});
            
            if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
            i++;
        }
        i = columnaOrigen - 1;
        while(i >= 0  ){
            posicionActual = tablero[filaOrigen][i];
            
            if(posicionActual != null)
                    if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                        break;
            
           lista.add( new int[]{filaOrigen,columnaOrigen,filaOrigen,i});
           
           if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
           
            i--;
        }
        System.out.print("Dama " + esBlanco + " genero: ");
        lista.forEach((pos) -> {
            System.out.print(Juego.juego.ConvertirANotacion(pos) + " ");
        });
        System.out.println("");
        return MovimientosValidos(lista, tablero, esBlanco);
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