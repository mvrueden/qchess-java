package de.marskuh.qchess;

// TODO MVR ... maybe also add capture and normal move here
// Inspired by stockfish
public enum MoveFlag {
    Normal,
    Castling,
    EnPassant,
    Promotion
}
