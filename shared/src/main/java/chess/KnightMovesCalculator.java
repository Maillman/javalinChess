package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator extends PieceMovesCalculator {

    public KnightMovesCalculator() {
        super(false);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> allMoves = new HashSet<>();
        allMoves.addAll(addAllMoves(board, myPosition, 2, 1));
        allMoves.addAll(addAllMoves(board, myPosition, -2, 1));
        allMoves.addAll(addAllMoves(board, myPosition, 2, -1));
        allMoves.addAll(addAllMoves(board, myPosition, -2, -1));
        allMoves.addAll(addAllMoves(board, myPosition, 1, 2));
        allMoves.addAll(addAllMoves(board, myPosition, -1, 2));
        allMoves.addAll(addAllMoves(board, myPosition, 1, -2));
        allMoves.addAll(addAllMoves(board, myPosition, -1, -2));
        return allMoves;
    }

}
