package app.Piezas;

import java.util.List;

public interface Pieza {
    public List<int[]> ObtenerMovimientos(Pieza[][] tablero, int[] posicion);
    public boolean AtacaCasilla(Pieza [][] tablero, int[] posicion, int[] casilla);
    public boolean EsBlanca();
    public String Nombre();
}