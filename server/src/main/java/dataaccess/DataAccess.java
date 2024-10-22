package dataaccess;

import model.AuthData;
import model.UserData;

public interface DataAccess {
    void clear();
    UserData getUser(String username);
    void createUser(UserData userData);
    AuthData getAuthData(String authToken);
    void createAuthData(AuthData authData);
}
