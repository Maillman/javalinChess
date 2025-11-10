package client;

import model.*;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private final UserData existingUser = new UserData("existingUser", "existingPass", "existing@mail.com");
    private final UserData newUser = new UserData("newUser", "newPass", "new@mail.com");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void setup() throws ResponseException {
        serverFacade.clear();
        serverFacade.register(existingUser);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerSuccess() {
        Assertions.assertDoesNotThrow(() -> serverFacade.register(newUser));
    }

    @Test
    public void registerFailure() {
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.register(existingUser));
    }

    @Test
    public void loginSuccess() {
        Assertions.assertDoesNotThrow(() -> serverFacade.login(existingUser));
    }

    @Test
    public void loginFailure() {
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.login(newUser));
    }

    @Test
    public void logoutSuccess() {
        Assertions.assertDoesNotThrow(() -> serverFacade.logout());
    }

    @Test
    public void logoutFailure() {
        Assertions.assertDoesNotThrow(() -> serverFacade.logout());
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout());
    }

    @Test
    public void listGamesSuccess() {
        Assertions.assertDoesNotThrow(() -> serverFacade.listGames());
    }

    @Test
    public void listGamesFailure() {
        Assertions.assertDoesNotThrow(() -> serverFacade.logout());
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.listGames());
    }

    @Test
    public void createGameSuccess() {
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame("newGame"));
    }

    @Test
    public void createGameFailure() {
        Assertions.assertThrows(NullPointerException.class, () -> serverFacade.createGame(null));
    }

    @Test
    public void joinGameSuccess() throws ResponseException {
        JoinData joinData = serverFacade.createGame("newGame");
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame("WHITE", joinData.gameID()));
    }

    @Test
    public void joinGameFailure() {
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame("WHITE", -1));
    }
}
