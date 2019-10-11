package app.Piezas;

/**
 * Base
 */
public class Base {

    public boolean CasillaAtacada(int fila, int columna, Pieza[][] tablero,boolean blanco){
         
        if(AtaqueFilaColumna(fila, columna, tablero, blanco)) return true;
        if(AtaqueDiagonal(fila, columna, tablero, blanco)) return true;
        if(AtaqueCaballo(fila, columna , tablero, blanco)) return true;

        return false;
    }
    public int[] BuscarPosicionRey(boolean blanco, Pieza[][] tablero){
            for (int i = 0; i < tablero.length; i++) {
                for (int j = 0; j < tablero.length; j++) {
                    if(tablero[i][j] instanceof Rey)
                        {
                            if(tablero[i][j].EsBlanca() == blanco)
                                return new int[]{i,j};
                        }
                }
            }
            return new int[]{};
    }
    public boolean ReyEnJaque(Pieza [][] tablero, boolean blanco){
        var posicionRey = BuscarPosicionRey(blanco,tablero);
        return CasillaAtacada(posicionRey[0], posicionRey[1], tablero,blanco);   
    }
    private boolean AtaqueFilaColumna(int filaOrigen, int columnaOrigen, Pieza[][] tablero, boolean blanco){

        var i = filaOrigen + 1;
        Pieza posicionActual;
        while(i < 8){

            posicionActual = tablero[i][columnaOrigen];
            if(posicionActual != null)
                if(posicionActual.EsBlanca() != blanco && (posicionActual instanceof Torre || posicionActual instanceof Dama))
                    return true;
                else break;
            i++;
        }

        i = filaOrigen - 1;
        while(i >= 0){

            posicionActual = tablero[i][columnaOrigen];
            if(posicionActual != null)
                if(posicionActual.EsBlanca() != blanco && (posicionActual instanceof Torre || posicionActual instanceof Dama))
                    return true;
                else break;
           i--;
        }
        i = columnaOrigen + 1;
        while(i < 8){

            posicionActual = tablero[filaOrigen][i];
            if(posicionActual != null)
                if(posicionActual.EsBlanca() != blanco && (posicionActual instanceof Torre || posicionActual instanceof Dama))
                    return true;
                else break;
            i++;
        }
        i = columnaOrigen - 1;
        while(i >= 0){

            posicionActual = tablero[filaOrigen][i];
            if(posicionActual != null)
                if(posicionActual.EsBlanca() != blanco && (posicionActual instanceof Torre || posicionActual instanceof Dama))
                    return true;
                else break;
            i--;
        }
     
        return false;
    }
    private boolean AtaqueDiagonal(int filaOrigen, int columnaOrigen, Pieza[][] tablero, boolean blanco){

        var f = filaOrigen + 1;
        var c = columnaOrigen + 1;
        Pieza posicionActual;
        while(f < 8 && c < 8){
            posicionActual = tablero[f][c];
            if( posicionActual !=null){
                if(posicionActual.EsBlanca() != blanco && (posicionActual instanceof Dama || posicionActual instanceof Alfil))
                    return true;
                else break;
            }
            ++f;++c;
        }

         f = filaOrigen -1;
         c = columnaOrigen -1;
        while(f >= 0 && c >=0){
            posicionActual = tablero[f][c];
            if( posicionActual !=null){
                if(posicionActual.EsBlanca() != blanco && (posicionActual instanceof Dama || posicionActual instanceof Alfil))
                    return true;
                else break;
            }
            --f;--c;
        }

         f = filaOrigen +1;
         c = columnaOrigen -1;
        while(f < 8 && c >=0){
            posicionActual = tablero[f][c];
            if( posicionActual !=null){
                if(posicionActual.EsBlanca() != blanco && (posicionActual instanceof Dama || posicionActual instanceof Alfil))
                    return true;
                else break;
            }
            ++f;--c;
        }

         f = filaOrigen -1;
         c = columnaOrigen +1;
        while(f >= 0 && c < 8){
            posicionActual = tablero[f][c];
            if( posicionActual !=null){
                if(posicionActual.EsBlanca() != blanco && (posicionActual instanceof Dama || posicionActual instanceof Alfil))
                    return true;
                else break;
            }
            --f;++c;
        }
     
        return false;
    }
    private boolean AtaqueCaballo(int filaOrigen, int columnaOrigen, Pieza[][] tablero, boolean blanco){
        Pieza posicionActual;

        if(filaOrigen + 2 < 8){
            if(columnaOrigen + 1 < 8){
                posicionActual = tablero[filaOrigen + 2][columnaOrigen+1];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                        return true;
                }
                
            }
            if(columnaOrigen - 1 >= 0){
            posicionActual = tablero[filaOrigen + 2][columnaOrigen-1];
            if( posicionActual != null){
                if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                    return true;
            }
        }
        }

        if(filaOrigen - 2 >=0 ){
            if(columnaOrigen + 1 < 8){
                posicionActual = tablero[filaOrigen - 2][columnaOrigen+1];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                        return true;
                }
            }
            if(columnaOrigen - 1 >= 0){
                posicionActual = tablero[filaOrigen - 2][columnaOrigen-1];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                        return true;
                }
        }
        }

        if(columnaOrigen - 2 >= 0){
            if(filaOrigen + 1 < 8){
                posicionActual = tablero[filaOrigen + 1][columnaOrigen -2];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                        return true;
                }
            }
            if(filaOrigen - 1 >=0 ){
                posicionActual =tablero[filaOrigen - 1][columnaOrigen- 2];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                        return true;
                }
        }
        }
        if(columnaOrigen + 2 < 8){
            if(filaOrigen + 1 < 8){
                posicionActual = tablero[filaOrigen + 1][columnaOrigen +2];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                        return true;
                }
            }
            if(filaOrigen - 1 >=0 ){
                posicionActual = tablero[filaOrigen - 1][columnaOrigen+ 2];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                        return true;
                }
        }
        }
        return false;
    }
}