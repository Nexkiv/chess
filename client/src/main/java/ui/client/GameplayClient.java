package ui.client;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import server.ServerFacade;
import ui.WebsocketError;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class GameplayClient implements ChessClient {
    private final String username;
    private final String authToken;
    private final int gameID;
    private String message;
    private final WebSocketFacade webSocket;
    private final ServerFacade server;
    private final NotificationHandler notificationHandler;
    private boolean resignStarted = false;

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
               move <START> <END> <PROMOTION> - to move a piece from start to end
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

        if (resignStarted) {
            if (command.equals("yes") || command.equals("y")) {
                return resignGame();
            } else if (command.equals("no") || command.equals("n")) {
                resignStarted = false;
                message = "Okay enjoy the game.";
                return this;
            }
        } else {
            return switch (command) {
                case "redraw", "r" -> redrawBoard();
                case "leave", "l" -> leaveGame();
                case "move", "m" -> movePiece(params);
                case "resign", "x" -> resignStart();
                case "highlight", "h" -> highlightMoves(params);
                default -> this;
            };
        }

        return this;
    }

    private ChessClient redrawBoard() {
        message = webSocket.getBoard();
        return this;
    }

    private ChessClient leaveGame() throws ResponseException {
        webSocket.leave(authToken, gameID);
        return new LoggedInClient(server, username, authToken, notificationHandler);
    }

    private ChessClient movePiece(String[] params) throws ResponseException {
        if (params.length < 2 || params.length > 3) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        ChessMove move = getChessMove(params);

        webSocket.move(authToken, gameID, move);

        return this;
    }

    private static ChessMove getChessMove(String[] params) {
        String startPositionName = params[0];
        int rank = startPositionName.charAt(1) - 48;
        int file = startPositionName.toLowerCase().charAt(0) - 96;
        ChessPosition startPosition = new ChessPosition(rank, file);

        String endPositionName = params[1];
        rank = endPositionName.charAt(1) - 48;
        file = endPositionName.toLowerCase().charAt(0) - 96;
        ChessPosition endPosition = new ChessPosition(rank, file);

        ChessPiece.PieceType promotionPiece = getPromotionPiece(params);

        return new ChessMove(startPosition, endPosition, promotionPiece);
    }

    private static ChessPiece.PieceType getPromotionPiece(String[] params) {
        ChessPiece.PieceType promotionPiece = null;
        if (params.length == 3) {
            String promotion = params[2];
            switch (promotion) {
                case "queen", "q" -> promotionPiece = ChessPiece.PieceType.QUEEN;
                case "rook", "r" -> promotionPiece = ChessPiece.PieceType.ROOK;
                case "bishop", "b" -> promotionPiece = ChessPiece.PieceType.BISHOP;
                case "knight", "k", "n" -> promotionPiece = ChessPiece.PieceType.KNIGHT;
                default -> throw new WebsocketError("Illegal promotion piece");
            }
        }
        return promotionPiece;
    }

    private ChessClient resignStart() {
        resignStarted = true;
        message = "Are you sure you want to resign the game? [y/n]";
        return this;
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
            throw new WebsocketError("Invalid piece position");
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
