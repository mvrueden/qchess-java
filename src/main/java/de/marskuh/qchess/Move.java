package de.marskuh.qchess;

// A move is encoded as a 16 bit integer in the history
// Inspired by stockfish
// TODO MVR do we really need this or can we abstract it a bit more?!
public class Move {

    private final int internalRepresentation;

    public Move(int fromIndex, int toIndex) {
        this(fromIndex, toIndex, MoveFlag.Normal, null);
    }

    public Move(int fromIndex, int toIndex, MoveFlag moveFlag) {
        this(fromIndex, toIndex, moveFlag, null);
    }

    public Move(int fromIndex, int toIndex, PieceType promoteTo) {
        this(fromIndex, toIndex, MoveFlag.Promotion, promoteTo);
    }

    private Move(int fromIndex, int toIndex, MoveFlag moveFlag, PieceType promoteTo) {
        if (moveFlag == MoveFlag.Promotion && promoteTo == null)
            throw new IllegalArgumentException("If you want to promote a piece, promoteTo must not be null"); // TODO MVR
        if (moveFlag != MoveFlag.Promotion && promoteTo != null)
            throw new IllegalArgumentException("If you want to promote a piece, moveflag should be Promotion"); // TODO MVR
        if (fromIndex < 0 || fromIndex > 63)
            throw new IllegalArgumentException("fromIndex must be between 0 and 63");
        if (toIndex < 0 || toIndex > 63)
            throw new IllegalArgumentException("toIndex must be between 0 and 63");

        int representation = (fromIndex << 10);
        representation |= (toIndex << 4);
        representation += moveFlag.ordinal();

        // In case we have a promoteTo, we need to add it
        if (promoteTo != null && moveFlag == MoveFlag.Promotion) {
            // 0 => queen
            // 1 => bishop
            // 2 => knight
            // 3 => rook
            int bit = getPromoteToBit(promoteTo);
            representation += (bit << 2);
        }
        this.internalRepresentation = representation;
    }

    public boolean isCastleMove() {
        return getMoveFlag() == MoveFlag.Castling;
    }

    public boolean isEnPassantMove() {
        return getMoveFlag() == MoveFlag.EnPassant;
    }

    public boolean isPromotionMove() {
        return getMoveFlag() == MoveFlag.Promotion;
    }

    public boolean isNormalMove() {
        return getMoveFlag() == MoveFlag.Normal;
    }

    public boolean isMove(MoveFlag moveFlag) {
        return getMoveFlag() == moveFlag;
    }

    public int getFromIndex() {
        return internalRepresentation >>> 10;
    }

    public int getToIndex() {
        return (internalRepresentation >> 4) & 0x0000003F;
    }

    public int getBits() {
        return internalRepresentation;
    }

    public PieceType getPromoteTo() {
        if (!isPromotionMove()) {
            throw new IllegalStateException("This move is not a promotion move");
        }
        final int pieceTypeBit = (internalRepresentation >>> 2) & 3;
        switch (pieceTypeBit) {
            case 0:
                return PieceType.Queen;
            case 1:
                return PieceType.Knight;
            case 2:
                return PieceType.Bishop;
            case 3:
                return PieceType.Rook;
            default:
                throw new IllegalStateException("The provided pieceTypeBit is unknown and cannot be converted");
        }
    }

    public static int getPromoteToBit(PieceType promoteTo) {
        switch (promoteTo) {
            case Queen:
                return 0;
            case Knight:
                return 1;
            case Bishop:
                return 2;
            case Rook:
                return 3;
            default:
                throw new IllegalArgumentException("Can only promote to Queen, Knight, Bishop or Rook");
        }
    }

    public MoveFlag getMoveFlag() {
        int index = internalRepresentation & 3;
        return MoveFlag.values()[index];
    }

}
