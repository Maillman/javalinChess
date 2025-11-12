package server.websocket;


import org.eclipse.jetty.websocket.api.Session;

import java.util.Objects;

public class Connection {
    private final String authToken;
    private final Session session;
    public Connection(String authToken, Session session) {
        this.authToken = authToken;
        this.session = session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Connection that)) {
            return false;
        }
        return Objects.equals(getAuthToken(), that.getAuthToken()) && Objects.equals(getSession(), that.getSession());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthToken(), getSession());
    }

    public Session getSession() {
        return session;
    }

    public String getAuthToken() {
        return authToken;
    }
}
