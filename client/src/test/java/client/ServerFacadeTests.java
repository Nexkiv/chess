package client;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static UserData existingUser;
    private static UserData newUser;
    private static ServerFacade serverFacade;
    private static Server server;
    private static String gameName;
    private String existingAuth;
    private AuthData loginResult;
    private AuthData registerResult;
    private int gameID;

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

        assertHTTPSuccess();
        assertEquals(existingUser.username(), loginResult.username(),
                "Response did not give the same username as user");
        assertNotNull(loginResult.authToken(), "Response did not return authentication String");
    }

    @Test
    @DisplayName("Login Invalid User")
    public void loginInvalidUser() {
        assertThrows(ResponseException.class, () -> loginResult = serverFacade.login(newUser), "Action was successful");

        assertNull(loginResult, "Response did return authentication String");
    }

    @Test
    @DisplayName("Login Wrong Password")
    public void loginWrongPassword() {
        UserData loginRequest = new UserData(existingUser.username(), newUser.password(), null);

        assertThrows(ResponseException.class, () -> loginResult = serverFacade.login(loginRequest), "Action was successful");

        assertNull(loginResult, "AuthData returned");
    }

    @Test
    @DisplayName("Normal User Registration")
    public void successRegister() throws ResponseException {
        //submit register request
        registerResult = serverFacade.register(newUser);

        assertHTTPSuccess();
        assertEquals(newUser.username(), registerResult.username(),
                "Response did not have the same username as was registered");
        assertNotNull(registerResult.authToken(), "Response did not contain an authentication string");
    }

    @Test
    @DisplayName("Re-Register User")
    public void registerTwice() {
        //submit register request trying to register existing user
        assertThrows(ResponseException.class, () -> registerResult = serverFacade.register(existingUser), "Action was successful");

        assertNull(registerResult, "AuthData returned");
    }

    @Test
    @DisplayName("Register Bad Request")
    public void failRegister() {
        //attempt to register a user without a password
        UserData registerRequest = new UserData(newUser.username(), null, newUser.email());

        assertThrows(ResponseException.class, () -> registerResult = serverFacade.register(registerRequest), "Action was successful");

        assertNull(registerResult, "AuthData returned");
    }

    @Test
    @DisplayName("Normal Logout")
    public void successLogout() throws ResponseException {
        //log out existing user
        serverFacade.logout(existingAuth);

        assertHTTPSuccess();
    }

    @Test
    @DisplayName("Invalid Auth Logout")
    public void failLogout() throws ResponseException {
        //log out user twice
        //second logout should fail
        serverFacade.logout(existingAuth);
        assertThrows(ResponseException.class, () -> serverFacade.logout(existingAuth), "Action was successful");
    }

    @Test
    @DisplayName("Valid Creation")
    public void goodCreate() throws ResponseException {
        gameID = serverFacade.createGame(gameName, existingAuth);

        assertHTTPSuccess();

        assertTrue(gameID > 0, "Result returned invalid game ID");
    }

    @Test
    @DisplayName("Create with Bad Authentication")
    public void badAuthCreate() throws ResponseException {
        //log out user so auth is invalid
        serverFacade.logout(existingAuth);

        assertThrows(ResponseException.class, () -> serverFacade.createGame(gameName, existingAuth), "Action was successful");
    }

    @Test
    @DisplayName("Join Created Game")
    public void goodJoin() throws ResponseException {
        //create game
        gameID = serverFacade.createGame(gameName, existingAuth);

        //join as white
        serverFacade.joinGame(gameID, "WHITE", existingAuth);

        //check
        assertHTTPSuccess();

        GameData[] listResult = serverFacade.listGames(existingAuth);

        assertEquals(1, listResult.length);
        assertEquals(existingUser.username(), listResult[0].whiteUsername(), "Player wasn't added to the game");
        assertNull(listResult[0].blackUsername(), "Invalid player assigned to black position");
    }

    @Test
    @DisplayName("Join Bad Authentication")
    public void badAuthJoin() throws ResponseException {
        //create game
        gameID = serverFacade.createGame(gameName, existingAuth);

        //try join as white
        assertThrows(ResponseException.class, () -> serverFacade.joinGame(gameID, "WHITE", existingAuth + "bad stuff"), "Action was successful");

        //check
        assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, serverFacade.getStatusCode(), "Incorrect HTTP code");
    }

    @Test
    @DisplayName("Join Bad Team Color")
    public void badColorJoin() throws ResponseException {
        //create game
        gameID = serverFacade.createGame(gameName, existingAuth);

        //try join as white
        assertThrows(ResponseException.class, () -> serverFacade.joinGame(gameID, null, existingAuth), "Action was successful");

        //check
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, serverFacade.getStatusCode(), "Incorrect HTTP code");
    }

    @Test
    @DisplayName("Join Steal Team Color")
    public void stealColorJoin() throws ResponseException {
        //create game
        gameID = serverFacade.createGame(gameName, existingAuth);

        //add existing user as black
        serverFacade.joinGame(gameID, "BLACK", existingAuth);

        //register second user
        AuthData newAuth = serverFacade.register(newUser);

        //join request trying to also join  as black
        assertThrows(ResponseException.class, () -> serverFacade.joinGame(gameID, "BLACK", newAuth.authToken()), "Action was successful");

        //check failed
        assertEquals(HttpURLConnection.HTTP_FORBIDDEN, serverFacade.getStatusCode(), "Incorrect HTTP code");
    }

    @Test
    @DisplayName("Join Bad Game ID")
    public void badGameIDJoin() throws ResponseException {
        //create game
        gameID = serverFacade.createGame(gameName, existingAuth);

        //try join as white
        assertThrows(ResponseException.class, () -> serverFacade.joinGame(0, "WHITE", existingAuth), "Action was successful");

        //check
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, serverFacade.getStatusCode(), "Incorrect HTTP code");
    }

    @Test
    @DisplayName("List No Games")
    public void noGamesList() throws ResponseException {
        GameData[] result = serverFacade.listGames(existingAuth);

        assertHTTPSuccess();
        assertEquals(0, result.length, "Found games when none should be there");
    }

    @Test
    @Order(12)
    @DisplayName("List Multiple Games")
    public void gamesList() throws ResponseException {
        //register a few users to create games
        UserData userA = new UserData("a", "A", "a.A");
        UserData userB = new UserData("b", "B", "b.B");
        UserData userC = new UserData("c", "C", "c.C");

        AuthData authA = serverFacade.register(userA);
        AuthData authB = serverFacade.register(userB);
        AuthData authC = serverFacade.register(userC);

        //create games
        Collection<GameData> expectedList = new HashSet<>();

        //1 as black from A
        String game1Name = "I'm numbah one!";
        int game1ID = serverFacade.createGame(game1Name, authA.authToken());
        serverFacade.joinGame(game1ID, "BLACK", authA.authToken());
        expectedList.add(new GameData(game1ID, null, authA.username(), game1Name, new ChessGame()));


        //1 as white from B
        String game2Name = "Lonely";
        int game2ID = serverFacade.createGame(game2Name, authB.authToken());
        serverFacade.joinGame(game2ID, "WHITE", authB.authToken());
        expectedList.add(new GameData(game2ID, authB.username(), null, game2Name, new ChessGame()));


        //1 of each from C
        String game3Name = "GG";
        int game3ID = serverFacade.createGame(game3Name, authC.authToken());
        serverFacade.joinGame(game3ID, "WHITE", authC.authToken());
        serverFacade.joinGame(game3ID, "BLACK", authA.authToken());
        expectedList.add(new GameData(game3ID, authC.username(), authA.username(), game3Name, new ChessGame()));


        //C play self
        String game4Name = "All by myself";
        int game4ID = serverFacade.createGame(game4Name, authC.authToken());
        serverFacade.joinGame(game4ID, "WHITE", authC.authToken());
        serverFacade.joinGame(game4ID, "BLACK", authC.authToken());
        expectedList.add(new GameData(game4ID, authC.username(), authC.username(), game4Name, new ChessGame()));


        //list games
        GameData[] listResult = serverFacade.listGames(existingAuth);
        assertHTTPSuccess();
        Collection<GameData> returnedList = new HashSet<>(Arrays.asList(listResult));

        //check
        assertEquals(expectedList, returnedList, "Returned Games list was incorrect");
    }

    @Test
    @Order(13)
    @DisplayName("Unique Authtoken Each Login")
    public void uniqueAuthorizationTokens() throws ResponseException {
        AuthData loginOne = serverFacade.login(existingUser);
        assertHTTPSuccess();
        Assertions.assertNotNull(loginOne.authToken(), "Login result did not contain an authToken");

        AuthData loginTwo = serverFacade.login(existingUser);
        assertHTTPSuccess();
        Assertions.assertNotNull(loginTwo.authToken(), "Login result did not contain an authToken");

        Assertions.assertNotEquals(existingAuth, loginOne.authToken(),
                "Authtoken returned by login matched authtoken from prior register");
        Assertions.assertNotEquals(existingAuth, loginTwo.authToken(),
                "Authtoken returned by login matched authtoken from prior register");
        Assertions.assertNotEquals(loginOne.authToken(), loginTwo.authToken(),
                "Authtoken returned by login matched authtoken from prior login");


        gameID = serverFacade.createGame(gameName, existingAuth);
        assertHTTPSuccess();


        serverFacade.logout(existingAuth);
        assertHTTPSuccess();

        serverFacade.joinGame(gameID, "WHITE", loginOne.authToken());
        assertHTTPSuccess();

        GameData[] listResult = serverFacade.listGames(loginTwo.authToken());
        assertEquals(HttpURLConnection.HTTP_OK, serverFacade.getStatusCode(),
                    "Server response code was not 200 OK");
        Assertions.assertEquals(1, listResult.length);
        Assertions.assertEquals(existingUser.username(), listResult[0].whiteUsername());
    }

    @Test
    @DisplayName("Clear Test")
    public void clearData() throws ResponseException {
        //create filler games
        serverFacade.createGame("Mediocre game", existingAuth);
        serverFacade.createGame("Awesome game", existingAuth);

        //log in new user
        UserData user = new UserData("ClearMe", "cleared", "clear@mail.com");
        AuthData authData = serverFacade.register(user);

        //create and join game for new user
        gameID = serverFacade.createGame("Clear game", authData.authToken());

        serverFacade.joinGame(gameID, "WHITE", authData.authToken());

        //do clear
        serverFacade.clearDataBase("monkeypie");

        //test clear successful
        assertHTTPSuccess();

        //make sure neither user can log in
        //first user
        assertThrows(ResponseException.class, () -> serverFacade.login(existingUser));
        assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, serverFacade.getStatusCode(), "User authorization not removed");

        //second user
        assertThrows(ResponseException.class, () -> serverFacade.login(user));
        assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, serverFacade.getStatusCode(), "User authorization not removed");

        //try to use old auth token to list games
        assertThrows(ResponseException.class, () -> serverFacade.listGames(existingAuth));
        assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, serverFacade.getStatusCode(), "User authorization not removed");

        //log in new user and check that list is empty
        authData = serverFacade.register(user);
        assertHTTPSuccess();
        GameData[] listResult = serverFacade.listGames(authData.authToken());
        assertHTTPSuccess();

        //check listResult
        Assertions.assertEquals(0, listResult.length, "list result did not return 0 games after clear");
    }

    @Test
    @DisplayName("Multiple Clears")
    public void multipleClear() throws ResponseException {

        //clear multiple times
        serverFacade.clearDataBase("monkeypie");
        serverFacade.clearDataBase("monkeypie");
        serverFacade.clearDataBase("monkeypie");

        //make sure returned good
        assertHTTPSuccess();
    }

    private void assertHTTPSuccess() {
        assertEquals(HttpURLConnection.HTTP_OK, serverFacade.getStatusCode(),
                "Server response code was not 200 OK");
    }
}
