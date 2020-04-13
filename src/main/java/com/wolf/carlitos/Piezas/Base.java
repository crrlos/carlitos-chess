package com.wolf.carlitos.Piezas;

import com.wolf.carlitos.EstadoTablero;
import com.wolf.carlitos.Juego;
import java.util.List;

/**
 * Base
 */
public class Base {
    public boolean CasillaAtacada(int fila, int columna, Pieza[][] tablero,boolean blanco){
        
         if(AtaqueFilaColumna(fila, columna, tablero, blanco)) return true;
        if(AtaqueDiagonal(fila, columna, tablero, blanco)) return true;
        if(AtaqueCaballo(fila, columna , tablero, blanco)) return true;
        if(AtaquePeon(fila, columna, tablero, blanco)) return true;
        if(AtaqueRey(fila, columna, tablero, blanco)) return true;
        
        return false;
    }
   
    public boolean ReyEnJaque(Pieza [][] tablero, boolean blanco){
        
        var posicionRey = blanco ? Juego.estadoTablero.PosicionReyBlanco : Juego.estadoTablero.PosicionReyNegro;
        
       return CasillaAtacada(posicionRey[0], posicionRey[1], tablero,blanco); 
       
    }
    private boolean AtaqueRey(int fila, int columna, Pieza[][] tablero, boolean blanco){
        
       if(columna + 1 < 8)
           if(tablero[fila][columna + 1] != null && (tablero[fila][columna + 1] instanceof Rey))
            return true;
      if(columna - 1 >= 0)
           if(tablero[fila][columna - 1] != null && (tablero[fila][columna - 1] instanceof Rey))
            return true;
      
    if(fila + 1 < 8){
        if(tablero[fila + 1][columna] != null && (tablero[fila + 1][columna] instanceof Rey))
            return true;
        if(columna + 1 < 8)
            if(tablero[fila + 1][columna + 1] != null && (tablero[fila + 1][columna + 1] instanceof Rey))
            return true;
        if(columna - 1 >= 0)
            if(tablero[fila + 1][columna - 1] != null && (tablero[fila + 1][columna - 1] instanceof Rey))
            return true; 
    }
    if(fila - 1 >= 0){
            if(tablero[fila - 1][columna] != null && (tablero[fila - 1][columna] instanceof Rey))
                return true;
            if(columna + 1 < 8)
                if(tablero[fila - 1][columna + 1] != null && (tablero[fila - 1][columna + 1] instanceof Rey))
                return true;
            if(columna - 1 >= 0)
                if(tablero[fila - 1][columna - 1] != null && (tablero[fila - 1][columna - 1] instanceof Rey))
                return true; 
        }    
        
    return false;
    }
    private boolean AtaquePeon(int fila, int columna, Pieza[][] tablero, boolean blanco){
        Pieza pieza;
        boolean condicion;
        
         fila = fila + (blanco ? 1 : -1);
         condicion = blanco ? fila < 8 :  fila >= 0;
        
        if(condicion){
            if(columna + 1 < 8)
                if((pieza = tablero[fila][columna + 1]) != null){
                    if(pieza.EsBlanca() != blanco && pieza instanceof Peon) return true;

                }
            if(columna - 1 >= 0)
                if((pieza = tablero[fila][columna - 1]) != null){
                    if(pieza.EsBlanca() != blanco && pieza instanceof Peon) return true;
                }
        }
            
        return false;
    }
    private boolean AtaqueFilaColumna(int fila, int columna, Pieza[][] tablero, boolean blanco){

        var i = fila + 1;
        Pieza posicionActual;
        while(i < 8){

            posicionActual = tablero[i][columna];
            if(posicionActual != null)
                if(posicionActual.EsBlanca() != blanco && (posicionActual instanceof Torre || posicionActual instanceof Dama))
                    return true;
                else break;
            i++;
        }

        i = fila - 1;
        while(i >= 0){

            posicionActual = tablero[i][columna];
            if(posicionActual != null)
                if(posicionActual.EsBlanca() != blanco && (posicionActual instanceof Torre || posicionActual instanceof Dama))
                    return true;
                else break;
           i--;
        }
        i = columna + 1;
        while(i < 8){

            posicionActual = tablero[fila][i];
            if(posicionActual != null)
                if(posicionActual.EsBlanca() != blanco && (posicionActual instanceof Torre || posicionActual instanceof Dama))
                    return true;
                else break;
            i++;
        }
        i = columna - 1;
        while(i >= 0){

            posicionActual = tablero[fila][i];
            if(posicionActual != null)
                if(posicionActual.EsBlanca() != blanco && (posicionActual instanceof Torre || posicionActual instanceof Dama))
                    return true;
                else break;
            i--;
        }
     
        return false;
    }
    private boolean AtaqueDiagonal(int fila, int columna, Pieza[][] tablero, boolean blanco){

        var f = fila + 1;
        var c = columna + 1;
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

         f = fila -1;
         c = columna -1;
        while(f >= 0 && c >=0){
            posicionActual = tablero[f][c];
            if( posicionActual !=null){
                if(posicionActual.EsBlanca() != blanco && (posicionActual instanceof Dama || posicionActual instanceof Alfil))
                    return true;
                else break;
            }
            --f;--c;
        }

         f = fila +1;
         c = columna -1;
        while(f < 8 && c >=0){
            posicionActual = tablero[f][c];
            if( posicionActual !=null){
                if(posicionActual.EsBlanca() != blanco && (posicionActual instanceof Dama || posicionActual instanceof Alfil))
                    return true;
                else break;
            }
            ++f;--c;
        }

         f = fila -1;
         c = columna +1;
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
    private boolean AtaqueCaballo(int fila, int columna, Pieza[][] tablero, boolean blanco){
        Pieza posicionActual;

        if(fila + 2 < 8){
            if(columna + 1 < 8){
                posicionActual = tablero[fila + 2][columna+1];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                        return true;
                }
                
            }
            if(columna - 1 >= 0){
            posicionActual = tablero[fila + 2][columna-1];
            if( posicionActual != null){
                if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                    return true;
            }
        }
        }

        if(fila - 2 >=0 ){
            if(columna + 1 < 8){
                posicionActual = tablero[fila - 2][columna+1];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                        return true;
                }
            }
            if(columna - 1 >= 0){
                posicionActual = tablero[fila - 2][columna-1];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                        return true;
                }
        }
        }

        if(columna - 2 >= 0){
            if(fila + 1 < 8){
                posicionActual = tablero[fila + 1][columna -2];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                        return true;
                }
            }
            if(fila - 1 >=0 ){
                posicionActual =tablero[fila - 1][columna- 2];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                        return true;
                }
        }
        }
        if(columna + 2 < 8){
            if(fila + 1 < 8){
                posicionActual = tablero[fila + 1][columna +2];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                        return true;
                }
            }
            if(fila - 1 >=0 ){
                posicionActual = tablero[fila - 1][columna+ 2];
                if( posicionActual != null){
                    if(posicionActual.EsBlanca() != blanco && posicionActual instanceof Caballo)
                        return true;
                }
        }
        }
        return false;
    }
    public List<int[]> MovimientosValidos(List<int[]> movimientos, Pieza[][] tablero,boolean blanco){

        var iterator = movimientos.iterator();
        while(iterator.hasNext()){
            var mov = iterator.next();
            
            if(!MovimientoValido(mov,tablero,blanco))
            {
                iterator.remove();
            }
        }
        return movimientos; 
    }
    private boolean MovimientoValido(int[] movimiento,Pieza [][] tablero,boolean blanco){
        var fo = movimiento[0];
        var co = movimiento[1];
        var fd = movimiento[2];
        var cd = movimiento[3];

        Pieza piezaActual  = tablero[fo][co];
        Pieza piezaDestino = tablero[fd][cd];

        tablero[fo][co] = null;
        tablero[fd][cd] = piezaActual;

        var jaque = ReyEnJaque(tablero,blanco);

        tablero[fd][cd] = piezaDestino;
        tablero[fo][co] = piezaActual;
        //Juego.ImprimirPosicicion();
        return !jaque;
    }
}