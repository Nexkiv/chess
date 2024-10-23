package dataaccess;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

public interface DataAccess {
    void clear() throws ResponseException;
    UserData getUser(String username) throws ResponseException;
    void createUser(UserData userData) throws ResponseException;
    AuthData getAuthData(String authToken) throws ResponseException;
    void createAuthData(AuthData authData) throws ResponseException;
    void deleteAuth(String authToken) throws ResponseException;
    int createGame(String gameName) throws ResponseException;
    GameData getGameData(int gameID) throws ResponseException;
    void updateGameData(GameData newGameData) throws ResponseException;
    Object getGames() throws ResponseException;
}
