package ui.client;

import server.ServerFacade;

import java.util.Arrays;

public class LoggedInClient implements ChessClient {
    private String message = help();
    private final String username;
    private final String authToken;
    private final ServerFacade server;

    public LoggedInClient(ServerFacade server, String username, String authToken) {
        this.server = server;
        this.username = username;
        this.authToken = authToken;
    }

    @Override
    public String help() {
        return ("""
                create <NAME> - to create a game with the given name
                list - to list all the games
                join <ID> [WHITE|BLACK] - to join the game with game ID as chosen color
                observe <ID> - to observe the game with game ID
                logout - to sign out of the application
                quit - to sign out and close the application
                help - to see possible commands
                """);
    }

    @Override
    public ChessClient eval(String input) {
        String[] tokens = input.toLowerCase().split(" ");
        String command = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        message = help();

        return switch (command) {
            case "create", "c" -> null;
            case "list" -> null;
            case "join", "j" -> null;
            case "observe", "o" -> null;
            case "logout" -> null;
            case "quit", "q" -> null;
            default -> this;
        };
    }

    @Override
    public String getMessage() {
        return message;
    }
}
