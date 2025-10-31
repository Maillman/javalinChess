package client;

import ui.*;

import java.util.Scanner;

public class ChessClient {
    private final ServerFacade serverFacade;
    private final Scanner scanner;
    private ClientUI currentUI;
    public ChessClient(int port) {
        this.serverFacade = new ServerFacade("http://localhost:" + port);
        this.scanner = new Scanner(System.in);
        this.currentUI = new PreloginUI(this.serverFacade, this.scanner, System.out);
    }

    public void run() {
        System.out.println("Are you ready to play a game of chess? Sign in to start.");
        System.out.print(currentUI.help());

        Object result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = currentUI.eval(line);
                switch (result) {
                    case ClientUI clientUI -> this.currentUI = clientUI;
                    case String ignored -> System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
                    case null -> result = "";
                    default -> throw new RuntimeException("Received unknown result");
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + currentUI.currentState() + " >>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }
}
