package de.marskuh.qchess;

import de.marskuh.qchess.renderer.BitboardRenderer;
import org.junit.Test;

import static de.marskuh.qchess.FenParserTest.STARTING_FEN;

// TODO MVR rework so it is actually verifying and not printing :D
public class BitBoardsTest {

    @Test
    public void verifyFiles() {
        final long bitboard = BitBoards.ALL_MASK;
        System.out.println(new BitboardRenderer("FILE_1").render(BitBoards.FILE_A & bitboard));
        System.out.println(new BitboardRenderer("FILE_2").render(BitBoards.FILE_B & bitboard));
        System.out.println(new BitboardRenderer("FILE_3").render(BitBoards.FILE_C & bitboard));
        System.out.println(new BitboardRenderer("FILE_4").render(BitBoards.FILE_D & bitboard));
        System.out.println(new BitboardRenderer("FILE_5").render(BitBoards.FILE_E & bitboard));
        System.out.println(new BitboardRenderer("FILE_6").render(BitBoards.FILE_F & bitboard));
        System.out.println(new BitboardRenderer("FILE_7").render(BitBoards.FILE_G & bitboard));
        System.out.println(new BitboardRenderer("FILE_8").render(BitBoards.FILE_H & bitboard));
    }

    @Test
    public void verifyRanks() {
        final long bitboard = BitBoards.ALL_MASK;
        System.out.println(new BitboardRenderer("RANK_1").render(BitBoards.RANK_1 & bitboard));
        System.out.println(new BitboardRenderer("RANK_2").render(BitBoards.RANK_2 & bitboard));
        System.out.println(new BitboardRenderer("RANK_3").render(BitBoards.RANK_3 & bitboard));
        System.out.println(new BitboardRenderer("RANK_4").render(BitBoards.RANK_4 & bitboard));
        System.out.println(new BitboardRenderer("RANK_5").render(BitBoards.RANK_5 & bitboard));
        System.out.println(new BitboardRenderer("RANK_6").render(BitBoards.RANK_6 & bitboard));
        System.out.println(new BitboardRenderer("RANK_7").render(BitBoards.RANK_7 & bitboard));
        System.out.println(new BitboardRenderer("RANK_8").render(BitBoards.RANK_8 & bitboard));
    }

    @Test
    public void verifyBlackSquares() {
        final long bitboard = BitBoards.ALL_MASK;
        System.out.println(new BitboardRenderer("Black Squares").render(BitBoards.BLACK_SQUARES & bitboard));
    }

    @Test
    public void verifyWhiteSquares() {
        final String input = BitUtils.toBinaryString(BitBoards.ALL_MASK);
        final long bitboard = BitUtils.parseLong(input);
        System.out.println(new BitboardRenderer("Black Squares").render(BitBoards.WHITE_SQUARES & bitboard));
    }

    @Test
    public void verifyOccupiedSquares() {
        final Board board = new FenParser().parse(STARTING_FEN);
        System.out.println(new BitboardRenderer("Occupied Squares").render(board.getOccupiedSquares()));
    }

    @Test
    public void verifyEmptySquares() {
        final Board board = new FenParser().parse(STARTING_FEN);
        System.out.println(new BitboardRenderer("Empty Squares").render(board.getEmptySquares()));
    }

    @Test
    public void verifySquaresOccupiedByWhite() {
        final Board board = new FenParser().parse(STARTING_FEN);
        System.out.println(new BitboardRenderer("Squares occupied by Black Pieces").render(board.getOccupiedSquares(Team.White)));
    }
    @Test
    public void verifySquaresOccupiedByBlack() {
        final Board board = new FenParser().parse(STARTING_FEN);
        System.out.println(new BitboardRenderer("Squares occupied by Black Pieces").render(board.getOccupiedSquares(Team.Black)));
    }


}
