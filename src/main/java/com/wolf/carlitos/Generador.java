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
import static java.lang.Math.log;

/**
 * @author carlos
 */
public class Generador {

    private int piezaJaque;
    private Movimientos movimientos;

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


    public void generarMovimientos(int[] pieza, int[] color, long estado,Movimientos movimientos) {
        
        this.movimientos = movimientos;

        piezaJaque = reyEnJaque(pieza,color, estado);

        for (int i = 0; i < pieza.length; i++) {

            var piezaActual = pieza[i];

            if (piezaActual != NOPIEZA && color[i] == BLANCO == esTurnoBlanco(estado)) {
                switch (piezaActual){
                    case PEON:
                        movimientosDePeon(pieza,color,estado,i);
                        break;
                    case CABALLO:
                        movimientosDeCaballo(pieza,color, estado, i);
                        break;
                    case ALFIL:
                        movimientosDeAlfil(pieza,color, estado, i);
                        break;
                    case TORRE:
                        movimientosDeTorre(pieza,color, estado, i);
                        break;
                    case DAMA:
                        movimientosDeDama(pieza,color, estado, i);
                        break;
                    case REY:
                        movimientosDeRey(pieza,color, estado, i);
                        break;
                }
            }

        }
        piezaJaque = 0;
    }

    public void movimientosDeTorre(int[] tablero,int[]color, long estado, int posicion) {
        int pieza = tablero[posicion];

        var movimientosDeTorre = Generador.movimientosTorre.get(posicion);

        if (!enJaque()) {

            boolean vertical;
            boolean horizontal;

            var vertical1 = movimientosDeTorre.get(0);
            var vertical2 = movimientosDeTorre.get(1);
            var horizontal1 = movimientosDeTorre.get(2);
            var horizontal2 = movimientosDeTorre.get(3);

            vertical = isVertical(tablero,color, estado, posicion, pieza, vertical1, vertical2);
            horizontal = isVertical(tablero,color, estado, posicion, pieza, horizontal1, horizontal2);

            if (horizontal) {

                procesarLinea(tablero,color, posicion, horizontal1);
                procesarLinea(tablero,color, posicion, horizontal2);

            }
            if (vertical) {

                procesarLinea(tablero,color, posicion, vertical1);
                procesarLinea(tablero,color, posicion, vertical2);

            }
            return;
        }


        for (int i = 0; i < movimientosDeTorre.size(); i++) {
            var recorrido = movimientosDeTorre.get(i);
            procesarLineaValidado(tablero,color, posicion, recorrido, estado);

        }
        
    }

    private  void procesarLinea(int[] tablero,int[]color, int posicion, List<Integer> horizontal1) {
        int posicionActual;
        for (int j = 0; j < horizontal1.size(); j++) {
            var mov = horizontal1.get(j);
            posicionActual = tablero[mov];

            if (posicionActual == NOPIEZA) {
                movimientos.add(posicion << 6 | mov);
                continue;
            }

            var sonMismoColor = color[mov] == color[posicion];

            if (!sonMismoColor && !(posicionActual == REY)) {
                movimientos.add(posicion << 6 | mov);
            }
            break;
        }
    }
    private  void procesarLineaValidado(int[] tablero,int[]color, int posicion, List<Integer> horizontal1, long estado) {
        int posicionActual;
        for (int j = 0; j < horizontal1.size(); j++) {
            var mov = horizontal1.get(j);
            posicionActual = tablero[mov];

            if (posicionActual == NOPIEZA) {
                validarYAgregar(tablero,color, estado, posicion, mov);
                continue;
            }

            var sonMismoColor = color[mov] == color[posicion];

            if (!sonMismoColor && !(posicionActual == REY)) {
                validarYAgregar(tablero,color, estado, posicion, mov);
                break;
            }

            break;
        }
    }

    private static boolean isVertical(int[] tablero,int[]color, long estado, int posicion, int pieza, List<Integer> vertical1, List<Integer> vertical2) {
        if (!vertical1.isEmpty()) {
            var mov = vertical1.get(0);
            var pi = tablero[mov];

            if (pi == NOPIEZA || color[mov] != color[posicion] && !(pi == REY)) {
                var m = posicion << 6 | mov;
                return movimientoValido(m, tablero,color,estado);
            } else if (!vertical2.isEmpty()) {
                mov = vertical2.get(0);
                pi = tablero[mov];

                if (pi == NOPIEZA || color[mov] != color[posicion] && !(pi == REY)) {
                    var m = posicion << 6 | mov;
                    return movimientoValido(m, tablero,color, estado);

                }

            }
        } else if (!vertical2.isEmpty()) {
            var mov = vertical2.get(0);
            var pi = tablero[mov];

            if (pi == NOPIEZA || color[mov] != color[posicion] && !(pi == REY)) {
                var m = posicion << 6 | mov;
                return movimientoValido(m, tablero,color,estado);

            }
        }
        return true;
    }

    private void movimientosDeDama(int[] tablero,int[]color, long estado, int posicion) {
        int pieza;

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

            diagonal1 = isVertical(tablero,color, estado, posicion, pieza, digonal1Arriba, diagonal1Abajo);
            diagonal2 = isVertical(tablero,color, estado, posicion, pieza, diagonal2Arriba, diagonal2Abajo);

            if (diagonal2) {

                procesarLinea(tablero,color, posicion, diagonal2Arriba);
                procesarLinea(tablero,color, posicion, diagonal2Abajo);

            }
            if (diagonal1) {

                procesarLinea(tablero,color, posicion, digonal1Arriba);
                procesarLinea(tablero,color, posicion, diagonal1Abajo);
            }


            boolean vertical;
            boolean horizontal;


            vertical = isVertical(tablero,color, estado, posicion, pieza, vertical1, vertical2);
            horizontal = isVertical(tablero,color, estado, posicion, pieza, horizontal1, horizontal2);

            if (horizontal) {

                procesarLinea(tablero,color, posicion, horizontal1);
                procesarLinea(tablero,color, posicion, horizontal2);

            }
            if (vertical) {

                procesarLinea(tablero,color, posicion, vertical1);
                procesarLinea(tablero,color, posicion, vertical2);

            }


            return;
        }

        procesarLineaValidado(tablero,color, posicion, diagonal1Abajo, estado);
        procesarLineaValidado(tablero,color, posicion, digonal1Arriba, estado);
        procesarLineaValidado(tablero,color, posicion, diagonal2Abajo, estado);
        procesarLineaValidado(tablero,color, posicion, diagonal2Arriba, estado);
        procesarLineaValidado(tablero,color, posicion, vertical1, estado);
        procesarLineaValidado(tablero,color, posicion, vertical2, estado);
        procesarLineaValidado(tablero,color, posicion, horizontal1, estado);
        procesarLineaValidado(tablero,color, posicion, horizontal2, estado);

        
    }

    private void movimientosDeCaballo(int[] tablero,int[]color, long estado, int posicion) {
        int posicionActual;

        var movimientosCaballo = Generador.movimientosCaballo.get(posicion);

        for (int i = 0; i < movimientosCaballo.size(); i++) {
            var mov = movimientosCaballo.get(i);
            posicionActual = tablero[mov];

            if (posicionActual == NOPIEZA || (color[mov] != color[posicion] && !(posicionActual == REY))) {
                validarYAgregar(tablero,color,estado,posicion,mov);
            }

        }
        

    }
    
    private boolean enJaque() {
        return piezaJaque != NO_JAQUE;
    }
    
    public void movimientosDeAlfil(int[] tablero,int[] color, long estado, int posicion) {

        int pieza = tablero[posicion];

        var movimientosAlfil = Generador.movimientosAlfil.get(posicion);

        var digonal1Arriba = movimientosAlfil.get(0);
        var diagonal1Abajo = movimientosAlfil.get(1);
        var diagonal2Arriba = movimientosAlfil.get(2);
        var diagonal2Abajo = movimientosAlfil.get(3);


        if (!enJaque()) {

            boolean diagonal1;
            boolean diagonal2;

            diagonal1 = isVertical(tablero,color, estado, posicion, pieza, digonal1Arriba, diagonal1Abajo);
            diagonal2 = isVertical(tablero,color, estado, posicion, pieza, diagonal2Arriba, diagonal2Abajo);

            if (diagonal2) {

                procesarLinea(tablero,color, posicion, diagonal2Arriba);
                procesarLinea(tablero,color, posicion, diagonal2Abajo);

            }
            if (diagonal1) {

                procesarLinea(tablero,color, posicion, digonal1Arriba);
                procesarLinea(tablero,color, posicion, diagonal1Abajo);

            }
            return;
        }


        procesarLineaValidado(tablero,color, posicion, diagonal1Abajo, estado);
        procesarLineaValidado(tablero,color, posicion, digonal1Arriba, estado);
        procesarLineaValidado(tablero,color, posicion, diagonal2Abajo, estado);
        procesarLineaValidado(tablero,color, posicion, diagonal2Arriba, estado);

        


    }

    private  void movimientosDeRey(int[] tablero,int[]color, long estado, int posicion) {

        var movimietosRey = Generador.movimientosRey.get(posicion);

        for (int i = 0; i < movimietosRey.size(); i++) {

            int mov = movimietosRey.get(i);
            var m = tablero[mov];

            if (m == NOPIEZA || color[mov] != color[posicion]) {

                var posicionRey = esTurnoBlanco(estado) ? posicionRey(estado,27) : posicionRey(estado,21);
                var distancia = abs(mov - posicionRey);

                // TODO optimizar estas condiciones
                if (distancia == 1 || distancia == 7 || distancia == 8 || distancia == 9) {

                    if (distancia == 1 && esCasillaBlanca(posicionRey) == esCasillaBlanca(mov)) {
                        validarYAgregar(tablero,color, estado, posicion,  mov);
                    } else if (distancia == 7 && esCasillaBlanca(posicionRey) != esCasillaBlanca(mov)) {
                        validarYAgregar(tablero,color, estado, posicion,  mov);
                    } else if (distancia == 9 && esCasillaBlanca(posicionRey) != esCasillaBlanca(mov)) {
                        validarYAgregar(tablero,color, estado, posicion,  mov);
                    }

                    continue;
                }

                validarYAgregar(tablero,color, estado, posicion,  mov);

            }
        }
        // retornar si no hay enroques disponibles
        if((estado & 0b1111) > 0) return;

        if (esTurnoBlanco(estado)) {
            if ((estado & 0b000000_000000_000_00_000_0_000000_0_0_00_11L) > 0) {
                if (reyEnJaque(tablero,color, estado) == NO_JAQUE) {
                    if ((estado & 1) > 0 && posicionRey(estado, 27) != G2) {

                        // TODO revisar si la posicion sirve, puede estar de sobra
                        if (posicion == E1) {
                            if (tablero[F1] == NOPIEZA && tablero[G1] == NOPIEZA) {
                                long estadoCopia = moverReyUnaCasilla(tablero,color, estado, E1, F1);
                                if (reyEnJaque(tablero,color, estadoCopia) == NO_JAQUE) {
                                    estadoCopia = moverReyUnaCasilla(tablero,color, estado, F1, G1);
                                    if (reyEnJaque(tablero,color, estadoCopia) == NO_JAQUE) {
                                        movimientos.add(E1 << 6 | G1);
                                    }
                                    moverReyUnaCasilla(tablero,color, estado, G1, F1);
                                }
                                moverReyUnaCasilla(tablero,color, estado, F1, E1);
                            }
                        }
                    }

                    if ((estado & 2) > 0 && posicionRey(estado,27) != B2 && posicionRey(estado,27) != C2) {

                        if (posicion == E1) {
                            if (tablero[D1] == NOPIEZA && tablero[C1] == NOPIEZA && tablero[B1] == NOPIEZA) {
                                long ec = moverReyUnaCasilla(tablero,color, estado, E1, D1);
                                if (reyEnJaque(tablero,color, ec) == NO_JAQUE) {
                                    ec = moverReyUnaCasilla(tablero,color, estado, D1, C1);
                                    if (reyEnJaque(tablero,color, ec) == NO_JAQUE) {
                                        movimientos.add(E1 << 6 | C1);
                                    }
                                    moverReyUnaCasilla(tablero,color, estado, C1, D1);
                                }
                                moverReyUnaCasilla(tablero,color, estado, D1, E1);
                            }
                        }
                    }
                }
            }
        } else {
            if ((estado & 0b000000_000000_000_00_000_0_000000_0_0_11_00L) > 0) {
                if (reyEnJaque(tablero,color, estado) == NO_JAQUE) {
                    if ((estado & 4) > 0 && posicionRey(estado,21) != G7) {

                        // TODO revisar si la posicion sirve, puede estar de sobra
                        if (posicion == E8) {
                            if (tablero[F8] == NOPIEZA && tablero[G8] == NOPIEZA) {
                                long ec = moverReyUnaCasilla(tablero,color, estado, E8, F8);
                                if (reyEnJaque(tablero,color, ec) == NO_JAQUE) {
                                    ec = moverReyUnaCasilla(tablero,color, estado, F8, G8);
                                    if (reyEnJaque(tablero,color, ec) == NO_JAQUE) {
                                        movimientos.add(E8 << 6 | G8);
                                    }
                                    moverReyUnaCasilla(tablero,color, estado, G8, F8);
                                }
                                moverReyUnaCasilla(tablero,color, estado, F8, E8);
                            }
                        }
                    }

                    if ((estado & 8)>0 && posicionRey(estado,21) != B7 && posicionRey(estado,21) != C7) {

                        if (posicion == E8) {
                            if (tablero[D8] == NOPIEZA && tablero[C8] == NOPIEZA && tablero[B8] == NOPIEZA) {
                                long ec = moverReyUnaCasilla(tablero,color, estado, E8, D8);
                                if (reyEnJaque(tablero,color, ec) == NO_JAQUE) {
                                    ec = moverReyUnaCasilla(tablero,color, estado, D8, C8);
                                    if (reyEnJaque(tablero,color, ec) == NO_JAQUE) {
                                        movimientos.add(E8 << 6 | C8);
                                    }
                                    moverReyUnaCasilla(tablero,color, estado, C8, D8);
                                }
                                moverReyUnaCasilla(tablero,color, estado, D8, E8);
                            }
                        }
                    }
                }
            }
        }
        
    }


    private static long moverReyUnaCasilla(int[] tablero,int[]color, long estado, int inicio, int destino) {
        tablero[destino] = tablero[inicio];
        tablero[inicio] = NOPIEZA;
        color[destino] = color[inicio];
        color[inicio] = NOCOLOR;
        if (esTurnoBlanco(estado))
            estado = estado & 0b111111_000000_111_11_111_1_111111_1_1_11_11L | (long)destino << 21;
        else
            estado = estado & 0b000000_111111_111_11_111_1_111111_1_1_11_11L | (long)destino << 27;
        return  estado;
    }

    private void movimientosDePeon(int[] tablero,int[]color, long estado, int posicion) {
        
        var turnoBlanco = esTurnoBlanco(estado);
        int pieza = tablero[posicion];

        if (posicion >= (turnoBlanco ? A2 : A7) && posicion <= (turnoBlanco ? H2 : H7)) {
            if (tablero[posicion + (turnoBlanco ? 8 : -8)] == NOPIEZA && tablero[posicion + (turnoBlanco ? 16 : -16)] == NOPIEZA) {
                validarYAgregar(tablero,color, estado, posicion, posicion + (turnoBlanco ? 16 : -16));
            }
        }

        var destino = posicion + (turnoBlanco ? 8 : -8);
        //avance una casilla
        if (tablero[destino] == NOPIEZA) {
            var m = posicion << 6 | destino;
            if (destino <= H1 || destino >= A8 && destino <= H8) {
                if(movimientoValido(m,tablero,color,estado)){
                    movimientos.add(posicion << 6  | destino | 1 << 12);
                    movimientos.add(posicion << 6  | destino | 2 << 12);
                    movimientos.add(posicion << 6  | destino | 3 << 12);
                    movimientos.add(posicion << 6  | destino | 4 << 12);
                }

            } else {
                validarYAgregar(tablero,color,estado,posicion,destino);
            }
        }
        avanceDiagonal(tablero,color, estado, posicion, turnoBlanco, pieza, 9);
        avanceDiagonal(tablero,color, estado, posicion, turnoBlanco, pieza, 7);

        
    }

    private void avanceDiagonal(int[] tablero,int[]color, long estado, int posicion,  boolean turnoBlanco, int pieza, int i) {
        int destino;
        int posicionActual;
        destino = posicion + (turnoBlanco ? i : -i);
        if ((destino >= 0 && destino < 64) && esCasillaBlanca(destino) == esCasillaBlanca(posicion)) {
            posicionActual = tablero[destino];
            var m = posicion << 6 | destino;
            if (posicionActual != NOPIEZA) {
                if (color[destino] == BLANCO != (color[posicion] == BLANCO) && !(posicionActual == REY)) {
                    if (destino >= A8 || destino <= H1) {
                        if (movimientoValido(m, tablero,color, estado)) {
                            movimientos.add(posicion << 6  | destino | 1 << 12);
                            movimientos.add(posicion << 6  | destino | 2 << 12);
                            movimientos.add(posicion << 6  | destino | 3 << 12);
                            movimientos.add(posicion << 6  | destino | 4 << 12);
                        }
                    } else {
                        validarYAgregar(tablero,color, estado, posicion,  destino);
                    }
                }
            } else if (alPaso(estado) && posicion >= (turnoBlanco ? A5 : A4) && posicion <= (turnoBlanco ? H5 : H4)) {
                if (destino == (estado >> 6 & 0b111111)) {
                    validarYAgregar(tablero,color, estado, posicion,  destino);
                }
            }
        }
    }

    private  void validarYAgregar(int[] tablero,int[]color, long estado, int posicion, int i) {
        var mm = posicion << 6 | i;
        if (movimientoValido(mm, tablero,color, estado))
            movimientos.add(mm);
    }
}
