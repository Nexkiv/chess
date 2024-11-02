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

import dataaccess.MySqlDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.lang.reflect.Executable;
import java.util.Collection;

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
    public void testAddUser() {
        UserData userData = new UserData("name","pass","email");
        Assertions.assertDoesNotThrow(() -> dataAccess.createUser(userData));
    }

    @Test
    @DisplayName("Retrieve user from Database")
    public void testRetrieveUser() {
        UserData userData = new UserData("name","pass","email");
        Assertions.assertDoesNotThrow(() -> dataAccess.createUser(userData));
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(userData, dataAccess.getUser(userData.username())));
    }

    @Test
    @DisplayName("Add authData to Database")
    public void testAddAuthData() {
        AuthData authData = new AuthData("username","authToken");
        Assertions.assertDoesNotThrow(() -> dataAccess.createAuthData(authData));
    }

    @Test
    @DisplayName("Retrieve authData from Database")
    public void testRetrieveAuthData() {
        AuthData authData = new AuthData("username","authToken");
        Assertions.assertDoesNotThrow(() -> dataAccess.createAuthData(authData));
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(authData, dataAccess.getAuthData(authData.authToken())));
    }

    @Test
    @DisplayName("Remove authData from Database")
    public void testRemoveAuthData() {
        AuthData authData = new AuthData("username","authToken");
        Assertions.assertDoesNotThrow(() -> dataAccess.createAuthData(authData));
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(authData, dataAccess.getAuthData(authData.authToken())));
        Assertions.assertDoesNotThrow(() -> dataAccess.deleteAuth(authData.authToken()));
        Assertions.assertDoesNotThrow(() -> Assertions.assertNull(dataAccess.getAuthData(authData.authToken())));
    }

}
