package service.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;

import java.io.IOException;

public class Connection {

    public PlayerInformation playerInfo;
    public Session session;

    public Connection(PlayerInformation playerInfo, Session session) {
        this.playerInfo = playerInfo;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

    public static void sendError(RemoteEndpoint remote, String msg) throws IOException {
        remote.sendString(msg);
    }
}