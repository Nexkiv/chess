package model;

import com.google.gson.Gson;

public record User(int id, String username, String password) {

    public User setID (int id) {
        return new User(id, username, password);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
