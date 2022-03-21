package de.marskuh.qchess;

import static de.marskuh.qchess.BitBoards.Tiles.*;
import static de.marskuh.qchess.BitBoards.Tiles.E5;

public interface BitBoards {

    // Selects the whole board, most likely only used for testing purposes
    long ALL_MASK = 0xFFFFFFFFFFFFFFFFL;

    long FILE_A = 0x8080808080808080L; // 0b1000000010000000100000001000000010000000100000001000000010000000L
    long FILE_B = FILE_A >>> 1;
    long FILE_C = FILE_A >>> 2;
    long FILE_D = FILE_A >>> 3;
    long FILE_E = FILE_A >>> 4;
    long FILE_F = FILE_A >>> 5;
    long FILE_G = FILE_A >>> 6;
    long FILE_H = FILE_A >>> 7;

    long RANK_1 = 0x00000000000000FFL;
    long RANK_2 = RANK_1 << 8;
    long RANK_3 = RANK_1 << 16;
    long RANK_4 = RANK_1 << 24;
    long RANK_5 = RANK_1 << 32;
    long RANK_6 = RANK_1 << 40;
    long RANK_7 = RANK_1 << 48;
    long RANK_8 = RANK_1 << 56;

    long BLACK_SQUARES = 0x55AA55AA55AA55AAL;
    long WHITE_SQUARES = 0xAA55AA55AA55AA55L;

    // Helper arrays, to address each file/rank mask by its index
    long[] FILE_MASKS = new long[]{FILE_A, FILE_B, FILE_C, FILE_D, FILE_E, FILE_F, FILE_G, FILE_H};
    long[] RANK_MASKS = new long[]{RANK_1, RANK_2, RANK_3, RANK_4, RANK_5, RANK_6, RANK_7, RANK_8};

    // Positioned in the upper right, at F3, which is index => 45
    long KNIGHT_ATTACK_MASK = G5 | H4 | H2 | G1 | E1 | D2 | D4 | E5;

    // Mask to address each tile individually.
    // Especially useful for testing
    interface Tiles {
        long A1 = FILE_A & RANK_1;
        long A2 = FILE_A & RANK_2;
        long A3 = FILE_A & RANK_3;
        long A4 = FILE_A & RANK_4;
        long A5 = FILE_A & RANK_5;
        long A6 = FILE_A & RANK_6;
        long A7 = FILE_A & RANK_7;
        long A8 = FILE_A & RANK_8;

        long B1 = FILE_B & RANK_1;
        long B2 = FILE_B & RANK_2;
        long B3 = FILE_B & RANK_3;
        long B4 = FILE_B & RANK_4;
        long B5 = FILE_B & RANK_5;
        long B6 = FILE_B & RANK_6;
        long B7 = FILE_B & RANK_7;
        long B8 = FILE_B & RANK_8;

        long C1 = FILE_C & RANK_1;
        long C2 = FILE_C & RANK_2;
        long C3 = FILE_C & RANK_3;
        long C4 = FILE_C & RANK_4;
        long C5 = FILE_C & RANK_5;
        long C6 = FILE_C & RANK_6;
        long C7 = FILE_C & RANK_7;
        long C8 = FILE_C & RANK_8;

        long D1 = FILE_D & RANK_1;
        long D2 = FILE_D & RANK_2;
        long D3 = FILE_D & RANK_3;
        long D4 = FILE_D & RANK_4;
        long D5 = FILE_D & RANK_5;
        long D6 = FILE_D & RANK_6;
        long D7 = FILE_D & RANK_7;
        long D8 = FILE_D & RANK_8;

        long E1 = FILE_E & RANK_1;
        long E2 = FILE_E & RANK_2;
        long E3 = FILE_E & RANK_3;
        long E4 = FILE_E & RANK_4;
        long E5 = FILE_E & RANK_5;
        long E6 = FILE_E & RANK_6;
        long E7 = FILE_E & RANK_7;
        long E8 = FILE_E & RANK_8;

        long F1 = FILE_F & RANK_1;
        long F2 = FILE_F & RANK_2;
        long F3 = FILE_F & RANK_3;
        long F4 = FILE_F & RANK_4;
        long F5 = FILE_F & RANK_5;
        long F6 = FILE_F & RANK_6;
        long F7 = FILE_F & RANK_7;
        long F8 = FILE_F & RANK_8;

        long G1 = FILE_G & RANK_1;
        long G2 = FILE_G & RANK_2;
        long G3 = FILE_G & RANK_3;
        long G4 = FILE_G & RANK_4;
        long G5 = FILE_G & RANK_5;
        long G6 = FILE_G & RANK_6;
        long G7 = FILE_G & RANK_7;
        long G8 = FILE_G & RANK_8;

        long H1 = FILE_H & RANK_1;
        long H2 = FILE_H & RANK_2;
        long H3 = FILE_H & RANK_3;
        long H4 = FILE_H & RANK_4;
        long H5 = FILE_H & RANK_5;
        long H6 = FILE_H & RANK_6;
        long H7 = FILE_H & RANK_7;
        long H8 = FILE_H & RANK_8;
    }
}
