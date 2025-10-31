package ui;

import client.ServerFacade;

import java.io.PrintStream;
import java.util.Scanner;

public class GameplayUI extends ClientUI {
    private String username;
    private gameplayState currentGameplayState;
    public enum gameplayState {
        WHITE,
        BLACK,
        OBSERVER
    }
    public GameplayUI(ServerFacade serverFacade, Scanner scanner, PrintStream out, String username, String state) {
        super(serverFacade, scanner, out);
        this.username = username;
        switch(state) {
            case "WHITE" -> this.currentGameplayState = gameplayState.WHITE;
            case "BLACK" -> this.currentGameplayState = gameplayState.BLACK;
            default -> this.currentGameplayState = gameplayState.OBSERVER;
        }
    }

    @Override
    public Object eval(String command) {
        switch (command.toLowerCase()){
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

    private Object leave() {
        out.println("Leaving the game!");
        return new PostloginUI(this.serverFacade, this.scanner, this.out, this.username);
    }

    @Override
    public String help() {
        return """
                leave - Leave the game
                help - Run this help menu
                quit - Quit this application""";
    }

    @Override
    public String currentState() {
        if(this.currentGameplayState==gameplayState.OBSERVER) {
            return "[Observing the game]";
        } else {
            return "[Playing game as " + this.currentGameplayState.name() + "]";
        }
    }
}
