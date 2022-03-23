package de.marskuh.qchess;

import java.util.HashMap;
import java.util.Map;

public class FenParser {

    public Board parse(String fen) {
        if (fen == null || fen.isEmpty()) throw new IllegalArgumentException("Provided input string is empty");
        final String[] segments = fen.split(" ");
        if (segments.length != 1 && segments.length != 6) {
            throw new IllegalArgumentException("The provided string is not a valid fen expression");
        }
        final Map<Team, Side> boardMap = ParsePositions(segments[0]);
        boardMap.get(Team.White).withCastling(segments[2].contains("K"), segments[2].contains("Q"));
        boardMap.get(Team.Black).withCastling(segments[2].contains("k"), segments[2].contains("q"));
        final Team nextTurn = segments[1].equalsIgnoreCase("w") ? Team.White : Team.Black;
        return new Board(boardMap, nextTurn);
    }

    private Map<Team, Side> ParsePositions(String segment) {
        final Map<Team, Side> boardMap = new HashMap<>();
        boardMap.put(Team.Black, new Side());
        boardMap.put(Team.White, new Side());
        int col = 0;
        int row = 7;
        int index = 0;
        while (index < segment.length()) {
            char c = segment.charAt(index);
            if (Character.isDigit(c)) {
                col += Integer.parseInt(Character.toString(c));
            } else if (c == '/') {
                col = 0;
                row--;
            } else {
                final PieceType type = DetermineType(c);
                final Team team = Character.isLowerCase(c) ? Team.Black : Team.White;
                final Side board = boardMap.get(team);
                board.setPieceType(col, row, type);
                col++;
            }
            index++;
        }
        return boardMap;
    }

    private static PieceType DetermineType(char c) {
        switch(Character.toLowerCase(c)) {
            case 'r': return PieceType.Rook;
            case 'b': return PieceType.Bishop;
            case 'q': return PieceType.Queen;
            case 'k': return PieceType.King;
            case 'n': return PieceType.Knight;
            case 'p': return PieceType.Pawn;
            default: throw new IllegalArgumentException("Provided character " + c + " is not a valid chess piece");
        }
    }
}