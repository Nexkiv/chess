package model;

import com.google.gson.Gson;

public record UserData(int id, String username, String password, String email) {

    public UserData setID(int id) {
        return new UserData(id, username, password, email);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
