package com.wolf.carlitos;

public class Perft {
    private final Generador generador;
    private final Tablero tab;
    private final Acumulador acumulador;

    Perft(Tablero tablero) {
        this.tab = tablero;
        this.generador = new Generador(tab);
        this.acumulador = new Acumulador();
    }

    private void perftSearch(int deep, boolean reset) {

        if (deep == 0) {
            acumulador.contador++;
            acumulador.contadorPerft++;
            return;
        }
        var respuesta = generador.generarMovimientos(deep + 50);

        var movimientos = respuesta.movimientosGenerados;
        var fin = respuesta.cantidadDeMovimientos;


        for (int i = 0; i < fin; i++) {
            var mov = movimientos[i];

            tab.makeMove(mov);

            perftSearch(deep - 1, false);

            tab.takeBack(mov);

            if (reset) {
                System.out.println(Utilidades.convertirANotacion(mov) + " " + acumulador.contadorPerft);
                acumulador.contadorPerft = 0;
            }
        }
    }

    public void perft(int deep) {
        var tinicio = System.currentTimeMillis();
        perftSearch(deep, true);
        System.out.println(acumulador.contador);

        var tfin = (System.currentTimeMillis() - tinicio) / 1000;
    }
}

