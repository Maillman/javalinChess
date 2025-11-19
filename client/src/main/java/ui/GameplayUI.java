package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ServerFacade;
import client.ServerMessageObserver;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.PrintStream;
import java.util.Scanner;

public class GameplayUI extends ClientUI implements ServerMessageObserver {
    private final String username;
    private final GameplayState currentGameplayState;
    private ChessGame currentGame;

    @Override
    public void notify(ServerMessage serverMessage) {
        out.println();
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> {
                currentGame = ((LoadGameMessage) serverMessage).getGame();
                redraw();
            }
            case ERROR -> displayError(((ErrorMessage) serverMessage).getErrorMessage());
            case NOTIFICATION -> out.println(((NotificationMessage) serverMessage).getMessage());
        }
        printPrompt();
    }

    public enum GameplayState {
        WHITE,
        BLACK,
        OBSERVER
    }
    public GameplayUI(ServerFacade serverFacade, Scanner scanner, PrintStream out, String username, String state) {
        super(serverFacade, scanner, out);
        this.username = username;
        switch(state) {
            case "WHITE" -> this.currentGameplayState = GameplayState.WHITE;
            case "BLACK" -> this.currentGameplayState = GameplayState.BLACK;
            default -> this.currentGameplayState = GameplayState.OBSERVER;
        }
    }

    @Override
    public Object eval(String command) {
        switch (command.toLowerCase()){
            case "redraw" -> {
                return redraw();
            }
            case "move" -> {
                return move();
            }
            case "leave" -> {
                return leave();
            }
            case "resign" -> {
                return resign();
            }
            case "quit" -> {
                Object result = leave();
                if(result instanceof ClientUI clientUI) {
                    return clientUI.eval("quit");
                }
                return "quit";
            }
            default -> {
                return help();
            }
        }
    }

    private Object move() {
        out.println("What's the starting position (in algebraic notation)?");
        String startPosString = scanner.nextLine();
        ChessPosition startPos = ChessPosition.fromAlgebraicNotation(startPosString);
        out.println("What's the ending position (in algebraic notation)?");
        String endPosString = scanner.nextLine();
        ChessPosition endPos = ChessPosition.fromAlgebraicNotation(endPosString);
        //TODO: Handle promotion piece for pawns
        ChessPiece.PieceType pieceType = null;
        return handleServerOperation(() -> {
            serverFacade.makeMove(new ChessMove(startPos, endPos, pieceType));
            return null;
        });
    }

    private Object resign() {
        out.println("Are you sure you want to resign (yes/no)?");
        String confirmation = scanner.nextLine();
        if(confirmation.equalsIgnoreCase("yes")) {
            return handleServerOperation(() -> {
                serverFacade.resignGame();
                return "You have resigned from the game!";
            });
        } else {
            return "You have chosen not to resign from the game.";
        }
    }

    private Object redraw() {
        this.out.println(EscapeSequences.RESET_TEXT_COLOR);
        ChessBoardUI.drawChessBoard(this.out, currentGame, this.currentGameplayState, null);
        return null;
    }

    private Object leave() {
        out.println("Leaving the game!");
        return handleServerOperation(() -> {
            serverFacade.leaveGame();
            return new PostloginUI(this.serverFacade, this.scanner, this.out, this.username);
        });
    }

    @Override
    public String help() {
        return """
                redraw - Redraw the current board
                move - Move a piece on the board
                leave - Leave the game
                resign - Resign from the game
                help - Run this help menu
                quit - Quit this application""";
    }

    @Override
    public String currentState() {
        if(this.currentGameplayState== GameplayState.OBSERVER) {
            return "[Observing the game]";
        } else {
            return "[Playing game as " + this.currentGameplayState.name() + "]";
        }
    }
}
