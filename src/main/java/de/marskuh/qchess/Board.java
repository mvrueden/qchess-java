package de.marskuh.qchess;

import java.util.*;

public class Board {

    public Map<Team, Side> bitboards = new HashMap<>();
    private Team nextTurn;

    private Board() {
        this(new HashMap<>());
    }

    public Board(Map<Team, Side> boardMap) {
        this(boardMap, Team.White);
    }

    public Board(Map<Team, Side> boardMap, Team nextTurn) {
        this.bitboards.putAll(boardMap);
        this.bitboards.putIfAbsent(Team.White, new Side());
        this.bitboards.putIfAbsent(Team.Black, new Side());
        this.nextTurn = nextTurn == null ? Team.White : nextTurn;
    }

    public long getEmptySquares() {
        return ~getOccupiedSquares();
    }

    public long getOccupiedSquares() {
        return bitboards.values().stream().map(Side::getOccupiedSquares).reduce((left, right) -> left | right).orElse(0L);
    }

    public Piece getPieceAt(int x, int y) {
        final int index = BitUtils.calculateIndex(x, y);
        return getPieceAt(index);
    }

    private Piece getPieceAt(int index) {
        final Piece piece = bitboards.entrySet().stream()
            .map(e -> {
                final Piece p = e.getValue().getPieceAt(index);
                if (p != null) {
                    p.setTeam(e.getKey());
                }
                return p;
            })
            .filter(Objects::nonNull)
            .findFirst().orElse(null);
        return piece;
    }

    public long getOccupiedSquares(Team white) {
        return bitboards.get(white).mergeBitBoardsWithKing();
    }

    public Team getActiveTeam() {
        return nextTurn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(bitboards, board.bitboards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bitboards);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Board board = new Board();

        public Board build() {
            return board;
        }

        public Builder withRooks(Team team, long... positions) {
            board.bitboards.get(team).withRooks(BitUtils.OR(positions));
            return this;
        }

        public Builder withPawns(Team team, long... positions) {
            board.bitboards.get(team).withPawns(BitUtils.OR(positions));
            return this;
        }

        public Builder withKing(Team team, long position) {
            board.bitboards.get(team).withKing(position);
            return this;
        }

        public Builder withBishops(Team team, long... positions) {
            board.bitboards.get(team).withBishops(BitUtils.OR(positions));
            return this;
        }

        public Builder withKnights(Team team, long... positions) {
            board.bitboards.get(team).withKnights(BitUtils.OR(positions));
            return this;
        }

        public Builder withQueens(Team team, long... positions) {
            board.bitboards.get(team).withQueens(BitUtils.OR(positions));
            return this;
        }
    }
}
