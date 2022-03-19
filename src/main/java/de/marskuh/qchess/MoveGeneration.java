package de.marskuh.qchess;

import de.marskuh.qchess.renderer.BitboardRenderer;
import de.marskuh.qchess.renderer.BoardRenderer;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneration {

    long pawnAttackMask;

    public static void main(String[] args) {
        final Board board = new FenParser().parse("rnbqkbnr/pppppppp/8/8/4R3/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        final long rookPositions = board.bitboards.get(Team.White).rooks;
        final long rookAttackMask = HAndVMoves(36, board.getOccupiedSquares());
        final long rookAttacks = rookAttackMask & board.getOccupiedSquares(Team.Black);
        System.out.println(new BoardRenderer().render(board));
        System.out.println();
        System.out.println(new BitboardRenderer("Rook Attack Mask").render(rookAttackMask));
        System.out.println();
        System.out.println(new BitboardRenderer("Rook Attacks").render(rookAttacks));
        System.out.println();
    }

//    public List<Move> generatePawnMoves(Team team, long bitboard) {
//        final List<Move> moves = new ArrayList<>();
//        if (team.isWhite()) {
//            generatePawnMovesWhite(bitboard, moves);
//        }
//        return moves;
//    }

    static long HAndVMoves(int pieceIndex, long OCCUPIED)
    {
        long piecePositionBitboard = 1L << pieceIndex; // Convert to 64 bitboard
        int rank = pieceIndex / 8;
        int file = pieceIndex % 8;
        // TODO MVR figure out why this is, or if there is another way to implement this https://www.youtube.com/watch?v=bCH4YK6oq8M
        long horizontalMoves = ((OCCUPIED - 2 * piecePositionBitboard) ^ Long.reverse(Long.reverse(OCCUPIED) - 2 * Long.reverse(piecePositionBitboard)));
        long possibilitiesVertical = ((OCCUPIED&BitBoards.FILE_MASKS[file]) - (2 * piecePositionBitboard)) ^ Long.reverse(Long.reverse(OCCUPIED&BitBoards.FILE_MASKS[file]) - (2 * Long.reverse(piecePositionBitboard)));
        return (possibilitiesVertical & BitBoards.FILE_MASKS[file]) | horizontalMoves & BitBoards.RANK_MASKS[rank];
    }

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
