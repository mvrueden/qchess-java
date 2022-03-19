package de.marskuh.qchess;

// TODO MVR Maybe rename to Color?
public enum Team {
    Black,
    White;

    public boolean isWhite() {
        return this == White;
    }

    public boolean isBlack() {
        return this == Black;
    }
}
