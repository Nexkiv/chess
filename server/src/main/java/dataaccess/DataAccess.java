package dataaccess;

import model.User;

import java.util.UUID;

public interface DataAccess {
    void clear();
    User getUser(String username);
    User createUser(User user);
}
