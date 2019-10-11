package app.Piezas;

import java.util.List;

public interface Pieza {
    public List<int[]> ObtenerMovimientos(Pieza[][] tablero, int[] posicion);
    public boolean EsBlanca();
    public String Nombre();
}