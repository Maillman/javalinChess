package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLUserDAO;
import model.AuthData;
import model.GameData;
import model.ListGamesData;
import model.UserData;

public class DataAccessObjectTests {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserData existingUser = new UserData("existingUser", "existingPass", "existingEmail");
    private final AuthData authData = new AuthData("R4ND0M-UU1D-STRING", "existingUser");
    private int existingGameID;
    private GameData existingGame;

    public DataAccessObjectTests() {
        this.userDAO = new SQLUserDAO();
        this.authDAO = new SQLAuthDAO();
        this.gameDAO = new SQLGameDAO();
    }

    @BeforeEach
    void clear() throws DataAccessException {
        userDAO.clearUsers();
        authDAO.clearAuths();
        gameDAO.clearGames();
        userDAO.addUser(existingUser);
        authDAO.addAuth(authData);
        existingGameID = gameDAO.createGame("existingGame");
        existingGame = gameDAO.getGame(existingGameID);
    }

    @Test
    void addUserSuccess() {
        Assertions.assertDoesNotThrow(() -> userDAO.addUser(new UserData("newUser", "newPass", "newEmail")));
    }

    @Test
    void addUserFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.addUser(null));
    }

    @Test
    void getUserSuccess() {
        Assertions.assertDoesNotThrow(() -> userDAO.getUser("existingUser"));
    }

    @Test
    void getUserFailure() throws DataAccessException {
        UserData foundUser = userDAO.getUser("newUser");
        Assertions.assertNull(foundUser);
    }

    @Test
    void clearUserSuccess() {
        Assertions.assertDoesNotThrow(() -> userDAO.clearUsers());
    }

    @Test
    void addAuthSuccess() {
        Assertions.assertDoesNotThrow(() -> authDAO.addAuth(new AuthData("4N0TH3R-R4ND0M-UU1D-STRING", "existingUser")));
    }

    @Test
    void addAuthFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.addAuth(null));
    }

    @Test
    void getAuthSuccess() {
        Assertions.assertDoesNotThrow(() -> authDAO.getAuth("R4ND0M-UU1D-STRING"));
    }

    @Test
    void getAuthFailure() throws DataAccessException {
        AuthData authData = authDAO.getAuth("F4K3-UU1D-STRING");
        Assertions.assertNull(authData);
    }

    @Test
    void clearAuthSuccess() {
        Assertions.assertDoesNotThrow(() -> authDAO.clearAuths());
    }

    @Test
    void createGameSuccess() {
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("newGame"));
    }

    @Test
    void createGameFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    void getGameSuccess() {
        Assertions.assertDoesNotThrow(() -> gameDAO.getGame(existingGameID));
    }

    @Test
    void getGameFailure() throws DataAccessException {
        GameData gameData = gameDAO.getGame(-1);
        Assertions.assertNull(gameData);
    }

    @Test
    void getAllGamesSuccess() {
        Assertions.assertDoesNotThrow(() -> gameDAO.getAllGames());
    }

    @Test
    void getAllGamesFailure() throws DataAccessException {
        gameDAO.clearGames();
        ListGamesData allGamesData = gameDAO.getAllGames();
        Assertions.assertEquals(0, allGamesData.games().size());
    }

    @Test
    void updateGameSuccess() {
        GameData updatedGame = new GameData(existingGameID, "newWhite", existingGame.blackUsername(), existingGame.gameName(), existingGame.game());
        Assertions.assertDoesNotThrow(() -> gameDAO.updateGame(updatedGame));
    }

    @Test
    void updateGameFailure() {
        GameData updatedGame = new GameData(-1, "newWhiteUsername", existingGame.blackUsername(), existingGame.gameName(), existingGame.game());
        Assertions.assertDoesNotThrow(() -> gameDAO.updateGame(updatedGame));
    }

    @Test
    void clearGameSuccess() {
        Assertions.assertDoesNotThrow(() -> gameDAO.clearGames());
    }
}
