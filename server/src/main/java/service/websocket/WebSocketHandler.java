package service.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import exception.ResponseException;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebSocket
public class WebSocketHandler {

    private final DataAccess dataAccess;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, ResponseException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        Connection connection = getConnection(command.getAuthToken(), command.getGameID(), session);

        if (connection != null) {
            connections.add(connection);
            switch (command.getCommandType()) {
//                case JOIN_PLAYER -> join(conn, msg);
//                case JOIN_OBSERVER -> observe(conn, msg);
                case CONNECT -> connect(connection, message);
                case MAKE_MOVE -> makeMove(connection, message);
                case LEAVE -> leaveGame(connection);
                case RESIGN -> resign(connection);
            }
        } else {
            Connection.sendError(session.getRemote(), "unknown user");
        }

    }

    Connection getConnection(String authToken, int gameID, Session session) throws ResponseException {
        AuthData authData = dataAccess.getAuthData(authToken);

        if (authData != null) {
            String whiteUsername = dataAccess.getGameData(gameID).whiteUsername();
            String blackUsername = dataAccess.getGameData(gameID).blackUsername();
            ChessGame.TeamColor userColor;
            if (authData.username().equals(whiteUsername)) {
                userColor = ChessGame.TeamColor.WHITE;
            } else if (authData.username().equals(blackUsername)) {
                userColor = ChessGame.TeamColor.BLACK;
            } else {
                userColor = null;
            }
            PlayerInformation playerInfo = new PlayerInformation(gameID, authData.username(), userColor);
            return new Connection(playerInfo, session);
        } else {
            return null;
        }
    }

    private void connect(Connection connection, String message) {

    }

    private void makeMove(Connection connection, String message) {
        MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
    }

    private void leaveGame(Connection connection) {
    }

    private void resign(Connection connection) {
    }
}