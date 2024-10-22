package dataaccess;

import model.User;

import java.util.UUID;
import java.util.Vector;

public class MemoryDataAccess implements DataAccess{

    private Vector<User> users;
    private Vector<UUID> authTokens;

    @Override
    public void clear() {

    }

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public User createUser(User user) {
        return null;
    }
}
