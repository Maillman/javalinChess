package ui;

import client.ServerFacade;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class GameplayUI extends ClientUI {
    public GameplayUI(ServerFacade serverFacade, Scanner scanner, PrintStream out) {
        super(serverFacade, scanner, out);
    }

    @Override
    public Object eval(String command) {
        return null;
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String currentState() {
        return null;
    }
}
