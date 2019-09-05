package app.Piezas;
public interface Pieza{
    public String[] ObtenerMovimientos(Pieza[][] tablero, String posicion);
    public boolean AtacaCasilla(Pieza [][] tablero, String posicion, String casilla);
    public boolean EsBlanca();
    public String Nombre();
}