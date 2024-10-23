package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccess implements DataAccess{

    private final Map<String, UserData> userDataMap = new HashMap<>();
    private final Map<String, AuthData> authTokensMap = new HashMap<>();

    @Override
    public void clear() {
        userDataMap.clear();
        authTokensMap.clear();
    }

    @Override
    public UserData getUser(String username) {
        return userDataMap.get(username);
    }

    @Override
    public void createUser(UserData userData) {
        userDataMap.put(userData.username(), userData);
    }

    @Override
    public AuthData getAuthData(String authToken) {
        return authTokensMap.get(authToken);
    }

    @Override
    public void createAuthData(AuthData authData) {
        authTokensMap.put(authData.authToken(), authData);
    }
}
