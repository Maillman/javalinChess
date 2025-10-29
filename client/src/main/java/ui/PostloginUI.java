package ui;

import client.ServerFacade;
import model.GameData;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Scanner;

public class PostloginUI extends ClientUI{
    private String username;
    public PostloginUI(ServerFacade serverFacade, Scanner scanner, PrintStream out, String username) {
        super(serverFacade, scanner, out);
        this.username = username;
    }

    @Override
    public Object eval(String command) {
        switch (command.toLowerCase()){
            case "list" -> {
                return list();
            }
            case "create" -> {
                return create();
            }
            case "join" -> {
                return join();
            }
            case "logout" -> {
                return logout();
            }
            case "quit" -> {
                return "quit";
            }
            default -> {
                return help();
            }
        }
    }

    private Object list(){
        Collection<GameData> games = serverFacade.listGames();
        StringBuilder listedGames = new StringBuilder();
        int i = 1;
        for(GameData game : games) {
            listedGames.append(String.format("%d -> %s - White Player: %s, Black Player: %s",i,game.gameName(),game.whiteUsername(),game.blackUsername()));
            i++;
        }
        if(listedGames.isEmpty()){
            listedGames.append("No games to be list");
        }
        return listedGames;
    }

    private Object create(){
        out.println("What's the game name?");
        String gameName = scanner.nextLine();
        serverFacade.createGame(gameName);
        return "game created!";
    }

    private Object join(){
        return null;
    }

    private Object logout(){
        serverFacade.logout();
        return new PreloginUI(this.serverFacade, this.scanner, this.out);
    }

    @Override
    public String help() {
        return """
                list - List all games from server
                create - Create a new chess game
                join - Join a created game
                logout - Log out as user
                help - Run this help menu
                quit - Quit this application""";
    }

    @Override
    public String currentState() {
        return "["+ username +"]";
    }
}
