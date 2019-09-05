package app.Piezas;

import java.util.List;

/**
 * Dama
 */
public class Dama  implements Pieza{
    public boolean esBlanco;
    public Dama(boolean bando){
        this.esBlanco = bando;
    }
    @Override
    public List<int[]> ObtenerMovimientos(Pieza[][] tablero, int[] posicion) {
        return null;
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
        return esBlanco ? "D" : "d";
    }
}