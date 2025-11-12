package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.GameService;
import service.UserService;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Map;

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
        LoadGameMessage loadGameMessage = new LoadGameMessage(game.game());
        connectionManager.displayToSession(session, loadGameMessage);
        String isJoiningAs = isJoiningAs(username, game);
        String notification = String.format("%s has joined the game as %s", username, isJoiningAs);
        NotificationMessage notificationMessage = new NotificationMessage(notification);
        connectionManager.broadcastOthers(command.getAuthToken(), command.getGameID(), notificationMessage);
    }

    private String isJoiningAs(String username, GameData game) {
        if(username.equals(game.whiteUsername())) {
            return "white";
        } else if(username.equals(game.blackUsername())) {
            return "black";
        } else {
            return "an observer";
        }
    }

    private void handleMakeMoveCommand(MakeMoveCommand command, Session session) throws DataAccessException, InvalidMoveException, IOException {
        String username = userService.getUsername(command.getAuthToken());
        GameData game = gameService.getGame(command.getGameID());
        ChessMove move = command.getMove();
        ChessGame updatedChessGame = handleMoveForPlayer(username, game, move);
        LoadGameMessage loadGameMessage = new LoadGameMessage(updatedChessGame);
        connectionManager.broadcastAll(game.gameID(), loadGameMessage);
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        String notification = String.format("%s has moved %s from %s to %s", username, updatedChessGame.getBoard().getPiece(endPos), ChessPosition.algebraicNotation(startPos), ChessPosition.algebraicNotation(endPos));
        NotificationMessage notificationMessage = new NotificationMessage(notification);
        connectionManager.broadcastOthers(command.getAuthToken(), command.getGameID(), notificationMessage);
        //TODO: Handle logic for check/checkmate/stalemate notification
    }

    private ChessGame handleMoveForPlayer(String username, GameData game, ChessMove move) throws InvalidMoveException, DataAccessException {
        ChessGame chessGame = game.game();
        ChessGame.TeamColor teamTurn = chessGame.getTeamTurn();
        if(username.equals(game.whiteUsername()) && teamTurn == ChessGame.TeamColor.WHITE ||
                username.equals(game.blackUsername()) && teamTurn == ChessGame.TeamColor.BLACK
        ) {
            chessGame.makeMove(move);
        } else {
            throw new InvalidMoveException("Not your piece to move");
        }
//        chessGame.makeMove(move);
        GameData updatedGame = GameData.updateGameInGameData(game, chessGame);
        gameService.updateGame(updatedGame);
        return chessGame;
    }

    private void handleLeaveCommand(LeaveCommand command, Session session) {

    }

    private void handleResignCommand(ResignCommand command, Session session) {

    }

    public void handleException(Exception e, WsContext ctx) {
        try {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            connectionManager.displayToSession(ctx.session, errorMessage);
        }catch (IOException ioException) {
            System.out.println("Something went wrong sending message to session:");
            System.out.println(ioException.getMessage());
        }
    }
}
