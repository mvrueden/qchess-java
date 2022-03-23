package de.marskuh.qchess;

import java.util.Objects;

// TODO MVR not the ideal naming perhaps
public class Side {

    public long pawns;
    public long knights;
    public long bishops;
    public long queens;
    public long rooks;
    public long king;
    public MoveGeneration.CastlingInfo castling = new MoveGeneration.CastlingInfo();

    public Side(MoveGeneration.CastlingInfo castling) {
        this.castling = Objects.requireNonNull(castling);
    }

    public Side() {

    }

    public MoveGeneration.CastlingInfo getCastling() {
        return castling;
    }

    public Side withPawns(long pawns) {
        this.pawns = pawns;
        return this;
    }

    public Side withKnights(long knights) {
        this.knights = knights;
        return this;
    }

    public Side withRooks(long rooks) {
        this.rooks = rooks;
        return this;
    }

    public Side withBishops(long bishops) {
        this.bishops = bishops;
        return this;
    }

    public Side withQueens(long queens) {
        this.queens = queens;
        return this;
    }

    public Side withKing(long king) {
        this.king = king;
        return this;
    }

    public Side withCastling(boolean canCastleKingSide, boolean canCastleQueenSide) {
        this.castling = new MoveGeneration.CastlingInfo(canCastleKingSide, canCastleQueenSide);
        return this;
    }

    public long mergeBitBoardsWithoutKing() {
        return pawns | knights | bishops | queens | rooks;
    }

    public long mergeBitBoardsWithKing() {
        return mergeBitBoardsWithoutKing() | king;
    }

    public long getOccupiedSquares() {
        return mergeBitBoardsWithKing();
    }

    public void setPieceType(int col, int row, PieceType type) {
        final int index = BitUtils.calculateIndex(col, row);
        final String empty = "0000000000000000000000000000000000000000000000000000000000000000"; // 64 bit
        final String modifiedBit = empty.substring(0, index) + "1" + empty.substring(index + 1);
        final long update = BitUtils.parseLong(modifiedBit);
        switch (type) {
            case Pawn:
                pawns |= update;
                break;
            case King:
                king |= update;
                break;
            case Bishop:
                bishops |= update;
                break;
            case Queen:
                queens |= update;
                break;
            case Knight:
                knights |= update;
                break;
            case Rook:
                rooks |= update;
                break;
        }
    }

    public Piece getPieceAt(int index) {
        final PieceType type = getPieceType(index);
        if (type == null) return null;
        final int x = index % 8;
        final int y = index / 8;
        final Piece piece = new Piece(type, x, y);
        return piece;
    }

    private PieceType getPieceType(int index) {
        if (BitUtils.toBinaryString(pawns).charAt(index) == '1')
            return PieceType.Pawn;
        if (BitUtils.toBinaryString(knights).charAt(index) == '1')
            return PieceType.Knight;
        if (BitUtils.toBinaryString(bishops).charAt(index) == '1')
            return PieceType.Bishop;
        if (BitUtils.toBinaryString(queens).charAt(index) == '1')
            return PieceType.Queen;
        if (BitUtils.toBinaryString(rooks).charAt(index) == '1')
            return PieceType.Rook;
        if (BitUtils.toBinaryString(king).charAt(index) == '1')
            return PieceType.King;
        return null; // No piece Type
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Side side = (Side) o;
        return pawns == side.pawns && knights == side.knights && bishops == side.bishops && queens == side.queens && rooks == side.rooks && king == side.king;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pawns, knights, bishops, queens, rooks, king);
    }
}
