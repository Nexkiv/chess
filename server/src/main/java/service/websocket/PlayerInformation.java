package service.websocket;

import chess.ChessGame;

import java.util.Objects;

public record PlayerInformation(int gameID, String username, ChessGame.TeamColor teamColor) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerInformation that = (PlayerInformation) o;
        return gameID == that.gameID && Objects.equals(username, that.username) && teamColor == that.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, username, teamColor);
    }
}
