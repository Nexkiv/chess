package ui.client;

import chess.ChessPiece;
import exception.ResponseException;
import server.ServerFacade;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class GameplayClient implements ChessClient {
    private final String username;
    private final String authToken;
    private final int gameID;
    private String message = null;
    private WebSocketFacade webSocket;
    private final NotificationHandler notificationHandler;

    public GameplayClient(ServerFacade server, String username, String authToken, int gameID,
                          NotificationHandler notificationHandler) throws ResponseException {
        this.username = username;
        this.authToken = authToken;
        this.gameID = gameID;
        this.notificationHandler = notificationHandler;

        String url = server.getServerUrl().replace("http", "ws");
        webSocket = new WebSocketFacade(url, null);

        message = webSocket.getGameBoard(this.authToken, gameID);
    }

    @Override
    public String help() {
        return (SET_TEXT_COLOR_BLUE + """
               redraw - to redraw the chess board
               leave - to remove yourself from the game
               move <START> <END> - to move a piece from start to end
               resign - to forfeit and end the game
               highlight <SPACE> - to highlight the legal moves of the piece
               help - to see possible commands
               """ + RESET_TEXT_COLOR);
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
        message = webSocket.getGameBoard(authToken, gameID);

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
