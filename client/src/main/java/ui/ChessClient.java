package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import server.ServerFacade;

public class ChessClient {
    private String playerName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }
}
