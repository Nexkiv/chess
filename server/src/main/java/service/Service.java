package service;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Service {
    private DataAccess dataAccess;

    public Service (DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear () {
        try {
            dataAccess.clear();
        } catch (ResponseException exception) {
            throw new RuntimeException(exception);
        }
    }

    public AuthData register(UserData userData) throws ResponseException {
        UserData emptyUserData = dataAccess.getUser(userData.username());

        if (emptyUserData == null) {
            dataAccess.createUser(userData);
            return createAuthData(userData.username());
        } else {
            return null;
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData login(UserData userData) throws ResponseException {
        UserData validUserData = dataAccess.getUser(userData.username());

        if (validUserData.loginEquals(userData)) {
            return createAuthData(userData.username());
        } else {
            return null;
        }
    }

    public void logout(String authToken) throws ResponseException {
        if (validAuthToken(authToken)) {
            dataAccess.deleteAuth(authToken);
        }
    }

    public int create(String authToken, String gameName) throws ResponseException {
        if (validAuthToken(authToken)) {
            int gameId = dataAccess.createGame(gameName);
            return gameId;
        } else {
            return 0;
        }
    }

    public void join(String authToken, String playerColor, int gameID) throws ResponseException {
        AuthData authData = dataAccess.getAuthData(authToken);
        GameData gameData = dataAccess.getGameData(gameID);

        if (authData != null && gameData != null) {
            GameData newGameData;
            if (playerColor == null) {
                // TODO: Add the user as a spectator
            } else if (playerColor.equals("WHITE")) {
                if (gameData.whiteUsername() == null) {
                    newGameData = new GameData(gameData.gameID(), authData.username(), gameData.blackUsername(), gameData.gameName(), gameData.game());
                    dataAccess.updateGameData(newGameData);
                }
            } else if (playerColor.equals("BLACK")) {
                if (gameData.blackUsername() == null) {
                    newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), authData.username(), gameData.gameName(), gameData.game());
                    dataAccess.updateGameData(newGameData);
                }
            } else {
                // TODO: Add error saying the position is already taken
            }
        }
    }

    public String list(String authToken) throws ResponseException {
        if (validAuthToken(authToken)) {
            String allGames = new Gson().toJson(dataAccess.getGames());
            return allGames;
        } else {
            return null;
        }
    }

    @NotNull
    private AuthData createAuthData(String username) {
        AuthData userAuthData = new AuthData(username, generateToken());
        try {
            dataAccess.createAuthData(userAuthData);
        } catch (ResponseException exception) {
            throw new RuntimeException(exception);
        }
        return userAuthData;
    }

    private boolean validAuthToken(String authToken) {
        AuthData authData = null;
        try {
            authData = dataAccess.getAuthData(authToken);
        } catch (ResponseException exception) {
            throw new RuntimeException(exception);
        }
        return authData != null;
    }
}
