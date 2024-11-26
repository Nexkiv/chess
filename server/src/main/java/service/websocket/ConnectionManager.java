package service.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(Session session, String visitorName) {
        var connection = new Connection(session, visitorName);
        connections.put(visitorName, connection);
    }

    public void add(Connection connection) {
        Connection newConnection = new Connection(connection.session, connection.username);
        connections.put(connection.username, newConnection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void remove(Connection connection) {
        connections.remove(connection.username);
    }

    public void broadcast(String excludeVisitorName, ServerMessage serverMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeVisitorName)) {
                    c.send(serverMessage.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }
}
