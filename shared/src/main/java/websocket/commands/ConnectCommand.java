package websocket.commands;


import chess.ChessMove;

public class ConnectCommand extends UserGameCommand {
    private ChessMove move;

    public ConnectCommand(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
        move = null;
    }

    public ChessMove getMove() {
        return move;
    }
}
