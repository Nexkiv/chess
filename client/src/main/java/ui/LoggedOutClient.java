package ui;

import server.ServerFacade;

public class LoggedOutClient implements ChessClient {
    private String playerName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;

    public LoggedOutClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String command) {
        return command;
    }

    public String help() {
        return ("""
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - closes the chess UI
                    help - display possible commands with explanations
                """);
    }
}
