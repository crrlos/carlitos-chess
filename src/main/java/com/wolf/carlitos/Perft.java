package com.wolf.carlitos;

import static com.wolf.carlitos.Utilidades.convertirANotacion;

public class Perft {

    private final Generador generador;
    private final Posicion tab;

    private int movimientosTotales;
    private int totalPorMovimiento;

    Perft(Posicion posicion) {
        this.tab = posicion;
        this.generador = new Generador(tab);
    }

    private void perftSearch(int deep, boolean reset) {

        if (deep == 0) {
            movimientosTotales++;
            totalPorMovimiento++;
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
                System.out.printf("%s %d\n", convertirANotacion(mov), totalPorMovimiento);
                totalPorMovimiento = 0;
            }
        }
    }

    public void perft(int deep) {
        var tinicio = System.currentTimeMillis();
        perftSearch(deep, true);
        System.out.println(movimientosTotales);

    }
}

