package de.marskuh.qchess;

import java.util.Arrays;

public final class BitUtils {

    private BitUtils() {

    }

    public static boolean hasValue(long bitboard, int x, int y) {
        final int index = BitUtils.calculateIndex(x, y);
        return hasValue(bitboard, index);
    }

    public static boolean hasValue(long bitboard, int index) {
        return (bitboard >>> (63 - index) & 1) == 1;
    }

    public static long parseLong(String bits) {
        bits = padLeft(bits, 64);
        // If bits start with 1, it is a negative number, which we don't support,
        // so we invert the two-compliment representation
        if (bits.charAt(0) == '1') {
            return -1 * (Long.MAX_VALUE - Long.parseLong(bits.substring(1), 2) + 1);
        }
        // Default number
        return Long.parseLong(bits, 2);
    }

    public static int parseInt(String bits) {
        bits = padLeft(bits, 32);
        // If bits start with 1, it is a negative number, which we don't support,
        // so we invert the two-compliment representation
        if (bits.charAt(0) == '1') {
            return -1 * (Integer.MAX_VALUE - Integer.parseInt(bits.substring(1), 2) + 1);
        }
        // Default number
        return Integer.parseInt(bits, 2);
    }

    // TODO MVR move somewhere else
    public static int calculateIndex(int col, int row) {
        return (7 - row) * 8 + col; // TODO MVR magic numbers
    }

    // TODO MVR move somewhere else
    public static String toBinaryString(long input) {
        return toBinaryString(input, 64);
    }

    // TODO MVR move somewhere else
    public static String toBinaryString(long input, int maxLength) {
        final String binaryString = Long.toBinaryString(input);
        // Already long enough
        if (binaryString.length() >= maxLength) {
            return binaryString;
        }
        return padLeft(binaryString, maxLength);
    }

    // pad left with 0
    public static String padLeft(String binaryString, int maxLength) {
        final StringBuilder sb = new StringBuilder();
        for (int i = maxLength; i > binaryString.length(); i--) {
            sb.append('0');
        }
        sb.append(binaryString);
        return sb.toString();
    }

    public static long OR(long... positions) {
        if (positions == null) return 0;
        return Arrays.stream(positions).reduce((left, right) -> left | right).orElse(0L);
    }

    // TODO MVR unused...
    public static long AND(long... positions) {
        if (positions == null) return 0;
        return Arrays.stream(positions).reduce((left, right) -> left & right).orElse(0L);
    }
}
