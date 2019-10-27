package com.wolf.carlitos.Piezas;

import java.util.List;

public interface Pieza {
    public List<int[]> ObtenerMovimientos(int fila, int columnas);
    public boolean EsBlanca();
    public String Nombre();
    public int Valor();
}