package com.wolf.carlitos.Piezas;

import com.wolf.carlitos.Juego;
import java.util.ArrayList;
import java.util.List;

public class Peon extends Base implements Pieza {
    public boolean esBlanco;
    public Peon(boolean bando){
        this.esBlanco = bando;
    }
    @Override
    public List<int[]> ObtenerMovimientos(Pieza[][] tablero, int[] posicion) {
        int filaOrigen = posicion[0];
        int columnaOrigen = posicion[1];
        int filaDestino;
        
        Pieza posicionActual;
        var lista = new ArrayList<int[]>();
        
            //avance dos casillas
            if(filaOrigen == (esBlanco ? 1 : 7)){
                if(tablero[esBlanco ? 2 : 5][columnaOrigen] == null && tablero[esBlanco ? 3 : 4][columnaOrigen] == null)
                        lista.add(new int[]{filaOrigen,columnaOrigen,esBlanco ? 3 : 4,columnaOrigen});
                }
            //avance una casilla
            filaDestino = filaOrigen + (esBlanco ? 1 : -1);
            if(tablero[filaDestino][columnaOrigen] == null) {
                if(filaDestino == (esBlanco ? 7 : 0)){
                    lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen,1});
                    lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen,2});
                    lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen,3});
                    lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen,4});
                }else{
                    lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen});
                }
            }
            
            if(columnaOrigen > 0){
                posicionActual = tablero[filaDestino][columnaOrigen -1];
                if( posicionActual != null)
                    if(posicionActual.EsBlanca() != esBlanco && !(posicionActual instanceof Rey))
                        if(filaDestino == (esBlanco ? 7 : 0)){
                            lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen -1,1});
                            lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen -1,2});
                            lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen -1,3});
                            lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen -1,4});
                        }else{
                            lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen -1});
                        }
                 }
                if(columnaOrigen < 7){
                    posicionActual = tablero[filaDestino][columnaOrigen +1];
                    if(posicionActual!= null)
                        if(posicionActual.EsBlanca() != esBlanco && !(posicionActual instanceof Rey))
                            if(filaDestino == (esBlanco ? 7 : 0)){
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen +1,1});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen +1,2});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen +1,3});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen +1,4});
                            }else{
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaDestino,columnaOrigen +1});
                            }
                        }
            
        System.out.print("Peon " + esBlanco + " genero: ");
        lista.forEach((pos) -> {
            System.out.print(Juego.juego.ConvertirANotacion(pos) + " ");
        });
        System.out.println("");
        var mValidos =  MovimientosValidos(lista, tablero, esBlanco);
        System.out.print("Peon " + esBlanco + " genero: ");
        mValidos.forEach((pos) -> {
            System.out.print(Juego.juego.ConvertirANotacion(pos) + " ");
        });
        System.out.println("");
            return mValidos;
    }

    @Override
    public boolean EsBlanca() {
        return esBlanco;
    }
    @Override
    public String Nombre() {
        return esBlanco ? "P" : "p";
    }
}