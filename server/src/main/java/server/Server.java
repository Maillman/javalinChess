package server;

import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.json.JavalinGson;
import server.websocket.WebSocketHandler;

public class Server {

    private final Javalin javalin;
    private final Handler handler;
    private final WebSocketHandler webSocketHandler;

    public Server() {

        handler = new Handler();
        webSocketHandler = new WebSocketHandler();
        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
            config.jsonMapper(new JavalinGson());
        })
                .post("/user", handler::register)
                .post("/session", handler::login)
                .delete("/session", handler::logout)
                .get("/game", handler::listGames)
                .post("/game", handler::createGame)
                .put("/game", handler::joinGame)
                .delete("/db", handler::clear)
                .ws("/ws", ws -> {
                    ws.onConnect(webSocketHandler);
                    ws.onMessage(webSocketHandler);
                    ws.onClose(webSocketHandler);
                })
                .exception(DataAccessException.class, handler::handleException);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
