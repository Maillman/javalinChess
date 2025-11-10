package client;

import model.*;

import java.util.*;

public class ServerFacade {
    private final HTTPCommunicator httpCommunicator;
    private String authToken = null;

    public ServerFacade(String url) {
        this.httpCommunicator = new HTTPCommunicator(url);
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
    public void clear() throws ResponseException {
        httpCommunicator.makeRequest("DELETE", "/db", null, null, null);
    }
}
