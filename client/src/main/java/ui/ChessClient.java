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

    public String help() {
        if (state == State.SIGNEDOUT) {
            return ("""
                        register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                        login <USERNAME> <PASSWORD> - to play chess
                        quit - closes the chess UI
                        help - display possible commands with explanations
                    """);
        }
        return null;
    }
}
