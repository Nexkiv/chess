package websocket;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import ui.DisplayBoard;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    private final Session session;
    private ChessGame chessGame;
    private ChessGame.TeamColor teamColor;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case LOAD_GAME -> {
                            LoadGameMessage loadMessage = new Gson().fromJson(message, LoadGameMessage.class);
                            chessGame = loadMessage.getGame();
                            teamColor = loadMessage.getTeamColor();
                            DisplayBoard board = new DisplayBoard(chessGame.getBoard());
                            notificationHandler.notify(board.getBoard(teamColor));
                        }
                        case NOTIFICATION -> {
                            NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                            notificationHandler.notify(notificationMessage.getMessage());
                        }
                        case ERROR -> {
                            ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                            notificationHandler.notify(SET_TEXT_COLOR_RED + errorMessage.getMessage());
                        }
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameId) throws ResponseException {
        try {
            UserGameCommand connectCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId);
            session.getBasicRemote().sendText(new Gson().toJson(connectCommand));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public String getBoard() {
        DisplayBoard board = new DisplayBoard(chessGame.getBoard());
        return board.getBoard(teamColor);
    }

    public String highlightPiece(ChessPosition position) {
        DisplayBoard board = new DisplayBoard(chessGame.getBoard());
        if (chessGame.getBoard().getPiece(position) != null) {
            return board.getHighlightedBoard(teamColor, chessGame.validMoves(position));
        } else {
            throw new RuntimeException("There does not exist a piece in that position");
        }
    }
}

