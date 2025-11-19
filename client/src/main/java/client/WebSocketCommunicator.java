package client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
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

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(ServerMessage.class, new ServerMessage.ServerMessageAdapter())
                            .create();
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    serverMessageObserver.notify(serverMessage);
                }
            });
        } catch (URISyntaxException | DeploymentException | IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void sendUserGameCommand(UserGameCommand userGameCommand) throws ResponseException {
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        }catch (IOException e) {
            throw new ResponseException(e.getMessage());
        }
    }
}
