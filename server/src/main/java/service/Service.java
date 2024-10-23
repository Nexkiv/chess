package service;

import dataaccess.DataAccess;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class Service {
    private DataAccess dataAccess;

    public Service (DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear () {
        dataAccess.clear();
    }

    public AuthData register(UserData userData) {
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

    public AuthData login(UserData userData) {
        UserData validUserData = dataAccess.getUser(userData.username());

        if (validUserData.loginEquals(userData)) {
            return createAuthData(userData.username());
        } else {
            return null;
        }
    }

    public void logout(String authToken) {
        if (validAuthToken(authToken)) {
            dataAccess.deleteAuth(authToken);
        }
    }

    public int create(String authToken, String gameName) {
        if (validAuthToken(authToken)) {
            int gameId = dataAccess.createGame(gameName);
            return gameId;
        } else {
            return 0;
        }
    }

    @org.jetbrains.annotations.NotNull
    private AuthData createAuthData(String username) {
        AuthData userAuthData = new AuthData(username, generateToken());
        dataAccess.createAuthData(userAuthData);
        return userAuthData;
    }

    private boolean validAuthToken(String authToken) {
        AuthData authData = dataAccess.getAuthData(authToken);
        return authData != null;
    }


}
