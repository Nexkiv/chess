package ui.client;

import exception.ResponseException;
import websocket.NotificationHandler;

public interface ChessClient {
    String help();
    ChessClient eval(String input) throws ResponseException;
    String getMessage();
}
