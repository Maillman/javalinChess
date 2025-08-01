package dataaccess.memory;

import java.util.Collection;
import java.util.HashMap;

import chess.ChessGame;
import dataaccess.GameDAO;
import model.GameData;
import model.ListGamesData;

public class MemoryGameDAO implements GameDAO {
    
    private final HashMap<Integer, GameData> games = new HashMap<>();
    private int id = 0;

    @Override
    public GameData getGame(int gameID) {
        return this.games.get(gameID);
    }

    @Override
    public ListGamesData getAllGames() {
        Collection<GameData> lGames = this.games.values();
        return new ListGamesData(lGames);
    }

    @Override
    public int createGame(String gameName) {
        id++;
        GameData game = new GameData(id, null, null, gameName, new ChessGame());
        games.put(id, game);
        return id;
    }
    
    @Override
    public void updateGame(GameData updatedGame) {
        this.games.put(updatedGame.gameID(), updatedGame);
    }

    @Override
    public void clearGames() {
        this.games.clear();
    }
}