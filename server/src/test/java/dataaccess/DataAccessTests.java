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
import dataaccess.MySqlDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.lang.reflect.Executable;
import java.util.Collection;
import java.util.Iterator;

public class DataAccessTests {
    private final MySqlDataAccess dataAccess = new MySqlDataAccess();

    public DataAccessTests() throws ResponseException {
    }

    @BeforeEach
    void setUp() throws ResponseException {
        dataAccess.clear();
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
        dataAccess.createUser(userData);
    }

    @Test
    @DisplayName("Retrieve user from Database")
    public void testRetrieveUser() throws ResponseException {
        UserData userData = new UserData("name","pass","email");
        dataAccess.createUser(userData);
        Assertions.assertEquals(userData, dataAccess.getUser(userData.username()));
    }

    @Test
    @DisplayName("Add authData to Database")
    public void testAddAuthData() throws ResponseException {
        AuthData authData = new AuthData("username","authToken");
        dataAccess.createAuthData(authData);
    }

    @Test
    @DisplayName("Retrieve authData from Database")
    public void testRetrieveAuthData() throws ResponseException {
        AuthData authData = new AuthData("username","authToken");
        dataAccess.createAuthData(authData);
        Assertions.assertEquals(authData, dataAccess.getAuthData(authData.authToken()));
    }

    @Test
    @DisplayName("Remove authData from Database")
    public void testRemoveAuthData() throws ResponseException {
        AuthData authData = new AuthData("username","authToken");
        dataAccess.createAuthData(authData);
        dataAccess.deleteAuth(authData.authToken());
        Assertions.assertNull(dataAccess.getAuthData(authData.authToken()));
    }

    @Test
    @DisplayName("Add game to Database")
    public void testAddGame() {
        String gameName = "gameName";
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(1, dataAccess.createGame(gameName)));
    }

    @Test
    @DisplayName("Retrieve gameData from Database")
    public void testRetrieveGame() throws ResponseException {
        String gameName = "gameName";
        int gameId;
        gameId = dataAccess.createGame(gameName);
        Assertions.assertEquals(gameName, dataAccess.getGameData(gameId).gameName());
    }

    @Test
    @DisplayName("Update gameData in Database")
    public void testUpdateGame() throws ResponseException {
        String gameName = "gameName";
        int gameId;
        gameId = dataAccess.createGame(gameName);
        GameData newGameData = new GameData(gameId, "user1", "user2", "newGameName", new ChessGame());
        dataAccess.updateGameData(newGameData);
        GameData updatedGameData = dataAccess.getGameData(gameId);
        Assertions.assertEquals(newGameData, updatedGameData);
    }

    @Test
    @DisplayName("Return a list of games")
    public void testRetrieveAllGames() throws ResponseException {
        String gameName1 = "gameName1";
        int gameId1;
        gameId1 = dataAccess.createGame(gameName1);
        GameData gameData1 = dataAccess.getGameData(gameId1);
        String gameName2 = "gameName2";
        int gameId2;
        gameId2 = dataAccess.createGame(gameName2);
        GameData gameData2 = dataAccess.getGameData(gameId2);

        Collection<GameData> games = dataAccess.getGames();

        Iterator<GameData> iter = games.iterator();

        Assertions.assertEquals(iter.next(), gameData1);
        Assertions.assertEquals(iter.next(), gameData2);

    }
}
