package de.marskuh.qchess;

import de.marskuh.qchess.renderer.BitboardRenderer;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static de.marskuh.qchess.BitBoards.*;
import static de.marskuh.qchess.FenParserTest.STARTING_FEN;

public class BitBoardsTest {

    @Test
    public void verifyAllMask() {
        Assertions.assertThat(ALL_MASK).isEqualTo(0xFFFFFFFFFFFFFFFFL);
    }

    @Test
    public void verifyFiles() {
        final long bitboard = ALL_MASK;
        final long[] expected = new long[] {
                0b1000000010000000100000001000000010000000100000001000000010000000L,
                0b0100000001000000010000000100000001000000010000000100000001000000L,
                0b0010000000100000001000000010000000100000001000000010000000100000L,
                0b0001000000010000000100000001000000010000000100000001000000010000L,
                0b0000100000001000000010000000100000001000000010000000100000001000L,
                0b0000010000000100000001000000010000000100000001000000010000000100L,
                0b0000001000000010000000100000001000000010000000100000001000000010L,
                0b0000000100000001000000010000000100000001000000010000000100000001L,
        };
        Assertions.assertThat(expected).hasSize(8); // TODO MVR magic numbers

        // Assert each file individually
        Assertions.assertThat(expected[0]).isEqualTo(FILE_A);
        Assertions.assertThat(expected[1]).isEqualTo(FILE_B);
        Assertions.assertThat(expected[2]).isEqualTo(FILE_C);
        Assertions.assertThat(expected[3]).isEqualTo(FILE_D);
        Assertions.assertThat(expected[4]).isEqualTo(FILE_E);
        Assertions.assertThat(expected[5]).isEqualTo(FILE_F);
        Assertions.assertThat(expected[6]).isEqualTo(FILE_G);
        Assertions.assertThat(expected[7]).isEqualTo(FILE_H);

        // Ensure each entry in the array is also setup correctly
        for (int i=0; i<8; i++) {
            LoggerFactory.getLogger(getClass()).info("Verify file {}", (char) (i + 'A'));
            Assertions.assertThat(bitboard & FILE_MASKS[i]).isEqualTo(expected[i]);
        }
    }

    @Test
    public void verifyRanks() {
        final long bitboard = ALL_MASK;
        final long[] expected = new long[] {
                0b0000000000000000000000000000000000000000000000000000000011111111L,
                0b0000000000000000000000000000000000000000000000001111111100000000L,
                0b0000000000000000000000000000000000000000111111110000000000000000L,
                0b0000000000000000000000000000000011111111000000000000000000000000L,
                0b0000000000000000000000001111111100000000000000000000000000000000L,
                0b0000000000000000111111110000000000000000000000000000000000000000L,
                0b0000000011111111000000000000000000000000000000000000000000000000L,
                0b1111111100000000000000000000000000000000000000000000000000000000L,
        };
        Assertions.assertThat(expected).hasSize(8); // TODO MVR magic numbers

        // Assert each rank individually
        Assertions.assertThat(expected[0]).isEqualTo(RANK_1);
        Assertions.assertThat(expected[1]).isEqualTo(RANK_2);
        Assertions.assertThat(expected[2]).isEqualTo(RANK_3);
        Assertions.assertThat(expected[3]).isEqualTo(RANK_4);
        Assertions.assertThat(expected[4]).isEqualTo(RANK_5);
        Assertions.assertThat(expected[5]).isEqualTo(RANK_6);
        Assertions.assertThat(expected[6]).isEqualTo(RANK_7);
        Assertions.assertThat(expected[7]).isEqualTo(RANK_8);

        // Ensure each entry in the array is also setup correctly
        for (int i=0; i<8; i++) {
            LoggerFactory.getLogger(getClass()).info("Verify file {}", (char) (i + 'A'));
            Assertions.assertThat(bitboard & RANK_MASKS[i]).isEqualTo(expected[i]);
        }
    }

    @Test
    public void verifyBlackSquares() {
        final long expected = 0b0101010110101010010101011010101001010101101010100101010110101010L;
        Assertions.assertThat(BLACK_SQUARES).isEqualTo(expected);
    }

    @Test
    public void verifyWhiteSquares() {
        final long expected = 0b1010101001010101101010100101010110101010010101011010101001010101L;
        Assertions.assertThat(WHITE_SQUARES).isEqualTo(expected);
    }

    @Test
    public void verifySquaresOrToAll() {
        Assertions.assertThat(BLACK_SQUARES | WHITE_SQUARES).isEqualTo(ALL_MASK);
    }
}
