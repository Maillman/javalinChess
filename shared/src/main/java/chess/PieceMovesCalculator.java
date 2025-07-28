package chess;

import java.util.Collection;
import java.util.HashSet;

public abstract class PieceMovesCalculator {

    public boolean repeat;

    public PieceMovesCalculator(boolean repeat) {
        this.repeat = repeat;
    }

    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    public boolean inBounds(ChessPosition thisPosition) {
        return (thisPosition.getRow() > 0 && thisPosition.getColumn() > 0
                && thisPosition.getRow() < 9 && thisPosition.getColumn() < 9);
    }

    public boolean isOpposingPiece(ChessBoard board, ChessPosition myPosition, ChessPosition foundPosition) {
        return (board.getPiece(myPosition).getTeamColor() != board.getPiece(foundPosition).getTeamColor());
    }

    public boolean foundPiece(ChessBoard board, ChessPosition foundPosition) {
        return board.getPiece(foundPosition) != null;
    }

    public Collection<ChessMove> addAllMoves(ChessBoard board, ChessPosition myPosition, int moveV, int moveH) {
        Collection<ChessMove> allMoves = new HashSet<>();
        int iter = 1;
        do {
            ChessPosition curPosition = new ChessPosition(myPosition.getRow() + moveV * iter, myPosition.getColumn() + moveH * iter);
            //System.out.println(curPosition);
            if (!this.inBounds(curPosition)) {
                break;
            }
            if (this.foundPiece(board, curPosition)) {
                if (this.isOpposingPiece(board, myPosition, curPosition)) {
                    //System.out.println("found opposing piece");
                    allMoves.add(new ChessMove(myPosition, curPosition, null));
                }
                break;
            }
            allMoves.add(new ChessMove(myPosition, curPosition, null));
            iter++;
        } while (repeat);
        return allMoves;
    }
}
