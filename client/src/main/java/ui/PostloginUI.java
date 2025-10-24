package ui;

import client.ServerFacade;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class PostloginUI extends ClientUI{
    private String username;
    public PostloginUI(ServerFacade serverFacade, Scanner scanner, PrintStream out, String username) {
        super(serverFacade, scanner, out);
        this.username = username;
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
        return "["+ username +"]";
    }
}
