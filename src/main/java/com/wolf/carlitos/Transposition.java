package com.wolf.carlitos;

import static com.wolf.carlitos.Constantes.*;

public class Transposition {
    public static class Entry {
        public long zobrist;
        public int score;
        public int depth;
        public int flag;
        public int bestMove;
    }

    private static final Entry[] transposition = new Entry[1_000_000];

    public static void init(){
        for (int i = 0; i < transposition.length; i++) {
            transposition[i] = new Entry();
        }
    }

    public static int checkEntry(long zobrist, int depth, int alfa, int beta) {

        int index = (int) (zobrist % transposition.length);

        Entry entry = transposition[index];

        if (entry.zobrist == zobrist && entry.depth >= depth) {
            if (entry.flag == BETA && entry.score >= beta) return beta;
            if (entry.flag == ALFA && entry.score <= alfa) return alfa;
            if (entry.flag == EXACT) return entry.score;
        }

        return NOENTRY;
    }

    public static void setEntry(long zobrist, int depth, int score, int flag, int bestMove) {


        int index = (int) (zobrist % transposition.length);

        Entry entry = transposition[index];

        if (depth < entry.depth && entry.zobrist == zobrist) return;

        entry.zobrist = zobrist;
        entry.depth = depth;
        entry.score = score;
        entry.flag = flag;
        entry.bestMove = bestMove;
    }

    public static int bestMove(long zobrist) {
        int index = (int) (zobrist % transposition.length);
        return transposition[index].bestMove;
    }
}
