package service;

import dataaccess.DataAccess;
import model.User;

public class Service {
    private DataAccess dataAccess;

    public Service (DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear () {
        dataAccess.clear();
    }

    public User register(User user) {
        // return dataAccess.register(User user);

        return user;
    }

}
