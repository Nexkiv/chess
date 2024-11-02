package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlDataAccess implements DataAccess {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` TEXT NOT NULL,
              `email` varchar(256) DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """,
            // TODO: incorporate foreign keys, "`userId` int NOT NULL, FOREIGN KEY (`userId`) REFERENCES user(`id`) ON DELETE CASCADE,"
            """
            CREATE TABLE IF NOT EXISTS  authentication (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `authToken` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(username),
              INDEX(authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """,
            """
            CREATE TABLE IF NOT EXISTS  game (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUserName` varchar(256) DEFAULT NULL,
              `blackUserName` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `gameJson` TEXT NOT NULL,
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
                // Disable the foreign key checks
                stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");

                // Truncate the tables
                stmt.execute("TRUNCATE TABLE authentication;");
                stmt.execute("TRUNCATE TABLE game;");
                stmt.execute("TRUNCATE TABLE user;");

                // Re-enable the foreign key checks
                stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", "truncate all tables", e.getMessage()));
        }
    }

    @Override
    public UserData getUser(String username) throws ResponseException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, password, email FROM user WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username, password, email);
    }

    @Override
    public void createUser(UserData userData) throws ResponseException {
        String statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        int id = executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    @Override
    public AuthData getAuthData(String authToken) throws ResponseException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, authToken FROM authentication WHERE authToken = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String authToken = rs.getString("authToken");
        return new AuthData(username, authToken);
    }

    @Override
    public void createAuthData(AuthData authData) throws ResponseException {
        String statement = "INSERT INTO authentication (username, authToken) VALUES (?, ?)";
        int id = executeUpdate(statement, authData.username(), authData.authToken());
    }

    @Override
    public void deleteAuth(String authToken) throws ResponseException {
        String statement = "DELETE FROM authentication WHERE authToken = ?";
        int id = executeUpdate(statement, authToken);
    }

    @Override
    public int createGame(String gameName) throws ResponseException {
        String statement = "INSERT INTO game (gameName, gameJson) VALUES (?, ?)";
        ChessGame chessGame = new ChessGame();

        return executeUpdate(statement, gameName, chessGame.toJson());
    }

    @Override
    public GameData getGameData(int gameId) throws ResponseException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT id, whiteUsername, blackUsername, gameName, gameJson FROM game WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("id");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        ChessGame game = new Gson().fromJson(rs.getString("gameJson"), ChessGame.class);

        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    @Override
    public void updateGameData(GameData newGameData) throws ResponseException {
        String statement = "UPDATE game SET whiteUsername = ?, blackUsername = ?, gameName = ?, gameJson = ? WHERE id = ?";
        executeUpdate(statement, newGameData.whiteUsername(), newGameData.blackUsername(), newGameData.gameName(),
                newGameData.game().toJson(), newGameData.gameID());
    }

    @Override
    public Collection<GameData> getGames() throws ResponseException {
        Collection<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, gameName, gameJson FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        games.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return games;
    }

    private int executeUpdate(String statement, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                compileParameters(ps, params);

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

    private void compileParameters(PreparedStatement ps, Object... params) throws SQLException {
        for (var i = 0; i < params.length; i++) {
            var param = params[i];
            switch (param) {
                case String p -> ps.setString(i + 1, p);
                case Integer p -> ps.setInt(i + 1, p);
                case ChessGame p -> ps.setString(i + 1, p.toJson());
                case null -> ps.setNull(i + 1, NULL);
                default -> {}
            }
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

    public void eraseDatabase() throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.createStatement()) {
                // Disable the foreign key checks
                stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");

                // Truncate the tables
                stmt.execute("DROP TABLE authentication;");
                stmt.execute("DROP TABLE game;");
                stmt.execute("DROP TABLE user;");

                // Re-enable the foreign key checks
                stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", "truncate all tables", e.getMessage()));
        }
    }
}
