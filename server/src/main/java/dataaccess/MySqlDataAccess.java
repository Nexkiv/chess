package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlDataAccess implements DataAccess {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` TEXT NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """,
            """
            CREATE TABLE IF NOT EXISTS  authentication (
              `id` int NOT NULL AUTO_INCREMENT,
              `userId` int NOT NULL,
              `username` varchar(256) NOT NULL,
              `authtoken` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              FOREIGN KEY (`userId`) REFERENCES user(`id`) ON DELETE CASCADE,
              INDEX(username),
              INDEX(authtoken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """,
            """
            CREATE TABLE IF NOT EXISTS  game (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUserName` varchar(256) DEFAULT NULL,
              `blackUserName` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `json` TEXT NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };

    public MySqlDataAccess() throws ResponseException {
        configureDatabase();
    }

    @Override
    public void clear() throws ResponseException {

        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.createStatement()) {
                // Disable foreign key checks
                stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");

                // Truncate your tables
                stmt.execute("TRUNCATE authentication;");
                stmt.execute("TRUNCATE game;");
                stmt.execute("TRUNCATE user;");

                // Re-enable foreign key checks
                stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", "truncate all tables", e.getMessage()));
        }
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

    private int executeUpdate(String statement, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case ChessGame p -> ps.setString(i + 1, p.toJson());
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
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
