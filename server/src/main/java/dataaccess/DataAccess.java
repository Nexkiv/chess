package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

public interface DataAccess {
    void clear();
    UserData getUser(String username);
    void createUser(UserData userData);
    AuthData getAuthData(String authToken);
    void createAuthData(AuthData authData);
    void deleteAuth(String authToken);

    int createGame(String gameName);

    GameData getGameData(int gameID);
    void updateGameData(GameData newGameData);
}
