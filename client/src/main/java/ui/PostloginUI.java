package ui;

import client.ServerFacade;
import model.GameData;
import model.ListGamesData;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class PostloginUI extends ClientUI{
    private final String username;
    private Map<Integer, GameData> savedGames;
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
                return joinObserve(true);
            }
            case "observe" -> {
                return joinObserve(false);
            }
            case "logout" -> {
                return logout();
            }
            case "quit" -> {
                logout();
                return "quit";
            }
            default -> {
                return help();
            }
        }
    }

    private Object list(){
        return handleServerOperation(() -> {
            ListGamesData games = serverFacade.listGames();
            StringBuilder listedGames = new StringBuilder();
            int i = 1;
            savedGames = new HashMap<>();
            for(GameData game : games.games()) {
                listedGames.append(String.format("%d -> %s - White Player: %s, Black Player: %s\n",i,game.gameName(),game.whiteUsername(),game.blackUsername()));
                savedGames.put(i, game);
                i++;
            }
            if(listedGames.isEmpty()){
                listedGames.append("No games to be list");
            }
            return listedGames.toString();
        });
    }

    private Object create(){
        out.println("What's the game name?");
        String gameName = scanner.nextLine();
        return handleServerOperation(() -> {
            serverFacade.createGame(gameName);
            return gameName + " created!";
        });
    }

    private Object joinObserve(boolean isJoin){
        out.printf("What game do you want to %s?\n", isJoin ? "play" : "observe");
        String gameIndex = scanner.nextLine();
        GameData game;
        try {
            game = savedGames.get(Integer.parseInt(gameIndex));
        }catch(NumberFormatException e){
            return "That is not a correct index (please put in an integer)";
        }
        if(game==null){
            return "No game found at that index";
        }
        AtomicReference<String> color = new AtomicReference<>("OBSERVE");
        return handleServerOperation(() -> {
            if(isJoin) {
                out.println("What color do you want to join as (WHITE|BLACK)?");
                color.set(scanner.nextLine());
                serverFacade.joinGame(color.get(), game.gameID());
            }
            out.printf("%s the game!", isJoin ? "Joined" : "Observing");
            GameplayUI gamePlayUI = new GameplayUI(this.serverFacade, this.scanner, this.out, this.username, color.get());
            gamePlayUI.eval("redraw");
            return gamePlayUI;
        });

    }

    private Object logout(){
        return handleServerOperation(() -> {
            serverFacade.logout();
            out.println("You are now logged out!");
            return new PreloginUI(this.serverFacade, this.scanner, this.out);
        });
    }

    @Override
    public String help() {
        return """
                list - List all games from server
                create - Create a new chess game
                join - Join a created game
                observe - Observe a created game
                logout - Log out as user
                help - Run this help menu
                quit - Quit this application""";
    }

    @Override
    public String currentState() {
        return "["+ username +"]";
    }
}
