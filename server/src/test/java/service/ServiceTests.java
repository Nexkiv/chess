package service;

import dataaccess.MemoryDataAccess;
import dataaccess.MySqlDataAccess;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Collection;

public class ServiceTests {
    private final Service service = new Service(new MemoryDataAccess());

    public ServiceTests() throws ResponseException {
    }

    @BeforeEach
    void setUp() throws ResponseException {
        service.clear();
    }

    @Test
    @DisplayName("Bologna")
    public void testBologna() {
        Assertions.assertEquals(1, 1, "The numbers 1 and 1 are not equal?");
    }

    @Test
    @DisplayName("Register Success")
    public void testRegisterSuccess() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);

        Assertions.assertEquals(newUser.username(), authData.username());
    }

    @Test
    @DisplayName("Register with Missing Information")
    public void testRegisterWithMissingInformation() throws ResponseException {
        UserData newUser = new UserData(null, "test", "test");
        Assertions.assertNull(service.register(newUser));

        newUser = new UserData("test", null, "test");
        Assertions.assertNull(service.register(newUser));

        newUser = new UserData("test", "test", null);
        Assertions.assertNull(service.register(newUser));
    }

    @Test
    @DisplayName("Register as PreExisting User")
    public void testRegisterAsPreExistingUser() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);

        Assertions.assertNull(service.register(newUser));
    }

    @Test
    @DisplayName("Logout Success")
    public void testLogoutSuccess() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);

        Assertions.assertTrue(service.successfulLogout(authData.authToken()));
    }

    @Test
    @DisplayName("Logout with Invalid AuthToken")
    public void testLogoutWithInvalidAuthToken() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);

        Assertions.assertFalse(service.successfulLogout("Bologna"));
    }

    @Test
    @DisplayName("Login Success")
    public void testLoginSuccess() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);
        service.successfulLogout(authData.authToken());

        AuthData newAuthData = service.login(newUser);
        Assertions.assertEquals(newUser.username(), newAuthData.username());
    }

    @Test
    @DisplayName("Login with Missing Information")
    public void testLoginWithMissingInformation() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);
        service.successfulLogout(authData.authToken());

        UserData userLogin1 = new UserData("test", null,null);
        Assertions.assertNull(service.login(userLogin1));

        UserData userLogin2 = new UserData(null, "test",null);
        Assertions.assertNull(service.login(userLogin2));
    }

    @Test
    @DisplayName("Login with Incorrect Password")
    public void testLoginWithIncorrectPassword() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);
        service.successfulLogout(authData.authToken());

        UserData userLogin1 = new UserData("test", "Bologna",null);
        Assertions.assertNull(service.login(userLogin1));
    }

    @Test
    @DisplayName("Create Game Success")
    public void testCreateGameSuccess() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);
        int newGameID = service.create(authData.authToken(), "gameTest");

        Assertions.assertNotEquals(0, newGameID);
    }

    @Test
    @DisplayName("Create Game with Invalid Name")
    public void testCreateGameWithInvalidName() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);
        int newGameID = service.create(authData.authToken(), null);

        Assertions.assertEquals(-400, newGameID);
    }

    @Test
    @DisplayName("Create Game with Invalid AuthToken")
    public void testCreateGameWithInvalidAuthToken() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);
        int newGameID = service.create("Bologna", "gameTest");

        Assertions.assertEquals(-401, newGameID);
    }

    @Test
    @DisplayName("List Games Success")
    public void testListGamesSuccess() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);
        int newGameID1 = service.create(authData.authToken(), "gameTest1");
        int newGameID2 = service.create(authData.authToken(), "gameTest2");

        Collection<GameData> games = service.list(authData.authToken());
        Assertions.assertEquals(2, games.size());
    }

    @Test
    @DisplayName("List Games with Invalid AuthToken")
    public void testListGamesWithInvalidAuthToken() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);
        int newGameID1 = service.create(authData.authToken(), "gameTest1");
        int newGameID2 = service.create(authData.authToken(), "gameTest2");

        Collection<GameData> games = service.list("Bologna");
        Assertions.assertNull(games);
    }

    @Test
    @DisplayName("Join Game Success")
    public void testJoinGameSuccess() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);
        int newGameID = service.create(authData.authToken(), "gameTest");

        int successCode1 = service.join(authData.authToken(), "WHITE", newGameID);
        Assertions.assertEquals(200, successCode1);

        int successCode2 = service.join(authData.authToken(), "BLACK", newGameID);
        Assertions.assertEquals(200, successCode2);

        int successCode3 = service.join(authData.authToken(), null, newGameID);
        Assertions.assertEquals(200, successCode3);
    }

    @Test
    @DisplayName("Join Game with Invalid Input")
    public void testJoinGameWithInvalidInput() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);
        int newGameID = service.create(authData.authToken(), "gameTest");

        int failureCode1 = service.join(authData.authToken(), "GREEN", newGameID);
        Assertions.assertEquals(400, failureCode1);

        int failureCode2 = service.join(authData.authToken(), "WHITE", 0);
        Assertions.assertEquals(400, failureCode2);
    }

    @Test
    @DisplayName("Join Game with Invalid AuthToken")
    public void testJoinGameWithInvalidAuthToken() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);
        int newGameID = service.create(authData.authToken(), "gameTest");

        int failureCode = service.join("Bologna", "WHITE", newGameID);
        Assertions.assertEquals(401, failureCode);
    }

    @Test
    @DisplayName("Join Game that is Occupied")
    public void testJoinGameThatIsOccupied() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);
        int newGameID = service.create(authData.authToken(), "gameTest");
        service.join(authData.authToken(), "WHITE", newGameID);
        service.join(authData.authToken(), "BLACK", newGameID);

        int failureCode1 = service.join(authData.authToken(), "WHITE", newGameID);
        Assertions.assertEquals(403, failureCode1);

        int failureCode2 = service.join(authData.authToken(), "BLACK", newGameID);
        Assertions.assertEquals(403, failureCode2);
    }

    @Test
    @DisplayName("Clear Success")
    public void testClearSuccess() throws ResponseException {
        UserData newUser = new UserData("test", "test", "test");
        AuthData authData = service.register(newUser);
        int newGameID = service.create(authData.authToken(), "gameTest");
        service.clear();

        Assertions.assertNull(service.login(newUser));
    }
}
