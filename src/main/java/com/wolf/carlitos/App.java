package com.wolf.carlitos;

public class App {

    public static void main(String[] args) {
        Ataque.init();
        Zobrist.init();
        Transposition.init();
        new UCI();
    }


}
