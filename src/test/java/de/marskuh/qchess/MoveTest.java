package de.marskuh.qchess;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MoveTest {

    @Test
    public void verifyNormalMoveCreation() {
        final Move m = new Move(10, 18);
        Assertions.assertThat(m.getMoveFlag()).isEqualTo(MoveFlag.Normal);
        Assertions.assertThat(m.isPromotionMove()).isFalse();
        Assertions.assertThat(m.isCastleMove()).isFalse();
        Assertions.assertThat(m.isEnPassantMove()).isFalse();
        Assertions.assertThat(m.isNormalMove()).isTrue();
        Assertions.assertThat(m.getFromIndex()).isEqualTo(10);
        Assertions.assertThat(m.getToIndex()).isEqualTo(18);
        Assertions.assertThat(m.getBits()).isEqualTo(BitUtils.parseInt("0010100100100000"));
    }

    @Test
    public void verifyEnPassantMoveCreation() {
        final  Move m = new Move(20, 17, MoveFlag.EnPassant);
        Assertions.assertThat(m.getMoveFlag()).isEqualTo(MoveFlag.EnPassant);
        Assertions.assertThat(m.isEnPassantMove()).isTrue();
        Assertions.assertThat(m.isCastleMove()).isFalse();
        Assertions.assertThat(m.isPromotionMove()).isFalse();
        Assertions.assertThat(m.isNormalMove()).isFalse();
        Assertions.assertThat(m.getFromIndex()).isEqualTo(20);
        Assertions.assertThat(m.getToIndex()).isEqualTo(17);
        Assertions.assertThat(m.getBits()).isEqualTo(BitUtils.parseInt("0101000100010010"));
    }

    @Test
    public void verifyCastleMoveCreation() {
        final Move m = new Move(63, 0, MoveFlag.Castling);
        Assertions.assertThat(m.getMoveFlag()).isEqualTo(MoveFlag.Castling);
        Assertions.assertThat(m.isEnPassantMove()).isFalse();
        Assertions.assertThat(m.isCastleMove()).isTrue();
        Assertions.assertThat(m.isPromotionMove()).isFalse();
        Assertions.assertThat(m.isNormalMove()).isFalse();
        Assertions.assertThat(m.getFromIndex()).isEqualTo(63);
        Assertions.assertThat(m.getToIndex()).isEqualTo(0);
        Assertions.assertThat(m.getBits()).isEqualTo(BitUtils.parseInt("1111110000000001"));
    }

    @Test
    public void verifyPromotionMoveCreationSuccess() {
        final PieceType[] allowed = new PieceType[]{ PieceType.Bishop, PieceType.Knight, PieceType.Rook, PieceType.Queen};
        for (PieceType eachType : allowed) {
            final Move m = new Move(10, 2, eachType);
            Assertions.assertThat(m.isEnPassantMove()).isFalse();
            Assertions.assertThat(m.isPromotionMove()).isTrue();
            Assertions.assertThat(m.isCastleMove()).isFalse();
            Assertions.assertThat(m.isNormalMove()).isFalse();
            Assertions.assertThat(m.getPromoteTo()).isEqualTo(eachType);
            final String expectedBits = String.format("001010000010%s11", BitUtils.toBinaryString(Move.getPromoteToBit(eachType), 2));
            Assertions.assertThat(m.getBits()).isEqualTo(BitUtils.parseInt(expectedBits));
        }
    }

    // Promotion should not be allow for king or pawn
    @Test
    public void verifyPromotionMoveCreationFailure() {
        final PieceType[] illegal = new PieceType[]{ PieceType.King, PieceType.Pawn};
        for (PieceType eachType : illegal) {
            Assertions.assertThatThrownBy(() -> {
                new Move(20, 17, eachType);
            });
        }
    }

    // Promotion should not use moveflag constructor, but promote to constructor
    @Test
    public void verifyPromotionMoveCreationWrongConstructor() {
        Assertions.assertThatThrownBy(() -> new Move(20, 17, MoveFlag.Promotion))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("must not be null");
    }

}