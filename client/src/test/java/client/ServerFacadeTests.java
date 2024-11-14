package client;

import exception.ResponseException;
import jdk.jfr.Category;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.net.HttpURLConnection;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static UserData existingUser;
    private static UserData newUser;
    private static ServerFacade serverFacade;
    private static Server server;
    private static String gameName;
    private String existingAuth;
    private AuthData loginResult;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var url = "http://localhost:" + port;
        serverFacade = new ServerFacade(url);

        existingUser = new UserData("ExistingUser", "existingUserPassword", "eu@mail.com");
        newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");

        gameName = "testGame";
    }

    @BeforeEach
    public void clearDatabase() throws ResponseException {
        serverFacade.clearDataBase("monkeypie");

        //one user already logged in
        AuthData regResult = serverFacade.register(existingUser);
        existingAuth = regResult.authToken();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Normal User Login")
    public void successLogin() throws ResponseException {
        loginResult = serverFacade.login(existingUser);

        Assertions.assertEquals(HttpURLConnection.HTTP_OK, serverFacade.getStatusCode(), "Action was unsuccessful");
        Assertions.assertEquals(existingUser.username(), loginResult.username(),
                "Response did not give the same username as user");
        Assertions.assertNotNull(loginResult.authToken(), "Response did not return authentication String");
    }

    @Test
    @DisplayName("Login Invalid User")
    public void loginInvalidUser() {
        assertThrows(ResponseException.class, () -> loginResult = serverFacade.login(newUser), "Action was successfully blocked");

        assertNull(loginResult, "Response did return authentication String");
    }

    @Test
    @DisplayName("Login Wrong Password")
    public void loginWrongPassword() {
        UserData loginRequest = new UserData(existingUser.username(), newUser.password(), null);

        assertThrows(ResponseException.class, () -> loginResult = serverFacade.login(loginRequest), "Action was successfully blocked");

        assertNull(loginResult);
    }



    @Test
    void register() throws Exception {
        UserData userData = new UserData("player1", "password", "p1@email.com");
        String authToken = serverFacade.register(userData).authToken();
        assertTrue(authToken.length() > 10);
    }
}
