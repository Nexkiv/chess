package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;

public class LoadGameMessage extends ServerMessage {

    private final ChessGame game;
    private final ChessGame.TeamColor teamColor;

    public LoadGameMessage(ServerMessageType type, ChessGame game, ChessGame.TeamColor teamColor) {
        super(type);
        this.game = game;
        this.teamColor = teamColor;
    }

    public ChessGame getGame() {
        return game;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
}
