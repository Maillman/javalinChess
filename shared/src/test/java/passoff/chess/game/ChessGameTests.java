package passoff.chess.game;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import passoff.chess.EqualsTestingUtility;
import passoff.chess.TestUtilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ChessGameTests extends EqualsTestingUtility<ChessGame> {
    public ChessGameTests() {
        super("ChessGame", "games");
    }

    @Override
    protected Map.Entry<String, ChessGame> buildOriginal() {
        return Map.entry("Default board", new ChessGame());
    }

    @Override
    protected Map<String, ChessGame> buildAllDifferent() {
        Map<String, ChessGame> differentGames = new HashMap<>();

        try {
            ChessGame game1 = new ChessGame();
            game1.setTeamTurn(ChessGame.TeamColor.BLACK);
            differentGames.put("Different team turn", game1);

            ChessGame game2 = new ChessGame();
            game2.makeMove(new ChessMove(
                    new ChessPosition(2, 5),
                    new ChessPosition(4, 5),
                    null));
            differentGames.put("Moved pawn", game2);

            ChessGame game3 = new ChessGame();
            game3.makeMove(new ChessMove(
                    new ChessPosition(1, 7),
                    new ChessPosition(3, 6),
                    null));
            differentGames.put("Moved knight", game3);

            ChessGame game4 = new ChessGame();
            game4.setBoard(TestUtilities.loadBoard("""
                    | | | |R| | | | |
                    | | | | | | | | |
                    | | |p|n|p| | | |
                    |R| |n|k|r| | |R|
                    | | |p|q| | | | |
                    | | | | | |K| | |
                    | | | | |P| | | |
                    | | | |R| | | | |
                    """));
            differentGames.put("Preset board", game4);

        } catch (InvalidMoveException e) {
            throw new RuntimeException("All moves in ChessGameTests are valid and should be allowed.", e);
        }

        return differentGames;
    }
}
