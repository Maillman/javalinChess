package server.websocket;


import org.eclipse.jetty.websocket.api.Session;

public class Connection {
    private final String authToken;
    private final Session session;
    public Connection(String authToken, Session session) {
        this.authToken = authToken;
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public String getAuthToken() {
        return authToken;
    }
}
