package ui.client;

public class GameplayClient implements ChessClient {


    @Override
    public String help() {
        return """
               redraw - to redraw the chess board
               leave - to remove yourself from the game
               move <START_END> - to move a piece from start to end
               resign - to forfeit and end the game
               highlight <SPACE> - to highlight the legal moves of the piece
               help - to see possible commands
               """;
    }

    @Override
    public ChessClient eval(String input) {
        return null;
    }

    @Override
    public String getMessage() {
        return "";
    }
}
