package client;

import ui.*;

import java.util.Scanner;

public class ChessClient {
    private final ServerFacade serverFacade;
    private ClientUI currentUI;
    public ChessClient(int port) {
        this.serverFacade = new ServerFacade("http://localhost:" + port);
        this.currentUI = new PreloginUI();
    }

    public void run() {
        System.out.println("Are you ready to play a game of chess? Sign in to start.");
        currentUI.help();

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = currentUI.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + currentUI.currentState() + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }
}
