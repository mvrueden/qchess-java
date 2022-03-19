package de.marskuh.qchess;

public class Piece {

    private Team team;
    private final PieceType type;
    private final int x;
    private final int y;

    public Piece(PieceType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public Piece(Team team, PieceType type, int x, int y) {
        this(type, x, y);
        this.team = team;
    }

    public PieceType getPieceType() {
        return type;
    }

    public boolean isWhite() {
        return team == Team.White;
    }

    public boolean isBlack() {
        return team == Team.Black;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
