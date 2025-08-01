package dataaccess;

import model.GameData;
import model.ListGamesData;

public interface GameDAO {
    public GameData getGame(int gameID) throws DataAccessException;
    public ListGamesData getAllGames() throws DataAccessException;
    public int createGame(String gameName) throws DataAccessException;
    public void updateGame(GameData updatedGame) throws DataAccessException;
    public void clearGames() throws DataAccessException;
}