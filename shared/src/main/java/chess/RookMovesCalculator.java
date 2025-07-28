package chess;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator extends PieceMovesCalculator {

    public RookMovesCalculator() {
        super(true);
    }

    public RookMovesCalculator(boolean repeat) {
        super(repeat);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> allMoves = new HashSet<>();
        allMoves.addAll(addAllMoves(board, myPosition, 1, 0));
        allMoves.addAll(addAllMoves(board, myPosition, -1, 0));
        allMoves.addAll(addAllMoves(board, myPosition, 0, 1));
        allMoves.addAll(addAllMoves(board, myPosition, 0, -1));
        return allMoves;
    }

}
