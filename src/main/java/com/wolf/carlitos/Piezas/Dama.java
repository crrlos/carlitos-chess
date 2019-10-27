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
    public List<int[]> ObtenerMovimientos(int fila, int columna) {
        var tablero = Juego.tablero;
        Pieza posicionActual;
        var lista = new ArrayList<int[]>();
        
        var f = fila + 1;
        var c = columna + 1;
        while(f < 8 && c < 8){
            posicionActual = tablero[f][c];
            
            if(posicionActual != null)
                if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                    break;
            
            lista.add( new int[]{fila,columna,f,c});
            
            if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
            
            
            f++; c++;
        }

         f = fila -1;
         c = columna -1;
        while(f >= 0 && c >=0){
            
            posicionActual = tablero[f][c];
            
            if(posicionActual != null)
                if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                    break;
            
            lista.add( new int[]{fila,columna,f,c});
            
            if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
            
            f--;c--;
        }

         f = fila +1;
         c = columna -1;
        while(f < 8 && c >=0){
            posicionActual = tablero[f][c];
            
            if(posicionActual != null)
                if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                    break;
            
            lista.add( new int[]{fila,columna,f,c});
            
            if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
            
            f++;c--;
        }

         f = fila -1;
         c = columna +1;
        while(f >= 0 && c < 8){
            posicionActual = tablero[f][c];
            
            if(posicionActual != null)
                if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                    break;
            
            lista.add( new int[]{fila,columna,f,c});
            
            if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
            
            f--;c++;
        }

        var i = fila +1;

        while(i < 8){
            posicionActual = tablero[i][columna];
            
            if(posicionActual != null)
                    if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                        break;
            
            lista.add( new int[]{fila,columna,i,columna});
            
            if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
            
            i++;
        }
        i = fila - 1;
        while(i >= 0 ){
            posicionActual = tablero[i][columna];
            
            if(posicionActual != null)
                    if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                        break;
            
            lista.add( new int[]{fila,columna,i,columna});
            
            if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
            
            i--;
        }
        i = columna + 1;
        while(i < 8  ){
            posicionActual = tablero[fila][i];
            
            if(posicionActual != null)
                    if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                        break;
            
            lista.add( new int[]{fila,columna,fila,i});
            
            if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
            i++;
        }
        i = columna - 1;
        while(i >= 0  ){
            posicionActual = tablero[fila][i];
            
            if(posicionActual != null)
                    if(posicionActual.EsBlanca() == this.EsBlanca() || posicionActual instanceof Rey)
                        break;
            
           lista.add( new int[]{fila,columna,fila,i});
           
           if(posicionActual != null && posicionActual.EsBlanca() != this.EsBlanca())
                break;
           
            i--;
        }
//        System.out.print("Dama " + esBlanco + " genero: ");
//        lista.forEach((pos) -> {
//            System.out.print(Juego.ConvertirANotacion(pos) + " ");
//        });
//        System.out.println("");
return MovimientosValidos(lista, tablero, esBlanco);
//        System.out.print("Dama " + esBlanco + " genero: ");
//        mValidos.forEach((pos) -> {
//            System.out.print(Juego.ConvertirANotacion(pos) + " ");
//        });
//        System.out.println("");
//            return mValidos;
    }
    @Override
    public boolean EsBlanca() {
        return esBlanco;
    }

    @Override
    public String Nombre() {
        return esBlanco ? "D" : "d";
    }
    @Override
    public int Valor() {
        return esBlanco ? 9 : -9;
    }
}