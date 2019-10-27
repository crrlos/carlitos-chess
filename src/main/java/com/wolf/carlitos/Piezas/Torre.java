package com.wolf.carlitos.Piezas;

import com.wolf.carlitos.Juego;
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
    public List<int[]> ObtenerMovimientos(int fila, int columna) {
        var tablero = Juego.tablero;
        Pieza posicionActual;
        var lista = new ArrayList<int[]>();


        var i = fila + 1;

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
//        System.out.print("Torre " + esBlanco + " genero: ");
//        lista.forEach((pos) -> {
//            System.out.print(Juego.ConvertirANotacion(pos) + " " );
//        });
//        System.out.println("");
return MovimientosValidos(lista, tablero, esBlanco);
//        System.out.print("Torre " + esBlanco + " genero: ");
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
        return esBlanco ? "T" : "t";
    }
    @Override
    public int Valor() {
        return esBlanco ? 5 : -5;
    }
    
}