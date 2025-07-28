package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator extends PieceMovesCalculator {

    public KingMovesCalculator() {
        super(false);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> allMoves = new HashSet<>();
        allMoves.addAll(new QueenMovesCalculator(this.repeat).pieceMoves(board, myPosition));
        return allMoves;
    }

}
