package de.marskuh.qchess;

import de.marskuh.qchess.renderer.BitboardRenderer;
import de.marskuh.qchess.renderer.BoardRenderer;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static de.marskuh.qchess.BitBoards.RANK_2;
import static de.marskuh.qchess.BitBoards.RANK_7;
import static de.marskuh.qchess.BitBoards.Tiles.*;
import static de.marskuh.qchess.BitBoards.Tiles.A8;
import static de.marskuh.qchess.BitBoards.Tiles.H8;

public class FenParserTest {

    private static final Logger logger = LoggerFactory.getLogger(FenParserTest.class);

    public static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Test
    public void verifyStartingFen() {
        final Map<Team, Map<PieceType, Long>> expectedBitboards = new HashMap<>();

        // Black
        expectedBitboards.put(Team.Black, new HashMap<>());
        expectedBitboards.get(Team.Black).put(PieceType.Rook, A8 | H8);
        expectedBitboards.get(Team.Black).put(PieceType.Knight, B8 | G8);
        expectedBitboards.get(Team.Black).put(PieceType.Bishop, C8 | F8);
        expectedBitboards.get(Team.Black).put(PieceType.Queen, D8);
        expectedBitboards.get(Team.Black).put(PieceType.King, E8);
        expectedBitboards.get(Team.Black).put(PieceType.Pawn, RANK_7);

        // White
        expectedBitboards.put(Team.White, new HashMap<>());
        expectedBitboards.get(Team.White).put(PieceType.Rook, A1 | H1);
        expectedBitboards.get(Team.White).put(PieceType.Knight, B1 | G1);
        expectedBitboards.get(Team.White).put(PieceType.Bishop, C1 | F1);
        expectedBitboards.get(Team.White).put(PieceType.Queen, D1);
        expectedBitboards.get(Team.White).put(PieceType.King, E1);
        expectedBitboards.get(Team.White).put(PieceType.Pawn, RANK_2);

        // Parse and validate
        final Board board = new FenParser().parse(STARTING_FEN);
        for (Team eachTeam : Team.values()) {
            logger.info("Verifying starting position for {}", eachTeam);
            final Side side = board.bitboards.get(eachTeam);
            Assertions.assertThat(side.rooks).describedAs("Rook bitboard").isEqualTo(expectedBitboards.get(eachTeam).get(PieceType.Rook));
            Assertions.assertThat(side.knights).describedAs("Knight bitboard").isEqualTo(expectedBitboards.get(eachTeam).get(PieceType.Knight));
            Assertions.assertThat(side.bishops).describedAs("Bishop bitboard").isEqualTo(expectedBitboards.get(eachTeam).get(PieceType.Bishop));
            Assertions.assertThat(side.queens).describedAs("Queen bitboard").isEqualTo(expectedBitboards.get(eachTeam).get(PieceType.Queen));
            Assertions.assertThat(side.king).describedAs("King bitboard").isEqualTo(expectedBitboards.get(eachTeam).get(PieceType.King));
            Assertions.assertThat(side.pawns).describedAs("Pawn bitboard").isEqualTo(expectedBitboards.get(eachTeam).get(PieceType.Pawn));
        }
    }

    @Test
    public void verifyCustomPosition() {
        final Board expectedBoard = Board.builder()
                .withRooks(Team.Black, A8, C7)
                .withPawns(Team.Black, A7, G7, H6, B5)
                .withPawns(Team.White, A6)
                .withBishops(Team.White, E6, E5)
                .withKing(Team.Black, B8)
                .withKing(Team.White, G6)
                .build();
        final Board actualBoard = new FenParser().parse("rk6/p1r3p1/P3B1Kp/1p2B3/8/8/8/8 w - - 0 1");
        Assertions.assertThat(actualBoard).isEqualTo(expectedBoard);
    }

}