package server.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.ClearService;
import service.GameService;
import service.UserService;
import websocket.commands.*;
import websocket.messages.NotificationMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final UserService userService;
    private final GameService gameService;
    ConnectionManager connectionManager = new ConnectionManager();

    public WebSocketHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) throws Exception {
        ctx.enableAutomaticPings(); //Need to enable automatic ping or else connection will close after 30 seconds
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(UserGameCommand.class, new UserGameCommand.UserGameCommandAdapter())
                .create();
        UserGameCommand userGameCommand = gson.fromJson(ctx.message(), UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case CONNECT -> handleConnectCommand((ConnectCommand) userGameCommand, ctx.session);
            case MAKE_MOVE -> handleMakeMoveCommand((MakeMoveCommand) userGameCommand, ctx.session);
            case LEAVE -> handleLeaveCommand((LeaveCommand) userGameCommand, ctx.session);
            case RESIGN -> handleResignCommand((ResignCommand) userGameCommand, ctx.session);
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
        // Nothing needs to be done here
    }

    private void handleConnectCommand(ConnectCommand command, Session session) throws IOException, DataAccessException {
        connectionManager.add(command.getAuthToken(), session, command.getGameID());
        String username = userService.getUsername(command.getAuthToken());
        GameData game = gameService.getGame(command.getGameID());
        //TODO: Handle logic for user joining as either player or observer
        String notification = String.format("%s has joined the game as %s", username, "TODO");
        NotificationMessage notificationMessage = new NotificationMessage(notification);
        connectionManager.broadcastOthers(command.getAuthToken(), command.getGameID(), notificationMessage);
    }

    private void handleMakeMoveCommand(MakeMoveCommand command, Session session) {

    }

    private void handleLeaveCommand(LeaveCommand command, Session session) {

    }

    private void handleResignCommand(ResignCommand command, Session session) {

    }
}
