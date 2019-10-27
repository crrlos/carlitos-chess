package com.wolf.carlitos.Piezas;

import com.wolf.carlitos.Juego;
import java.util.ArrayList;
import java.util.List;

/**
 * Caballo
 */
public class Caballo extends Base implements Pieza{
    public boolean esBlanco;
   public Caballo(boolean bando){
        this.esBlanco = bando;
    }
    @Override
    public List<int[]> ObtenerMovimientos(int fila, int columna) {
        Pieza posicionActual;

        var lista = new ArrayList<int[]>();
        var tablero = Juego.tablero;

        if(fila + 2 < 8){
            if(columna + 1 < 8){
                posicionActual = tablero[fila + 2][columna+1];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                        lista.add( new int[]{fila,columna,fila +2,columna +1});
                }
                else
                    lista.add( new int[]{fila,columna,fila +2,columna +1});
            }
            if(columna - 1 >= 0){
            posicionActual = tablero[fila + 2][columna-1];
            if(posicionActual != null){
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                    lista.add( new int[]{fila,columna,fila +2,columna -1});
            }
            else
                lista.add( new int[]{fila,columna,fila +2,columna -1});
        }
        }

        if(fila - 2 >=0 ){
            if(columna + 1 < 8){
                posicionActual = tablero[fila - 2][columna+1];
                if(posicionActual != null){
                    if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                        lista.add( new int[]{fila,columna,fila -2,columna +1});
                }
                else
                    lista.add( new int[]{fila,columna,fila -2,columna +1});
            }
            if(columna - 1 >= 0){
                posicionActual = tablero[fila - 2][columna-1];
            if(posicionActual != null){
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                    lista.add( new int[]{fila,columna,fila -2,columna -1});
            }
            else
                lista.add( new int[]{fila,columna,fila -2,columna -1});
        }
        }

        if(columna - 2 >= 0){
            if(fila + 1 < 8){
                posicionActual = tablero[fila + 1][columna -2];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                        lista.add( new int[]{fila,columna,fila +1,columna -2});
                }
                else
                    lista.add( new int[]{fila,columna,fila +1,columna -2});
            }
            if(fila - 1 >=0 ){
                posicionActual =tablero[fila - 1][columna- 2];
            if(posicionActual != null){
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                    lista.add( new int[]{fila,columna,fila -1,columna -2});
            }
            else
                lista.add( new int[]{fila,columna,fila -1,columna -2});
        }
        }
        if(columna + 2 < 8){
            if(fila + 1 < 8){
                posicionActual = tablero[fila + 1][columna +2];
                if(posicionActual != null){
                    if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                         lista.add( new int[]{fila,columna,fila +1,columna +2});
                }
                else
                    lista.add( new int[]{fila,columna,fila +1,columna +2});
            }
            if(fila - 1 >=0 ){
                posicionActual = tablero[fila - 1][columna+ 2];
            if(posicionActual != null){
                if(posicionActual.EsBlanca() != this.EsBlanca() && !(posicionActual instanceof Rey))
                    lista.add( new int[]{fila,columna,fila -1,columna +2});
            }
            else
                lista.add( new int[]{fila,columna,fila -1,columna +2});
        }
        }
//        System.out.print("Caballo " + esBlanco + " genero: ");
//        lista.forEach((pos) -> {
//            System.out.print(Juego.ConvertirANotacion(pos) + " ");
//        });
//        System.out.println("");
return MovimientosValidos(lista, tablero, esBlanco);
//        System.out.print("Caballo " + esBlanco + " genero: ");
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
        return esBlanco ? "C" : "c";
    }
    @Override
    public int Valor() {
        return esBlanco ? 3 : -3;
    }

    
}