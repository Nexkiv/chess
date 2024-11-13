package ui.client;

import exception.ResponseException;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

public class LoggedOutClient implements ChessClient {
    private final ServerFacade server;

    public LoggedOutClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public LoggedOutClient(ServerFacade server) {
        this.server = server;
    }

    public ChessClient eval(String input) throws ResponseException {
        String[] tokens = input.toLowerCase().split(" ");
        String command = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        return switch (command) {
            case "login", "l" -> loginPlayer(params);
            case "register", "r" -> registerPlayer(params);
            case "quit", "q" -> null;
            default -> this;
        };
    }

    private ChessClient loginPlayer(String[] params) throws ResponseException {
        if (params.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        String username = params[0];
        String password = params[1];

        UserData newPlayer = new UserData(username, password, null);

        String authToken = server.signin(newPlayer);

        return new LoggedInClient(server, username, authToken);
    }

    private ChessClient registerPlayer(String[] params) throws ResponseException {
        if (params.length != 3) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        String username = params[0];
        String password = params[1];
        String email = params[2];

        UserData newPlayer = new UserData(username, password, email);

        String authToken = server.register(newPlayer);

        return new LoggedInClient(server, username, authToken);
    }

    public String getMessage() {
        return help();
    }

    public String help() {
        return ("""
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
                quit - closes the chess UI
                help - display possible commands with explanations
                """);
    }
}
