package dataaccess;

import model.UserData;

import java.util.UUID;
import java.util.Vector;

public class MemoryDataAccess implements DataAccess{

    private Vector<UserData> userData;
    private Vector<UUID> authTokens;

    @Override
    public void clear() {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public UserData createUser(UserData userData) {
        return null;
    }
}
