/*
Database Unit Tests

    The pass off tests do not examine your game board.
    That means it is critical that you write tests that fully test everything you are persisting to the database.
    This includes tests that store an initial board, add players, make moves, and update the game state.

    As part of your unit test deliverable you need to meet the following requirements.

    Write a positive and a negative JUNIT test case for each public method on your DAO classes,
    except for Clear methods which only need a positive test case.
    A positive test case is one for which the action happens successfully
    (e.g., creating a new user in the database).
    A negative test case is one for which the operation fails
    (e.g., creating a User that has the same username as an existing user).
    Ensure that all of your unit tests work,
    including the new DAO tests and the Service tests you wrote in the previous assignment.
 */

package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.Iterator;

public class DataAccessTests {
    private static final MySqlDataAccess DATA_ACCESS;

    static {
        try {
            DATA_ACCESS = new MySqlDataAccess();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    public DataAccessTests() throws ResponseException {
    }

    @BeforeEach
    void setUp() throws ResponseException {
        DATA_ACCESS.clear();
    }

    @Test
    @DisplayName("Bologna")
    public void testBologna() {
        Assertions.assertEquals(1, 1, "The numbers 1 and 1 are not equal?");
    }

    @Test
    @DisplayName("Add user to Database")
    public void testAddUser() throws ResponseException {
        UserData userData = new UserData("name","pass","email");
        DATA_ACCESS.createUser(userData);
    }

    @Test
    @DisplayName("Add bad user to Database")
    public void testAddBadUser() throws ResponseException {
        UserData userData = new UserData("name",null,null);
        Assertions.assertThrows(ResponseException.class, () -> DATA_ACCESS.createUser(userData));
    }

    @Test
    @DisplayName("Retrieve user from Database")
    public void testRetrieveUser() throws ResponseException {
        UserData userData = new UserData("name","pass","email");
        DATA_ACCESS.createUser(userData);
        Assertions.assertEquals(userData, DATA_ACCESS.getUser(userData.username()));
    }

    @Test
    @DisplayName("Bad user retrieval from Database")
    public void testBadUserRetrieveUser() throws ResponseException {
        Assertions.assertNull(DATA_ACCESS.getUser("name"));
    }

    @Test
    @DisplayName("Add authData to Database")
    public void testAddAuthData() throws ResponseException {
        AuthData authData = new AuthData("username","authToken");
        DATA_ACCESS.createAuthData(authData);
    }

    @Test
    @DisplayName("Add bad authData to Database")
    public void testAddBadAuthData() throws ResponseException {
        AuthData authData = new AuthData("username",null);
        Assertions.assertThrows(ResponseException.class, () -> DATA_ACCESS.createAuthData(authData));
    }

    @Test
    @DisplayName("Retrieve authData from Database")
    public void testRetrieveAuthData() throws ResponseException {
        AuthData authData = new AuthData("username","authToken");
        DATA_ACCESS.createAuthData(authData);
        Assertions.assertEquals(authData, DATA_ACCESS.getAuthData(authData.authToken()));
    }

    @Test
    @DisplayName("Bad authData retrieval from Database")
    public void testBadAuthDataRetrieveAuthData() throws ResponseException {
        Assertions.assertNull(DATA_ACCESS.getAuthData(null));
    }

    @Test
    @DisplayName("Remove authData from Database")
    public void testRemoveAuthData() throws ResponseException {
        AuthData authData = new AuthData("username","authToken");
        DATA_ACCESS.createAuthData(authData);
        DATA_ACCESS.deleteAuth(authData.authToken());
        Assertions.assertNull(DATA_ACCESS.getAuthData(authData.authToken()));
    }

    @Test
    @DisplayName("Bad removal of authData from Database")
    public void testBadRemoveAuthData() throws ResponseException {
        AuthData authData = new AuthData("username","authToken");
        DATA_ACCESS.createAuthData(authData);
        DATA_ACCESS.deleteAuth("wrongToken");
        Assertions.assertNotNull(DATA_ACCESS.getAuthData(authData.authToken()));
    }

    @Test
    @DisplayName("Add game to Database")
    public void testAddGame() {
        String gameName = "gameName";
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(1, DATA_ACCESS.createGame(gameName)));
    }

    @Test
    @DisplayName("Retrieve gameData from Database")
    public void testRetrieveGame() throws ResponseException {
        String gameName = "gameName";
        int gameId;
        gameId = DATA_ACCESS.createGame(gameName);
        Assertions.assertEquals(gameName, DATA_ACCESS.getGameData(gameId).gameName());
    }

    @Test
    @DisplayName("Update gameData in Database")
    public void testUpdateGame() throws ResponseException {
        String gameName = "gameName";
        int gameId;
        gameId = DATA_ACCESS.createGame(gameName);
        GameData newGameData = new GameData(gameId, "user1", "user2", "newGameName", new ChessGame());
        DATA_ACCESS.updateGameData(newGameData);
        GameData updatedGameData = DATA_ACCESS.getGameData(gameId);
        Assertions.assertEquals(newGameData, updatedGameData);
    }

    @Test
    @DisplayName("Return a list of games")
    public void testRetrieveAllGames() throws ResponseException {
        String gameName1 = "gameName1";
        int gameId1;
        gameId1 = DATA_ACCESS.createGame(gameName1);
        GameData gameData1 = DATA_ACCESS.getGameData(gameId1);
        String gameName2 = "gameName2";
        int gameId2;
        gameId2 = DATA_ACCESS.createGame(gameName2);
        GameData gameData2 = DATA_ACCESS.getGameData(gameId2);

        Collection<GameData> games = DATA_ACCESS.getGames();

        Iterator<GameData> iter = games.iterator();

        Assertions.assertEquals(iter.next(), gameData1);
        Assertions.assertEquals(iter.next(), gameData2);

    }

    @Test
    @DisplayName("Clear functionality")
    public void testClear() throws ResponseException {
        UserData userData = new UserData("name","pass","email");
        DATA_ACCESS.createUser(userData);
        AuthData authData = new AuthData("username","authToken");
        DATA_ACCESS.createAuthData(authData);
        String gameName = "gameName";
        int gameId;
        gameId = DATA_ACCESS.createGame(gameName);

        DATA_ACCESS.clear();

        Assertions.assertNull(DATA_ACCESS.getUser(userData.username()));
        Assertions.assertNull(DATA_ACCESS.getAuthData(authData.authToken()));
        Assertions.assertNull(DATA_ACCESS.getGameData(gameId));
    }

    @AfterAll
    public static void tearDown() throws ResponseException {
        DATA_ACCESS.eraseDatabase();
    }
}
