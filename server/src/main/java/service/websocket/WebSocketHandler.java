package service.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebSocket
public class WebSocketHandler {

    private final DataAccess dataAccess;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, ResponseException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        Connection connection = getConnection(command.getAuthToken(), command.getGameID(), session);

        if (connection != null) {
            try {
                switch (command.getCommandType()) {
                    case CONNECT -> connect(connection, message);
                    case MAKE_MOVE -> makeMove(connection, message);
                    case LEAVE -> leaveGame(connection);
                    case RESIGN -> resign(connection);
                }
            } catch (ResponseException | InvalidMoveException | RuntimeException e) {
                Connection.sendError(session.getRemote(), e.getMessage());
            }
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        connections.cleanUp();
    }

    Connection getConnection(String authToken, int gameID, Session session) throws ResponseException, IOException {
        AuthData authData = dataAccess.getAuthData(authToken);

        if (authData != null) {
            GameData game = dataAccess.getGameData(gameID);
            if (game != null) {
                PlayerInformation playerInfo = getPlayerInformation(gameID, game, authData);
                if (connections.validateConnection(playerInfo, session)) {
                    return new Connection(playerInfo, session);
                } else {
                    Connection.sendError(session.getRemote(), "Error: You cannot control the other player");
                    return null;
                }
            } else {
                Connection.sendError(session.getRemote(), "Error: Invalid Game ID");
                return null;
            }
        } else {
            Connection.sendError(session.getRemote(), "Error: Invalid Credentials");
            return null;
        }
    }

    private static PlayerInformation getPlayerInformation(int gameID, GameData game, AuthData authData) {
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        ChessGame.TeamColor userColor;
        if (authData.username().equals(whiteUsername)) {
            userColor = ChessGame.TeamColor.WHITE;
        } else if (authData.username().equals(blackUsername)) {
            userColor = ChessGame.TeamColor.BLACK;
        } else {
            userColor = null;
        }
        return new PlayerInformation(gameID, authData.username(), userColor);
    }

    private void connect(Connection connection, String message) throws IOException, ResponseException {
        connections.add(connection);
        PlayerInformation playerInfo = connection.playerInfo;
        String connectionMessage;
        if (playerInfo.teamColor() != null) {
            connectionMessage = connection.playerInfo.username() + " has joined as " + connection.playerInfo.teamColor().toString();
        } else {
            connectionMessage = connection.playerInfo.username() + " has joined as an observer.";
        }
        NotificationMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, connectionMessage);
        connections.broadcast(connection.playerInfo, notification);

        ChessGame currentGame = dataAccess.getGameData(playerInfo.gameID()).game();
        LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, currentGame, playerInfo.teamColor());
        connections.respond(connection.playerInfo, loadGameMessage);
    }

    private void makeMove(Connection connection, String message) throws ResponseException, InvalidMoveException, IOException {
        MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
        int gameID = command.getGameID();

        GameData gameData = dataAccess.getGameData(gameID);
        ChessGame game = gameData.game();

        if (connection.playerInfo.teamColor() == game.getTeamTurn()) {
            String moveName = command.getMove().getMoveName(game.getBoard());
            game.makeMove(command.getMove());

            PlayerInformation playerInfo = connection.playerInfo;

            dataAccess.updateGameData(gameData);
            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game(), playerInfo.teamColor());
            connections.sendAll(playerInfo, loadGameMessage);

            ChessGame.TeamColor opponentColor = playerInfo.teamColor() ==
                    ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

            String movementMessage = playerInfo.username() + " has moved " + moveName;

            NotificationMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, movementMessage);
            connections.broadcast(connection.playerInfo, notification);

            if (game.isInCheckmate(opponentColor)) {
                String checkmateDeclaration;
                if (opponentColor == ChessGame.TeamColor.WHITE) {
                    checkmateDeclaration = gameData.whiteUsername() + " is in checkmate";
                } else {
                    checkmateDeclaration = gameData.blackUsername() + " is in checkmate";
                }
                NotificationMessage gameplayNotification =
                        new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkmateDeclaration);
                connections.sendAll(playerInfo, gameplayNotification);
            } else if (game.isInCheck(opponentColor)) {
                String checkDeclaration;
                if (opponentColor == ChessGame.TeamColor.WHITE) {
                    checkDeclaration = gameData.whiteUsername() + " is in check";
                } else {
                    checkDeclaration = gameData.blackUsername() + " is in check";
                }
                NotificationMessage gameplayNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkDeclaration);
                connections.sendAll(playerInfo, gameplayNotification);
            } else if (game.isInStalemate(opponentColor)) {
                String stalemateDeclaration;
                if (opponentColor == ChessGame.TeamColor.WHITE) {
                    stalemateDeclaration = gameData.whiteUsername() + " is in stalemate";
                } else {
                    stalemateDeclaration = gameData.blackUsername() + " is in stalemate";
                }
                NotificationMessage gameplayNotification =
                        new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, stalemateDeclaration);
                connections.sendAll(playerInfo, gameplayNotification);
            }
        } else if (connection.playerInfo.teamColor() == null) {
            throw new InvalidMoveException("Observers cannot move pieces");
        } else {
            throw new InvalidMoveException("It is not your turn");
        }
    }

    private void leaveGame(Connection connection) throws IOException, ResponseException {
        PlayerInformation playerInfo = connection.playerInfo;
        if (playerInfo.teamColor() != null) {
            GameData gameData = dataAccess.getGameData(playerInfo.gameID());
            GameData newGameData;
            if (playerInfo.teamColor() == ChessGame.TeamColor.WHITE) {
                newGameData = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
            } else {
                newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
            }
            dataAccess.updateGameData(newGameData);
            String message = playerInfo.username() + ", has left the game.";
            NotificationMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(playerInfo, notification);
        } else {
            String message = "The observer, " + playerInfo.username() + ", has left the game.";
            NotificationMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(playerInfo, notification);
        }
        connections.remove(connection);
    }

    private void resign(Connection connection) throws ResponseException, IOException {
        PlayerInformation playerInfo = connection.playerInfo;
        if (playerInfo.teamColor() != null) {
            GameData game = dataAccess.getGameData(playerInfo.gameID());
            game.game().resign(playerInfo.teamColor());
            dataAccess.updateGameData(game);
            String resignationMessage = playerInfo.username() + " has resigned. The game is now over.";
            NotificationMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, resignationMessage);
            connections.sendAll(playerInfo, notification);
        } else {
            String errorMessage = "An observer cannot resign.";
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, errorMessage);
            connections.respond(playerInfo, error);
        }
    }
}