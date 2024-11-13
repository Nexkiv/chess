package ui.client;

import chess.ChessPiece;
import server.ServerFacade;

import java.util.Arrays;

public class GameplayClient implements ChessClient {
    private final ServerFacade server;
    private final String username;
    private final String authToken;
    private final int gameID;
    private String message = null;

    public GameplayClient(ServerFacade server, String username, String authToken, int gameID) {
        this.server = server;
        this.username = username;
        this.authToken = authToken;
        this.gameID = gameID;

        message = server.getGameBoard(authToken, gameID);
    }

    @Override
    public String help() {
        return """
               redraw - to redraw the chess board
               leave - to remove yourself from the game
               move <START> <END> - to move a piece from start to end
               resign - to forfeit and end the game
               highlight <SPACE> - to highlight the legal moves of the piece
               help - to see possible commands
               """;
    }

    @Override
    public ChessClient eval(String input) {
        String[] tokens = input.toLowerCase().split(" ");
        String command = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        message = help();

        return switch (command) {
            case "redraw" -> redrawBoard();
            case "leave" -> leaveGame();
            case "move" -> movePiece(params);
            case "resign" -> resignGame();
            case "highlight" -> highlightMoves(params);
            default -> this;
        };
    }

    private ChessClient redrawBoard() {
        message = server.getGameBoard(authToken, gameID);

        return this;
    }

    private ChessClient leaveGame() {
        throw new RuntimeException("not implemented");
    }

    private ChessClient movePiece(String[] params) {
        throw new RuntimeException("not implemented");
    }

    private ChessClient resignGame() {
        throw new RuntimeException("not implemented");
    }

    private ChessClient highlightMoves(String[] params) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public String getMessage() {
        return message;
    }
}
