package dataaccess;

import model.UserData;

public interface DataAccess {
    void clear();
    UserData getUser(String username);
    UserData createUser(UserData userData);
}
