package service.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<PlayerInformation, Connection> connections = new ConcurrentHashMap<>();

    public void add(PlayerInformation playerInfo, Session session) {
        var connection = new Connection(playerInfo, session);
        connections.put(playerInfo, connection);
    }

    public void add(Connection connection) {
        Connection newConnection = new Connection(connection.playerInfo, connection.session);
        connections.put(connection.playerInfo, newConnection);
    }

    public void remove(PlayerInformation playerInfo) {
        connections.remove(playerInfo);
    }

    public void remove(Connection connection) {
        connections.remove(connection.playerInfo);
    }

    public void cleanUp() {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (!c.session.isOpen()) {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.playerInfo);
        }
    }

    public void broadcast(PlayerInformation excludedPlayerInfo, ServerMessage serverMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.playerInfo.equals(excludedPlayerInfo) && c.playerInfo.gameID() == excludedPlayerInfo.gameID()) {
                    c.send(serverMessage.toJSON());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.playerInfo);
        }
    }

    public void respond(PlayerInformation playerInfo, ServerMessage serverMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.playerInfo.equals(playerInfo) && c.playerInfo.gameID() == playerInfo.gameID()) {
                    c.send(serverMessage.toJSON());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.playerInfo);
        }
    }

    public void sendAll(PlayerInformation playerInfo, ServerMessage serverMessage) throws IOException {
        broadcast(playerInfo, serverMessage);
        respond(playerInfo, serverMessage);
    }

    public boolean validateConnection(PlayerInformation playerInfo, Session session) {
        if (connections.containsKey(playerInfo)) {
            return connections.get(playerInfo).session == session;
        } else {
            return true;
        }

    }
}
