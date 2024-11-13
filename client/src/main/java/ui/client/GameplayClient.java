package ui.client;

import server.ServerFacade;

public class GameplayClient implements ChessClient {
    private final ServerFacade server;
    private final String username;
    private final String authToken;
    private final int gameId;
    private String message;

    public GameplayClient(ServerFacade server, String username, String authToken, int gameId) {
        this.server = server;
        this.username = username;
        this.authToken = authToken;
        this.gameId = gameId;

        message = null; //server.getGameBoard();
    }

    @Override
    public String help() {
        return """
               redraw - to redraw the chess board
               leave - to remove yourself from the game
               move <START_END> - to move a piece from start to end
               resign - to forfeit and end the game
               highlight <SPACE> - to highlight the legal moves of the piece
               help - to see possible commands
               """;
    }

    @Override
    public ChessClient eval(String input) {
        return null;
    }

    @Override
    public String getMessage() {
        return "";
    }
}
