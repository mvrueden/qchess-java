package de.marskuh.qchess.renderer;

import de.marskuh.qchess.BitUtils;

public class BitboardRenderer {
    private final char visualization;
    private final String title;

    public BitboardRenderer(final String title, final char visualization) {
        this.title = title;
        this.visualization = visualization;
    }

    public BitboardRenderer(String title) {
        this(title, 'x');
    }

    public String render(long bitboard) {
        final StringBuilder value = new StringBuilder();
        value.append(title);
        value.append("\n");
        for(int y=7; y>=0; y--) {
            value.append(y+1).append("[ ");
            for(int x=0; x<8; x++) {
                if (BitUtils.hasValue(bitboard, x, y)) {
                    value.append(visualization).append(' ');
                } else {
                    value.append("  ");
                }
                if (x != 7) {
                    value.append(", ");
                }
            }
            value.append("]\n");
        }
        value.append("   a   b   c   d   e   f   g   h");
        return value.toString();
    }
}
