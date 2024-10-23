package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.*;

public class MemoryDataAccess implements DataAccess{

    private final Map<String, UserData> userDataMap = new HashMap<>();
    private final Map<String, AuthData> authTokensMap = new HashMap<>();
    private final Map<Integer, GameData> gameDataMap = new HashMap<>();
    private int nextGameId = 1;

    @Override
    public void clear() throws ResponseException {
        userDataMap.clear();
        authTokensMap.clear();
        gameDataMap.clear();
    }

    @Override
    public UserData getUser(String username) throws ResponseException {
        return userDataMap.get(username);
    }

    @Override
    public void createUser(UserData userData) throws ResponseException {
        userDataMap.put(userData.username(), userData);
    }

    @Override
    public AuthData getAuthData(String authToken) throws ResponseException {
        return authTokensMap.get(authToken);
    }

    @Override
    public void createAuthData(AuthData authData) throws ResponseException {
        authTokensMap.put(authData.authToken(), authData);
    }

    @Override
    public void deleteAuth(String authToken) throws ResponseException {
        authTokensMap.remove(authToken);
    }

    @Override
    public int createGame(String gameName) throws ResponseException {
        int gameId = nextGameId;
        GameData newGame = new GameData(gameId, null, null, gameName, new ChessGame());
        gameDataMap.put(gameId, newGame);

        nextGameId++;

        return gameId;
    }

    @Override
    public GameData getGameData(int gameID) throws ResponseException {
        return gameDataMap.get(gameID);
    }

    @Override
    public void updateGameData(GameData newGameData) throws ResponseException {
        gameDataMap.replace(newGameData.gameID(), newGameData);
    }

    @Override
    public Collection<GameData> getGames() throws ResponseException {
        List<GameData> targetList = new ArrayList<>(gameDataMap.values());
        return targetList;
    }
}
