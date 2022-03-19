package de.marskuh.qchess;

import de.marskuh.qchess.renderer.BitboardRenderer;
import de.marskuh.qchess.renderer.BoardRenderer;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneration {

    long pawnAttackMask;

//    public List<Move> generatePawnMoves(Team team, long bitboard) {
//        final List<Move> moves = new ArrayList<>();
//        if (team.isWhite()) {
//            generatePawnMovesWhite(bitboard, moves);
//        }
//        return moves;
//    }

    public static long generatePawnAttackMaskWhite(long pawnPositions) {
        final long pawnAttackMask = (pawnPositions << 7 & ~BitBoards.FILE_A) | (pawnPositions << 9 & ~BitBoards.FILE_H);
        return pawnAttackMask;
    }

    public static long generatePawnAttackMaskBlack(long pawnPositions) {
        final long pawnAttackMask = (pawnPositions >>> 7 & ~BitBoards.FILE_H) | (pawnPositions >>> 9 & ~BitBoards.FILE_A);
        return pawnAttackMask;
    }

    public static void generatePawnMovesWhite(long pawnAttackMask, Board board, List<Move> moves) {
        final long pawnAttacks = pawnAttackMask & board.getOccupiedSquares(Team.Black);
        // TODO MVR add attacks
    }


    /**
     *
     * @param knightIndex the index, the knight is positioned. 0 => A8, 63 => F1
     * @return A bitboard where each attack is marked with 1
     */
    public static long generateKnightAttackMask(long knightIndex) {
        // The KnightMask is positioned at F3, which is at Index 45, so if
        // the knightIndex is > 45, we shift right, otherwise left
        final long threshold = 64 - 19;
        // Masks off any overhead at AB, GH
        long additionalMask = (knightIndex % 8 < 4) ?  ~(BitBoards.FILE_G | BitBoards.FILE_H) :  ~(BitBoards.FILE_A | BitBoards.FILE_B);
        if (knightIndex > threshold) {
            return BitBoards.KNIGHT_ATTACK_MASK >>> (knightIndex - threshold) & additionalMask;
        } else {
            return BitBoards.KNIGHT_ATTACK_MASK << (threshold - knightIndex) & additionalMask;
        }
    }
}
