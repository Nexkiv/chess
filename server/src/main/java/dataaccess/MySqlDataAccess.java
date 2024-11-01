package dataaccess;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class MySqlDataAccess implements DataAccess {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  pet (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    public MySqlDataAccess() throws ResponseException {
        configureDatabase();
    }

    @Override
    public void clear() throws ResponseException {

    }

    @Override
    public UserData getUser(String username) throws ResponseException {
        return null;
    }

    @Override
    public void createUser(UserData userData) throws ResponseException {

    }

    @Override
    public AuthData getAuthData(String authToken) throws ResponseException {
        return null;
    }

    @Override
    public void createAuthData(AuthData authData) throws ResponseException {

    }

    @Override
    public void deleteAuth(String authToken) throws ResponseException {

    }

    @Override
    public int createGame(String gameName) throws ResponseException {
        return 0;
    }

    @Override
    public GameData getGameData(int gameID) throws ResponseException {
        return null;
    }

    @Override
    public void updateGameData(GameData newGameData) throws ResponseException {

    }

    @Override
    public Collection<GameData> getGames() throws ResponseException {
        return List.of();
    }

    private void configureDatabase() throws ResponseException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
