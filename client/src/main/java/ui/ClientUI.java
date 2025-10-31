package ui;

import client.ResponseException;
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

    protected <T> T handleServerOperation(ServerCall<T> serverCall) {
        try {
            return serverCall.execute();
        }catch(ResponseException ex){
            out.println(EscapeSequences.SET_TEXT_COLOR_RED + ex.getMessage());
            return null;
        }
    }

    @FunctionalInterface
    protected interface ServerCall<T> {
        T execute() throws ResponseException;
    }
}
