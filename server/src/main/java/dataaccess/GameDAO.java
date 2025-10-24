package dataaccess;

import model.GameData;
import model.ListGamesData;

public interface GameDAO {
    GameData getGame(int gameID) throws DataAccessException;
    ListGamesData getAllGames() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    void updateGame(GameData updatedGame) throws DataAccessException;
    void clearGames() throws DataAccessException;
}
