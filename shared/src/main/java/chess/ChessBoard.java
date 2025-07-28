package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter signature of the
 * existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] board = new ChessPiece[8][8];

    public ChessBoard() {

    }

    public ChessBoard(ChessBoard board) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition curPosition = new ChessPosition(i, j);
                addPiece(curPosition, board.getPiece(curPosition));
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board (How the game of chess
     * normally starts)
     */
    public void resetBoard() {
        ChessGame.TeamColor curColor;
        for (int i = 1; i <= 8; i++) {
            if (i == 1 || i == 8) {
                if (i == 1) {
                    curColor = ChessGame.TeamColor.WHITE;
                } else {
                    curColor = ChessGame.TeamColor.BLACK;
                }
                addPiece(new ChessPosition(i, 1), new ChessPiece(curColor, ChessPiece.PieceType.ROOK));
                addPiece(new ChessPosition(i, 8), new ChessPiece(curColor, ChessPiece.PieceType.ROOK));
                addPiece(new ChessPosition(i, 2), new ChessPiece(curColor, ChessPiece.PieceType.KNIGHT));
                addPiece(new ChessPosition(i, 7), new ChessPiece(curColor, ChessPiece.PieceType.KNIGHT));
                addPiece(new ChessPosition(i, 3), new ChessPiece(curColor, ChessPiece.PieceType.BISHOP));
                addPiece(new ChessPosition(i, 6), new ChessPiece(curColor, ChessPiece.PieceType.BISHOP));
                addPiece(new ChessPosition(i, 4), new ChessPiece(curColor, ChessPiece.PieceType.QUEEN));
                addPiece(new ChessPosition(i, 5), new ChessPiece(curColor, ChessPiece.PieceType.KING));
            } else if (i == 2 || i == 7) {
                if (i == 2) {
                    curColor = ChessGame.TeamColor.WHITE;
                } else {
                    curColor = ChessGame.TeamColor.BLACK;
                }
                for (int j = 1; j <= 8; j++) {
                    addPiece(new ChessPosition(i, j), new ChessPiece(curColor, ChessPiece.PieceType.PAWN));
                }
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Arrays.deepHashCode(this.board);
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
        final ChessBoard other = (ChessBoard) obj;
        return Arrays.deepEquals(this.board, other.board);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                sb.append(board[i][j]);
            }
        }
        return sb.toString();
    }
}
