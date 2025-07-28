package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator extends PieceMovesCalculator {

    private final int startRow;

    private final int forwardMove;

    public PawnMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(false);
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            this.forwardMove = 1;
            this.startRow = 2;
        } else {
            this.forwardMove = -1;
            this.startRow = 7;
        }
    }

    public void checkAndAddPosition(Collection<ChessMove> allMoves, ChessBoard board,
                ChessPosition myPosition, ChessPosition checkPosition, int row, boolean isCapture) {
        if (inBounds(checkPosition)
                && (board.getPiece(checkPosition) == null && !isCapture
                || board.getPiece(checkPosition) != null && board.getPiece(checkPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()
                && isCapture)) {
            if (row == 9 - startRow) {
                allMoves.add(new ChessMove(myPosition, checkPosition, ChessPiece.PieceType.QUEEN));
                allMoves.add(new ChessMove(myPosition, checkPosition, ChessPiece.PieceType.ROOK));
                allMoves.add(new ChessMove(myPosition, checkPosition, ChessPiece.PieceType.BISHOP));
                allMoves.add(new ChessMove(myPosition, checkPosition, ChessPiece.PieceType.KNIGHT));
            } else {
                allMoves.add(new ChessMove(myPosition, checkPosition, null));
            }
        }
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> allMoves = new HashSet<>();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        ChessPosition forwardPosition = new ChessPosition(row + forwardMove, column);
        //Forward
        checkAndAddPosition(allMoves, board, myPosition, forwardPosition, row, false);
        if (board.getPiece(forwardPosition) == null) {
            if (row == startRow) {
                ChessPosition extraForwardPosition = new ChessPosition(row + forwardMove * 2, column);
                if (board.getPiece(extraForwardPosition) == null) {
                    allMoves.add(new ChessMove(myPosition, extraForwardPosition, null));
                }
            }
        }
        //Capturing
        ChessPosition forwardLeftPosition = new ChessPosition(row + forwardMove, column - 1);
        checkAndAddPosition(allMoves, board, myPosition, forwardLeftPosition, row, true);
        ChessPosition forwardRightPosition = new ChessPosition(row + forwardMove, column + 1);
        checkAndAddPosition(allMoves, board, myPosition, forwardRightPosition, row, true);
        return allMoves;
    }

}
