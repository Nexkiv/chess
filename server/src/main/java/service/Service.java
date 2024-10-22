package service;

import dataaccess.DataAccess;
import model.UserData;

public class Service {
    private DataAccess dataAccess;

    public Service (DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear () {
        dataAccess.clear();
    }

    public UserData register(UserData userData) {
        // return dataAccess.register(User user);

        return userData;
    }

}
