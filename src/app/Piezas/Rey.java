package app.Piezas;

/**
 * Rey
 */
public class Rey implements Pieza{
    public boolean esBlanco;
    public Rey(boolean bando){
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
        return esBlanco ? "R" : "r";
    }
    
}