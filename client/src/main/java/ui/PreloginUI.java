package ui;

import client.ServerFacade;
import model.AuthData;
import model.UserData;

import java.io.PrintStream;
import java.util.Scanner;

public class PreloginUI extends ClientUI{
    public PreloginUI(ServerFacade serverFacade, Scanner scanner, PrintStream out) {
        super(serverFacade, scanner, out);
    }

    @Override
    public Object eval(String command) {
        switch (command.toLowerCase()){
            case "register" -> {
                return handleRegisterOrLogin(false);
            }
            case "login" -> {
                return handleRegisterOrLogin(true);
            }
            case "quit" -> {
                return "quit";
            }
            default -> {
                return help();
            }
        }
    }

    private Object handleRegisterOrLogin(boolean isLogin) {
        out.println("What's your username?");
        String username = scanner.nextLine();
        out.println("What's your password?");
        String password = scanner.nextLine();
        String email = null;
        if(!isLogin) {
            out.println("What's your email?");
            email = scanner.nextLine();
        }
        AuthData authData = serverFacade.register(new UserData(username, password, email));
        out.println(isLogin ? "Logged in!" : "Registered!");
        return new PostloginUI(this.serverFacade, this.scanner, this.out, authData.username());
    }

    @Override
    public String help() {
        return """
                register - Register as a new user
                login - Login as an existing user
                help - Run this help menu
                quit - Quit this application""";
    }

    @Override
    public String currentState() {
        return "[NOT LOGGED IN]";
    }
}
