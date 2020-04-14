/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wolf.carlitos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author carlos
 */
public class Hilos {

    static List<Proceso> procesos = new ArrayList<Proceso>();
    static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1000);

    public Hilos() {
        for (int i = 0; i < 100; i++) {
            procesos.add(new Proceso());

        }
        System.out.println("procesos iniciados");
    }

    public static void comparar(List<int[]> movimientos, List<String> movs,EstadoTablero estado) {

        executor.execute(() -> {

            var proceso = remover();

            while (proceso == null) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Hilos.class.getName()).log(Level.SEVERE, null, ex);
                }
                proceso = remover();
            }

            var res = proceso.movimentosValidos(movs);
            
            agregar(proceso);
            
            if (movimientos.size() == res.size()) {
                return;
            }
            
            var a = estado.PosicionReyBlanco;
            var b = estado.PosicionReyNegro;
            
            Collections.sort(res);

            var movGenerados = movimientos.stream().map(p -> Utilidades.ConvertirANotacion(p))
                    .collect(Collectors.toList());

            Collections.sort(movGenerados);

            res.forEach(m -> {

                if (!movGenerados.contains(m)) {
                    System.out.println("no se encontró en la secuencia: ");
                    System.out.println(movs);
                    System.out.println(movGenerados);
                    System.out.println(res);
                }

            });

        });

    }

    synchronized static Proceso remover() {
        if (procesos.size() > 0) {
            //System.out.println("removido");
            return procesos.remove(0);
        }

        return null;
    }
    synchronized static  void agregar(Proceso p){
        procesos.add(p);
    }

}
