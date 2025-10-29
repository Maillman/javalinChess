package passoff.chess;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Map;


public class ChessMoveTests extends EqualsTestingUtility<ChessMove> {
    public ChessMoveTests() {
        super("ChessMove", "moves");
    }

    @Override
    protected Map.Entry<String, ChessMove> buildOriginal() {
        return Map.entry("Chess Move from row 2, column 6 to row 1, column 5",
                new ChessMove(new ChessPosition(2, 6), new ChessPosition(1, 5), null));
    }

    @Override
    protected Map<String, ChessMove> buildAllDifferent() {
        return Map.of(
                "Chess Move from row 1, column 5 to row 2, column 6",
                new ChessMove(new ChessPosition(1, 5), new ChessPosition(2, 6), null),
                "Chess Move from row 2, column 4 to row 1, column 5",
                new ChessMove(new ChessPosition(2, 4), new ChessPosition(1, 5), null),
                "Chess Move from row 2, column 6 to row 5, column 3",
                new ChessMove(new ChessPosition(2, 6), new ChessPosition(5, 3), null),
                "Chess Move from row 2, column 6 to row 1, column 5 with promotion piece of Queen",
                new ChessMove(new ChessPosition(2, 6), new ChessPosition(1, 5), ChessPiece.PieceType.QUEEN),
                "Chess Move from row 2, column 6 to row 1, column 5 with promotion piece of Rook",
                new ChessMove(new ChessPosition(2, 6), new ChessPosition(1, 5), ChessPiece.PieceType.ROOK),
                "Chess Move from row 2, column 6 to row 1, column 5 with promotion piece of Bishop",
                new ChessMove(new ChessPosition(2, 6), new ChessPosition(1, 5), ChessPiece.PieceType.BISHOP),
                "Chess Move from row 2, column 6 to row 1, column 5 with promotion piece of Knight",
                new ChessMove(new ChessPosition(2, 6), new ChessPosition(1, 5), ChessPiece.PieceType.KNIGHT)
        );
    }

}
