import com.wolf.carlitos.EstadoTablero;
import com.wolf.carlitos.Juego;
import com.wolf.carlitos.Piezas.Base;
import com.wolf.carlitos.Piezas.Caballo;
import com.wolf.carlitos.Piezas.Pieza;
import com.wolf.carlitos.Piezas.Rey;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test{



    public static void main(String... args) throws CloneNotSupportedException {

        Juego j = new Juego();
        j.tablero[3][3] = new Caballo(true);

        var t1 = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            var movs  = movimientosCaballo2(j.tablero,j.estadoTablero,3,3);
        }

        System.out.println(System.currentTimeMillis() -t1);



    }

    private  static List<int[]> movimientosCaballo2(Pieza[][] tablero, EstadoTablero estado, int fila, int columna){
        Pieza posicionActual;
        Pieza pieza;

        var lista = new ArrayList<int[]>();

        pieza = tablero[fila][columna];

        if (fila + 2 < 8) {
            if (columna + 1 < 8) {
                posicionActual = tablero[fila + 2][columna + 1];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila + 2, columna + 1});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila + 2, columna + 1});
                }
            }
            if (columna - 1 >= 0) {
                posicionActual = tablero[fila + 2][columna - 1];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila + 2, columna - 1});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila + 2, columna - 1});
                }
            }
        }

        if (fila - 2 >= 0) {
            if (columna + 1 < 8) {
                posicionActual = tablero[fila - 2][columna + 1];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila - 2, columna + 1});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila - 2, columna + 1});
                }
            }
            if (columna - 1 >= 0) {
                posicionActual = tablero[fila - 2][columna - 1];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila - 2, columna - 1});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila - 2, columna - 1});
                }
            }
        }

        if (columna - 2 >= 0) {
            if (fila + 1 < 8) {
                posicionActual = tablero[fila + 1][columna - 2];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila + 1, columna - 2});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila + 1, columna - 2});
                }
            }
            if (fila - 1 >= 0) {
                posicionActual = tablero[fila - 1][columna - 2];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila - 1, columna - 2});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila - 1, columna - 2});
                }
            }
        }
        if (columna + 2 < 8) {
            if (fila + 1 < 8) {
                posicionActual = tablero[fila + 1][columna + 2];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila + 1, columna + 2});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila + 1, columna + 2});
                }
            }
            if (fila - 1 >= 0) {
                posicionActual = tablero[fila - 1][columna + 2];
                if (posicionActual != null) {
                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{fila, columna, fila - 1, columna + 2});
                    }
                } else {
                    lista.add(new int[]{fila, columna, fila - 1, columna + 2});
                }
            }
        }

        return  lista;
    }

    private static List<int[]> movimientosDeCaballo(Pieza[][] tablero, EstadoTablero estado, int fila, int columna) {

        Pieza pieza = tablero[fila][columna];

        var lista = new ArrayList<int[]>();

        lista.add(new int[]{fila, columna, fila + 2, columna + 1});
        lista.add(new int[]{fila, columna, fila + 2, columna - 1});
        lista.add(new int[]{fila, columna, fila - 2, columna + 1});
        lista.add(new int[]{fila, columna, fila - 2, columna - 1});
        lista.add(new int[]{fila, columna, fila + 1, columna + 2});
        lista.add(new int[]{fila, columna, fila + 1, columna - 2});
        lista.add(new int[]{fila, columna, fila - 1, columna + 2});
        lista.add(new int[]{fila, columna, fila - 1, columna - 2});

        var iterator = lista.iterator();

        while(iterator.hasNext()){
            var movimiento = iterator.next();

            if(movimiento[2] < 0 || movimiento[2] >7 || movimiento[3] < 0 || movimiento[3] > 7)
            {
                iterator.remove(); continue;
            }

            var piezaActual = tablero[movimiento[2]][movimiento[3]];

            if (!(piezaActual == null ||
                    (piezaActual.esBlanca() != pieza.esBlanca()
                            && !(piezaActual instanceof Rey)))) {
                iterator.remove();
            }

        }


        return  lista;

    }
}