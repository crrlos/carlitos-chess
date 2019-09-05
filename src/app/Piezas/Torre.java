package app.Piezas;

/**
 * Torre
 */
public class Torre implements Pieza{
    public boolean esBlanco;
    public Torre(boolean bando){
        this.esBlanco = bando;
    }
    @Override
    public String[] ObtenerMovimientos(Pieza[][] tablero, String posicion) {
        return null;
    }

    @Override
    public boolean AtacaCasilla(Pieza[][] tablero, String posicion, String casilla) {
        return false;
    }

    @Override
    public boolean EsBlanca() {
        return esBlanco;
    }
    @Override
    public String Nombre() {
        return esBlanco ? "T" : "t";
    }
    
}