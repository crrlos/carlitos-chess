package com.wolf.carlitos;

import java.util.Arrays;

public class Transposition {
    private static class Entry {
        public long zobrist;
        public int score;
        public int color;
        public int depth;
        public int flag;
    }

    private static final Entry[] transposition = new Entry[1000];

    static {
        for (int i = 0; i < transposition.length; i++) {
            transposition[i] = new Entry();
        }
    }

    public static boolean checkEntry(int index, int ply) {
        return transposition[index].depth >= ply && transposition[index].zobrist != 0;
    }
    public static Entry getEntry(int index){
        return transposition[index];
    }
}
