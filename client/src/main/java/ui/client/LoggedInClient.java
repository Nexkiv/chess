package ui.client;

public class LoggedInClient implements ChessClient {
    private String message = help();
    private String username;
    private String authToken;

    public LoggedInClient(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    @Override
    public String help() {
        return ("""
                create <NAME> - to create a game with the given name
                list - to list all the games
                join <ID> [WHITE|BLACK] - to join the game with game ID as chosen color
                observe <ID> - to observe the game with game ID
                logout - to sign out of the application
                quit - to sign out and close the application
                help - to see possible commands
                """);
    }

    @Override
    public ChessClient eval(String input) {
        return null;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
