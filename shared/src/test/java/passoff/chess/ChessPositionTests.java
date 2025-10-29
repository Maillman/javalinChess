package passoff.chess;

import chess.ChessPosition;

import java.util.List;
import java.util.Map;

public class ChessPositionTests extends EqualsTestingUtility<ChessPosition> {
    public ChessPositionTests() {
        super("ChessPosition", "positions");
    }

    @Override
    protected Map.Entry<String, ChessPosition> buildOriginal() {
        return Map.entry("Chess Position w/ row 3, column 7", new ChessPosition(3, 7));
    }

    @Override
    protected Map<String, ChessPosition> buildAllDifferent() {
        return Map.of(
                "Chess Position w/ row 7, column 3", new ChessPosition(7, 3),
                "Chess Position w/ row 6, column 3", new ChessPosition(6, 3),
                "Chess Position w/ row 4, column 3", new ChessPosition(4, 3),
                "Chess Position w/ row 3, column 1", new ChessPosition(3, 1),
                "Chess Position w/ row 3, column 2", new ChessPosition(3, 2),
                "Chess Position w/ row 3, column 3", new ChessPosition(3, 3)
        );
    }

}
