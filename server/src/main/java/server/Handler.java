package server;

import com.google.gson.Gson;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLUserDAO;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import model.JoinData;
import model.ListGamesData;
import model.UserData;
import service.ClearService;
import service.GameService;
import service.UserService;

public class Handler {
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;
    private final Gson serializer;

    public Handler() {
        UserDAO userDAO;
        AuthDAO authDAO;
        GameDAO gameDAO;
        try {
            DatabaseManager.createDatabase();
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            System.out.println("An error occurred starting the database:" + e);
            userDAO = new MemoryUserDAO();
            authDAO = new MemoryAuthDAO();
            gameDAO = new MemoryGameDAO();
        }
        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(userDAO, authDAO, gameDAO);
        this.clearService = new ClearService(userDAO, authDAO, gameDAO);
        this.serializer = new Gson();
    }

    public void register(Context ctx) throws DataAccessException {
        UserData user = serializer.fromJson(ctx.body(), UserData.class);
        AuthData auth = userService.register(user);
        ctx.json(serializer.toJson(auth));
    }

    public void login(Context ctx) throws DataAccessException {
        UserData user = serializer.fromJson(ctx.body(), UserData.class);
        AuthData auth = userService.login(user);
        ctx.json(serializer.toJson(auth));
    }

    public void logout(Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        this.userService.logout(authToken);
    }

    public void listGames(Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        ListGamesData games = gameService.listGames(authToken);
        ctx.json(serializer.toJson(games));
    }

    public void createGame(Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        GameData game = serializer.fromJson(ctx.body(), GameData.class);
        int gameID = gameService.createGame(authToken, game.gameName());
        JoinData joinData = new JoinData(null, gameID);
        ctx.json(serializer.toJson(joinData));
    }

    public void joinGame(Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        JoinData join = serializer.fromJson(ctx.body(), JoinData.class);
        this.gameService.joinGame(authToken, join);
    }

    public void clear(Context ctx) throws DataAccessException {
        clearService.clear();
    }

    public void handleException(DataAccessException ex, Context ctx) {
        ctx.status(ex.statusCode());
        ctx.json(ex.toJson());
    }
}
