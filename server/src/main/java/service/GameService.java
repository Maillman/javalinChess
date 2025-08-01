package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.JoinData;
import model.ListGamesData;

public class GameService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesData listGames(String authToken) throws DataAccessException {
        verifyUser(authToken);
        return this.gameDAO.getAllGames();
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        verifyUser(authToken);
        if(gameName==null){
            throw new DataAccessException("Error: Game name not provided", 400);
        }
        return this.gameDAO.createGame(gameName);
    }

    public void joinGame(String authToken, JoinData join) throws DataAccessException {
        AuthData authData = verifyUser(authToken);
        GameData game = this.gameDAO.getGame(join.gameID());
        if(game==null){
            throw new DataAccessException("Error: game doesn't exist", 400);
        }
        if(join.playerColor()==null){
            throw new DataAccessException("Error: player color not chosen", 400);
        }
        GameData updatedGame;
        switch (join.playerColor()) {
            case "WHITE" -> {
                if(game.whiteUsername()!=null){
                    throw new DataAccessException("Error: Player color already taken", 403);
                }
                updatedGame = new GameData(game.gameID(), authData.username(), game.blackUsername(), game.gameName(), game.game());
            }
            case "BLACK" -> {
                if(game.blackUsername()!=null){
                    throw new DataAccessException("Error: Player color already taken", 403);
                }
                updatedGame = new GameData(game.gameID(), game.whiteUsername(), authData.username(), game.gameName(), game.game());
            }
            default -> throw new DataAccessException("Error: invalid player color", 400);
        }
        this.gameDAO.updateGame(updatedGame);
    }

    public AuthData verifyUser(String authToken) throws DataAccessException {
        AuthData authData = this.authDAO.getAuth(authToken);
        if(authData==null){
            throw new DataAccessException("Error: Invalid authtoken", 401);
        }
        return authData;
    }
}