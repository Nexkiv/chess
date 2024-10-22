package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.Map;

public class MemoryDataAccess implements DataAccess{

    private Map<String, UserData> userDataMap;
    private Map<String, AuthData> authTokensMap;

    @Override
    public void clear() {
        userDataMap.clear();
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
        authTokensMap.put(authData.username(), authData);
    }
}
