package de.marskuh.qchess;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Bytes;
import de.marskuh.qchess.renderer.BitboardRenderer;
import de.marskuh.qchess.renderer.BoardRenderer;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static de.marskuh.qchess.BitBoards.*;
import static de.marskuh.qchess.BitBoards.RANK_1;
import static de.marskuh.qchess.BitBoards.Tiles.*;

public class MoveGenerationTest {

    final Logger logger = LoggerFactory.getLogger(MoveGenerationTest.class);

    @Test
    public void verifyKnightMoves() {
        // Here we store the expected fields the knight is attacking, starting from A8 to H1
        final long[] expectedAttackMask = new long[]{
                // Rank 8
                B6 | C7,                            // A8
                A6 | C6 | D7,                       // B8
                A7 | B6 | D6 | E7,
                B7 | C6 | E6 | F7,
                C7 | D6 | F6 | G7,
                D7 | E6 | G6 | H7,
                E7 | F6 | H6,
                F7 | G6,                            // H8

                // Rank 7
                B5 | C6 | C8,                       // A7
                A5 | C5 | D6 | D8,
                A8 | A6 | B5 | D5 | E6 | E8,
                B8 | B6 | C5 | E5 | F6 | F8,
                C8 | C6 | D5 | F5 | G6 | G8,
                D8 | D6 | E5 | G5 | H6 | H8,
                E8 | E6 | F5 | H5,
                F8 | F6 | G5,

                // Rank 6
                B8 | B4 | C5 | C7,
                A8 | A4 | C4 | D5 | D7 | C8,
                B8 | A7 | A5 | B4 | D4 | D8 | E7 | E5,
                C8 | B7 | B5 | C4 | E4 | F5 | F7 | E8,
                D8 | C7 | C5 | D4 | F4 | G5 | G7 | F8,
                E8 | D7 | D5 | E4 | G4 | H5 | H7 | G8,
                F8 | H8 | E7 | E5 | F4 | H4,
                G8 | F7 | F5 | G4,

                // Rank 5
                B7 | C6 | C4 | B3,
                A7 | C7 | D6 | D4 | C3 | A3,
                B7 | A6 | A4 | B3 | D3 | E4 | E6 | D7,
                C7 | B6 | B4 | C3 | E3 | F4 | F6 | E7,
                D7 | C6 | C4 | D3 | F3 | G4 | G6 | F7,
                E7 | D6 | D4 | E3 | G3 | H4 | H6 | G7,
                F7 | E6 | E4 | F3 | H3 | H7,
                G7 | F6 | F4 | G3,

                // Rank 4
                B6 | C5 | C3 | B2,
                A6 | C6 | D5 | D3 | C2 | A2,
                D6 | E5 | E3 | D2 | B2 | A3 | A5 | B6,
                E6 | F5 | F3 | E2 | C2 | B3 | B5 | C6,
                F6 | G5 | G3 | F2 | D2 | C3 | C5 | D6,
                G6 | H5 | H3 | G2 | E2 | D3 | D5 | E6,
                H6 | H2 | F2 | E3 | E5 | F6,
                G2 | F3 | F5 | G6,

                // Rank 3
                B5 | C4 | C2 | B1,
                C5 | D4 | D2 | C1 | A1 | A5,
                D5 | E4 | E2 | D1 | B1 | A2 | A4 | B5,
                E5 | F4 | F2 | E1 | C1 | B2 | B4 | C5,
                F5 | G4 | G2 | F1 | D1 | C2 | C4 | D5,
                G5 | H4 | H2 | G1 | E1 | D2 | D4 | E5,
                H5 | H1 | F1 | E2 | E4 | F5,
                G1 | F2 | F4 | G5,

                // Rank 2
                B4 | C3 | C1,
                C4 | D3 | D1 | A4,
                D4 | E3 | E1 | A1 | A3 | B4,
                E4 | F3 | F1 | B1 | C4 | B3,
                F4 | G3 | G1 | C1 | C3 | D4,
                G4 | H3 | H1 | D1 | D3 | E4,
                H4 | E1 | E3 | F4,
                F1 | F3 | G4,

                // Rank 1
                B3 | C2,
                A3 | C3 | D2,
                D3 | E2 | A2 | B3,
                C3 | E3 | F2 | B2,
                F3 | G2 | C2 | D3,
                G3 | H2 | D2 | E3,
                H3 | E2 | F3,
                G3 | F2
        };
        Assertions.assertThat(expectedAttackMask)
                .describedAs("Verify expectedAttackMask spans whole board")
                .hasSize(64); // TODO MVR magic number
        for (int i = 0; i < expectedAttackMask.length; i++) {
            logger.info("Verifying knight attacks at index {}", i);
            final long attackMask = MoveGeneration.generateKnightAttackMask(i);
            logger.debug(new BitboardRenderer("Actual Attack Mask for position " + i).render(attackMask));
            logger.debug(new BitboardRenderer("Expected Attack Mask for position " + i).render(expectedAttackMask[i]));
            Assertions.assertThat(attackMask).isEqualTo(expectedAttackMask[i]);
        }
    }

    @Test
    public void verifyHorizontalAndVerticalMoves() {
        final long[] expected = new long[]{
                FILE_A | RANK_8,
                FILE_B | RANK_8,
                FILE_C | RANK_8,
                FILE_D | RANK_8,
                FILE_E | RANK_8,
                FILE_F | RANK_8,
                FILE_G | RANK_8,
                FILE_H | RANK_8,

                FILE_A | RANK_7,
                FILE_B | RANK_7,
                FILE_C | RANK_7,
                FILE_D | RANK_7,
                FILE_E | RANK_7,
                FILE_F | RANK_7,
                FILE_G | RANK_7,
                FILE_H | RANK_7,

                FILE_A | RANK_6,
                FILE_B | RANK_6,
                FILE_C | RANK_6,
                FILE_D | RANK_6,
                FILE_E | RANK_6,
                FILE_F | RANK_6,
                FILE_G | RANK_6,
                FILE_H | RANK_6,

                FILE_A | RANK_5,
                FILE_B | RANK_5,
                FILE_C | RANK_5,
                FILE_D | RANK_5,
                FILE_E | RANK_5,
                FILE_F | RANK_5,
                FILE_G | RANK_5,
                FILE_H | RANK_5,

                FILE_A | RANK_4,
                FILE_B | RANK_4,
                FILE_C | RANK_4,
                FILE_D | RANK_4,
                FILE_E | RANK_4,
                FILE_F | RANK_4,
                FILE_G | RANK_4,
                FILE_H | RANK_4,

                FILE_A | RANK_3,
                FILE_B | RANK_3,
                FILE_C | RANK_3,
                FILE_D | RANK_3,
                FILE_E | RANK_3,
                FILE_F | RANK_3,
                FILE_G | RANK_3,
                FILE_H | RANK_3,

                FILE_A | RANK_2,
                FILE_B | RANK_2,
                FILE_C | RANK_2,
                FILE_D | RANK_2,
                FILE_E | RANK_2,
                FILE_F | RANK_2,
                FILE_G | RANK_2,
                FILE_H | RANK_2,

                FILE_A | RANK_1,
                FILE_B | RANK_1,
                FILE_C | RANK_1,
                FILE_D | RANK_1,
                FILE_E | RANK_1,
                FILE_F | RANK_1,
                FILE_G | RANK_1,
                FILE_H | RANK_1,
        };
        Assertions.assertThat(expected).hasSize(64); // TODO MVR magic numbers

        // We have to "remove" the rook bit, in order to get the correct result
        for (int i = 0; i < 64; i++) {
            expected[i] = expected[i] & ~(1L << (63 - i));
        }

        for (int i = 0; i < 64; i++) {
            LoggerFactory.getLogger(getClass()).info("Verifying {}", i);
            Assertions.assertThat(MoveGeneration.generateLineAttackMask(i, BitBoards.EMPTY_MASK | (1L << 63 - i))).isEqualTo(expected[i]);
        }
    }

    @Test
    public void verifyDiagonalMovement() {
        final long[] expectedAttackMask = new long[]{
                DIAGONAL_MASKS[0] | ANTI_DIAGONAL_MASKS[7],
                DIAGONAL_MASKS[1] | ANTI_DIAGONAL_MASKS[6],
                DIAGONAL_MASKS[2] | ANTI_DIAGONAL_MASKS[5],
                DIAGONAL_MASKS[3] | ANTI_DIAGONAL_MASKS[4],
                DIAGONAL_MASKS[4] | ANTI_DIAGONAL_MASKS[3],
                DIAGONAL_MASKS[5] | ANTI_DIAGONAL_MASKS[2],
                DIAGONAL_MASKS[6] | ANTI_DIAGONAL_MASKS[1],
                DIAGONAL_MASKS[7] | ANTI_DIAGONAL_MASKS[0],

                DIAGONAL_MASKS[1] | ANTI_DIAGONAL_MASKS[8],
                DIAGONAL_MASKS[2] | ANTI_DIAGONAL_MASKS[7],
                DIAGONAL_MASKS[3] | ANTI_DIAGONAL_MASKS[6],
                DIAGONAL_MASKS[4] | ANTI_DIAGONAL_MASKS[5],
                DIAGONAL_MASKS[5] | ANTI_DIAGONAL_MASKS[4],
                DIAGONAL_MASKS[6] | ANTI_DIAGONAL_MASKS[3],
                DIAGONAL_MASKS[7] | ANTI_DIAGONAL_MASKS[2],
                DIAGONAL_MASKS[8] | ANTI_DIAGONAL_MASKS[1],

                DIAGONAL_MASKS[2] | ANTI_DIAGONAL_MASKS[9],
                DIAGONAL_MASKS[3] | ANTI_DIAGONAL_MASKS[8],
                DIAGONAL_MASKS[4] | ANTI_DIAGONAL_MASKS[7],
                DIAGONAL_MASKS[5] | ANTI_DIAGONAL_MASKS[6],
                DIAGONAL_MASKS[6] | ANTI_DIAGONAL_MASKS[5],
                DIAGONAL_MASKS[7] | ANTI_DIAGONAL_MASKS[4],
                DIAGONAL_MASKS[8] | ANTI_DIAGONAL_MASKS[3],
                DIAGONAL_MASKS[9] | ANTI_DIAGONAL_MASKS[2],

                DIAGONAL_MASKS[3] | ANTI_DIAGONAL_MASKS[10],
                DIAGONAL_MASKS[4] | ANTI_DIAGONAL_MASKS[9],
                DIAGONAL_MASKS[5] | ANTI_DIAGONAL_MASKS[8],
                DIAGONAL_MASKS[6] | ANTI_DIAGONAL_MASKS[7],
                DIAGONAL_MASKS[7] | ANTI_DIAGONAL_MASKS[6],
                DIAGONAL_MASKS[8] | ANTI_DIAGONAL_MASKS[5],
                DIAGONAL_MASKS[9] | ANTI_DIAGONAL_MASKS[4],
                DIAGONAL_MASKS[10] | ANTI_DIAGONAL_MASKS[3],

                DIAGONAL_MASKS[4] | ANTI_DIAGONAL_MASKS[11],
                DIAGONAL_MASKS[5] | ANTI_DIAGONAL_MASKS[10],
                DIAGONAL_MASKS[6] | ANTI_DIAGONAL_MASKS[9],
                DIAGONAL_MASKS[7] | ANTI_DIAGONAL_MASKS[8],
                DIAGONAL_MASKS[8] | ANTI_DIAGONAL_MASKS[7],
                DIAGONAL_MASKS[9] | ANTI_DIAGONAL_MASKS[6],
                DIAGONAL_MASKS[10] | ANTI_DIAGONAL_MASKS[5],
                DIAGONAL_MASKS[11] | ANTI_DIAGONAL_MASKS[4],

                DIAGONAL_MASKS[5] | ANTI_DIAGONAL_MASKS[12],
                DIAGONAL_MASKS[6] | ANTI_DIAGONAL_MASKS[11],
                DIAGONAL_MASKS[7] | ANTI_DIAGONAL_MASKS[10],
                DIAGONAL_MASKS[8] | ANTI_DIAGONAL_MASKS[9],
                DIAGONAL_MASKS[9] | ANTI_DIAGONAL_MASKS[8],
                DIAGONAL_MASKS[10] | ANTI_DIAGONAL_MASKS[7],
                DIAGONAL_MASKS[11] | ANTI_DIAGONAL_MASKS[6],
                DIAGONAL_MASKS[12] | ANTI_DIAGONAL_MASKS[5],

                DIAGONAL_MASKS[6] | ANTI_DIAGONAL_MASKS[13],
                DIAGONAL_MASKS[7] | ANTI_DIAGONAL_MASKS[12],
                DIAGONAL_MASKS[8] | ANTI_DIAGONAL_MASKS[11],
                DIAGONAL_MASKS[9] | ANTI_DIAGONAL_MASKS[10],
                DIAGONAL_MASKS[10] | ANTI_DIAGONAL_MASKS[9],
                DIAGONAL_MASKS[11] | ANTI_DIAGONAL_MASKS[8],
                DIAGONAL_MASKS[12] | ANTI_DIAGONAL_MASKS[7],
                DIAGONAL_MASKS[13] | ANTI_DIAGONAL_MASKS[6],

                DIAGONAL_MASKS[7] | ANTI_DIAGONAL_MASKS[14],
                DIAGONAL_MASKS[8] | ANTI_DIAGONAL_MASKS[13],
                DIAGONAL_MASKS[9] | ANTI_DIAGONAL_MASKS[12],
                DIAGONAL_MASKS[10] | ANTI_DIAGONAL_MASKS[11],
                DIAGONAL_MASKS[11] | ANTI_DIAGONAL_MASKS[10],
                DIAGONAL_MASKS[12] | ANTI_DIAGONAL_MASKS[9],
                DIAGONAL_MASKS[13] | ANTI_DIAGONAL_MASKS[8],
                DIAGONAL_MASKS[14] | ANTI_DIAGONAL_MASKS[7]
        };
        Assertions.assertThat(expectedAttackMask).hasSize(64); // TODO MVR magic numbers
        // Remove the field, where the diagonal piece is positioned from the expected list
        for (int i = 0; i < expectedAttackMask.length; i++) {
            expectedAttackMask[i] ^= 1L << (63 - i);
        }
        for (int i = 0; i < expectedAttackMask.length; i++) {
            LoggerFactory.getLogger(getClass()).info("Verify diagonal attack mask at index {}", i);
            final long occupied = 1L << (63 - i); // Build occupied for bishop at index i
            final long actualAttackMask = MoveGeneration.generateDiagonalAttacks(i, occupied);
            Assertions.assertThat(actualAttackMask).isEqualTo(expectedAttackMask[i]);
        }
    }

    @Test
    public void verifyPerfsuite() throws IOException {
        final byte[] bytes = ByteStreams.toByteArray(getClass().getResourceAsStream("/perfsuite.epd"));
        final String content = new String(bytes);
        final FenParser parser = new FenParser();
        for(String eachLine : content.split("\n")) {
            final String[] columns = eachLine.split(";");
            final String fen = columns[0].trim();
//            final String fen = "8/8/8/8/8/8/6k1/4K2R w K - 0 1";
//            final String fen = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";
//            final String fen = "8/8/8/8/8/8/1k6/R3K3 w Q - 0 1";
//            final String fen = "4k2r/6K1/8/8/8/8/8/8 w k - 0 1";
//            final String fen = "K7/b7/1b6/1b6/8/8/8/k6B w - - 0 1";
//            final String fen = "7k/3p4/8/8/3P4/8/8/K7 w - - 0 1";
            final String expectedString = columns[1].split(" ")[1].trim();
            final long expected = Long.parseLong(expectedString);
            final Board board = parser.parse(fen);
//            printToConsole(board);
            if (board.getActiveTeam() == Team.White) {
                final List<Move> moves = MoveGeneration.generateMoves(board, board.getActiveTeam());
                LoggerFactory.getLogger(getClass()).info("{}, expected at depth {}: {}", fen, 1, expected);
                Assertions.assertThat(moves).hasSize((int) expected);
            }
        }
    }

    private static void printToConsole(Board board) {
        System.out.println(new BoardRenderer().render(board));
    }

}
