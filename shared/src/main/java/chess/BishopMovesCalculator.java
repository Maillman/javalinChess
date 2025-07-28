package chess;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator extends PieceMovesCalculator {

    public BishopMovesCalculator() {
        super(true);
    }

    public BishopMovesCalculator(boolean repeat) {
        super(repeat);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> allMoves = new HashSet<>();
        allMoves.addAll(addAllMoves(board, myPosition, 1, 1));
        allMoves.addAll(addAllMoves(board, myPosition, -1, 1));
        allMoves.addAll(addAllMoves(board, myPosition, 1, -1));
        allMoves.addAll(addAllMoves(board, myPosition, -1, -1));
        return allMoves;
    }

}
