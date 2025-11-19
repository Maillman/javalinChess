package client;

import chess.ChessMove;
import model.*;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;

import java.util.*;

public class ServerFacade {
    private final String url;
    private final HTTPCommunicator httpCommunicator;
    private WebSocketCommunicator webSocketCommunicator;
    private String authToken = null;
    private Integer gameID = null;

    public ServerFacade(String url) {
        this.url = url;
        this.httpCommunicator = new HTTPCommunicator(url);
        this.webSocketCommunicator = null;
    }

    public void createWebSocketCommunicator(ServerMessageObserver serverMessageObserver) throws ResponseException {
        this.webSocketCommunicator = new WebSocketCommunicator(url, serverMessageObserver);
    }

    public AuthData register(UserData user) throws ResponseException {
        AuthData authData = httpCommunicator.makeRequest("POST", "/user", user, null, AuthData.class);
        this.authToken = authData.authToken();
        return authData;
    }
    public AuthData login(UserData user) throws ResponseException {
        AuthData authData = httpCommunicator.makeRequest("POST", "/session", user, null, AuthData.class);
        this.authToken = authData.authToken();
        return authData;
    }
    public void logout() throws ResponseException {
        httpCommunicator.makeRequest("DELETE", "/session", null, authToken, null);
        this.authToken = null;
    }
    public ListGamesData listGames() throws ResponseException {
        return httpCommunicator.makeRequest("GET", "/game", null, authToken, ListGamesData.class);
    }
    public JoinData createGame(String gameName) throws ResponseException {
        return httpCommunicator.makeRequest("POST", "/game", Map.of("gameName", gameName), authToken, JoinData.class);
    }
    public void joinGame(String playerColor, int gameID) throws ResponseException {
        JoinData joinData = new JoinData(playerColor, gameID);
        httpCommunicator.makeRequest("PUT", "/game", joinData, authToken, null);
    }
    public void connectGame(int gameID) throws ResponseException {
        this.gameID = gameID;
        ConnectCommand connectCommand = new ConnectCommand(authToken, gameID);
        webSocketCommunicator.sendUserGameCommand(connectCommand);
    }
    public void leaveGame() throws ResponseException {
        LeaveCommand leaveCommand = new LeaveCommand(authToken, gameID);
        webSocketCommunicator.sendUserGameCommand(leaveCommand);
    }
    public void makeMove(ChessMove move) throws ResponseException {
        MakeMoveCommand makeMoveCommand = new MakeMoveCommand(authToken, gameID, move);
        webSocketCommunicator.sendUserGameCommand(makeMoveCommand);
    }
    public void resignGame() throws ResponseException {
        ResignCommand resignCommand = new ResignCommand(authToken, gameID);
        webSocketCommunicator.sendUserGameCommand(resignCommand);
    }
    public void clear() throws ResponseException {
        httpCommunicator.makeRequest("DELETE", "/db", null, null, null);
    }
}
