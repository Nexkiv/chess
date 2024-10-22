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

        if (emptyUserData != null) {
            dataAccess.createUser(userData);

            AuthData userAuthData = new AuthData(userData.username(), generateToken());

            dataAccess.createAuthData(userAuthData);

            return userAuthData;
        } else {
            return null;
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

}
