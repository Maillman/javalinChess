package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    ConcurrentHashMap<Integer, Set<Connection>> connections = new ConcurrentHashMap<>();

    public void add(String authToken, Session session, int gameID) {
        addOrRemove(true, authToken, session, gameID);
    }

    public void remove(String authToken, Session session, int gameID) {
        addOrRemove(false, authToken, session, gameID);
    }

    private void addOrRemove(boolean isAdding, String authToken, Session session, int gameID) {
        Connection theConnection = new Connection(authToken, session);
        Set<Connection> retrievedConnections = connections.getOrDefault(gameID, new HashSet<>());
        if(isAdding){
            retrievedConnections.add(theConnection);
        } else {
            retrievedConnections.remove(theConnection);
        }
        connections.put(gameID, retrievedConnections);
    }
    public void displayToSession(Session session, ServerMessage serverMessage) throws IOException {
        session.getRemote().sendString(new Gson().toJson(serverMessage));
    }
    public void broadcastOthers(String excludeAuthToken, int gameID, ServerMessage serverMessage) throws IOException {
        for(Connection c : connections.get(gameID)) {
            if(!c.getAuthToken().equals(excludeAuthToken)) {
                c.getSession().getRemote().sendString(new Gson().toJson(serverMessage));
            }
        }
    }

    public void broadcastAll(int gameID, ServerMessage serverMessage) throws IOException {
        broadcastOthers(null, gameID, serverMessage);
    }
}
