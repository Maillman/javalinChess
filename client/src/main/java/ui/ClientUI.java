package ui;

import client.ServerFacade;

import java.io.PrintStream;
import java.util.Scanner;

public abstract class ClientUI {
    protected final ServerFacade serverFacade;
    protected final Scanner scanner;
    protected final PrintStream out;

    ClientUI(ServerFacade serverFacade, Scanner scanner, PrintStream out){
        this.serverFacade = serverFacade;
        this.scanner = scanner;
        this.out = out;
    }
    abstract public Object eval(String command);
    abstract public String help();
    abstract public String currentState();
}
