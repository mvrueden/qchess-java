package de.marskuh.qchess;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static de.marskuh.qchess.BitBoards.*;
import static de.marskuh.qchess.FenParserTest.STARTING_FEN;

public class BoardTest {

    @Test
    public void verifyOccupiedSquares() {
        final Board board = new FenParser().parse(STARTING_FEN);
        Assertions.assertThat(board.getOccupiedSquares()).isEqualTo(RANK_1 | RANK_2 | RANK_7 | RANK_8);
    }

    @Test
    public void verifyEmptySquares() {
        final Board board = new FenParser().parse(STARTING_FEN);
        Assertions.assertThat(board.getEmptySquares()).isEqualTo(RANK_3 | RANK_4 | RANK_5 | RANK_6);
    }

    @Test
    public void verifySquaresOccupiedByWhite() {
        final Board board = new FenParser().parse(STARTING_FEN);
        Assertions.assertThat(board.getOccupiedSquares(Team.White)).isEqualTo(RANK_1 | RANK_2);
    }

    @Test
    public void verifySquaresOccupiedByBlack() {
        final Board board = new FenParser().parse(STARTING_FEN);
        Assertions.assertThat(board.getOccupiedSquares(Team.Black)).isEqualTo(RANK_7 | RANK_8);
    }
}