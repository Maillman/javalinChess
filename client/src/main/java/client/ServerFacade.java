package client;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.JoinData;
import model.UserData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ServerFacade {
    private final HTTPCommunicator httpCommunicator = new HTTPCommunicator();
    private final String serverUrl;
    private String authToken = null;
    //TODO: Remove when fully working with server
    private static final String TEMPAUTHTOKEN = "TEMPAUTHTOKEN";

    public ServerFacade(String url) {
        this.serverUrl = url;
    }

    public AuthData register(UserData user){
        this.authToken = TEMPAUTHTOKEN;
        return new AuthData(TEMPAUTHTOKEN, user.username());
    }
    public AuthData login(UserData user){
        this.authToken = TEMPAUTHTOKEN;
        return new AuthData(TEMPAUTHTOKEN, user.username());
    }
    public void logout(){

    }
    public Collection<GameData> listGames(){
        return new ArrayList<>(List.of(
                new GameData(1, null, null, "newGame", new ChessGame())
        ));
    }
    public JoinData createGame(String gameName){
        return new JoinData(null, 1);
    }
    public void joinGame(String playerColor, int gameID){

    }
}
