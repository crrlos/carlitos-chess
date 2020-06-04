package com.wolf.carlitos;

public class Perft {
    private Generador generador;
    Tablero tab;
    private void perftSearch(int deep, Acumulador acumulador, boolean reset) {

        if (deep == 0) {
            acumulador.contador++;
            acumulador.contadorPerft++;
            return;
        }
        var respuesta = generador.generarMovimientos(deep);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;


        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            tab.makeMove(mov);

            perftSearch(deep - 1, acumulador, false);

            tab.takeBack(mov);

            if (reset) {
                System.out.println(Utilidades.convertirANotacion(mov) + " " + acumulador.contadorPerft);
                acumulador.contadorPerft = 0;
            }
        }
    }

    public void perft(int deep) {
        tab = new Tablero();
        generador = new Generador(tab);

        var acumulador = new Acumulador();
        var tinicio = System.currentTimeMillis();
        perftSearch(deep, acumulador, true);
        System.out.println(acumulador.contador);

        var tfin = (System.currentTimeMillis() - tinicio) / 1000;
    }
}

