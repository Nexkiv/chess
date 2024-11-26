package ui.client;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import server.ServerFacade;
import ui.DisplayBoard;

import java.util.Arrays;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class LoggedInClient implements ChessClient {
    private String message;
    private GameData[] listOfGames;
    private final String username;
    private final String authToken;
    private final ServerFacade server;

    public LoggedInClient(ServerFacade server, String username, String authToken) {
        this.server = server;
        this.username = username;
        this.authToken = authToken;
        message = "Logged in as " + username + "\n\n" + help();
    }

    @Override
    public String help() {
        return (SET_TEXT_COLOR_BLUE + """
                create <NAME> - to create a game with the given name
                list - to list all the games
                join <ID> [WHITE|BLACK] - to join the game with game ID as chosen color
                observe <ID> - to observe the game with game ID
                logout - to sign out of the application
                quit - to sign out and close the application
                help - to see possible commands
                """ + RESET_TEXT_COLOR);
    }

    @Override
    public ChessClient eval(String input) throws ResponseException {
        String[] tokens = input.toLowerCase().split(" ");
        String command = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        message = help();

        return switch (command) {
            case "create", "c" -> createGame(params);
            case "list", "ls" -> listGames();
            case "join", "j" -> joinGame(params);
            case "observe", "o" -> observeGame(params);
            case "logout" -> logout();
            case "quit", "q" -> quit();
            default -> this;
        };
    }

    private ChessClient createGame(String[] params) throws ResponseException {
        if (params.length != 1) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        String gameName = params[0];

        server.createGame(gameName, authToken);
        message = "The game \"" + gameName + "\" was successfully created!\n";

        return this;
    }

    private ChessClient listGames() throws ResponseException {
        listOfGames = server.listGames(authToken);
        StringBuilder sb = new StringBuilder();


        if (listOfGames.length == 0) {
            sb.append("There are no games on this server at the moment.\n");
            sb.append("You can create a game by using the 'create' command.\n");
        } else {
            sb.append("List of games: \n");

            for (int i = 0; i < listOfGames.length; i++) {
                sb.append(i + 1).append(": ").append(listOfGames[i].gameName()).append("\n");
                sb.append("White: ").append(listOfGames[i].whiteUsername()).append(" Black: ").append(listOfGames[i].blackUsername());
                sb.append("\n");
            }
        }

        message = sb.toString();

        return this;
    }

    private ChessClient joinGame(String[] params) throws ResponseException {
        if (params.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        listOfGames = server.listGames(authToken);

        int playerSelection = Integer.parseInt(params[0]) - 1;
        if (playerSelection < 0 || playerSelection > listOfGames.length) {
            throw new IllegalArgumentException("Invalid game selection");
        }

        int gameID = listOfGames[playerSelection].gameID();

        String color = params[1].toUpperCase();

        server.joinGame(gameID, color, authToken);

        return new GameplayClient(server, username, authToken, gameID);
    }

    private ChessClient observeGame(String[] params) throws ResponseException {
        if (params.length != 1) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        int playerSelection = Integer.parseInt(params[0]) - 1;
        if (playerSelection < 0 || playerSelection > listOfGames.length) {
            throw new IllegalArgumentException("Invalid game selection");
        }

        int gameID = listOfGames[playerSelection].gameID();

        server.observeGame(gameID, authToken);

        // Temporary code as a precursor to the final product
        message = new DisplayBoard(new ChessGame().getBoard()).getBothBoards();

        return this; // new GameplayClient(server,username, authToken, gameID);
    }

    private ChessClient logout() throws ResponseException {
        server.logout(authToken);

        return new LoggedOutClient(server);
    }

    private ChessClient quit() throws ResponseException {
        server.logout(authToken);

        return null;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
