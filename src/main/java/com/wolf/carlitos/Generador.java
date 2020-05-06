/*
 * To change pieza license header, choose License Headers in Project Properties.
 * To change pieza template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import java.lang.annotation.Target;
import java.util.*;

//import static com.wolf.carlitos.Utilidades.movimientoValido;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Utilidades.*;
import static java.lang.Math.abs;

/**
 * @author carlos
 */
public class Generador {

    private int piezaJaque;
    private final List<int[]> movimientos = new ArrayList<>();

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
            var movimientos = new ArrayList<Integer>();


            boolean colorInicio = esCasillaBlanca(i);

            if (i + 1 < 64 && colorInicio != esCasillaBlanca(i + 1))
                movimientos.add(i + 1);
            if (i + 7 < 64 && colorInicio == esCasillaBlanca(i + 7))
                movimientos.add(i + 7);
            if (i + 8 < 64 && colorInicio != esCasillaBlanca(i + 8))
                movimientos.add(i + 8);
            if (i + 9 < 64 && colorInicio == esCasillaBlanca(i + 9))
                movimientos.add(i + 9);

            if (i - 1 >= 0 && colorInicio != esCasillaBlanca(i - 1))
                movimientos.add(i - 1);
            if (i - 7 >= 0 && colorInicio == esCasillaBlanca(i - 7))
                movimientos.add(i - 7);
            if (i - 8 >= 0 && colorInicio != esCasillaBlanca(i - 8))
                movimientos.add(i - 8);
            if (i - 9 >= 0 && colorInicio == esCasillaBlanca(i - 9))
                movimientos.add(i - 9);

            movimientosRey.put(i, movimientos);
        }
    }

    private static void llenarMovimientosDeCaballo() {
        for (int i = 0; i < 64; i++) {
            var movimientos = new ArrayList<Integer>();
            var colorInicio = esCasillaBlanca(i);

            if (i + 6 < 64 && colorInicio != esCasillaBlanca(i + 6))
                movimientos.add(i + 6);
            if (i + 10 < 64 && colorInicio != esCasillaBlanca(i + 10))
                movimientos.add(i + 10);
            if (i + 15 < 64 && colorInicio != esCasillaBlanca(i + 15))
                movimientos.add(i + 15);
            if (i + 17 < 64 && colorInicio != esCasillaBlanca(i + 17))
                movimientos.add(i + 17);

            if (i - 6 >= 0 && colorInicio != esCasillaBlanca(i - 6))
                movimientos.add(i - 6);
            if (i - 10 >= 0 && colorInicio != esCasillaBlanca(i - 10))
                movimientos.add(i - 10);
            if (i - 15 >= 0 && colorInicio != esCasillaBlanca(i - 15))
                movimientos.add(i - 15);
            if (i - 17 >= 0 && colorInicio != esCasillaBlanca(i - 17))
                movimientos.add(i - 17);

            movimientosCaballo.put(i, movimientos);
        }
    }

    private static void llenarMovimientosDeTorre() {
        for (int i = 0; i < 64; i++) {
            var movimientos = new ArrayList<List<Integer>>(4);

            var movimientosInterna = new ArrayList<Integer>();
            int base = i + 8;

            while (base < 64) {
                movimientosInterna.add(base);
                base += 8;
            }
            movimientos.add(0, movimientosInterna);

            movimientosInterna = new ArrayList<>();
            base = i - 8;
            while (base >= 0) {
                movimientosInterna.add(base);
                base -= 8;
            }

            movimientos.add(1, movimientosInterna);

            movimientosInterna = new ArrayList<>();


            int residuo = i / 8;

            int fin = 7 + (8 * residuo);

            for (int j = i + 1; j <= fin; j++) {
                movimientosInterna.add(j);
            }
            movimientos.add(2, movimientosInterna);

            residuo = i / 8;

            fin = 8 * residuo;

            movimientosInterna = new ArrayList<Integer>();

            for (int j = i - 1; j >= fin; j--) {
                movimientosInterna.add(j);
            }
            movimientos.add(3, movimientosInterna);

            movimientosTorre.put(i, movimientos);
        }
    }

    private static void llenarMovimientosDeAlfil() {


        for (int i = 0; i < 64; i++) {

            boolean colorInicio = esCasillaBlanca(i);

            var movimientos = new ArrayList<List<Integer>>(4);

            var movimientosInterna = new ArrayList<Integer>();

            int base = i + 9;

            while (base < 64 && colorInicio == esCasillaBlanca(base)) {
                movimientosInterna.add(base);
                base += 9;
            }
            movimientos.add(0, movimientosInterna);

            movimientosInterna = new ArrayList<Integer>();
            base = i - 9;
            while (base >= 0 && colorInicio == esCasillaBlanca(base)) {
                movimientosInterna.add(base);
                base -= 9;
            }
            movimientos.add(1, movimientosInterna);

            movimientosInterna = new ArrayList<Integer>();
            base = i + 7;
            while (base < 64 && colorInicio == esCasillaBlanca(base)) {
                movimientosInterna.add(base);
                base += 7;
            }
            movimientos.add(2, movimientosInterna);


            movimientosInterna = new ArrayList<Integer>();
            base = i - 7;
            while (base >= 0 && colorInicio == esCasillaBlanca(base)) {
                movimientosInterna.add(base);
                base -= 7;
            }
            movimientos.add(3, movimientosInterna);


            movimientosAlfil.put(i, movimientos);

        }
    }


    public List<int[]> generarMovimientos(Pieza[] tablero, EstadoTablero estado) {

        piezaJaque = reyEnJaque(tablero, estado);
        
        for (int i = 0; i < tablero.length; i++) {

            var pieza = tablero[i];

            if (pieza != null && pieza.esBlanca == estado.turnoBlanco) {
                switch (pieza.tipo){
                    case PEON:
                        movimientosDePeon(tablero,estado,i);
                        break;
                    case CABALLO:
                        movimientosDeCaballo(tablero, estado, i);
                        break;
                    case ALFIL:
                        movimientosDeAlfil(tablero, estado, i);
                        break;
                    case TORRE:
                        movimientosDeTorre(tablero, estado, i);
                        break;
                    case DAMA:
                        movimientosDeDama(tablero, estado, i);
                        break;
                    case REY:
                        movimientosDeRey(tablero, estado, i);
                        break;
                }
            }

        }
        piezaJaque = 0;
        return movimientos;
    }

    public void movimientosDeTorre(Pieza[] tablero, EstadoTablero estado, int posicion) {
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

                procesarLinea(tablero, posicion, pieza, horizontal1);
                procesarLinea(tablero, posicion, pieza, horizontal2);

            }
            if (vertical) {

                procesarLinea(tablero, posicion, pieza, vertical1);
                procesarLinea(tablero, posicion, pieza, vertical2);

            }
            return;
        }


        for (int i = 0; i < movimientosDeTorre.size(); i++) {
            var recorrido = movimientosDeTorre.get(i);
            procesarLineaValidado(tablero, posicion, pieza, recorrido, estado);

        }
        
    }

    private  void procesarLinea(Pieza[] tablero, int posicion, Pieza pieza, List<Integer> horizontal1) {
        Pieza posicionActual;
        for (int j = 0; j < horizontal1.size(); j++) {
            var mov = horizontal1.get(j);
            posicionActual = tablero[mov];

            if (posicionActual == null) {
                movimientos.add(new int[]{posicion, mov});
                continue;
            }

            var sonMismoColor = posicionActual.esBlanca == pieza.esBlanca;

            if (!sonMismoColor && !(posicionActual.tipo == REY)) {
                movimientos.add(new int[]{posicion, mov});
                break;
            }

            if (sonMismoColor) break;
        }
    }

    private  void procesarLineaValidado(Pieza[] tablero, int posicion, Pieza pieza, List<Integer> horizontal1, EstadoTablero estado) {
        Pieza posicionActual;
        for (int j = 0; j < horizontal1.size(); j++) {
            var mov = horizontal1.get(j);
            posicionActual = tablero[mov];

            if (posicionActual == null) {
                validarYAgregar(tablero, estado, posicion, mov);
                continue;
            }

            var sonMismoColor = posicionActual.esBlanca == pieza.esBlanca;

            if (!sonMismoColor && !(posicionActual.tipo == REY)) {
                validarYAgregar(tablero, estado, posicion, mov);
                break;
            }

            if (sonMismoColor) break;
        }
    }

    private static boolean isVertical(Pieza[] tablero, EstadoTablero estado, int posicion, Pieza pieza, List<Integer> vertical1, List<Integer> vertical2) {
        if (!vertical1.isEmpty()) {
            var mov = vertical1.get(0);
            var pi = tablero[mov];

            if (pi == null || pi.esBlanca != pieza.esBlanca && !(pi.tipo == REY)) {
                var m = new int[]{posicion, mov};
                return movimientoValido(m, tablero, estado);
            } else if (!vertical2.isEmpty()) {
                mov = vertical2.get(0);
                pi = tablero[mov];

                if (pi == null || pi.esBlanca != pieza.esBlanca && !(pi.tipo == REY)) {
                    var m = new int[]{posicion, mov};
                    return movimientoValido(m, tablero, estado);

                }

            }
        } else if (!vertical2.isEmpty()) {
            var mov = vertical2.get(0);
            var pi = tablero[mov];

            if (pi == null || pi.esBlanca != pieza.esBlanca && !(pi.tipo == REY)) {
                var m = new int[]{posicion, mov};
                return movimientoValido(m, tablero, estado);

            }
        }
        return true;
    }

    private void movimientosDeDama(Pieza[] tablero, EstadoTablero estado, int posicion) {
        Pieza pieza;

        pieza = tablero[posicion];

        var movimientosAlfil = Generador.movimientosAlfil.get(posicion);
        var movimietosDeTorre = Generador.movimientosTorre.get(posicion);

        var digonal1Arriba = movimientosAlfil.get(0);
        var diagonal1Abajo = movimientosAlfil.get(1);
        var diagonal2Arriba = movimientosAlfil.get(2);
        var diagonal2Abajo = movimientosAlfil.get(3);

        var vertical1 = movimietosDeTorre.get(0);
        var vertical2 = movimietosDeTorre.get(1);
        var horizontal1 = movimietosDeTorre.get(2);
        var horizontal2 = movimietosDeTorre.get(3);

        if (!enJaque()) {

            boolean diagonal1;
            boolean diagonal2;

            diagonal1 = isVertical(tablero, estado, posicion, pieza, digonal1Arriba, diagonal1Abajo);
            diagonal2 = isVertical(tablero, estado, posicion, pieza, diagonal2Arriba, diagonal2Abajo);

            if (diagonal2) {

                procesarLinea(tablero, posicion, pieza, diagonal2Arriba);
                procesarLinea(tablero, posicion, pieza, diagonal2Abajo);

            }
            if (diagonal1) {

                procesarLinea(tablero, posicion, pieza, digonal1Arriba);
                procesarLinea(tablero, posicion,  pieza, diagonal1Abajo);

            }


            boolean vertical;
            boolean horizontal;


            vertical = isVertical(tablero, estado, posicion, pieza, vertical1, vertical2);
            horizontal = isVertical(tablero, estado, posicion, pieza, horizontal1, horizontal2);

            if (horizontal) {

                procesarLinea(tablero, posicion,  pieza, horizontal1);
                procesarLinea(tablero, posicion,  pieza, horizontal2);

            }
            if (vertical) {

                procesarLinea(tablero, posicion,  pieza, vertical1);
                procesarLinea(tablero, posicion,  pieza, vertical2);

            }


            return;
        }

        procesarLineaValidado(tablero, posicion,  pieza, diagonal1Abajo, estado);
        procesarLineaValidado(tablero, posicion,  pieza, digonal1Arriba, estado);
        procesarLineaValidado(tablero, posicion,  pieza, diagonal2Abajo, estado);
        procesarLineaValidado(tablero, posicion,  pieza, diagonal2Arriba, estado);
        procesarLineaValidado(tablero, posicion,  pieza, vertical1, estado);
        procesarLineaValidado(tablero, posicion,  pieza, vertical2, estado);
        procesarLineaValidado(tablero, posicion,  pieza, horizontal1, estado);
        procesarLineaValidado(tablero, posicion,  pieza, horizontal2, estado);

        
    }

    private void movimientosDeCaballo(Pieza[] tablero, EstadoTablero estado, int posicion) {
        Pieza posicionActual;
        Pieza pieza = tablero[posicion];


        var movimientosCaballo = Generador.movimientosCaballo.get(posicion);

        for (int i = 0; i < movimientosCaballo.size(); i++) {
            var mov = movimientosCaballo.get(i);
            posicionActual = tablero[mov];

            if (posicionActual == null || (posicionActual.esBlanca != pieza.esBlanca && !(posicionActual.tipo == REY))) {
                validarYAgregar(tablero,estado,posicion,mov);
            }

        }
        

    }
    
    private boolean enJaque() {
        return piezaJaque != NO_JAQUE;
    }
    
    public void movimientosDeAlfil(Pieza[] tablero, EstadoTablero estado, int posicion) {

        Pieza pieza = tablero[posicion];

        var movimientosAlfil = Generador.movimientosAlfil.get(posicion);

        var digonal1Arriba = movimientosAlfil.get(0);
        var diagonal1Abajo = movimientosAlfil.get(1);
        var diagonal2Arriba = movimientosAlfil.get(2);
        var diagonal2Abajo = movimientosAlfil.get(3);


        if (!enJaque()) {

            boolean diagonal1;
            boolean diagonal2;

            diagonal1 = isVertical(tablero, estado, posicion, pieza, digonal1Arriba, diagonal1Abajo);
            diagonal2 = isVertical(tablero, estado, posicion, pieza, diagonal2Arriba, diagonal2Abajo);

            if (diagonal2) {

                procesarLinea(tablero, posicion,  pieza, diagonal2Arriba);
                procesarLinea(tablero, posicion,  pieza, diagonal2Abajo);

            }
            if (diagonal1) {

                procesarLinea(tablero, posicion,  pieza, digonal1Arriba);
                procesarLinea(tablero, posicion,  pieza, diagonal1Abajo);

            }
            return;
        }


        procesarLineaValidado(tablero, posicion,  pieza, diagonal1Abajo, estado);
        procesarLineaValidado(tablero, posicion,  pieza, digonal1Arriba, estado);
        procesarLineaValidado(tablero, posicion,  pieza, diagonal2Abajo, estado);
        procesarLineaValidado(tablero, posicion,  pieza, diagonal2Arriba, estado);

        


    }

    private  void movimientosDeRey(Pieza[] tablero, EstadoTablero estado, int posicion) {

        Pieza pieza = tablero[posicion];

        var movimietosRey = Generador.movimientosRey.get(posicion);

        for (int i = 0; i < movimietosRey.size(); i++) {

            int mov = movimietosRey.get(i);
            var m = tablero[mov];

            if (m == null || m.esBlanca != pieza.esBlanca) {

                var posicionRey = estado.turnoBlanco ? estado.posicionReyNegro : estado.posicionReyBlanco;
                var distancia = abs(mov - posicionRey);

                // TODO optimizar estas condiciones
                if (distancia == 1 || distancia == 7 || distancia == 8 || distancia == 9) {

                    if (distancia == 1 && esCasillaBlanca(posicionRey) == esCasillaBlanca(mov)) {
                        validarYAgregar(tablero, estado, posicion,  mov);
                    } else if (distancia == 7 && esCasillaBlanca(posicionRey) != esCasillaBlanca(mov)) {
                        validarYAgregar(tablero, estado, posicion,  mov);
                    } else if (distancia == 9 && esCasillaBlanca(posicionRey) != esCasillaBlanca(mov)) {
                        validarYAgregar(tablero, estado, posicion,  mov);
                    }

                    continue;
                }

                validarYAgregar(tablero, estado, posicion,  mov);

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
                                        movimientos.add(new int[]{E1, G1});
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
                                        movimientos.add(new int[]{E1, C1});
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
                                        movimientos.add(new int[]{E8, G8});
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
                                        movimientos.add(new int[]{E8, C8});
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
        
    }


    private static void moverReyUnaCasilla(Pieza[] tablero, EstadoTablero estado, int inicio, int destino) {
        tablero[destino] = tablero[inicio];
        tablero[inicio] = null;
        if (estado.turnoBlanco)
            estado.posicionReyBlanco = destino;
        else
            estado.posicionReyNegro = destino;
    }

    private void movimientosDePeon(Pieza[] tablero, EstadoTablero estado, int posicion) {
        
        var turnoBlanco = estado.turnoBlanco;
        Pieza pieza = tablero[posicion];

        if (posicion >= (turnoBlanco ? A2 : A7) && posicion <= (turnoBlanco ? H2 : H7)) {
            if (tablero[posicion + (turnoBlanco ? 8 : -8)] == null && tablero[posicion + (turnoBlanco ? 16 : -16)] == null) {
                validarYAgregar(tablero, estado, posicion, posicion + (turnoBlanco ? 16 : -16));
            }
        }

        var destino = posicion + (turnoBlanco ? 8 : -8);

        //avance una casilla
        if (tablero[destino] == null) {
            var m = new int[]{posicion,destino};
            if (destino <= H1 || destino >= A8 && destino <= H8) {
                if(movimientoValido(m,tablero,estado)){
                    movimientos.add(new int[]{posicion, destino, 1});
                    movimientos.add(new int[]{posicion, destino, 2});
                    movimientos.add(new int[]{posicion, destino, 3});
                    movimientos.add(new int[]{posicion, destino, 4});
                }

            } else {
                validarYAgregar(tablero,estado,posicion,destino);
            }
        }
        avanceDiagonal(tablero, estado, posicion, turnoBlanco, pieza, 9);
        avanceDiagonal(tablero, estado, posicion, turnoBlanco, pieza, 7);

        
    }

    private void avanceDiagonal(Pieza[] tablero, EstadoTablero estado, int posicion,  boolean turnoBlanco, Pieza pieza, int i) {
        int destino;
        Pieza posicionActual;
        destino = posicion + (turnoBlanco ? i : -i);
        if ((destino >= 0 && destino < 64) && esCasillaBlanca(destino) == esCasillaBlanca(posicion)) {
            posicionActual = tablero[destino];
            var m = new int[]{posicion, destino};
            if (posicionActual != null) {
                if (posicionActual.esBlanca != pieza.esBlanca && !(posicionActual.tipo == REY)) {
                    if (destino >= A8 || destino <= H1) {
                        if (movimientoValido(m, tablero, estado)) {
                            movimientos.add(new int[]{posicion, destino, 1});
                            movimientos.add(new int[]{posicion, destino, 2});
                            movimientos.add(new int[]{posicion, destino, 3});
                            movimientos.add(new int[]{posicion, destino, 4});
                        }
                    } else {
                        validarYAgregar(tablero, estado, posicion,  destino);
                    }
                }
            } else if (estado.alPaso && posicion >= (turnoBlanco ? A5 : A4) && posicion <= (turnoBlanco ? H5 : H4)) {
                if (tablero[destino - (turnoBlanco ? 8 : -8)] == estado.piezaALPaso) {
                    validarYAgregar(tablero, estado, posicion,  destino);
                }
            }
        }
    }

    private  void validarYAgregar(Pieza[] tablero, EstadoTablero estado, int posicion, int i) {
        var mm = new int[]{posicion, i};
        if (movimientoValido(mm, tablero, estado))
            movimientos.add(mm);
    }
}
