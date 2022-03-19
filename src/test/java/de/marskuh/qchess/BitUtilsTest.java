package de.marskuh.qchess;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class BitUtilsTest {
    @Test
    public void verifyToBinaryString() {
        Assertions.assertThat(BitUtils.toBinaryString(1)).isEqualTo("0000000000000000000000000000000000000000000000000000000000000001");
        Assertions.assertThat(BitUtils.toBinaryString(2)).isEqualTo("0000000000000000000000000000000000000000000000000000000000000010");
        Assertions.assertThat(BitUtils.toBinaryString(3)).isEqualTo("0000000000000000000000000000000000000000000000000000000000000011");
        Assertions.assertThat(BitUtils.toBinaryString(4)).isEqualTo("0000000000000000000000000000000000000000000000000000000000000100");
        Assertions.assertThat(BitUtils.toBinaryString(5)).isEqualTo("0000000000000000000000000000000000000000000000000000000000000101");
        Assertions.assertThat(BitUtils.toBinaryString(6)).isEqualTo("0000000000000000000000000000000000000000000000000000000000000110");
        Assertions.assertThat(BitUtils.toBinaryString(7)).isEqualTo("0000000000000000000000000000000000000000000000000000000000000111");
        Assertions.assertThat(BitUtils.toBinaryString(8)).isEqualTo("0000000000000000000000000000000000000000000000000000000000001000");
        Assertions.assertThat(BitUtils.toBinaryString(9)).isEqualTo("0000000000000000000000000000000000000000000000000000000000001001");
        Assertions.assertThat(BitUtils.toBinaryString(10)).isEqualTo("0000000000000000000000000000000000000000000000000000000000001010");
        Assertions.assertThat(BitUtils.toBinaryString(Long.MIN_VALUE)).isEqualTo("1000000000000000000000000000000000000000000000000000000000000000");
        Assertions.assertThat(BitUtils.toBinaryString(Long.MAX_VALUE)).isEqualTo("0111111111111111111111111111111111111111111111111111111111111111");
    }

    @Test
    public void verifyIndexCalculation() {
        // rank 8
        Assertions.assertThat(BitUtils.calculateIndex(0, 7)).isEqualTo(0);
        Assertions.assertThat(BitUtils.calculateIndex(1, 7)).isEqualTo(1);
        Assertions.assertThat(BitUtils.calculateIndex(2, 7)).isEqualTo(2);
        Assertions.assertThat(BitUtils.calculateIndex(3, 7)).isEqualTo(3);
        Assertions.assertThat(BitUtils.calculateIndex(4, 7)).isEqualTo(4);
        Assertions.assertThat(BitUtils.calculateIndex(5, 7)).isEqualTo(5);
        Assertions.assertThat(BitUtils.calculateIndex(6, 7)).isEqualTo(6);
        Assertions.assertThat(BitUtils.calculateIndex(7, 7)).isEqualTo(7);

        // Rank 7
        Assertions.assertThat(BitUtils.calculateIndex(0, 6)).isEqualTo(8);
        Assertions.assertThat(BitUtils.calculateIndex(1, 6)).isEqualTo(9);
        Assertions.assertThat(BitUtils.calculateIndex(2, 6)).isEqualTo(10);
        Assertions.assertThat(BitUtils.calculateIndex(3, 6)).isEqualTo(11);
        Assertions.assertThat(BitUtils.calculateIndex(4, 6)).isEqualTo(12);
        Assertions.assertThat(BitUtils.calculateIndex(5, 6)).isEqualTo(13);
        Assertions.assertThat(BitUtils.calculateIndex(6, 6)).isEqualTo(14);
        Assertions.assertThat(BitUtils.calculateIndex(7, 6)).isEqualTo(15);

        // Rank 6
        Assertions.assertThat(BitUtils.calculateIndex(0, 5)).isEqualTo(16);
        Assertions.assertThat(BitUtils.calculateIndex(1, 5)).isEqualTo(17);
        Assertions.assertThat(BitUtils.calculateIndex(2, 5)).isEqualTo(18);
        Assertions.assertThat(BitUtils.calculateIndex(3, 5)).isEqualTo(19);
        Assertions.assertThat(BitUtils.calculateIndex(4, 5)).isEqualTo(20);
        Assertions.assertThat(BitUtils.calculateIndex(5, 5)).isEqualTo(21);
        Assertions.assertThat(BitUtils.calculateIndex(6, 5)).isEqualTo(22);
        Assertions.assertThat(BitUtils.calculateIndex(7, 5)).isEqualTo(23);

        // Rank 5
        Assertions.assertThat(BitUtils.calculateIndex(0, 4)).isEqualTo(24);
        Assertions.assertThat(BitUtils.calculateIndex(1, 4)).isEqualTo(25);
        Assertions.assertThat(BitUtils.calculateIndex(2, 4)).isEqualTo(26);
        Assertions.assertThat(BitUtils.calculateIndex(3, 4)).isEqualTo(27);
        Assertions.assertThat(BitUtils.calculateIndex(4, 4)).isEqualTo(28);
        Assertions.assertThat(BitUtils.calculateIndex(5, 4)).isEqualTo(29);
        Assertions.assertThat(BitUtils.calculateIndex(6, 4)).isEqualTo(30);
        Assertions.assertThat(BitUtils.calculateIndex(7, 4)).isEqualTo(31);

        // Rank 4
        Assertions.assertThat(BitUtils.calculateIndex(0, 3)).isEqualTo(32);
        Assertions.assertThat(BitUtils.calculateIndex(1, 3)).isEqualTo(33);
        Assertions.assertThat(BitUtils.calculateIndex(2, 3)).isEqualTo(34);
        Assertions.assertThat(BitUtils.calculateIndex(3, 3)).isEqualTo(35);
        Assertions.assertThat(BitUtils.calculateIndex(4, 3)).isEqualTo(36);
        Assertions.assertThat(BitUtils.calculateIndex(5, 3)).isEqualTo(37);
        Assertions.assertThat(BitUtils.calculateIndex(6, 3)).isEqualTo(38);
        Assertions.assertThat(BitUtils.calculateIndex(7, 3)).isEqualTo(39);

        // Rank 3
        Assertions.assertThat(BitUtils.calculateIndex(0, 2)).isEqualTo(40);
        Assertions.assertThat(BitUtils.calculateIndex(1, 2)).isEqualTo(41);
        Assertions.assertThat(BitUtils.calculateIndex(2, 2)).isEqualTo(42);
        Assertions.assertThat(BitUtils.calculateIndex(3, 2)).isEqualTo(43);
        Assertions.assertThat(BitUtils.calculateIndex(4, 2)).isEqualTo(44);
        Assertions.assertThat(BitUtils.calculateIndex(5, 2)).isEqualTo(45);
        Assertions.assertThat(BitUtils.calculateIndex(6, 2)).isEqualTo(46);
        Assertions.assertThat(BitUtils.calculateIndex(7, 2)).isEqualTo(47);

        // Rank 2
        Assertions.assertThat(BitUtils.calculateIndex(0, 1)).isEqualTo(48);
        Assertions.assertThat(BitUtils.calculateIndex(1, 1)).isEqualTo(49);
        Assertions.assertThat(BitUtils.calculateIndex(2, 1)).isEqualTo(50);
        Assertions.assertThat(BitUtils.calculateIndex(3, 1)).isEqualTo(51);
        Assertions.assertThat(BitUtils.calculateIndex(4, 1)).isEqualTo(52);
        Assertions.assertThat(BitUtils.calculateIndex(5, 1)).isEqualTo(53);
        Assertions.assertThat(BitUtils.calculateIndex(6, 1)).isEqualTo(54);
        Assertions.assertThat(BitUtils.calculateIndex(7, 1)).isEqualTo(55);

        // Rank 1
        Assertions.assertThat(BitUtils.calculateIndex(0, 0)).isEqualTo(56);
        Assertions.assertThat(BitUtils.calculateIndex(1, 0)).isEqualTo(57);
        Assertions.assertThat(BitUtils.calculateIndex(2, 0)).isEqualTo(58);
        Assertions.assertThat(BitUtils.calculateIndex(3, 0)).isEqualTo(59);
        Assertions.assertThat(BitUtils.calculateIndex(4, 0)).isEqualTo(60);
        Assertions.assertThat(BitUtils.calculateIndex(5, 0)).isEqualTo(61);
        Assertions.assertThat(BitUtils.calculateIndex(6, 0)).isEqualTo(62);
        Assertions.assertThat(BitUtils.calculateIndex(7, 0)).isEqualTo(63);
    }
}
