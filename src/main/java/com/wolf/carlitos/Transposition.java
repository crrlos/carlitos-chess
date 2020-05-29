package com.wolf.carlitos;

import java.util.Arrays;

public class Transposition {
    static class Entry{
        public long zobrist;
        public int score;
        public int color;
        public int depth;

    }
    public static Entry[] transposition = new Entry[1000];
    static {
        for (int i = 0; i < transposition.length; i++) {
            transposition[i] = new Entry();
        }
    }
}
