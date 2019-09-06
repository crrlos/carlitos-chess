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
        int filaOrigen = posicion[0];
        int columnaOrigen = posicion[1];

        var lista = new ArrayList<int[]>();
        if(esBlanco){
            if(filaOrigen == 1){
                if(tablero[2][columnaOrigen] == null && tablero[3][columnaOrigen] == null)
                        lista.add(new int[]{filaOrigen,columnaOrigen,3,columnaOrigen});
                if(tablero[2][columnaOrigen] == null) 
                        lista.add(new int[]{filaOrigen,columnaOrigen,2,columnaOrigen});
                
            }else{
                if(tablero[filaOrigen + 1][columnaOrigen] == null) {
                    if(filaOrigen + 1 == 7){
                        lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen,1});
                        lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen,2});
                        lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen,3});
                        lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen,4});
                    }else{
                        lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen});
                    }
                }

                if(columnaOrigen > 0)
                    if(tablero[filaOrigen + 1][columnaOrigen -1] != null)
                        if(!tablero[filaOrigen + 1][columnaOrigen -1].EsBlanca())
                            if(filaOrigen + 1 == 7){
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen -1,1});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen -1,2});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen -1,3});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen -1,4});
                            }else{
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen -1});
                            }
                if(columnaOrigen < 7)
                    if(tablero[filaOrigen + 1][columnaOrigen +1] != null)
                        if(!tablero[filaOrigen +1][columnaOrigen +1].EsBlanca())
                            if(filaOrigen + 1 == 7){
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen +1,1});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen +1,2});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen +1,3});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen +1,4});
                            }else{
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen + 1,columnaOrigen +1});
                            }
            }
        }else{
            if(filaOrigen == 6){
                if(tablero[5][columnaOrigen] == null && tablero[4][columnaOrigen] == null)
                        lista.add(new int[]{filaOrigen,columnaOrigen,4,columnaOrigen});
                if(tablero[5][columnaOrigen] == null) 
                        lista.add(new int[]{filaOrigen,columnaOrigen,5,columnaOrigen});
                
            }else{
                if(tablero[filaOrigen -1 ][columnaOrigen] == null) 
                    if(filaOrigen -1 == 0){
                        lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen,1});
                        lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen,2});
                        lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen,3});
                        lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen,4});
                    }else{
                        lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen});
                    }

                    if(columnaOrigen > 0)
                    if(tablero[filaOrigen -1][columnaOrigen -1] != null)
                        if(tablero[filaOrigen -1][columnaOrigen -1].EsBlanca())
                            if(filaOrigen - 1 ==0){
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen -1,1});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen -1,2});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen -1,3});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen -1,4});
                            }else{
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen -1});
                            }
                    if(columnaOrigen < 7)
                    if(tablero[filaOrigen -1][columnaOrigen +1] != null)
                        if(tablero[filaOrigen -1][columnaOrigen +1].EsBlanca())
                            if(filaOrigen - 1 == 0){
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen +1,1});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen +1,2});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen +1,3});
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen +1,4});
                            }else{
                                lista.add(new int[]{filaOrigen,columnaOrigen,filaOrigen - 1,columnaOrigen +1});
                            }
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