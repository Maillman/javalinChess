package client;

import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator extends Endpoint {

    Session session;
    ServerMessageObserver serverMessageObserver;

    public WebSocketCommunicator(String url, ServerMessageObserver serverMessageObserver) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageObserver = serverMessageObserver;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                serverMessageObserver.notify(serverMessage);
            });
        } catch (URISyntaxException | DeploymentException | IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
