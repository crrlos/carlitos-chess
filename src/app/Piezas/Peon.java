package app.Piezas;

import java.util.ArrayList;
import java.util.List;

public class Peon implements Pieza {
    public boolean esBlanco;
    public Peon(boolean bando){
        this.esBlanco = bando;
    }
    @Override
    public List<int[]> ObtenerMovimientos(Pieza[][] tablero, int[] posicion) {
        var lista = new ArrayList<int[]>();
        if(esBlanco){
            if(posicion[0] == 1){
                if(tablero[2][posicion[1]] == null && tablero[3][posicion[1]] == null)
                        lista.add(new int[]{3,posicion[1]});
                if(tablero[2][posicion[1]] == null) 
                        lista.add(new int[]{2,posicion[1]});
                
            }else{
                if(tablero[posicion[0] + 1][posicion[1]] == null) 
                    lista.add(new int[]{posicion[0] + 1,posicion[1]});
            }
        }else{
            if(posicion[0] == 6){
                if(tablero[5][posicion[1]] == null && tablero[4][posicion[1]] == null)
                        lista.add(new int[]{4,posicion[1]});
                if(tablero[5][posicion[1]] == null) 
                        lista.add(new int[]{5,posicion[1]});
                
            }else{
                if(tablero[posicion[0] -1 ][posicion[1]] == null) 
                    lista.add(new int[]{posicion[0] - 1,posicion[1]});
            }
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
        return esBlanco ? "P" : "p";
    }
}