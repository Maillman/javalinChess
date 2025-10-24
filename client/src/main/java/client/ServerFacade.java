package client;

import model.AuthData;
import model.GameData;
import model.JoinData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class ServerFacade {
    private final HTTPCommunicator httpCommunicator = new HTTPCommunicator();
    private final String serverUrl;
    private String authToken = null;

    public ServerFacade(String url) {
        this.serverUrl = url;
    }

    public AuthData register(UserData user){
        return new AuthData("TODOAUTHTOKEN", user.username());
    }
    public AuthData login(UserData user){
        return new AuthData("TODOAUTHTOKEN", user.username());
    }
    public void logout(){

    }
    public Collection<GameData> listGames(){
        return new ArrayList<>();
    }
    public JoinData createGame(String gameName){
        return new JoinData(null, 1);
    }
    public void joinGame(String playerColor, int gameID){

    }
}
