package service;

import dataaccess.DataAccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collection;
import java.util.UUID;

public class Service {
    private final DataAccess dataAccess;

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
        if (userData == null || userData.username() == null ||
                userData.password() == null || userData.email() == null) {
            return null;
        }

        UserData emptyUserData = dataAccess.getUser(userData.username());

        if (emptyUserData == null) {
            UserData userWithHashedPassword = new UserData(userData.username(), hashPassword(userData.password()), userData.email());
            dataAccess.createUser(userWithHashedPassword);
            return createAuthData(userWithHashedPassword.username());
        } else {
            return null;
        }
    }

    private String hashPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData login(UserData userData) throws ResponseException {
        if (!verifyUser(userData.username(), userData.password())) {
            return null;
        } else {
            return createAuthData(userData.username());
        }
    }

    private boolean verifyUser(String username, String providedClearTextPassword) throws ResponseException {
        UserData validUserData = dataAccess.getUser(username);

        if (validUserData == null) {
            return false;
        } else {
            return BCrypt.checkpw(providedClearTextPassword, validUserData.password());
        }
    }

    public boolean successfulLogout(String authToken) throws ResponseException {
        if (validAuthToken(authToken)) {
            dataAccess.deleteAuth(authToken);
            return true;
        } else {
            return false;
        }
    }

    public int create(String authToken, String gameName) throws ResponseException {
        if (gameName == null) {
            return -400;
        } else if (validAuthToken(authToken)) {
            return dataAccess.createGame(gameName);
        } else {
            return -401;
        }
    }

    public int join(String authToken, String playerColor, int gameID) throws ResponseException {
        AuthData authData = dataAccess.getAuthData(authToken);
        GameData gameData = dataAccess.getGameData(gameID);

        if (authData == null) {
            return 401;
        } else if (gameData != null) {
            GameData newGameData;
            switch (playerColor) {
                case "null" -> {
                    return 200;
                }
                case "WHITE" -> {
                    if (gameData.whiteUsername() == null) {
                        newGameData = new GameData(gameData.gameID(), authData.username(), gameData.blackUsername(),
                                gameData.gameName(), gameData.game());
                        dataAccess.updateGameData(newGameData);
                        return 200;
                    } else {
                        return 403;
                    }
                }
                case "BLACK" -> {
                    if (gameData.blackUsername() == null) {
                        newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), authData.username(),
                                gameData.gameName(), gameData.game());
                        dataAccess.updateGameData(newGameData);
                        return 200;
                    } else {
                        return 403;
                    }
                }
            }
        }
        return 400;
    }

    public Collection<GameData> list(String authToken) throws ResponseException {
        if (validAuthToken(authToken)) {
            return dataAccess.getGames();
        } else {
            return null;
        }
    }

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
        AuthData authData;
        try {
            authData = dataAccess.getAuthData(authToken);
        } catch (ResponseException exception) {
            throw new RuntimeException(exception);
        }
        return authData != null;
    }
}
