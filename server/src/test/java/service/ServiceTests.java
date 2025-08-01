package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import model.AuthData;
import model.JoinData;
import model.ListGamesData;
import model.UserData;

public class ServiceTests {
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;

    private static final UserData EXISTING_USER = new UserData("existingUser", "existingPass", "existingEmail");
    private static final UserData NEW_USER = new UserData("newUser", "newPass", "newEmail");
    private AuthData authData;

    public ServiceTests() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(userDAO, authDAO, gameDAO);
        this.clearService = new ClearService(userDAO, authDAO, gameDAO);
    }

    @BeforeEach
    void clear() throws DataAccessException {
        clearService.clear();
        this.authData = userService.register(EXISTING_USER);
    }

    @Test
    void registerSuccess() {
        Assertions.assertDoesNotThrow(() -> userService.register(NEW_USER));
    }

    @Test
    void registerFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(EXISTING_USER));
    }

    @Test
    void loginSuccess() {
        Assertions.assertDoesNotThrow(() -> userService.login(EXISTING_USER));
    }

    @Test
    void loginFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(NEW_USER));
    }

    @Test
    void logoutSuccess() {
        Assertions.assertDoesNotThrow(() -> userService.logout(this.authData.authToken()));
    }

    @Test
    void logoutFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> userService.logout("badAuthtoken"));
    }

    @Test
    void listGamesSuccess() {
        Assertions.assertDoesNotThrow(() -> gameService.listGames(authData.authToken()));
    }

    @Test
    void listGamesEmpty() throws DataAccessException {
        ListGamesData listGames = gameService.listGames(authData.authToken());
        Assertions.assertEquals(0, listGames.games().size());
    }


    @Test
    void listGamesFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> gameService.listGames("badAuthtoken"));
    }

    @Test
    void createGameSuccess() {
        Assertions.assertDoesNotThrow(() -> gameService.createGame(authData.authToken(), "newGame"));
    }

    @Test
    void createGameFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(authData.authToken(), null));
    }

    @Test
    void joinGameSuccess() throws DataAccessException {
        int gameID = gameService.createGame(authData.authToken(), "newGame");
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(authData.authToken(), new JoinData("WHITE", gameID)));
    }

    @Test
    void joinGameFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(authData.authToken(), null));
    }

    @Test
    void clearSuccess() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> clearService.clear());
    }
}