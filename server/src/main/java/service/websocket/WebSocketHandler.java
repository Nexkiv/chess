package service.websocket;

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

        Connection conn = getConnection(command.getAuthToken(), session);

        if (conn != null) {
            switch (command.getCommandType()) {
//                case JOIN_PLAYER -> join(conn, msg);
//                case JOIN_OBSERVER -> observe(conn, msg);
                case CONNECT -> connect(conn);
                case MAKE_MOVE -> makeMove(conn, message);
                case LEAVE -> leaveGame(conn);
                case RESIGN -> resign(conn);
            }
        } else {
            Connection.sendError(session.getRemote(), "unknown user");
        }

    }

    Connection getConnection(String authToken, Session session) throws ResponseException {
        AuthData authData = dataAccess.getAuthData(authToken);

        if (authData != null) {
            String username = authData.username();
            return new Connection(session, username);
        } else {
            return null;
        }
    }

    private void connect(Connection connection) {
        connections.add(connection);
    }

    private void makeMove(Connection connection, String message) {
        MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
    }

    private void leaveGame(Connection connection) {
        connections.remove(connection);
    }

    private void resign(Connection connection) {
        connections.remove(connection);
    }
}