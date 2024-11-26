package service.websocket;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;

import java.io.IOException;

public class Connection {
    public Session session;
    public String username;

    public Connection(Session session, String username) {
        this.session = session;
        this.username = username;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

    public static void sendError(RemoteEndpoint remote, String msg) throws IOException {
        remote.sendString(msg);
    }
}