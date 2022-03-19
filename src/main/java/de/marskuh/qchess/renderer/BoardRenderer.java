package de.marskuh.qchess.renderer;

import de.marskuh.qchess.Piece;
import de.marskuh.qchess.Board;

public class BoardRenderer {
    public String render(Board board) {
        final StringBuilder sb = new StringBuilder();
        for (int y = 7; y >= 0; y--) {
            sb.append(y + 1).append(" ");
            for (int x = 0; x < 8; x++) {
                final Piece piece = board.getPieceAt(x, y);
                final char c = getPieceRepresentation(piece);
                sb.append(c).append(" ");
            }
            sb.append("\n");
        }
        sb.append("  a  b c  d  e f  g  h");
        return sb.toString();
    }

    private static char getPieceRepresentation(Piece piece) {
        if (piece == null)
            return ' ';
        final char[][] chars = new char[][] {
                {'♔', '♕', '♖', '♗', '♘', '♙'},
                {'♚', '♛', '♜', '♝', '♞', '♟'}
        };
        final int teamIndex = piece.isWhite() ? 0 : 1;
        switch(piece.getPieceType()) {
            case King: return chars[teamIndex][0];
            case Queen:return chars[teamIndex][1];
            case Rook:return chars[teamIndex][2];
            case Bishop: return chars[teamIndex][3];
            case Knight: return chars[teamIndex][4];
            case Pawn: return chars[teamIndex][5];
            default: throw new IllegalArgumentException("Cannot convert piece to char representation");
        }
    }
}
