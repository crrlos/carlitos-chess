package com.wolf.carlitos.Piezas;

import com.wolf.carlitos.Juego;
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
    public List<int[]> ObtenerMovimientos(int fila, int columna) {
        Pieza posicionActual;
        var lista = new ArrayList<int[]>();
        var tablero = Juego.tablero;
        
        var f = fila + 1;
        var c = fila + 1;
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
//        System.out.print("Alfil " + esBlanco + " genero: ");
//        lista.forEach((pos) -> {
//            System.out.print(Juego.ConvertirANotacion(pos) + " ");
//        });
//        System.out.println("");
        var mValidos =  MovimientosValidos(lista, tablero, esBlanco);
//        System.out.print("Alfil " + esBlanco + " genero: ");
//        mValidos.forEach((pos) -> {
//            System.out.print(Juego.ConvertirANotacion(pos) + " ");
//        });
//        System.out.println("");
            return mValidos;
    }

    

    @Override
    public boolean EsBlanca() {
        return esBlanco;
    }

    @Override
    public String Nombre() {
        return esBlanco ? "A" : "a";
    }

    @Override
    public int Valor() {
        return esBlanco ? 3 : -3;
    }


    
}