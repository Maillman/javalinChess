package ui;

import chess.ChessGame;
import client.ServerFacade;

import java.io.PrintStream;
import java.util.Scanner;

public class GameplayUI extends ClientUI {
    private String username;
    private GameplayState currentGameplayState;
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
            case "leave" -> {
                return leave();
            }
            case "quit" -> {
                Object result = leave();
                if(result instanceof ClientUI) {
                    return ((ClientUI) result).eval("quit");
                }
                return "quit";
            }
            default -> {
                return help();
            }
        }
    }

    private Object redraw() {
        this.out.println(EscapeSequences.RESET_TEXT_COLOR);
        ChessBoardUI.drawChessBoard(this.out, new ChessGame(), this.currentGameplayState, null);
        return null;
    }

    private Object leave() {
        out.println("Leaving the game!");
        return new PostloginUI(this.serverFacade, this.scanner, this.out, this.username);
    }

    @Override
    public String help() {
        return """
                redraw - Redraw the current board
                leave - Leave the game
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
