package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccess implements DataAccess{

    private final Map<String, UserData> userDataMap = new HashMap<>();
    private final Map<String, AuthData> authTokensMap = new HashMap<>();
    private final Map<Integer, GameData> gameDataMap = new HashMap<>();
    private int nextGameId = 1;

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

    @Override
    public void deleteAuth(String authToken) {
        authTokensMap.remove(authToken);
    }

    @Override
    public int createGame(String gameName) {
        int gameId = nextGameId;
        GameData newGame = new GameData(gameId, null, null, gameName, new ChessGame());
        nextGameId++;

        return gameId;
    }
}
