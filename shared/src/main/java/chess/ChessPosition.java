package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter signature of the
 * existing methods.
 */
public class ChessPosition {
    private static final char[] COLS = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.row;
        hash = 37 * hash + this.col;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChessPosition other = (ChessPosition) obj;
        if (this.row != other.row) {
            return false;
        }
        return this.col == other.col;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(row);
        sb.append(",").append(col);
        sb.append(')');
        return sb.toString();
    }


    public static String toAlgebraicNotation(ChessPosition chessPosition) {
        String column = String.valueOf(COLS[chessPosition.getColumn()-1]);
        return column + chessPosition.getRow();
    }

    public static ChessPosition fromAlgebraicNotation(String algebraicNotation) {
        if (algebraicNotation.length() != 2) {
            throw new IllegalArgumentException("Position was not in algebraic notation");
        }
        char[] splitNotation = algebraicNotation.toCharArray();
        int column = splitNotation[0] - COLS[0] + 1;
        int row = splitNotation[1] - '0';
        if(row < 1 || row > 8 || column < 1 || column > 8) {
            throw new IllegalArgumentException("""
                    Position was parsed correctly.
                    Ensure it is in algebraic notation and in bounds.""");
        }
        return new ChessPosition(row, column);
    }
}
