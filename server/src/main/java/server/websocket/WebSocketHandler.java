package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import kotlin.Pair;
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
            case MAKE_MOVE -> handleMakeMoveCommand((MakeMoveCommand) userGameCommand);
            case LEAVE -> handleLeaveCommand((LeaveCommand) userGameCommand, ctx.session);
            case RESIGN -> handleResignCommand((ResignCommand) userGameCommand);
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
        // Nothing needs to be done here
    }

    private void handleConnectCommand(ConnectCommand command, Session session) throws IOException, DataAccessException {
        String username = userService.getUsername(command.getAuthToken());
        connectionManager.add(command.getAuthToken(), session, command.getGameID());
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

    private void handleMakeMoveCommand(MakeMoveCommand command) throws DataAccessException, InvalidMoveException, IOException {
        String username = userService.getUsername(command.getAuthToken());
        GameData game = gameService.getGame(command.getGameID());
        ChessMove move = command.getMove();
        Pair<ChessGame, NotificationMessage> gameNotificationMessagePair = handleMoveForPlayer(username, game, move);
        ChessGame updatedChessGame = gameNotificationMessagePair.getFirst();
        LoadGameMessage loadGameMessage = new LoadGameMessage(updatedChessGame);
        connectionManager.broadcastAll(game.gameID(), loadGameMessage);
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        String notification = String.format("%s has moved %s from %s to %s", username, updatedChessGame.getBoard().getPiece(endPos), ChessPosition.algebraicNotation(startPos), ChessPosition.algebraicNotation(endPos));
        NotificationMessage notificationMessage = new NotificationMessage(notification);
        connectionManager.broadcastOthers(command.getAuthToken(), command.getGameID(), notificationMessage);
        NotificationMessage changedStatusMessage = gameNotificationMessagePair.getSecond();
        if(changedStatusMessage!=null){
            connectionManager.broadcastAll(game.gameID(), changedStatusMessage);
        }
    }

    private NotificationMessage changedStatusMessage(ChessGame updatedChessGame, String username, GameData game) {
        ChessGame.TeamColor opposingTurn =  updatedChessGame.getTeamTurn();
        if(updatedChessGame.isInCheck(opposingTurn)) {
            String opposingUsername = username.equals(game.whiteUsername()) ? game.blackUsername() : game.whiteUsername();
            String checkNotification;
            if(updatedChessGame.isInCheckmate(opposingTurn)) {
                //Checkmate
                checkNotification = String.format("%s is in checkmate. %s wins!", opposingUsername, username);
                updatedChessGame.setOver(true);
            } else {
                //Check
                checkNotification = String.format("%s is in check.", opposingUsername);
            }
            return new NotificationMessage(checkNotification);

        } else if(updatedChessGame.isInStalemate(opposingTurn)) {
            //Stalemate
            String opposingUsername = username.equals(game.whiteUsername()) ? game.blackUsername() : game.whiteUsername();
            String stalemateNotification = String.format("%s has put %s in stalemate. Draw!", username, opposingUsername);
            updatedChessGame.setOver(true);
            return new NotificationMessage(stalemateNotification);
        }
        return null;
    }

    private Pair<ChessGame, NotificationMessage> handleMoveForPlayer(String username, GameData game, ChessMove move) throws InvalidMoveException, DataAccessException {
        ChessGame chessGame = game.game();
        ChessGame.TeamColor teamTurn = chessGame.getTeamTurn();
        if(username.equals(game.whiteUsername()) && teamTurn == ChessGame.TeamColor.WHITE ||
                username.equals(game.blackUsername()) && teamTurn == ChessGame.TeamColor.BLACK
        ) {
            chessGame.makeMove(move);
        } else {
            throw new InvalidMoveException("Not your piece to move");
        }
        NotificationMessage changedStatusMessage = changedStatusMessage(chessGame, username, game);
        GameData updatedGame = GameData.updateGameInGameData(game, chessGame);
        gameService.updateGame(updatedGame);
        return new Pair<>(chessGame, changedStatusMessage);
    }

    private void handleLeaveCommand(LeaveCommand command, Session session) throws DataAccessException, IOException {
        String username = userService.getUsername(command.getAuthToken());
        GameData game = gameService.getGame(command.getGameID());
        handlePlayerLeaving(username, game);
        connectionManager.remove(command.getAuthToken(), session, command.getGameID());
        NotificationMessage notificationMessage = new NotificationMessage(username + " has left the game");
        connectionManager.broadcastOthers(command.getAuthToken(), command.getGameID(), notificationMessage);
    }

    private void handlePlayerLeaving(String username, GameData game) throws DataAccessException {
        String updatedWhiteUsername = game.whiteUsername();
        String updatedBlackUsername = game.blackUsername();
        if(username.equals(game.whiteUsername())) {
            updatedWhiteUsername = null;
        } else if(username.equals(game.blackUsername())) {
            updatedBlackUsername = null;
        }
        if(updatedWhiteUsername == null || updatedBlackUsername == null) {
            gameService.updateGame(new GameData(game.gameID(), updatedWhiteUsername, updatedBlackUsername, game.gameName(), game.game()));
        }
    }

    private void handleResignCommand(ResignCommand command) throws DataAccessException, IOException {
        String username = userService.getUsername(command.getAuthToken());
        GameData game = gameService.getGame(command.getGameID());
        NotificationMessage notificationMessage = handleResignation(username, game);
        connectionManager.broadcastAll(command.getGameID(), notificationMessage);
    }

    private NotificationMessage handleResignation(String username, GameData game) throws DataAccessException {
        ChessGame chessGame = game.game();
        if(chessGame.isOver()) {
            throw new DataAccessException("Error: The game is already over.");
        }
        String opposingPlayer;
        if(username.equals(game.whiteUsername())) {
            opposingPlayer = game.blackUsername();
        } else if(username.equals(game.blackUsername())) {
            opposingPlayer = game.whiteUsername();
        } else {
            throw new DataAccessException("Error: You are not a player, you cannot resign.");
        }
        String notification = String.format("%s has resigned from the game. %s wins!", username, opposingPlayer);
        chessGame.setOver(true);
        GameData updatedGame = GameData.updateGameInGameData(game, chessGame);
        gameService.updateGame(updatedGame);
        return new NotificationMessage(notification);
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
