package com.wolf.carlitos;


import javax.print.DocFlavor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.wolf.carlitos.Constantes.*;
import static com.wolf.carlitos.Utilidades.actualizarTablero;
import static com.wolf.carlitos.Utilidades.convertirAPosicion;


public class Juego {
    
    public int[] pieza = new int[64];
    public int [] color = new int[64];
    
    public long estadoTablero;
    public List<int[]> secuencia = new ArrayList<>();


    public Juego() {
        estadoTablero = 0b111100_000100_000_00_000_0_000000_0_1_11_11L;

        for (int i = 0; i < 64; i++) {
            pieza[i] = NOPIEZA;
            color[i] = NOCOLOR;
        }

        pieza[A1] = TORRE;
        pieza[B1] = CABALLO;
        pieza[C1] = ALFIL;
        pieza[D1] = DAMA;
        pieza[E1] = REY;
        pieza[F1] = ALFIL;
        pieza[G1] = CABALLO;
        pieza[H1] = TORRE;

        pieza[A8] = TORRE;
        pieza[B8] = CABALLO;
        pieza[C8] = ALFIL;
        pieza[D8] = DAMA;
        pieza[E8] = REY;
        pieza[F8] = ALFIL;
        pieza[G8] = CABALLO;
        pieza[H8] = TORRE;

        for (int i = 0; i < 8; i++) {
            pieza[8 + i] = PEON;
            pieza[48 + i] = PEON;
        }

        for (int i = 0; i < 16; i++) {
            color[i] = BLANCO;
            color[i + 48] = NEGRO;

        }
    }

    public void establecerPosicion(String... movimientos) {
        for (var movimiento : movimientos) {
            secuencia.add(convertirAPosicion(movimiento));
            estadoTablero = actualizarTablero(pieza,color, estadoTablero, convertirAPosicion(movimiento));
            estadoTablero ^= 0b10000;
        }
        String formateado = Long.toBinaryString(estadoTablero);


        String formato = "";
        formato = formateado.substring(formateado.length() - 2);
        formato = formateado.substring(formateado.length() - 4, formateado.length() -2) + "_" + formato;
        formato = formateado.substring(formateado.length() - 5, formateado.length() -4) + "_" + formato;
        formato = formateado.substring(formateado.length() - 6, formateado.length() -5) + "_" + formato;
        formato = formateado.substring(formateado.length() - 12, formateado.length() -6) + "_" + formato;
        formato = formateado.substring(formateado.length() - 13, formateado.length() -12) + "_" + formato;
        formato = formateado.substring(formateado.length() - 16, formateado.length() -13) + "_" + formato;
        formato = formateado.substring(formateado.length() - 18, formateado.length() -16) + "_" + formato;
        formato = formateado.substring(formateado.length() - 21, formateado.length() -18) + "_" + formato;
        formato = formateado.substring(formateado.length() - 27, formateado.length() -21) + "_" + formato;
        formato = formateado.substring(0, formateado.length() -27) + "_" + formato;


        System.out.println(formato);


    }

//    public void setFen(String linea) {
//        tablero = new Pieza[64];
//        String[] filas = linea.split("/");
//
//        for (int i = 0; i < filas.length; i++) {
//
//            if (i == 7) {
//                String[] ops = filas[i].split(" ");
//                iniciarFen(ops[0], 7 - i);
//
//                estadoTablero.turnoBlanco = ops[1].equals("w");
//                estadoTablero.enroqueLBlanco = ops[2].contains("Q");
//                estadoTablero.enroqueCNegro = ops[2].contains("k");
//                estadoTablero.enroqueCBlanco = ops[2].contains("K");
//                estadoTablero.enroqueLNegro = ops[2].contains("q");
//
//                if (!ops[3].contains("-")) {
//                    var posicion = Utilidades.casillaANumero(ops[3]) + (estadoTablero.turnoBlanco ? -8 : 8);
//                    estadoTablero.piezaALPaso = tablero[posicion];
//                    estadoTablero.alPaso = true;
//                }
//
//            } else {
//                iniciarFen(filas[i], 7 - i);
//            }
//
//        }
//
//        System.out.println("fen procesado");
//        Utilidades.imprimirPosicicion(tablero);
//    }
//
//    private void iniciarFen(String fila, int i) {
//
//        int indexInicio = 8 * i;
//
//
//        for (char c : fila.toCharArray()) {
//            if (c == 'k') {
//                tablero[indexInicio] = new Pieza(false,REY);
//                estadoTablero.posicionReyNegro = indexInicio;
//            }
//            if (c == 'q') {
//                tablero[indexInicio] = new Pieza(false,DAMA);
//            }
//            if (c == 'r') {
//                tablero[indexInicio] = new Pieza(false,TORRE);
//            }
//            if (c == 'b') {
//                tablero[indexInicio] = new Pieza(false,ALFIL);
//            }
//            if (c == 'n') {
//                tablero[indexInicio] = new Pieza(false,CABALLO);
//            }
//            if (c == 'p') {
//                tablero[indexInicio] = new Pieza(false,PEON);
//            }
//
//            if (c == 'K') {
//                tablero[indexInicio] = new Pieza(true,REY);
//                estadoTablero.posicionReyBlanco = indexInicio;
//            }
//            if (c == 'Q') {
//                tablero[indexInicio] = new Pieza(true,DAMA);
//            }
//            if (c == 'R') {
//                tablero[indexInicio] = new Pieza(true,TORRE);
//            }
//            if (c == 'B') {
//                tablero[indexInicio] = new Pieza(true,ALFIL);
//            }
//            if (c == 'N') {
//                tablero[indexInicio] = new Pieza(true,CABALLO);
//            }
//            if (c == 'P') {
//                tablero[indexInicio] = new Pieza(true,PEON);
//            }
//
//            if (Character.isDigit(c)) {
//                indexInicio += Integer.parseInt(String.valueOf(c));
//            } else {
//                indexInicio++;
//            }
//        }
//
//    }

    public void perft(int n){
        var search = new Search(pieza,color, estadoTablero);
        search.perft(n);

    }
//   public String mover(int n) throws CloneNotSupportedException, IOException, InterruptedException, ExecutionException {
//       var search = new Search(tablero, estadoTablero);
//       return Utilidades.convertirANotacion(search.search(n));
//   }
}