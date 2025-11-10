package server.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
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
            case CONNECT -> System.out.println("This is a connect command");
            case MAKE_MOVE -> System.out.println("This is a make move command");
            case LEAVE -> System.out.println("This is a leave command");
            case RESIGN -> System.out.println("This is a resign command");
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
        // Nothing needs to be done here
    }
}
