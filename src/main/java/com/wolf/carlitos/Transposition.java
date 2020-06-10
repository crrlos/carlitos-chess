package com.wolf.carlitos;

import static com.wolf.carlitos.Constantes.*;

public class Transposition {
    public static class Entry {
        public long zobrist;
        public int score;
        public int depth;
        public int flag;
    }

    public static int llamadas = 0;
    private static final Entry[] transposition = new Entry[25_000_000];

    static {
        for (int i = 0; i < transposition.length; i++) {
            transposition[i] = new Entry();
        }
    }

    public static int checkEntry(long zobrist, int depth, int alfa, int beta, boolean isPv) {

        if (!isPv) return NOENTRY;

        int index = (int) (zobrist % transposition.length);

        Entry entry = transposition[index];

        if (entry.zobrist == zobrist && entry.depth > depth) {
            if (entry.flag == BETA && entry.score >= beta) return beta;
            if (entry.flag == ALFA && entry.score <= alfa) return alfa;
            if (entry.flag == EXACT && entry.score > alfa && entry.score < beta) return entry.score;
        }

        return NOENTRY;
    }

    public static void setEntry(long zobrist, int depth, int score, int flag, boolean isPv) {

        if (!isPv) return;

        int index = (int) (zobrist % transposition.length);

        Entry entry = transposition[index];
        if (entry.depth >= depth && entry.zobrist != 0) return;

        entry.zobrist = zobrist;
        entry.depth = depth;
        entry.score = score;
        entry.flag = flag;
    }

    public static int entradasAsignadas() {
        int cont = 0;
        for (Entry entry : transposition) {
            if (entry.zobrist != 0) cont++;
        }
        return cont;
    }
}
