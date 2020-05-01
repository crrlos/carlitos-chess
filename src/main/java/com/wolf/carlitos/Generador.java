/*
 * To change pieza license header, choose License Headers in Project Properties.
 * To change pieza template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import com.wolf.carlitos.Piezas.*;

import java.util.*;

//import static com.wolf.carlitos.Utilidades.movimientoValido;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Utilidades.movimientoValido;
import static com.wolf.carlitos.Utilidades.reyEnJaque;
import static java.lang.Math.abs;

/**
 * @author carlos
 */
public class Generador {

    private static int piezaJaque;

    public static final HashMap<Integer, List<List<Integer>>> movimientosAlfil = new HashMap<>();
    public static final HashMap<Integer, List<List<Integer>>> movimientosTorre = new HashMap<>();
    public static final HashMap<Integer, List<Integer>> movimientosCaballo = new HashMap<>();
    public static final HashMap<Integer, List<Integer>> movimientosRey = new HashMap<>();

    static {

        llenarMovimientosDeAlfil();
        llenarMovimientosDeTorre();
        llenarMovimientosDeCaballo();
        llenarMovimientosDeRey();

    }

    private static void llenarMovimientosDeRey() {
        for (int i = 0; i < 64; i++) {
            var lista = new ArrayList<Integer>();


            boolean colorInicio = esCasillaBlanca(i);

//            if(i == E1){
//                lista.add(G1);
//                lista.add(C1);
//            }
//            if(i == E8){
//                lista.add(G8);
//                lista.add(C8);
//            }

            if (i + 1 < 64 && colorInicio != esCasillaBlanca(i + 1))
                lista.add(i + 1);
            if (i + 7 < 64 && colorInicio == esCasillaBlanca(i + 7))
                lista.add(i + 7);
            if (i + 8 < 64 && colorInicio != esCasillaBlanca(i + 8))
                lista.add(i + 8);
            if (i + 9 < 64 && colorInicio == esCasillaBlanca(i + 9))
                lista.add(i + 9);

            if (i - 1 >= 0 && colorInicio != esCasillaBlanca(i - 1))
                lista.add(i - 1);
            if (i - 7 >= 0 && colorInicio == esCasillaBlanca(i - 7))
                lista.add(i - 7);
            if (i - 8 >= 0 && colorInicio != esCasillaBlanca(i - 8))
                lista.add(i - 8);
            if (i - 9 >= 0 && colorInicio == esCasillaBlanca(i - 9))
                lista.add(i - 9);

            movimientosRey.put(i, lista);
        }
    }

    private static void llenarMovimientosDeCaballo() {
        for (int i = 0; i < 64; i++) {
            var lista = new ArrayList<Integer>();
            var colorInicio = esCasillaBlanca(i);

            if (i + 6 < 64 && colorInicio != esCasillaBlanca(i + 6))
                lista.add(i + 6);
            if (i + 10 < 64 && colorInicio != esCasillaBlanca(i + 10))
                lista.add(i + 10);
            if (i + 15 < 64 && colorInicio != esCasillaBlanca(i + 15))
                lista.add(i + 15);
            if (i + 17 < 64 && colorInicio != esCasillaBlanca(i + 17))
                lista.add(i + 17);

            if (i - 6 >= 0 && colorInicio != esCasillaBlanca(i - 6))
                lista.add(i - 6);
            if (i - 10 >= 0 && colorInicio != esCasillaBlanca(i - 10))
                lista.add(i - 10);
            if (i - 15 >= 0 && colorInicio != esCasillaBlanca(i - 15))
                lista.add(i - 15);
            if (i - 17 >= 0 && colorInicio != esCasillaBlanca(i - 17))
                lista.add(i - 17);

            movimientosCaballo.put(i, lista);
        }
    }

    private static void llenarMovimientosDeTorre() {
        for (int i = 0; i < 64; i++) {
            var lista = new ArrayList<List<Integer>>(4);

            var listaInterna = new ArrayList<Integer>();
            int base = i + 8;

            while (base < 64) {
                listaInterna.add(base);
                base += 8;
            }
            lista.add(0, listaInterna);

            listaInterna = new ArrayList<>();
            base = i - 8;
            while (base >= 0) {
                listaInterna.add(base);
                base -= 8;
            }

            lista.add(1, listaInterna);

            listaInterna = new ArrayList<>();


            int residuo = i / 8;

            int fin = 7 + (8 * residuo);

            for (int j = i + 1; j <= fin; j++) {
                listaInterna.add(j);
            }
            lista.add(2, listaInterna);

            residuo = i / 8;

            fin = 8 * residuo;

            listaInterna = new ArrayList<Integer>();

            for (int j = i - 1; j >= fin; j--) {
                listaInterna.add(j);
            }
            lista.add(3, listaInterna);

            movimientosTorre.put(i, lista);
        }
    }

    private static void llenarMovimientosDeAlfil() {


        for (int i = 0; i < 64; i++) {

            boolean colorInicio = esCasillaBlanca(i);

            var lista = new ArrayList<List<Integer>>(4);

            var listaInterna = new ArrayList<Integer>();

            int base = i + 9;

            while (base < 64 && colorInicio == esCasillaBlanca(base)) {
                listaInterna.add(base);
                base += 9;
            }
            lista.add(0, listaInterna);

            listaInterna = new ArrayList<Integer>();
            base = i - 9;
            while (base >= 0 && colorInicio == esCasillaBlanca(base)) {
                listaInterna.add(base);
                base -= 9;
            }
            lista.add(1, listaInterna);

            listaInterna = new ArrayList<Integer>();
            base = i + 7;
            while (base < 64 && colorInicio == esCasillaBlanca(base)) {
                listaInterna.add(base);
                base += 7;
            }
            lista.add(2, listaInterna);


            listaInterna = new ArrayList<Integer>();
            base = i - 7;
            while (base >= 0 && colorInicio == esCasillaBlanca(base)) {
                listaInterna.add(base);
                base -= 7;
            }
            lista.add(3, listaInterna);


            movimientosAlfil.put(i, lista);

        }
    }

    public static boolean esCasillaBlanca(int i) {
        boolean filaImpar = (i / 8 + 1) % 2 != 0;

        return filaImpar == ((i + 1) % 2 == 0);
    }


    //    enum Trayectoria {Recta, Diagonal, Ninguna}
//
//    public static int[] piezaJaque = null;
//    public static boolean jaqueDoble = false;
//
    public static List<int[]> generarMovimientos(Pieza[] tablero, EstadoTablero estado) {

        piezaJaque = reyEnJaque(tablero, estado);

        var movimientos = new ArrayList<int[]>();

        for (int i = 0; i < tablero.length; i++) {

            var pieza = tablero[i];

            if (pieza != null && pieza.esBlanca() == estado.turnoBlanco) {
                var movs = pieza instanceof Dama ? movimientosDeDama(tablero, estado, i)
//                        : pieza instanceof Torre ? movimientosDeTorre(tablero, estado, fila, columna)
//                        : pieza instanceof Alfil ? movimientosDeAlfil(tablero, estado, fila, columna)
//                        : pieza instanceof Caballo ? movimientosDeCaballo(tablero, estado, fila, columna)
//                        : pieza instanceof Peon ? movimientosDePeon(tablero, estado, fila, columna)
                        : movimientosDeRey(tablero, estado, i);
                movimientos.addAll(movs);
            }

        }
        piezaJaque = 0;
        return movimientos;
    }

    //
//    public static List<int[]> movimientosDeTorre(Pieza[][] tablero, EstadoTablero estado, int fila, int columna)
//    {
//
//        var lista = new ArrayList<int[]>();
//
//        Pieza posicionActual;
//        Pieza pieza;
//
//        pieza = tablero[fila][columna];
//
//
//        if (reyEnJaque()) {
//
//            var posicionRey = estado.turnoBlanco ? estado.posicionReyBlanco : estado.posicionReyNegro;
//
//            Trayectoria trayectoria = getTrayectoria(tablero, posicionRey);
//
//
//            int puntoX = 0;
//            int puntoY = 0;
//
//            int x1 = piezaJaque[1];
//            int y1 = piezaJaque[0];
//
//            int x2 = posicionRey[1];
//            int y2 = posicionRey[0];
//
//
//
//            switch (trayectoria) {
//                case Diagonal:
//
//                    var simplificador = abs(y2 - y1);
//                    var inversor = (x2 - x1) / simplificador;
//
//                    int constante = inversor * (x1 * (y2 - y1) - y1 * (x2 - x1)) / simplificador;
//                    boolean pendientePositiva = -inversor * (y2 - y1) > 0;
//                    boolean contantePositiva = constante > 0;
//                    constante = abs(constante);
//
//
//                    torreDiagonal(tablero, estado, fila, columna, lista, puntoX, puntoY, x1, y1, x2, y2, constante, pendientePositiva, contantePositiva);
//                    break;
//                case Recta:
//                    torreRecta(tablero, estado, fila, columna, lista, x1, y1, x2, y2);
//                    break;
//                case Ninguna:
//                    torreNinguna(tablero, estado, fila, columna, lista, x1, y1);
//                    break;
//            }
//            return lista;
//        }
//
//        boolean vertical;
//        boolean horizontal;
//
//        int filaPrueba = getFilaPrueba(tablero, fila, columna, pieza);
//        int columnaPrueba = getColumnaPrueba(tablero, fila, columna, pieza);
//
//        vertical = movimientoValido(new int[]{fila, columna, filaPrueba, columna}, tablero, estado);
//        horizontal = movimientoValido(new int[]{fila, columna, fila, columnaPrueba}, tablero, estado);
//
//        //vertical = horizontal = true;
//        int i;
//
//        if (vertical) {
//            i = fila + 1;
//            while (i < 8) {
//                posicionActual = tablero[i][columna];
//
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
//                        break;
//                    }
//                }
//
//                lista.add(new int[]{fila, columna, i, columna});
//
//                if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
//                    break;
//                }
//
//                i++;
//            }
//            i = fila - 1;
//            while (i >= 0) {
//                posicionActual = tablero[i][columna];
//
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
//                        break;
//                    }
//                }
//
//                lista.add(new int[]{fila, columna, i, columna});
//
//                if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
//                    break;
//                }
//
//                i--;
//            }
//        }
//        if (horizontal) {
//            i = columna + 1;
//            while (i < 8) {
//                posicionActual = tablero[fila][i];
//
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
//                        break;
//                    }
//                }
//
//                lista.add(new int[]{fila, columna, fila, i});
//
//                if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
//                    break;
//                }
//                i++;
//            }
//            i = columna - 1;
//            while (i >= 0) {
//                posicionActual = tablero[fila][i];
//
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
//                        break;
//                    }
//                }
//
//                lista.add(new int[]{fila, columna, fila, i});
//
//                if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
//                    break;
//                }
//
//                i--;
//            }
//        }
//
//        return lista;
//    }
//
//    private static void torreDiagonal(Pieza[][] tablero, EstadoTablero estado, int fila, int columna, ArrayList<int[]> lista, int puntoX, int puntoY, int x1, int y1, int x2, int y2, int constante, boolean pendientePositiva, boolean contantePositiva) {
//        if (!pendientePositiva && !contantePositiva) {
//            puntoX = fila - constante;
//            puntoY = columna + constante;
//        } else if (!pendientePositiva) {
//            puntoX = fila + constante;
//            puntoY = columna - constante;
//        } else if (!contantePositiva) {
//            puntoX = -fila + constante;
//            puntoY = -columna + constante;
//        }
//        if (Math.min(x1, x2) <= puntoX && puntoX <= Math.max(x1, x2)) {
//            if (!(tablero[fila][puntoX] instanceof Rey)) {
//                var mov = new int[]{fila, columna, fila, puntoX};
//                movimientoValidoYPuedeLlegar(tablero, estado, fila, columna, lista, mov);
//            }
//
//        }
//        if (Math.min(y1, y2) <= puntoY && puntoY <= Math.max(y1, y2)) {
//            if (!(tablero[puntoY][columna] instanceof Rey)) {
//                var mov = new int[]{fila, columna, puntoY, columna};
//                movimientoValidoYPuedeLlegar(tablero, estado, fila, columna, lista, mov);
//            }
//        }
//    }
//
//    private static void torreRecta(Pieza[][] tablero, EstadoTablero estado, int fila, int columna, ArrayList<int[]> lista, int x1, int y1, int x2, int y2) {
//        // torre puede capturar pieza que ataca
//        if (fila - y1 == 0) {//misma fila
//            var mov = new int[]{fila, columna, fila, x1};
//            movimientoValidoYPuedeLlegar(tablero, estado, fila, columna, lista, mov);
//        } else if (columna - x1 == 0) {
//            var mov = new int[]{fila, columna, y1, columna};
//            movimientoValidoYPuedeLlegar(tablero, estado, fila, columna, lista, mov);
//        }
//        //si torre puede bloquear
//        if (x1 - x2 != 0 && columna >= Math.min(x1, x2) && columna <= Math.max(x1, x2)) {
//            if (!(tablero[y1][columna] instanceof Rey)) {
//                // TODO mejorar o revisar este paso
//                if (lista.size() > 0) {
//                    if (!(lista.get(0)[2] == y1 && lista.get(0)[3] == columna)){
//                        var mov = new int[]{fila, columna, y1, columna};
//                        movimientoValidoYPuedeLlegar(tablero, estado, fila, columna, lista, mov);
//                    }
//                } else{
//                    var mov = new int[]{fila, columna, y1, columna};
//                    movimientoValidoYPuedeLlegar(tablero, estado, fila, columna, lista, mov);
//                }
//            }
//        } else if (y1 - y2 != 0 && fila >= Math.min(y1, y2) && fila <= Math.max(y1, y2)) {
//            if (!(tablero[fila][x2] instanceof Rey)) {
//                if (lista.size() > 0) {
//                    if (!(lista.get(0)[2] == fila && lista.get(0)[3] == x2)){
//                        var mov = new int[]{fila, columna, fila, x2};
//                        movimientoValidoYPuedeLlegar(tablero, estado, fila, columna, lista, mov);
//                    }
//                } else{
//                    var mov = new int[]{fila, columna, fila, x2};
//                    movimientoValidoYPuedeLlegar(tablero, estado, fila, columna, lista, mov);
//                }
//            }
//        }
//    }
//
//    private static void torreNinguna(Pieza[][] tablero, EstadoTablero estado, int fila, int columna, ArrayList<int[]> lista, int x1, int y1) {
//        if (x1 - columna == 0) {
//            var mov = new int[]{fila, columna, y1, columna};
//            movimientoValidoYPuedeLlegar(tablero, estado, fila, columna, lista, mov);
//        } else if (y1 - fila == 0) {
//            var mov = new int[]{fila, columna, fila, x1};
//            movimientoValidoYPuedeLlegar(tablero, estado, fila, columna, lista, mov);
//        }
//    }
//
//    private static void movimientoValidoYPuedeLlegar(Pieza[][] tablero, EstadoTablero estado, int fila, int columna, ArrayList<int[]> lista, int[] mov) {
//        if (movimientoValido(mov, tablero, estado) && caminoLibre(tablero, fila, columna, mov))
//            lista.add(mov);
//    }
//
//    private static boolean caminoLibre(Pieza[][] tablero, int fila, int columna, int[] mov) {
//        if (mov[2] > fila) {//arriba
//            for (int i = fila + 1; i < mov[2]; i++) {
//                if (tablero[i][columna] != null) {
//                    return  false;
//                }
//            }
//
//        } else if (mov[2] < fila) {//abajo
//            for (int i = fila - 1; i > mov[2]; i--) {
//                if (tablero[i][columna] != null) {
//                    return  false;
//                }
//            }
//        } else if (mov[3] > columna) {//derecha
//            for (int i = columna + 1; i < mov[3]; i++) {
//                if (tablero[fila][i] != null) {
//                    return  false;
//                }
//            }
//        } else if (mov[3] < columna) {//izquierda
//            for (int i = columna - 1; i > mov[3]; i--) {
//                if (tablero[fila][i] != null) {
//                   return  false;
//                }
//            }
//        }
//        return  true;
//    }
//
//    private static Trayectoria getTrayectoria(Pieza[][] tablero, int[] posicionRey) {
//        Trayectoria trayectoria = null;
//
//        if (tablero[piezaJaque[0]][piezaJaque[1]] instanceof Peon ||
//                tablero[piezaJaque[0]][piezaJaque[1]] instanceof Caballo) {
//            trayectoria = Trayectoria.Ninguna;
//
//        } else if (piezaJaque[0] == posicionRey[0] || piezaJaque[1] == posicionRey[1]) {
//            trayectoria = Trayectoria.Recta;
//
//        } else {
//            trayectoria = Trayectoria.Diagonal;
//
//        }
//        return trayectoria;
//    }
//
//    private static int getColumnaPrueba(Pieza[][] tablero, int fila, int columna, Pieza pieza) {
//        return columna < 7 && (tablero[fila][columna + 1] == null || (tablero[fila][columna + 1].esBlanca() != pieza.esBlanca()
//                && !(tablero[fila][columna + 1] instanceof Rey)))
//                ? columna + 1 :
//                columna > 0 && (tablero[fila][columna - 1] == null || (tablero[fila][columna - 1].esBlanca() != pieza.esBlanca()
//                        && !(tablero[fila][columna - 1] instanceof Rey)))
//                        ? columna - 1
//                        : columna;
//    }
//
//    private static int getFilaPrueba(Pieza[][] tablero, int fila, int columna, Pieza pieza) {
//        return fila < 7 && (tablero[fila + 1][columna] == null || (tablero[fila + 1][columna].esBlanca() != pieza.esBlanca()
//                && !(tablero[fila + 1][columna] instanceof Rey)))
//                ? fila + 1 :
//                fila > 0 && (tablero[fila - 1][columna] == null || (tablero[fila - 1][columna].esBlanca() != pieza.esBlanca()
//                        && !(tablero[fila - 1][columna] instanceof Rey)))
//                        ? fila - 1
//                        : fila;
//    }
//
    private static List<int[]> movimientosDeDama(Pieza[] tablero, EstadoTablero estado, int posicion) {
        var lista = new ArrayList<int[]>();

        Pieza posicionActual;
        Pieza pieza;

        pieza = tablero[posicion];

//        if (reyEnJaque()) {
//            var posicionRey = estado.turnoBlanco ? estado.posicionReyBlanco : estado.posicionReyNegro;
//
//            Trayectoria trayectoria = getTrayectoria(tablero, posicionRey);
//
//
//            double puntoX = -1000;
//            double puntoY = -1000;
//
//            double x1 = piezaJaque[1];
//            double y1 = piezaJaque[0];
//
//            double x2 = posicionRey[1];
//            double y2 = posicionRey[0];
//
//            switch (trayectoria) {
//                case Diagonal:
//
//                    var simplificador = abs(y2 - y1);
//                    var inversor = (x2 - x1) / simplificador;
//
//                    var constante = inversor * (x1 * (y2 - y1) - y1 * (x2 - x1)) / simplificador;
//                    boolean pendientePositiva = -inversor * (y2 - y1) > 0;
//                    boolean constantePositiva = constante > 0;
//                    constante = abs(constante);
//
//
//                    torreDiagonal(tablero, estado, fila, columna, lista, (int)puntoX,(int)puntoY, (int)x1, (int)y1, (int)x2, (int)y2, (int)constante, pendientePositiva, constantePositiva);
//                    alfilDiagonal(tablero, estado, fila, columna, lista, puntoX, puntoY, x1, y1, x2, y2, constante, pendientePositiva, constantePositiva);
//                    break;
//                case Recta:
//                    torreRecta(tablero, estado, fila, columna, lista, (int)x1, (int)y1, (int)x2, (int)y2);
//                    alfilRecta(tablero,estado,fila,columna,lista,puntoX,puntoY,x1,y1,x2,y2);
//                    break;
//                case Ninguna:
//                    torreNinguna(tablero, estado, fila, columna, lista, (int)x1, (int)y1);
//                    alfilNinguna(tablero,estado,fila, columna,lista);
//                    break;
//            }
//
//
//            return lista;
//
//        }


        boolean vertical;
        boolean horizontal;
        boolean diagonal1;
        boolean diagonal2;

//        int filaPrueba = getFilaPrueba(tablero, fila, columna, pieza);
//        int columnaPrueba = getColumnaPrueba(tablero, fila, columna, pieza);
//
//        vertical = movimientoValido(new int[]{fila, columna, filaPrueba, columna}, tablero, estado);
//        horizontal = movimientoValido(new int[]{fila, columna, fila, columnaPrueba}, tablero, estado);
//
//        diagonal1 = isDiagonal1(tablero,estado,fila,columna,pieza);
//        diagonal2 = isDiagonal2(tablero,estado,fila,columna,pieza);

        var movimientosAlfil = Generador.movimientosAlfil.get(posicion);

        for (int i = 0; i < movimientosAlfil.size(); i++) {

            var movimientoDiagonal = movimientosAlfil.get(i);

            for (int j = 0; j < movimientoDiagonal.size(); j++) {
                posicionActual = tablero[movimientoDiagonal.get(j)];

                if (posicionActual != null) {
                    if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                        break;
                    }
                }

                lista.add(new int[]{posicion, movimientoDiagonal.get(j)});

                if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                    break;
                }
            }


        }


        var movimietosDeTorre = Generador.movimientosTorre.get(posicion);

        for (int i = 0; i < movimietosDeTorre.size(); i++) {

            var movimientoRecta = movimietosDeTorre.get(i);

            for (int j = 0; j < movimientoRecta.size(); j++) {
                posicionActual = tablero[movimientoRecta.get(j)];

                if (posicionActual != null) {
                    if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
                        break;
                    }
                }

                lista.add(new int[]{posicion, movimientoRecta.get(j)});

                if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
                    break;
                }
            }


        }

        return lista;
    }

    //
//    private static List<int[]> movimientosDeCaballo(Pieza[][] tablero, EstadoTablero estado, int fila, int columna) {
//        Pieza posicionActual;
//        Pieza pieza;
//
//        var lista = new ArrayList<int[]>();
//
//        pieza = tablero[fila][columna];
//
//        if (fila + 2 < 8) {
//            if (columna + 1 < 8) {
//                posicionActual = tablero[fila + 2][columna + 1];
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
//                        lista.add(new int[]{fila, columna, fila + 2, columna + 1});
//                    }
//                } else {
//                    lista.add(new int[]{fila, columna, fila + 2, columna + 1});
//                }
//            }
//            if (columna - 1 >= 0) {
//                posicionActual = tablero[fila + 2][columna - 1];
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
//                        lista.add(new int[]{fila, columna, fila + 2, columna - 1});
//                    }
//                } else {
//                    lista.add(new int[]{fila, columna, fila + 2, columna - 1});
//                }
//            }
//        }
//
//        if (fila - 2 >= 0) {
//            if (columna + 1 < 8) {
//                posicionActual = tablero[fila - 2][columna + 1];
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
//                        lista.add(new int[]{fila, columna, fila - 2, columna + 1});
//                    }
//                } else {
//                    lista.add(new int[]{fila, columna, fila - 2, columna + 1});
//                }
//            }
//            if (columna - 1 >= 0) {
//                posicionActual = tablero[fila - 2][columna - 1];
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
//                        lista.add(new int[]{fila, columna, fila - 2, columna - 1});
//                    }
//                } else {
//                    lista.add(new int[]{fila, columna, fila - 2, columna - 1});
//                }
//            }
//        }
//
//        if (columna - 2 >= 0) {
//            if (fila + 1 < 8) {
//                posicionActual = tablero[fila + 1][columna - 2];
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
//                        lista.add(new int[]{fila, columna, fila + 1, columna - 2});
//                    }
//                } else {
//                    lista.add(new int[]{fila, columna, fila + 1, columna - 2});
//                }
//            }
//            if (fila - 1 >= 0) {
//                posicionActual = tablero[fila - 1][columna - 2];
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
//                        lista.add(new int[]{fila, columna, fila - 1, columna - 2});
//                    }
//                } else {
//                    lista.add(new int[]{fila, columna, fila - 1, columna - 2});
//                }
//            }
//        }
//        if (columna + 2 < 8) {
//            if (fila + 1 < 8) {
//                posicionActual = tablero[fila + 1][columna + 2];
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
//                        lista.add(new int[]{fila, columna, fila + 1, columna + 2});
//                    }
//                } else {
//                    lista.add(new int[]{fila, columna, fila + 1, columna + 2});
//                }
//            }
//            if (fila - 1 >= 0) {
//                posicionActual = tablero[fila - 1][columna + 2];
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
//                        lista.add(new int[]{fila, columna, fila - 1, columna + 2});
//                    }
//                } else {
//                    lista.add(new int[]{fila, columna, fila - 1, columna + 2});
//                }
//            }
//        }
//
//        if (reyEnJaque()) {
//
//            var posicionRey = estado.turnoBlanco ? estado.posicionReyBlanco : estado.posicionReyNegro;
//
//            Trayectoria trayectoria = getTrayectoria(tablero, posicionRey);
//
//
//            int x1 = piezaJaque[1];
//            int y1 = piezaJaque[0];
//
//            int x2 = posicionRey[1];
//            int y2 = posicionRey[0];
//
//
//            switch (trayectoria) {
//                case Diagonal:
//                    int temp;
//                    //se invierte para que cuadre la formula de los puntos
//                    //punto x1 < x2
//                    if (x1 > x2) {
//
//                        temp = x1;
//                        x1 = x2;
//                        x2 = temp;
//                        temp = y1;
//                        y1 = y2;
//                        y2 = temp;
//
//                    }
//
//                    int constante = (x1 * (y2 - y1) - y1 * (x2 - x1)) / abs(y2 - y1);
//                    boolean x;
//                    boolean c;
//                    x = -(y2 - y1) > 0;
//                    c = constante > 0;
//                    constante = abs(constante);
//
//
//                    int finalConstante = constante;
//                    int finalX = x1;
//                    int finalX1 = x2;
//                    lista.removeIf(m -> {
//
//                        boolean remover = false;
//                        if (!x && !c) {
//                            remover = m[2] == m[3] + finalConstante;
//                        } else if (!x && c) {
//                            remover = m[2] == m[3] - finalConstante;
//
//                        } else if (x && !c) {
//                            remover = m[2] == -m[3] + finalConstante;
//                        }
//
//                        boolean entrePuntos = Math.min(finalX, finalX1) <= m[3] && m[3] <= Math.max(finalX, finalX1);
//
//                        return !(remover && entrePuntos && movimientoValido(m, tablero, estado));
//
//                    });
//
//
//                    break;
//
//
//                case Recta:
//
//                    lista.removeIf(m -> {
//
//                        if (m[2] == piezaJaque[0]) {
//                            return !(Math.min(piezaJaque[1], posicionRey[1]) <= m[3] && m[3] <= Math.max(piezaJaque[1], posicionRey[1])
//                                    && movimientoValido(m, tablero, estado));
//
//                        }
//                        if (m[3] == piezaJaque[1]) {
//                            return !(Math.min(piezaJaque[0], posicionRey[0]) <= m[2] && m[2] <= Math.max(piezaJaque[0], posicionRey[0])
//                                    && movimientoValido(m, tablero, estado));
//
//                        }
//
//                        return true;
//                    });
//
//                    break;
//
//                case Ninguna:
//                    lista.removeIf(m -> !(m[2] == piezaJaque[0] && m[3] == piezaJaque[1] && movimientoValido(m, tablero, estado)));
//                    break;
//            }
//            return lista;
//        }
//
//        if (lista.size() > 0) {
//            if (!movimientoValido(lista.get(0), tablero, estado)) {
//                lista.clear();
//                //return lista;
//            }
//        }
//
//        return lista;
//
//    }
//
//    private static boolean reyEnJaque() {
//        return piezaJaque != null;
//    }
//
//    public static List<int[]> movimientosDeAlfil(Pieza[][] tablero, EstadoTablero estado, int fila, int columna) {
//
//        Pieza posicionActual;
//        Pieza pieza = tablero[fila][columna];
//        var lista = new ArrayList<int[]>();
//
//        if (reyEnJaque()) {
//            // alfil puede bloquear
//
//            var posicionRey = estado.turnoBlanco ? estado.posicionReyBlanco : estado.posicionReyNegro;
//
//            Trayectoria trayectoria = getTrayectoria(tablero, posicionRey);
//
//            double puntoX = -1000;
//            double puntoY = -1000;
//
//            double x1 = piezaJaque[1];
//            double y1 = piezaJaque[0];
//
//            double x2 = posicionRey[1];
//            double y2 = posicionRey[0];
//
//
//            switch (trayectoria) {
//                case Diagonal:
//
//                    var simplificador = abs(y2 - y1);
//                    var inversor = (x2 - x1) / simplificador;
//
//                    var constante = inversor * (x1 * (y2 - y1) - y1 * (x2 - x1)) / simplificador;
//
//                    boolean pendientePositiva = -inversor * (y2 - y1) > 0;
//                    boolean constantePositiva = constante > 0;
//
//                    constante = abs(constante);
//
//                    alfilDiagonal(tablero, estado, fila, columna, lista, puntoX, puntoY, x1, y1, x2, y2, constante, pendientePositiva, constantePositiva);
//
//                    break;
//                case Recta:
//                    alfilRecta(tablero, estado, fila, columna, lista, puntoX, puntoY, x1, y1, x2, y2);
//                    break;
//
//                case Ninguna:
//                    alfilNinguna(tablero, estado, fila, columna, lista);
//                    break;
//            }
//
//
//            return lista;
//        }
//
//        boolean diagonal1;
//        boolean diagonal2;
//
//        diagonal1 = isDiagonal1(tablero, estado, fila, columna, pieza);
//        diagonal2 = isDiagonal2(tablero, estado, fila, columna, pieza);
//
//        var f = fila + 1;
//        var c = columna + 1;
//        if (diagonal1) {
//            while (f < 8 && c < 8) {
//                posicionActual = tablero[f][c];
//
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
//                        break;
//                    }
//                }
//
//                lista.add(new int[]{fila, columna, f, c});
//
//                if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
//                    break;
//                }
//
//                f++;
//                c++;
//            }
//
//            f = fila - 1;
//            c = columna - 1;
//            while (f >= 0 && c >= 0) {
//
//                posicionActual = tablero[f][c];
//
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
//                        break;
//                    }
//                }
//
//                lista.add(new int[]{fila, columna, f, c});
//
//                if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
//                    break;
//                }
//
//                f--;
//                c--;
//            }
//        }
//
//        if (diagonal2) {
//            f = fila + 1;
//            c = columna - 1;
//            while (f < 8 && c >= 0) {
//                posicionActual = tablero[f][c];
//
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
//                        break;
//                    }
//                }
//
//                lista.add(new int[]{fila, columna, f, c});
//
//                if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
//                    break;
//                }
//
//                f++;
//                c--;
//            }
//
//            f = fila - 1;
//            c = columna + 1;
//            while (f >= 0 && c < 8) {
//                posicionActual = tablero[f][c];
//
//                if (posicionActual != null) {
//                    if (posicionActual.esBlanca() == pieza.esBlanca() || posicionActual instanceof Rey) {
//                        break;
//                    }
//                }
//
//                lista.add(new int[]{fila, columna, f, c});
//
//                if (posicionActual != null && posicionActual.esBlanca() != pieza.esBlanca()) {
//                    break;
//                }
//
//                f--;
//
//                c++;
//            }
//        }
//
//
//        return lista;
//
//
//    }
//
//    private static boolean isDiagonal2(Pieza[][] tablero, EstadoTablero estado, int fila, int columna, Pieza pieza) {
//        int[] movPrueba;
//        if ((fila < 7 && columna > 0) && (tablero[fila + 1][columna - 1] == null || tablero[fila + 1][columna - 1].esBlanca() != pieza.esBlanca()
//                && !(tablero[fila + 1][columna - 1] instanceof Rey))) {
//            movPrueba = new int[]{fila, columna, fila + 1, columna - 1};
//            return  movimientoValido(movPrueba, tablero, estado);
//        } else if ((fila > 0 && columna < 7) && (tablero[fila - 1][columna + 1] == null || tablero[fila - 1][columna + 1].esBlanca() != pieza.esBlanca()
//                && !(tablero[fila - 1][columna + 1] instanceof Rey))) {
//            movPrueba = new int[]{fila, columna, fila - 1, columna + 1};
//            return  movimientoValido(movPrueba, tablero, estado);
//        }
//        return true;
//    }
//
//    private static boolean isDiagonal1(Pieza[][] tablero, EstadoTablero estado, int fila, int columna, Pieza pieza) {
//        int[] movPrueba;
//        if ((fila < 7 && columna < 7) && (tablero[fila + 1][columna + 1] == null || tablero[fila + 1][columna + 1].esBlanca() != pieza.esBlanca()
//                && !(tablero[fila + 1][columna + 1] instanceof Rey))) {
//            movPrueba = new int[]{fila, columna, fila + 1, columna + 1};
//            return  movimientoValido(movPrueba, tablero, estado);
//        } else if ((fila > 0 && columna > 0) && (tablero[fila - 1][columna - 1] == null || tablero[fila - 1][columna - 1].esBlanca() != pieza.esBlanca()
//                && !(tablero[fila - 1][columna - 1] instanceof Rey))) {
//            movPrueba = new int[]{fila, columna, fila - 1, columna - 1};
//            return movimientoValido(movPrueba, tablero, estado);
//        }
//        return true;
//    }
//
//    private static void alfilNinguna(Pieza[][] tablero, EstadoTablero estado, int fila, int columna, ArrayList<int[]> lista) {
//        if (piezaPuedeCapturar(fila, columna) && diagonalDespejada(tablero, fila, columna, piezaJaque)) {
//
//            if (movimientoValido(new int[]{fila, columna, piezaJaque[0], piezaJaque[1]}, tablero, estado)) {
//                lista.add(new int[]{fila, columna, piezaJaque[0], piezaJaque[1]});
//            }
//        }
//    }
//
//    private static void alfilRecta(Pieza[][] tablero, EstadoTablero estado, int fila, int columna, ArrayList<int[]> lista, double puntoX, double puntoY, double x1, double y1, double x2, double y2) {
//        int constante1 = (columna * ((fila + 10) - fila) - fila * ((columna + 10) - columna)) / abs((fila + 10) - fila);
//
//        boolean esPositiva = constante1 > 0;
//
//        constante1 = abs(constante1);
//
//        if (x1 == x2) {
//
//            if (esPositiva) {
//                puntoY = x1 - constante1;
//
//            } else {
//                puntoY = x1 + constante1;
//            }
//            puntoX = x1;
//
//        } else if (y1 == y2) {
//            if (esPositiva) {
//                puntoX = y1 + constante1;
//            } else {
//                puntoX = y1 - constante1;
//            }
//            puntoY = y1;
//
//        }
//        if (puntosEntreTrayectoria(puntoX, puntoY, x1, y1, x2, y2)) {
//            if (siPiezaNoEsRey(tablero[(int) puntoY][(int) puntoX])) {
//                var mov = new int[]{fila, columna, (int) puntoY, (int) puntoX};
//                if (puedeLLegarEsValido(tablero, estado, fila, columna, mov))
//                    lista.add(mov);
//            }
//
//        }
//
//        int constantePendientePositiva = (-columna * ((fila + 10) - fila) + fila * ((columna - 10) - columna)) / abs((fila + 10) - fila);
//
//        constantePendientePositiva = abs(constantePendientePositiva);
//
//        // recta x
//        if (x1 == x2) {
//            puntoY = -x1 + constantePendientePositiva;
//            puntoX = x1;
//        } else if (y1 == y2) {// recta y
//            puntoX = -y1 + constantePendientePositiva;
//            puntoY = y1;
//        }
//
//        if (puntosEntreTrayectoria(puntoX, puntoY, x1, y1, x2, y2)) {
//            if (siPiezaNoEsRey(tablero[(int) puntoY][(int) puntoX])) {
//                var mov = new int[]{fila, columna, (int) puntoY, (int) puntoX};
//                if (puedeLLegarEsValido(tablero, estado, fila, columna, mov))
//                    lista.add(mov);
//            }
//
//        }
//    }
//
//    private static void alfilDiagonal(Pieza[][] tablero, EstadoTablero estado, int fila, int columna, ArrayList<int[]> lista, double puntoX, double puntoY, double x1, double y1, double x2, double y2, double constante, boolean pendientePositiva, boolean constantePositiva) {
//        // ecuacion de la recta pieza pendiente positiva
//        double constantePendientePositiva = (columna * ((fila + 10) - fila) - fila * ((columna + 10) - columna)) / abs((fila + 10) - fila);
//        boolean cc = constantePendientePositiva > 0;
//        constantePendientePositiva = abs(constantePendientePositiva);
//
//
//        if (pendientePositiva && !constantePositiva && cc) {
//            puntoX = (constantePendientePositiva + constante) / 2;
//            puntoY = -puntoX + constante;
//        } else if (pendientePositiva && !constantePositiva) {
//            puntoX = (-constantePendientePositiva + constante) / 2;
//            puntoY = -puntoX + constante;
//
//        } else if (!pendientePositiva && !constantePositiva && !cc && constante == constantePendientePositiva) {
//            puntoX = piezaJaque[1];
//            puntoY = piezaJaque[0];
//        } else if (!pendientePositiva && constantePositiva && cc && constante == constantePendientePositiva) {
//            puntoX = piezaJaque[1];
//            puntoY = piezaJaque[0];
//        }
//
//        if (!(puntoX == -1000 && puntoY == puntoX))
//            procesarPunto(tablero, estado, fila, columna, lista, puntoX, puntoY, x1, y1, x2, y2);
//
//
//        puntoX = puntoY = -1000;
//        // ecuacion de la recta pieza pendiente negativa
//        int constantePendienteNegativa = (-columna * ((fila + 10) - fila) + fila * ((columna - 10) - columna)) / abs((fila + 10) - fila);
//
//        cc = constantePendienteNegativa > 0;
//        constantePendienteNegativa = abs(constantePendienteNegativa);
//
//        if (!pendientePositiva && constantePositiva && !cc) {
//            puntoX = (constantePendienteNegativa + constante) / 2;
//            puntoY = puntoX - constante;
//        } else if (!pendientePositiva && !constantePositiva && !cc) {
//            puntoX = (constantePendienteNegativa - constante) / 2;
//            puntoY = puntoX + constante;
//
//        } else if (pendientePositiva && !constantePositiva && constante == constantePendienteNegativa) {
//            puntoX = piezaJaque[1];
//            puntoY = piezaJaque[0];
//        }
//
//        if (!(puntoX == -1000 && puntoY == puntoX))
//            procesarPunto(tablero, estado, fila, columna, lista, puntoX, puntoY, x1, y1, x2, y2);
//    }
//
//    private static boolean piezaPuedeCapturar(int fila, int columna) {
//        return piezaJaque[1] - columna == piezaJaque[0] - fila ||
//                piezaJaque[1] - columna == fila - piezaJaque[0];
//    }
//
//    private static void procesarPunto(Pieza[][] tablero, EstadoTablero estado, int fila, int columna, ArrayList<int[]> lista, double puntoX, double puntoY, double x1, double y1, double x2, double y2) {
//        if (puntoEntero(puntoX, puntoY))
//            if (puntosEntreTrayectoria(puntoX, puntoY, x1, y1, x2, y2)) {
//                if (siPiezaNoEsRey(tablero[(int) puntoY][(int) puntoX])) {
//                    var mov = new int[]{fila, columna, (int) puntoY, (int) puntoX};
//                    if (puedeLLegarEsValido(tablero, estado, fila, columna, mov))
//                        lista.add(mov);
//                }
//
//            }
//    }
//
//    private static boolean siPiezaNoEsRey(Pieza pieza1) {
//        return !(pieza1 instanceof Rey);
//    }
//
//    private static boolean puedeLLegarEsValido(Pieza[][] tablero, EstadoTablero estado, int fila, int columna, int[] mov) {
//        return diagonalDespejada(tablero, fila, columna, mov)
//                && movimientoValido(mov, tablero, estado);
//    }
//
//    private static boolean puntosEntreTrayectoria(double puntoX, double puntoY, double x1, double y1, double x2, double y2) {
//        return Math.min(x1, x2) <= puntoX && puntoX <= Math.max(x1, x2) &&
//                Math.min(y1, y2) <= puntoY && puntoY <= Math.max(y1, y2);
//    }
//
//    private static boolean puntoEntero(double puntoX, double puntoY) {
//        return puntoX - (int) puntoX == 0 && puntoY - (int) puntoY == 0;
//    }
//
//    private static boolean diagonalDespejada(Pieza[][] tablero, int fila, int columna, int[] mov) {
//
//        int[] m = mov.length == 2 ? new int[]{mov[0], mov[1]} : new int[]{mov[2], mov[3]};
//
//        //diagonal despejada
//        if (m[0] > fila && m[1] < columna) {
//            //izquierda arriba
//
//            for (int i = fila + 1; i < m[0]; i++) {
//                if (tablero[i][columna - (i - fila)] != null) {
//                    return false;
//                }
//            }
//
//        } else if (m[0] > fila && m[1] > columna) {
//            //derecha arriba
//
//            for (int i = fila + 1; i < m[0]; i++) {
//                if (tablero[i][columna + (i - fila)] != null) {
//                    return false;
//                }
//            }
//
//        } else if (m[0] < fila && m[1] > columna) {
//            //derecha abajo
//
//            for (int i = fila - 1; i > m[0]; i--) {
//                if (tablero[i][columna + (fila - i)] != null) {
//                    return false;
//                }
//            }
//
//        } else if (m[0] < fila && m[1] < columna) {
//            //izquierda abajo
//
//            for (int i = fila - 1; i > m[0]; i--) {
//                if (tablero[i][columna - (fila - i)] != null) {
//                    return false;
//                }
//            }
//
//        }
//        return true;
//    }
//
    private static List<int[]> movimientosDeRey(Pieza[] tablero, EstadoTablero estado, int posicion) {

        var lista = new ArrayList<int[]>();

        Pieza pieza = tablero[posicion];

        var movimietosRey = Generador.movimientosRey.get(posicion);

        for (int i = 0; i < movimietosRey.size(); i++) {

            int mov = movimietosRey.get(i);
            var m = tablero[mov];

            if (m == null || m.esBlanca() != pieza.esBlanca()) {

                var posicionRey = estado.turnoBlanco ? estado.posicionReyNegro : estado.posicionReyBlanco;
                var distancia = abs(mov - posicionRey);

                // TODO optimizar estas condiciones
                if (distancia == 1 || distancia == 7 || distancia == 8 || distancia == 9) {

                    if (distancia == 1 && esCasillaBlanca(posicionRey) == esCasillaBlanca(mov))
                        lista.add(new int[]{posicion, movimietosRey.get(i)});
                    else if (distancia == 7 && esCasillaBlanca(posicionRey) != esCasillaBlanca(mov))
                        lista.add(new int[]{posicion, movimietosRey.get(i)});
                    else if (distancia == 9 && esCasillaBlanca(posicionRey) != esCasillaBlanca(mov))
                        lista.add(new int[]{posicion, movimietosRey.get(i)});

                    continue;
                }
                lista.add(new int[]{posicion, movimietosRey.get(i)});
            }
        }
        if (estado.enroqueLBlanco || estado.enroqueCBlanco) {
            if (reyEnJaque(tablero, estado) == 0) {
                if (estado.enroqueCBlanco && pieza.esBlanca() &&
                estado.posicionReyNegro != G2) {

                    // TODO revisar si la posicion sirve, puede estar de sobra
                    if (posicion == E1) {
                        if (tablero[F1] == null && tablero[G1] == null) {
                            moverReyUnaCasilla(tablero, estado, E1, F1);
                            if (reyEnJaque(tablero, estado) == 0) {
                                moverReyUnaCasilla(tablero, estado, F1, G1);
                                if (reyEnJaque(tablero, estado) == 0) {
                                    lista.add(new int[]{E1, G1});
                                }
                                moverReyUnaCasilla(tablero, estado, G1, F1);
                            }
                            moverReyUnaCasilla(tablero, estado, F1, E1);
                        }
                    }
                }

                if (estado.enroqueLBlanco && pieza.esBlanca()
                && estado.posicionReyNegro != B2 && estado.posicionReyNegro != C2) {

                    if (posicion == E1) {
                        if (tablero[D1] == null && tablero[C1] == null && tablero[B1] == null) {
                            moverReyUnaCasilla(tablero, estado, E1, D1);
                            if (reyEnJaque(tablero, estado) == 0) {
                                moverReyUnaCasilla(tablero, estado, D1, C1);
                                if (reyEnJaque(tablero, estado) == 0) {
                                    lista.add(new int[]{E1, C1});
                                }
                                moverReyUnaCasilla(tablero, estado, C1, D1);
                            }
                            moverReyUnaCasilla(tablero, estado, D1, E1);
                        }
                    }
                }
            }
        }


        lista.removeIf(m -> !movimientoValido(m, tablero, estado));
        return lista;
    }


    private static void moverReyUnaCasilla(Pieza[] tablero, EstadoTablero estado, int inicio, int destino) {
        tablero[destino] = tablero[inicio];
        tablero[inicio] = null;
        if (estado.turnoBlanco)
            estado.posicionReyBlanco = destino;
        else
            estado.posicionReyNegro = destino;
    }
}
//
//    private static List<int[]> movimientosDePeon(Pieza[][] tablero, EstadoTablero estado, int fila, int columna) {
//
//        var lista = new ArrayList<int[]>();
//
//        Pieza posicionActual;
//        Pieza pieza = tablero[fila][columna];
//
//
//        int filaDestino;
//
//
//        //avance dos casillas
//        if (fila == (pieza.esBlanca() ? 1 : 6)) {
//            if (tablero[pieza.esBlanca() ? 2 : 5][columna] == null && tablero[pieza.esBlanca() ? 3 : 4][columna] == null) {
//                lista.add(new int[]{fila, columna, pieza.esBlanca() ? 3 : 4, columna});
//            }
//        }
//
//        //avance una casilla
//        filaDestino = fila + (pieza.esBlanca() ? 1 : -1);
//        if (tablero[filaDestino][columna] == null) {
//            if (filaDestino == (pieza.esBlanca() ? 7 : 0)) {
//                lista.add(new int[]{fila, columna, filaDestino, columna, 1});
//                lista.add(new int[]{fila, columna, filaDestino, columna, 2});
//                lista.add(new int[]{fila, columna, filaDestino, columna, 3});
//                lista.add(new int[]{fila, columna, filaDestino, columna, 4});
//            } else {
//                lista.add(new int[]{fila, columna, filaDestino, columna});
//            }
//        }
//        //avance diagonal
//        if (columna > 0) {
//            posicionActual = tablero[filaDestino][columna - 1];
//            if (posicionActual != null) {
//                if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
//                    if (filaDestino == (pieza.esBlanca() ? 7 : 0)) {
//                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 1});
//                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 2});
//                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 3});
//                        lista.add(new int[]{fila, columna, filaDestino, columna - 1, 4});
//                    } else {
//                        lista.add(new int[]{fila, columna, filaDestino, columna - 1});
//                    }
//                }
//            } else if (estado.alPaso && fila == (pieza.esBlanca() ? 4 : 3)) {
//                if (tablero[fila][columna - 1] == estado.piezaALPaso) {
//                    lista.add(new int[]{fila, columna, filaDestino, columna - 1});
//                }
//            }
//        }
//        if (columna < 7) {
//            posicionActual = tablero[filaDestino][columna + 1];
//            if (posicionActual != null) {
//                if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
//                    if (filaDestino == (pieza.esBlanca() ? 7 : 0)) {
//                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 1});
//                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 2});
//                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 3});
//                        lista.add(new int[]{fila, columna, filaDestino, columna + 1, 4});
//                    } else {
//                        lista.add(new int[]{fila, columna, filaDestino, columna + 1});
//                    }
//                }
//            } else if (estado.alPaso && fila == (pieza.esBlanca() ? 4 : 3)) {
//                if (tablero[fila][columna + 1] == estado.piezaALPaso) {
//                    lista.add(new int[]{fila, columna, filaDestino, columna + 1});
//                }
//            }
//        }
//        return new Base(estado).MovimientosValidos(lista, tablero, pieza.esBlanca());
//    }
//
//}
