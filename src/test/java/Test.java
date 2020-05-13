import com.wolf.carlitos.Juego;

import java.util.Arrays;
import java.util.Scanner;

public class Test {


    public static void main(String[] args) {

        // la cantidad de números de la secuencia a generar
        int cantidadAGenerar = 10;
        // el inicio de los número pares
        int inicioPares = 2;
        //el inicio de los números impares
        int inicioImpares = -3;

        // arreglo para almacenar los números generados
        int[] numerosGenerados = new int[cantidadAGenerar];

        // contador para dar seguimiento a los números generados
        int contador = 0;
        // en cada iteración se imprimen n + 1 números pares
        // esta variable permite llevar la secuencia
        int cantidadPares = 1;

        // iterar mientras no se hayan generado todos lo números
        while(contador < cantidadAGenerar){

            // imprimir secuencia de números pares, en la primera iteración imprime solo un número
            // en la segunda dos números ...
            for (int i = 0; i < cantidadPares; i++) {
                 // si ya se generaron todos lo números terminar
                if(contador == cantidadAGenerar) break;
                // guardar el número generado en el arreglo
                numerosGenerados[contador++] = inicioPares;
                // incrementar el siguiente número par
                inicioPares +=2;
            }

            // en la siguiente iteración se imprimirán  cantidadPares + 1 números pares
            cantidadPares++;

            // siempre se imprimen tres números impares
            for (int i = 0; i < 3; i++) {
                // si ya se generaron todos lo números terminar
                if(contador == cantidadAGenerar) break;
                // guardar el número generado en el arreglo
                numerosGenerados[contador++] = inicioImpares;
                // incrementar el siguiente número impar
                inicioImpares -=2;
            }

        }

        // imprimir la secuencia generada
        System.out.println(Arrays.toString(numerosGenerados));

    }
}