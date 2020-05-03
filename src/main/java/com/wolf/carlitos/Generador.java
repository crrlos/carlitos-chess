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
import static com.wolf.carlitos.Utilidades.*;
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
                        : pieza instanceof Torre ? movimientosDeTorre(tablero, estado, i)
                        : pieza instanceof Alfil ? movimientosDeAlfil(tablero, estado, i)
                        : pieza instanceof Caballo ? movimientosDeCaballo(tablero, estado, i)
                        : pieza instanceof Peon ? movimientosDePeon(tablero, estado, i)
                        : movimientosDeRey(tablero, estado, i);
                movimientos.addAll(movs);
            }

        }
        piezaJaque = 0;
        return movimientos;
    }

    //
    public static List<int[]> movimientosDeTorre(Pieza[] tablero, EstadoTablero estado, int posicion) {

        var lista = new ArrayList<int[]>();

        Pieza posicionActual;
        Pieza pieza = tablero[posicion];

        var movimientosDeTorre = Generador.movimientosTorre.get(posicion);

        if (!enJaque()) {

            boolean vertical;
            boolean horizontal;

            var vertical1 = movimientosDeTorre.get(0);
            var vertical2 = movimientosDeTorre.get(1);
            var horizontal1 = movimientosDeTorre.get(2);
            var horizontal2 = movimientosDeTorre.get(3);

            vertical = isVertical(tablero, estado, posicion, pieza, vertical1, vertical2);
            horizontal = isVertical(tablero, estado, posicion, pieza, horizontal1, horizontal2);

            if (horizontal) {

                procesarLinea(tablero, posicion, lista, pieza, horizontal1);
                procesarLinea(tablero, posicion, lista, pieza, horizontal2);

            }
            if (vertical) {

                procesarLinea(tablero, posicion, lista, pieza, vertical1);
                procesarLinea(tablero, posicion, lista, pieza, vertical2);

            }
            return  lista;
        }


        for (int i = 0; i < movimientosDeTorre.size(); i++) {
            var recorrido = movimientosDeTorre.get(i);
            procesarLinea(tablero, posicion, lista, pieza, recorrido);

        }

        lista.removeIf(m -> !movimientoValido(m, tablero, estado));
        return lista;
    }

    private static void procesarLinea(Pieza[] tablero, int posicion, ArrayList<int[]> lista, Pieza pieza, List<Integer> horizontal1) {
        Pieza posicionActual;
        for (int j = 0; j < horizontal1.size(); j++) {
            var mov = horizontal1.get(j);
            posicionActual = tablero[mov];

            if (posicionActual == null) {
                lista.add(new int[]{posicion, mov});
                continue;
            }

            var sonMismoColor = posicionActual.esBlanca() == pieza.esBlanca();

            if (!sonMismoColor && !(posicionActual instanceof Rey)) {
                lista.add(new int[]{posicion, mov});
                break;
            }

            if (sonMismoColor) break;
        }
    }

    private static boolean isVertical(Pieza[] tablero, EstadoTablero estado, int posicion, Pieza pieza, List<Integer> vertical1, List<Integer> vertical2) {
        if (!vertical1.isEmpty()) {
            var mov = vertical1.get(0);
            var pi = tablero[mov];

            if (pi == null || pi.esBlanca() != pieza.esBlanca() && !(pi instanceof Rey)) {
                var m = new int[]{posicion, mov};
                return  movimientoValido(m, tablero, estado);
            } else if (!vertical2.isEmpty()) {
                mov = vertical2.get(0);
                pi = tablero[mov];

                if (pi == null || pi.esBlanca() != pieza.esBlanca() && !(pi instanceof Rey)) {
                    var m = new int[]{posicion, mov};
                    return movimientoValido(m, tablero, estado);

                }

            }
        } else if (!vertical2.isEmpty()) {
            var mov = vertical2.get(0);
            var pi = tablero[mov];

            if (pi == null || pi.esBlanca() != pieza.esBlanca() && !(pi instanceof Rey)) {
                var m = new int[]{posicion, mov};
                return movimientoValido(m, tablero, estado);

            }
        }
        return true;
    }

        private static List<int[]> movimientosDeDama (Pieza[]tablero, EstadoTablero estado,int posicion){
            var lista = new ArrayList<int[]>();

            Pieza posicionActual;
            Pieza pieza;

            pieza = tablero[posicion];

            var movimientosAlfil = Generador.movimientosAlfil.get(posicion);
            var movimietosDeTorre = Generador.movimientosTorre.get(posicion);

            if (!enJaque()) {

                boolean diagonal1;
                boolean diagonal2;

                var digonal1Arriba = movimientosAlfil.get(0);
                var diagonal1Abajo = movimientosAlfil.get(1);
                var diagonal2Arriba = movimientosAlfil.get(2);
                var diagonal2Abajo = movimientosAlfil.get(3);

                diagonal1 = isVertical(tablero, estado, posicion, pieza, digonal1Arriba, diagonal1Abajo);
                diagonal2 = isVertical(tablero, estado, posicion, pieza, diagonal2Arriba, diagonal2Abajo);

                if (diagonal2) {

                    procesarLinea(tablero, posicion, lista, pieza, diagonal2Arriba);
                    procesarLinea(tablero, posicion, lista, pieza, diagonal2Abajo);

                }
                if (diagonal1) {

                    procesarLinea(tablero, posicion, lista, pieza, digonal1Arriba);
                    procesarLinea(tablero, posicion, lista, pieza, diagonal1Abajo);

                }


                boolean vertical;
                boolean horizontal;

                var vertical1 = movimietosDeTorre.get(0);
                var vertical2 = movimietosDeTorre.get(1);
                var horizontal1 = movimietosDeTorre.get(2);
                var horizontal2 = movimietosDeTorre.get(3);

                vertical = isVertical(tablero, estado, posicion, pieza, vertical1, vertical2);
                horizontal = isVertical(tablero, estado, posicion, pieza, horizontal1, horizontal2);

                if (horizontal) {

                    procesarLinea(tablero, posicion, lista, pieza, horizontal1);
                    procesarLinea(tablero, posicion, lista, pieza, horizontal2);

                }
                if (vertical) {

                    procesarLinea(tablero, posicion, lista, pieza, vertical1);
                    procesarLinea(tablero, posicion, lista, pieza, vertical2);

                }



                return  lista;
            }

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

            lista.removeIf(m -> !movimientoValido(m, tablero, estado));
            return lista;
        }

        private static List<int[]> movimientosDeCaballo (Pieza[]tablero, EstadoTablero estado,int posicion){
            Pieza posicionActual;
            Pieza pieza = tablero[posicion];

            var lista = new ArrayList<int[]>();

            var movimientosCaballo = Generador.movimientosCaballo.get(posicion);

            for (int i = 0; i < movimientosCaballo.size(); i++) {
                var mov = movimientosCaballo.get(i);
                posicionActual = tablero[mov];

                if (posicionActual == null || (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey))) {
                    lista.add(new int[]{posicion, mov});
                }

            }

            if(!enJaque()){
                if(!lista.isEmpty()){
                    if(movimientoValido(lista.get(0),tablero,estado))
                    {
                        return  lista;
                    }else{
                        return  new ArrayList<>();
                    }
                }
            }


            lista.removeIf(m -> !movimientoValido(m, tablero, estado));
            return lista;

        }

        //
        private static boolean enJaque () {
            return piezaJaque != NO_JAQUE;
        }
//
        public static List<int[]> movimientosDeAlfil (Pieza[]tablero, EstadoTablero estado,int posicion){

            Pieza posicionActual;
            Pieza pieza = tablero[posicion];
            var lista = new ArrayList<int[]>();

            var movimientosAlfil = Generador.movimientosAlfil.get(posicion);

            if (!enJaque()) {

                boolean diagonal1;
                boolean diagonal2;

                var digonal1Arriba = movimientosAlfil.get(0);
                var diagonal1Abajo = movimientosAlfil.get(1);
                var diagonal2Arriba = movimientosAlfil.get(2);
                var diagonal2Abajo = movimientosAlfil.get(3);

                diagonal1 = isVertical(tablero, estado, posicion, pieza, digonal1Arriba, diagonal1Abajo);
                diagonal2 = isVertical(tablero, estado, posicion, pieza, diagonal2Arriba, diagonal2Abajo);

                if (diagonal2) {

                    procesarLinea(tablero, posicion, lista, pieza, diagonal2Arriba);
                    procesarLinea(tablero, posicion, lista, pieza, diagonal2Abajo);

                }
                if (diagonal1) {

                    procesarLinea(tablero, posicion, lista, pieza, digonal1Arriba);
                    procesarLinea(tablero, posicion, lista, pieza, diagonal1Abajo);

                }
                return  lista;
            }


            for (int i = 0; i < movimientosAlfil.size(); i++) {
                var diagonal = movimientosAlfil.get(i);
                for (int j = 0; j < diagonal.size(); j++) {
                    var mov = diagonal.get(j);
                    posicionActual = tablero[mov];

                    if (posicionActual == null) {
                        lista.add(new int[]{posicion, mov});
                        continue;
                    }
                    var sonMismoColor = posicionActual.esBlanca() == pieza.esBlanca();

                    if (!sonMismoColor && !(posicionActual instanceof Rey)) {
                        lista.add(new int[]{posicion, mov});
                        break;
                    }

                    if (sonMismoColor) break;
                    ;


                }

            }

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


            lista.removeIf(m -> !movimientoValido(m, tablero, estado));
            return lista;


        }

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
        private static List<int[]> movimientosDeRey (Pieza[]tablero, EstadoTablero estado,int posicion){

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
            if (estado.turnoBlanco) {
                if (estado.enroqueLBlanco || estado.enroqueCBlanco) {
                    if (reyEnJaque(tablero, estado) == NO_JAQUE) {
                        if (estado.enroqueCBlanco && estado.posicionReyNegro != G2) {

                            // TODO revisar si la posicion sirve, puede estar de sobra
                            if (posicion == E1) {
                                if (tablero[F1] == null && tablero[G1] == null) {
                                    moverReyUnaCasilla(tablero, estado, E1, F1);
                                    if (reyEnJaque(tablero, estado) == NO_JAQUE) {
                                        moverReyUnaCasilla(tablero, estado, F1, G1);
                                        if (reyEnJaque(tablero, estado) == NO_JAQUE) {
                                            lista.add(new int[]{E1, G1});
                                        }
                                        moverReyUnaCasilla(tablero, estado, G1, F1);
                                    }
                                    moverReyUnaCasilla(tablero, estado, F1, E1);
                                }
                            }
                        }

                        if (estado.enroqueLBlanco && estado.posicionReyNegro != B2 && estado.posicionReyNegro != C2) {

                            if (posicion == E1) {
                                if (tablero[D1] == null && tablero[C1] == null && tablero[B1] == null) {
                                    moverReyUnaCasilla(tablero, estado, E1, D1);
                                    if (reyEnJaque(tablero, estado) == NO_JAQUE) {
                                        moverReyUnaCasilla(tablero, estado, D1, C1);
                                        if (reyEnJaque(tablero, estado) == NO_JAQUE) {
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
            } else {
                if (estado.enroqueLNegro || estado.enroqueCNegro) {
                    if (reyEnJaque(tablero, estado) == NO_JAQUE) {
                        if (estado.enroqueCNegro && estado.posicionReyBlanco != G7) {

                            // TODO revisar si la posicion sirve, puede estar de sobra
                            if (posicion == E8) {
                                if (tablero[F8] == null && tablero[G8] == null) {
                                    moverReyUnaCasilla(tablero, estado, E8, F8);
                                    if (reyEnJaque(tablero, estado) == NO_JAQUE) {
                                        moverReyUnaCasilla(tablero, estado, F8, G8);
                                        if (reyEnJaque(tablero, estado) == NO_JAQUE) {
                                            lista.add(new int[]{E8, G8});
                                        }
                                        moverReyUnaCasilla(tablero, estado, G8, F8);
                                    }
                                    moverReyUnaCasilla(tablero, estado, F8, E8);
                                }
                            }
                        }

                        if (estado.enroqueLNegro && estado.posicionReyBlanco != B7 && estado.posicionReyBlanco != C7) {

                            if (posicion == E8) {
                                if (tablero[D8] == null && tablero[C8] == null && tablero[B8] == null) {
                                    moverReyUnaCasilla(tablero, estado, E8, D8);
                                    if (reyEnJaque(tablero, estado) == NO_JAQUE) {
                                        moverReyUnaCasilla(tablero, estado, D8, C8);
                                        if (reyEnJaque(tablero, estado) == NO_JAQUE) {
                                            lista.add(new int[]{E8, C8});
                                        }
                                        moverReyUnaCasilla(tablero, estado, C8, D8);
                                    }
                                    moverReyUnaCasilla(tablero, estado, D8, E8);
                                }
                            }
                        }
                    }
                }
            }


            lista.removeIf(m -> !movimientoValido(m, tablero, estado));
            return lista;
        }


        private static void moverReyUnaCasilla (Pieza[]tablero, EstadoTablero estado,int inicio, int destino){
            tablero[destino] = tablero[inicio];
            tablero[inicio] = null;
            if (estado.turnoBlanco)
                estado.posicionReyBlanco = destino;
            else
                estado.posicionReyNegro = destino;
        }

        //
        private static List<int[]> movimientosDePeon (Pieza[]tablero, EstadoTablero estado,int posicion){

            var lista = new ArrayList<int[]>();

            var turnoBlanco = estado.turnoBlanco;

            Pieza posicionActual;
            Pieza pieza = tablero[posicion];

            if (posicion >= (turnoBlanco ? A2 : A7) && posicion <= (turnoBlanco ? H2 : H7)) {
                if (tablero[posicion + (turnoBlanco ? 8 : -8)] == null && tablero[posicion + (turnoBlanco ? 16 : -16)] == null) {
                    lista.add(new int[]{posicion, posicion + (turnoBlanco ? 16 : -16)});
                }
            }

            var destino = posicion + (turnoBlanco ? 8 : -8);

            //avance una casilla
            if (tablero[destino] == null) {
                if (destino <= H1 || destino >= A8 && destino <= H8) {
                    lista.add(new int[]{posicion, destino, 1});
                    lista.add(new int[]{posicion, destino, 2});
                    lista.add(new int[]{posicion, destino, 3});
                    lista.add(new int[]{posicion, destino, 4});
                } else {
                    lista.add(new int[]{posicion, destino});
                }
            }

            if (turnoBlanco) {
                destino = posicion + 9;
                if (destino < 64 && esCasillaBlanca(destino) == esCasillaBlanca(posicion)) {
                    posicionActual = tablero[destino];
                    if (posicionActual != null) {
                        if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                            if (destino >= A8 && destino <= H8) {
                                lista.add(new int[]{posicion, destino, 1});
                                lista.add(new int[]{posicion, destino, 2});
                                lista.add(new int[]{posicion, destino, 3});
                                lista.add(new int[]{posicion, destino, 4});
                            } else {
                                lista.add(new int[]{posicion, destino});
                            }
                        }
                    } else if (estado.alPaso && posicion >= A5 && posicion <= H5) {
                        if (tablero[destino - 8] == estado.piezaALPaso) {
                            lista.add(new int[]{posicion, destino});
                        }
                    }
                }

                destino = posicion + 7;
                if (destino < 64 && esCasillaBlanca(destino) == esCasillaBlanca(posicion)) {
                    posicionActual = tablero[destino];
                    if (posicionActual != null) {
                        if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                            if (destino >= A8 && destino <= H8) {
                                lista.add(new int[]{posicion, destino, 1});
                                lista.add(new int[]{posicion, destino, 2});
                                lista.add(new int[]{posicion, destino, 3});
                                lista.add(new int[]{posicion, destino, 4});
                            } else {
                                lista.add(new int[]{posicion, destino});
                            }
                        }
                    } else if (estado.alPaso && posicion >= A5 && posicion <= H5) {
                        if (tablero[destino - 8] == estado.piezaALPaso) {
                            lista.add(new int[]{posicion, destino});
                        }
                    }
                }
            } else {
                destino = posicion - 9;
                if (destino >= 0 && esCasillaBlanca(destino) == esCasillaBlanca(posicion)) {
                    posicionActual = tablero[destino];
                    if (posicionActual != null) {
                        if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                            if (destino >= A1 && destino <= H1) {
                                lista.add(new int[]{posicion, destino, 1});
                                lista.add(new int[]{posicion, destino, 2});
                                lista.add(new int[]{posicion, destino, 3});
                                lista.add(new int[]{posicion, destino, 4});
                            } else {
                                lista.add(new int[]{posicion, destino});
                            }
                        }
                    } else if (estado.alPaso && posicion >= A4 && posicion <= H4) {
                        if (tablero[destino + 8] == estado.piezaALPaso) {
                            lista.add(new int[]{posicion, destino});
                        }
                    }
                }

                destino = posicion - 7;
                if (destino >= 0 && esCasillaBlanca(destino) == esCasillaBlanca(posicion)) {
                    posicionActual = tablero[destino];
                    if (posicionActual != null) {
                        if (posicionActual.esBlanca() != pieza.esBlanca() && !(posicionActual instanceof Rey)) {
                            if (destino >= A1 && destino <= H1) {
                                lista.add(new int[]{posicion, destino, 1});
                                lista.add(new int[]{posicion, destino, 2});
                                lista.add(new int[]{posicion, destino, 3});
                                lista.add(new int[]{posicion, destino, 4});
                            } else {
                                lista.add(new int[]{posicion, destino});
                            }
                        }
                    } else if (estado.alPaso && posicion >= A4 && posicion <= H4) {
                        if (tablero[destino + 8] == estado.piezaALPaso) {
                            lista.add(new int[]{posicion, destino});
                        }
                    }
                }
            }


            lista.removeIf(m -> !movimientoValido(m, tablero, estado));
            return lista;
        }
    }
//
//}
