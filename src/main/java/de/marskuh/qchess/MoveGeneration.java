package de.marskuh.qchess;

import java.util.*;

import static de.marskuh.qchess.BitBoards.*;

public class MoveGeneration {

    public static class Attacks {
        public long pawnAttackMask;
        public long knightAttackMask;
        public long queenAttackMask;
        public long rookAttackMask;
        public long bishopAttackMask;
        public long kingAttackMask;

        public long getAll() {
            return pawnAttackMask | knightAttackMask | queenAttackMask | rookAttackMask | bishopAttackMask | kingAttackMask;
        }

        public long getCheckCount(int index) {
            return getCheckCount(1L << (63 - index));
        }

        // TODO MVR is this fast enough?
        public int getCheckCount(long bitboard) {
            return ((pawnAttackMask & bitboard) == 0 ? 0 : 1)
                    + ((knightAttackMask & bitboard) == 0 ? 0 : 1)
                    + ((queenAttackMask & bitboard) == 0 ? 0 : 1)
                    + ((rookAttackMask & bitboard) == 0 ? 0 : 1)
                    + ((bishopAttackMask & bitboard) == 0 ? 0 : 1)
                    + ((kingAttackMask & bitboard) == 0 ? 0 : 1);
        }
    }

    public static class CastlingInfo {
        public final boolean canCastleQueenSide;
        public final boolean canCastleKingSide;
        public CastlingInfo(final  boolean canCastleKingSide, final boolean canCastleQueenSide) {
            this.canCastleQueenSide = canCastleQueenSide;
            this.canCastleKingSide = canCastleKingSide;
        }
        public CastlingInfo() {
            this(true, true);
        }
    }

    public static List<Move> generateMoves(Board board, Team team) {
        final List<Move> moves = new ArrayList<>();
        final Side side = board.bitboards.get(team);
        final Side other = board.bitboards.get(team.other());
        final CastlingInfo castling = side.getCastling();
        if (team == Team.White) {
            generateMovesWhite(
                    castling,
                    side.pawns, side.knights, side.bishops, side.rooks, side.queens, side.king,
                    other.pawns, other.knights, other.bishops, other.rooks, other.queens, other.king,
                    board.getEmptySquares(),
                    board.getOccupiedSquares(),
                    board.getOccupiedSquares(Team.Black),
                    moves);
        } else {
            generateMovesBlack(
                    castling,
                    side.pawns, side.knights, side.bishops, side.rooks, side.queens, side.king,
                    other.pawns, other.knights, other.bishops, other.rooks, other.queens, other.king,
                    board.getEmptySquares(),
                    board.getOccupiedSquares(),
                    board.getOccupiedSquares(Team.White),
                    moves);
        }
        return moves;
    }

    public static void generateMovesWhite(final CastlingInfo castlingInfo,
                                          long WP, long WN, long WB, long WR, long WQ, long WK,
                                          long BP, long BN, long BB, long BR, long BQ, long BK,
                                          long empty,
                                          long occupied,
                                          long occupiedBlack,
                                          List<Move> moves) {
        final Attacks enemyAttacks = generateAttacks(BP, BN, BB, BR, BQ, BK, occupied, false);
        final int kingAttackCount = enemyAttacks.getCheckCount(WK);
        if (kingAttackCount >= 2) { // only king moves are valid
            generateKingMoves(enemyAttacks, empty, occupiedBlack, WK, moves);
        } else if(kingAttackCount == 1) {
            final Attacks protectTheKing = generateAttacks(WP, WN, WB, WR, WQ, WK, occupied, true);
            // When attacked, we can do normal king moves and moves, which kill the attacker
            // TODO MVR implement this :D
//            for (long eachAttacker : enemyAttacks.getAttackers(WK)) {
//                for (long eachDefender: protectTheKing.Attackers(eachAttacker)) {
//
//                }
//            }
            generateKingMoves(enemyAttacks, empty, occupiedBlack, WK, moves);
        } else {
            // all moves are possible
            generatePawnMovesWhite(WP, occupied, empty, occupiedBlack, moves);
            generateKnightMoves(WN, occupied, empty, occupiedBlack, moves);
            generateBishopMoves(WB, occupied, empty, occupiedBlack, moves);
            generateRookMoves(WR, occupied, empty, occupiedBlack, moves);
            generateQueenMoves(WQ, occupied, empty, occupiedBlack, moves);
            generateKingMoves(enemyAttacks, empty, occupiedBlack, WK, moves);
            generateKingCastlingMoves(enemyAttacks, castlingInfo, occupied, true, WK, moves);
        }
    }

    private static Attacks generateAttacks(final long pawns, final long knights, final long bishops, final long rooks, final long queens, final long king, final long occupied, boolean isWhite) {
        final Attacks attacks = new Attacks();
        attacks.pawnAttackMask = isWhite ?  (pawns << 7 & ~BitBoards.FILE_A) | (pawns << 9 & ~FILE_H) : (pawns >>> 7 & ~FILE_H) | (pawns >>> 9 & ~BitBoards.FILE_A);
        attacks.knightAttackMask = indices(knights).stream().map(MoveGeneration::generateKnightAttackMask).reduce((left, right) -> left | right).orElse(0L);
        attacks.bishopAttackMask = indices(bishops).stream().map(i -> MoveGeneration.generateDiagonalAttacks(i, occupied)).reduce((left, right) -> left | right).orElse(0L);
        attacks.rookAttackMask = indices(rooks).stream().map(i -> MoveGeneration.generateLineAttackMask(i, occupied)).reduce((left, right) -> left | right).orElse(0L);
        attacks.queenAttackMask = indices(queens).stream().map(i -> MoveGeneration.generateLineAttackMask(i, occupied) | MoveGeneration.generateDiagonalAttacks(i, occupied)).reduce((left, right) -> left | right).orElse(0L);
        attacks.kingAttackMask = generateKingMoveMask(king);
        return attacks;
    }

    private static List<Integer> indices(long input) {
        if (input == 0) return Collections.emptyList();
        final List<Integer> indexes = new ArrayList<>();
        final BitSet bitset = BitSet.valueOf(new long[]{input});
        for (int i = bitset.nextSetBit(0); i != -1; i = bitset.nextSetBit(i + 1)) {
            indexes.add(63 - i);
        }
        return indexes;
    }

    public static long generateKingMoveMask(long king) {
        final int kingIndex = 63 - Long.numberOfTrailingZeros(king);
        final int threshold = 9;
        final long kingAttackMask = Tiles.A8 | Tiles.B8 | Tiles.C8 | Tiles.C7 | Tiles.C6 | Tiles.B6 | Tiles.A6 | Tiles.A7; // TODO MVR ... define statically

        // Masks off any overhead at AB, GH
        long additionalMask = (kingIndex % 8 < 4) ? ~(BitBoards.FILE_G | FILE_H) : ~(BitBoards.FILE_A | BitBoards.FILE_B);
        if (kingIndex > threshold) {
            return kingAttackMask >>> (kingIndex - threshold) & additionalMask;
        } else {
            return kingAttackMask << (threshold - kingIndex) & additionalMask;
        }
    }

    private static long generateKingMoves(Attacks enemyAttacks, long empty, long occupiedByEnemy, long king, List<Move> moves) {
        final long moveMask = generateKingMoveMask(king);
        final int startIndex = 63 - Long.numberOfTrailingZeros(king);
        long tmpKing = moveMask & ~enemyAttacks.getAll() & empty;
        long possibleMove = tmpKing & ~(tmpKing - 1);
        long attackMask = 0L;
        while (possibleMove != 0) {
            final int toIndex = 63 - Long.numberOfTrailingZeros(possibleMove);
            attackMask |= possibleMove;
            moves.add(new Move(startIndex, toIndex));
            tmpKing &= ~possibleMove;
            possibleMove = tmpKing & ~(tmpKing - 1);
        }

        // Now we check all the fields the king potentially could capture
        long potentialCaptures = moveMask & occupiedByEnemy;
        if (potentialCaptures != 0) {
            // Verify for each potential capture, if it is checked by another piece
            for (int enemy : indices(potentialCaptures)) {
                if (enemyAttacks.getCheckCount(1L << (63 - enemy)) == 0) {
                    moves.add(new Move(startIndex, enemy));
                    attackMask |= enemy;
                }
            }
        }
        return attackMask;
    }

    private static void generateKingCastlingMoves(final Attacks attacks, final CastlingInfo castlingInfo, final long occupied, final boolean white, final long king, List<Move> moves) {
//        final long kingMoveMask = generateKingMoveMask(king); // TODO MVR generated again...
        final long unsafe = ~occupied & attacks.getAll();
        if ((king & unsafe) == 0) { // if in check, we cannot castle
            final int fromIndex = white ? 60 : 4;
            final int[] queenSideIndices = new int[]{fromIndex - 1, fromIndex-2, fromIndex-3};
            final int[] kingSideIndices = new int[]{fromIndex + 1, fromIndex+2};
            // TODO MVR can be hardcoded as well
            if (castlingInfo.canCastleKingSide && Arrays.stream(kingSideIndices).noneMatch(i -> (1L << (63 - i) & (occupied | unsafe)) != 0)) {
                moves.add(new Move(fromIndex, fromIndex + 2, MoveFlag.Castling));
            }
            // TODO MVR can be hardcoded as well
            if (castlingInfo.canCastleQueenSide
                    && Arrays.stream(queenSideIndices).noneMatch(i -> (1L << (63 - i) & (occupied)) != 0)
                    && Arrays.stream(new int[]{fromIndex -1, fromIndex -2}).noneMatch(i -> (1L << (63 -i) & (unsafe)) != 0)) {
                moves.add(new Move(fromIndex, fromIndex - 2, MoveFlag.Castling));
            }
        }
    }

    public static long generatePawnMovesWhite(long pawns, final long occupied, final long empty, final long occupiedBlack, List<Move> moves) {
        final long EMPTY = empty; // Fields which are empty, are marked with 1
        final long pawnAttackMask = (pawns << 7 & ~BitBoards.FILE_A) | (pawns << 9 & ~FILE_H);
        final long returnMe = pawnAttackMask;

        // First create push 1 movement
        // No promotion moves for now // TODO MVR why?
        long push1 = pawns << 8 & EMPTY & ~RANK_8;
        long push2 = (pawns << 16) & EMPTY & (EMPTY << 8) & RANK_4; // Empty is pushed as well, to prevent pushing through pieces
        long pawnAttacks = pawnAttackMask & occupiedBlack & ~RANK_8;

        // TODO MVR add promotions
        // Move 1
        long possibleMove = push1 & ~(push1 - 1);
        while (possibleMove != 0) {
            int index = 63 - Long.numberOfTrailingZeros(possibleMove);
            moves.add(new Move(index + 8, index));
            push1 &= ~possibleMove;
            possibleMove = push1 & ~(push1 - 1);
        }

        // Move 2
        possibleMove = push2 & ~(push2 - 1);
        while (possibleMove != 0) {
            int index = 63 - Long.numberOfTrailingZeros(possibleMove);
            moves.add(new Move(index + 16, index));
            push2 &= ~possibleMove;
            possibleMove = push2 & ~(push2 - 1);
        }

        // Attack
        possibleMove = pawnAttacks & ~(pawnAttacks - 1);
        while (possibleMove != 0) {
            final int index = 63 - Long.numberOfTrailingZeros(possibleMove);
            final long tileMaskAttackRight = BitBoards.FILE_MASKS[(index + 7) % 8] & BitBoards.RANK_MASKS[7 - (index + 7) / 8];
            final long tileMaskAttackLeft = BitBoards.FILE_MASKS[(index + 9) % 8] & BitBoards.RANK_MASKS[7 - (index + 9) / 8];
            if ((tileMaskAttackRight & pawns) != 0) {
                moves.add(new Move(index + 7, index));
            }
            if ((tileMaskAttackLeft & pawns) != 0) {
                moves.add(new Move(index + 9, index));
            }
            pawnAttacks &= ~possibleMove;
            possibleMove = pawnAttacks & ~(pawnAttacks - 1);
        }

        // Add promotion moves
        long pawnpromotions = pawns << 8 & EMPTY & RANK_8;
        for(int index : indices(pawnpromotions)) {
            moves.add(new Move(index + 8, index, PieceType.Bishop));
            moves.add(new Move(index + 8, index, PieceType.Knight));
            moves.add(new Move(index + 8, index, PieceType.Queen));
            moves.add(new Move(index + 8, index, PieceType.Rook));
        }

        // TODO MVR remove duplicated code :D
        // add promotion captures
        pawnpromotions = pawnAttackMask & occupiedBlack & RANK_8;
        for (int index : indices(pawnpromotions)) {
            final long tileMaskAttackRight = BitBoards.FILE_MASKS[(index + 7) % 8] & BitBoards.RANK_MASKS[7 - (index + 7) / 8];
            final long tileMaskAttackLeft = BitBoards.FILE_MASKS[(index + 9) % 8] & BitBoards.RANK_MASKS[7 - (index + 9) / 8];
            if ((tileMaskAttackRight & pawns) != 0) {
                moves.add(new Move(index + 7, index, PieceType.Bishop));
                moves.add(new Move(index + 7, index, PieceType.Knight));
                moves.add(new Move(index + 7, index, PieceType.Queen));
                moves.add(new Move(index + 7, index, PieceType.Rook));
            }
            if ((tileMaskAttackLeft & pawns) != 0) {
                moves.add(new Move(index + 9, index, PieceType.Bishop));
                moves.add(new Move(index + 9, index, PieceType.Knight));
                moves.add(new Move(index + 9, index, PieceType.Queen));
                moves.add(new Move(index + 9, index, PieceType.Rook));
            }
        }
        return returnMe;
    }

    public static long generatePawnMovesBlack(long pawns, final long occupied, final long empty, final long occupiedWhite, List<Move> moves) {
        final long EMPTY = empty; // Fields which are empty, are marked with 1
        final long pawnAttackMask = (pawns >>> 7 & ~BitBoards.FILE_H) | (pawns >>> 9 & ~FILE_A);
        final long returnMe = pawnAttackMask;

        // First create push 1 movement
        // No promotion moves for now // TODO MVR why?
        long push1 = pawns >>> 8 & EMPTY & ~RANK_1;
        long push2 = (pawns >>> 16) & EMPTY & (EMPTY >>> 8) & RANK_5; // Empty is pushed as well, to prevent pushing through pieces
        long pawnAttacks = pawnAttackMask & occupiedWhite & ~RANK_1;

        // TODO MVR add promotions
        // Move 1
        long possibleMove = push1 & ~(push1 - 1);
        while (possibleMove != 0) {
            int index = 63 - Long.numberOfTrailingZeros(possibleMove);
            moves.add(new Move(index - 8, index));
            push1 &= ~possibleMove;
            possibleMove = push1 & ~(push1 - 1);
        }

        // Move 2
        possibleMove = push2 & ~(push2 - 1);
        while (possibleMove != 0) {
            int index = 63 - Long.numberOfTrailingZeros(possibleMove);
            moves.add(new Move(index - 16, index));
            push2 &= ~possibleMove;
            possibleMove = push2 & ~(push2 - 1);
        }

        // Attack
        possibleMove = pawnAttacks & ~(pawnAttacks - 1);
        while (possibleMove != 0) {
            final int index = 63 - Long.numberOfTrailingZeros(possibleMove);
            final long tileMaskAttackRight = BitBoards.FILE_MASKS[(index - 7) % 8] & BitBoards.RANK_MASKS[7 - (index - 7) / 8];
            final long tileMaskAttackLeft = BitBoards.FILE_MASKS[(index - 9) % 8] & BitBoards.RANK_MASKS[7 - (index - 9) / 8];
            if ((tileMaskAttackRight & pawns) != 0) {
                moves.add(new Move(index - 7, index));
            }
            if ((tileMaskAttackLeft & pawns) != 0) {
                moves.add(new Move(index - 9, index));
            }
            pawnAttacks &= ~possibleMove;
            possibleMove = pawnAttacks & ~(pawnAttacks - 1);
        }

        // Add promotion moves
        long pawnpromotions = pawns >>> 8 & EMPTY & RANK_1;
        for(int index : indices(pawnpromotions)) {
            moves.add(new Move(index - 8, index, PieceType.Bishop));
            moves.add(new Move(index - 8, index, PieceType.Knight));
            moves.add(new Move(index - 8, index, PieceType.Queen));
            moves.add(new Move(index - 8, index, PieceType.Rook));
        }

        // TODO MVR remove duplicated code :D
        // add promotion captures
        pawnpromotions = pawnAttackMask & occupiedWhite & RANK_1;
        for (int index : indices(pawnpromotions)) {
            final long tileMaskAttackRight = BitBoards.FILE_MASKS[(index - 7) % 8] & BitBoards.RANK_MASKS[7 - (index - 7) / 8];
            final long tileMaskAttackLeft = BitBoards.FILE_MASKS[(index - 9) % 8] & BitBoards.RANK_MASKS[7 - (index - 9) / 8];
            if ((tileMaskAttackRight & pawns) != 0) {
                moves.add(new Move(index - 7, index, PieceType.Bishop));
                moves.add(new Move(index - 7, index, PieceType.Knight));
                moves.add(new Move(index - 7, index, PieceType.Queen));
                moves.add(new Move(index - 7, index, PieceType.Rook));
            }
            if ((tileMaskAttackLeft & pawns) != 0) {
                moves.add(new Move(index - 9, index, PieceType.Bishop));
                moves.add(new Move(index - 9, index, PieceType.Knight));
                moves.add(new Move(index - 9, index, PieceType.Queen));
                moves.add(new Move(index - 9, index, PieceType.Rook));
            }
        }
        return returnMe;
    }

    public static void generateMovesBlack(CastlingInfo castlingInfo,
                                          long BP, long BN, long BB, long BR, long BQ, long BK,
                                          long WP, long WN, long WB, long WR, long WQ, long WK,
                                          long empty,
                                          long occupied,
                                          long occupiedWhite,
                                          List<Move> moves) {
        final Attacks enemyAttacks = generateAttacks(WP, WN, WB, WR, WQ, WK, occupied, true);
        final int kingAttackCount = enemyAttacks.getCheckCount(BK);
        if (kingAttackCount >= 2) { // only king moves are valid
            generateKingMoves(enemyAttacks, empty, occupiedWhite, BK, moves);
        } else if(kingAttackCount == 1) {
//            final Attacks protectTheKing = generateAttacks(BP, BN, BB, BR, BQ, BK, occupied, true);
            // When attacked, we can do normal king moves and moves, which kill the attacker
            // TODO MVR implement this :D
//            for (long eachAttacker : enemyAttacks.getAttackers(WK)) {
//                for (long eachDefender: protectTheKing.Attackers(eachAttacker)) {
//
//                }
//            }
            generateKingMoves(enemyAttacks, empty, occupiedWhite, BK, moves);
        } else {
            // all moves are possible
            generatePawnMovesBlack(BP, occupied, empty, occupiedWhite, moves);
            generateKnightMoves(BN, occupied, empty, occupiedWhite, moves);
            generateBishopMoves(BB, occupied, empty, occupiedWhite, moves);
            generateRookMoves(BR, occupied, empty, occupiedWhite, moves);
            generateQueenMoves(BQ, occupied, empty, occupiedWhite, moves);
            generateKingMoves(enemyAttacks, empty, occupiedWhite, BK, moves);
            generateKingCastlingMoves(enemyAttacks, castlingInfo, occupied, false, BK, moves);
        }
    }

    private static long generateKnightMoves(final long knights, final long occupied, final long empty, final long occupiedByOtherTeam, final List<Move> moves) {
        long tmpKnights = knights;
        long possibleMove = tmpKnights & ~(tmpKnights - 1);
        long attackMask = 0L;
        while (possibleMove != 0) {
            final int startIndex = 63 - Long.numberOfTrailingZeros(possibleMove);
            long currentKnightMoves = generateKnightAttackMask(startIndex) & (occupiedByOtherTeam | empty);
            attackMask |= currentKnightMoves;
            long currentMove = currentKnightMoves & ~(currentKnightMoves - 1);
            while (currentMove != 0) {
                final int toIndex = 63 - Long.numberOfTrailingZeros(currentMove);
                moves.add(new Move(startIndex, toIndex));
                currentKnightMoves &= ~currentMove;
                currentMove = currentKnightMoves & ~(currentKnightMoves - 1);
            }
            tmpKnights &= ~possibleMove;
            possibleMove = tmpKnights & ~(tmpKnights - 1);
        }
        return attackMask;
    }

    private static long generateBishopMoves(long bishops, long occupied, long empty, long occupiedByOtherTeam, List<Move> moves) {
        long tmpBishops = bishops;
        long possibleMove = tmpBishops & ~(tmpBishops - 1);
        long attackMask = 0L;
        while (possibleMove != 0) {
            final int startIndex = 63 - Long.numberOfTrailingZeros(possibleMove);
            long currentBishopMoves = generateDiagonalAttacks(startIndex, occupied) & (occupiedByOtherTeam | empty);
            attackMask |= currentBishopMoves;
            long currentMove = currentBishopMoves & ~(currentBishopMoves - 1);
            while (currentMove != 0) {
                final int toIndex = 63 - Long.numberOfTrailingZeros(currentMove);
                moves.add(new Move(startIndex, toIndex));
                currentBishopMoves &= ~currentMove;
                currentMove = currentBishopMoves & ~(currentBishopMoves - 1);
            }
            tmpBishops &= ~possibleMove;
            possibleMove = tmpBishops & ~(tmpBishops - 1);
        }
        return attackMask;
    }

    private static long generateRookMoves(long rooks, long occupied, long empty, long occupiedByOtherTeam, List<Move> moves) {
        long tmpRooks = rooks;
        long possibleMove = tmpRooks & ~(tmpRooks - 1);
        long attackMask = 0L;
        while (possibleMove != 0) {
            final int startIndex = 63 - Long.numberOfTrailingZeros(possibleMove);
            long currentRookMove = generateLineAttackMask(startIndex, occupied) & (occupiedByOtherTeam | empty);
            attackMask |= currentRookMove;
            long currentMove = currentRookMove & ~(currentRookMove - 1);
            while (currentMove != 0) {
                final int toIndex = 63 - Long.numberOfTrailingZeros(currentMove);
                moves.add(new Move(startIndex, toIndex));
                currentRookMove &= ~currentMove;
                currentMove = currentRookMove & ~(currentRookMove - 1);
            }
            tmpRooks &= ~possibleMove;
            possibleMove = tmpRooks & ~(tmpRooks - 1);
        }
        return attackMask;
    }

    private static long generateQueenMoves(long queens, long occupied, long empty, long occupiedByOtherTeam, List<Move> moves) {
        long tmpQueens = queens;
        long possibleMove = tmpQueens & ~(tmpQueens - 1);
        long attackMask = 0L;
        while (possibleMove != 0) {
            final int startIndex = 63 - Long.numberOfTrailingZeros(possibleMove);
            long currentQueenMove = (generateLineAttackMask(startIndex, occupied) | generateDiagonalAttacks(startIndex, occupied)) & (occupiedByOtherTeam | empty);
            attackMask |= currentQueenMove;
            long currentMove = currentQueenMove & ~(currentQueenMove - 1);
            while (currentMove != 0) {
                final int toIndex = 63 - Long.numberOfTrailingZeros(currentMove);
                moves.add(new Move(startIndex, toIndex));
                currentQueenMove &= ~currentMove;
                currentMove = currentQueenMove & ~(currentQueenMove - 1);
            }
            tmpQueens &= ~possibleMove;
            possibleMove = tmpQueens & ~(tmpQueens - 1);
        }
        return attackMask;
    }

    /**
     * @param knightIndex the index, the knight is positioned. 0 => A8, 63 => F1
     * @return A bitboard where each attack is marked with 1
     */
    public static long generateKnightAttackMask(long knightIndex) {
        // The KnightMask is positioned at F3, which is at Index 45, so if
        // the knightIndex is > 45, we shift right, otherwise left
        final long threshold = 64 - 19;
        // Masks off any overhead at AB, GH
        long additionalMask = (knightIndex % 8 < 4) ? ~(BitBoards.FILE_G | FILE_H) : ~(BitBoards.FILE_A | BitBoards.FILE_B);
        if (knightIndex > threshold) {
            return BitBoards.KNIGHT_ATTACK_MASK >>> (knightIndex - threshold) & additionalMask;
        } else {
            return BitBoards.KNIGHT_ATTACK_MASK << (threshold - knightIndex) & additionalMask;
        }
    }

    // Checkout https://www.chessprogramming.org/Efficient_Generation_of_Sliding_Piece_Attacks#Sliding_Attacks_by_Calculation for more
    // details on how this works
    // You may also check out this video, which may explain it further: https://www.youtube.com/watch?v=bCH4YK6oq8M
    public static long generateLineAttackMask(int pieceIndex, final long occupiedSquares) {
        long piecePositionBitboard = 1L << (63 - pieceIndex); // Convert to 64 bitboard // TODO MVR magic number
        int rank = (8 - 1) - pieceIndex / 8;  // TODO MVR magic numbers
        int file = pieceIndex % 8;
        long horizontalMoves = ((occupiedSquares - 2 * piecePositionBitboard) ^ Long.reverse(Long.reverse(occupiedSquares) - 2 * Long.reverse(piecePositionBitboard)));
        long verticalMoves = ((occupiedSquares & BitBoards.FILE_MASKS[file]) - (2 * piecePositionBitboard)) ^ Long.reverse(Long.reverse(occupiedSquares & BitBoards.FILE_MASKS[file]) - (2 * Long.reverse(piecePositionBitboard)));
        return (verticalMoves & BitBoards.FILE_MASKS[file]) | horizontalMoves & BitBoards.RANK_MASKS[rank];
    }

    public static long generateDiagonalAttacks(int pieceIndex, long occupiedSquares) {
        final long piecePositionBitboard = 1L << (63 - pieceIndex); // TODO MVR magic numbers
        final long diagonalMask = BitBoards.DIAGONAL_MASKS[(pieceIndex / 8) + (pieceIndex % 8)]; // TODO MVR magic numbers
        final long antiDiagonalMask = BitBoards.ANTI_DIAGONAL_MASKS[(pieceIndex / 8 + 7) - (pieceIndex % 8)]; // TODO MVR magic numbers
        // TODO MVR should be identical to verticalMoves
        long diagonalAttacks = ((occupiedSquares & diagonalMask) - (2 * piecePositionBitboard)) ^ Long.reverse(Long.reverse(occupiedSquares & diagonalMask) - (2 * Long.reverse(piecePositionBitboard)));
        long antiDiagonalAttacks = ((occupiedSquares & antiDiagonalMask) - (2 * piecePositionBitboard)) ^ Long.reverse(Long.reverse(occupiedSquares & antiDiagonalMask) - (2 * Long.reverse(piecePositionBitboard)));
        return (diagonalAttacks & diagonalMask) | (antiDiagonalAttacks & antiDiagonalMask);
    }

    // TODO MVR ...
    public static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static void main(String[] args) {
        final Board board = new FenParser().parse(STARTING_FEN);
        final List<Move> moves = MoveGeneration.generateMoves(board, Team.White);
        System.out.println();
    }
}
