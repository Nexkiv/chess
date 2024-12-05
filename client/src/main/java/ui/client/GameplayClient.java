package ui.client;

import chess.ChessPiece;
import chess.ChessPosition;
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
    private final WebSocketFacade webSocket;
    private final ServerFacade server;
    private final NotificationHandler notificationHandler;

    public GameplayClient(ServerFacade server, String username, String authToken, int gameID,
                          NotificationHandler notificationHandler) throws ResponseException {
        this.username = username;
        this.authToken = authToken;
        this.gameID = gameID;
        this.notificationHandler = notificationHandler;
        this.server = server;

        String url = server.getServerUrl().replace("http", "ws");
        webSocket = new WebSocketFacade(url, notificationHandler);

        webSocket.connect(authToken, gameID);

        message = help();
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
    public ChessClient eval(String input) throws ResponseException {
        String[] tokens = input.toLowerCase().split(" ");
        String command = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        message = help();

        return switch (command) {
            case "redraw", "r" -> redrawBoard();
            case "leave", "l" -> leaveGame();
            case "move", "m" -> movePiece(params);
            case "resign", "x" -> resignGame();
            case "highlight", "h" -> highlightMoves(params);
            default -> this;
        };
    }

    private ChessClient redrawBoard() {
        message = webSocket.getBoard();
        return this;
    }

    private ChessClient leaveGame() throws ResponseException {
        webSocket.leave(authToken, gameID);
        return new LoggedInClient(server, username, authToken, notificationHandler);
    }

    private ChessClient movePiece(String[] params) {
        throw new RuntimeException("not implemented");
    }

    private ChessClient resignGame() throws ResponseException {
        webSocket.resign(authToken, gameID);
        return new LoggedInClient(server, username, authToken, notificationHandler);
    }

    private ChessClient highlightMoves(String[] params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        String positionName = params[0];
        int rank = positionName.charAt(1) - 48;
        int file = positionName.toLowerCase().charAt(0) - 96;


        if (rank > 8 || rank <= 0 || file > 8 || file <= 0) {
            throw new IllegalArgumentException("Invalid piece position");
        } else {
            ChessPosition position = new ChessPosition(rank, file);
            message = webSocket.highlightPiece(position);
            return this;
        }
    }

    @Override
    public String getMessage() {
        return message;
    }
}
