package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator extends PieceMovesCalculator {

    public QueenMovesCalculator() {
        super(true);
    }

    public QueenMovesCalculator(boolean repeat) {
        super(repeat);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> allMoves = new HashSet<>();
        allMoves.addAll(new BishopMovesCalculator(this.repeat).pieceMoves(board, myPosition));
        allMoves.addAll(new RookMovesCalculator(this.repeat).pieceMoves(board, myPosition));
        return allMoves;
    }

}
