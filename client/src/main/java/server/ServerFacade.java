package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.List;

public class ServerFacade {
    private final String serverUrl;
    private static int statusCode;
    private final static Gson SERIALIZER = new Gson();

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(statusCode, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = SERIALIZER.toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        statusCode = http.getResponseCode();
        if (!isSuccessful(statusCode)) {
            throw new ResponseException(statusCode, "failure: " + statusCode);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = SERIALIZER.fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status == 200;
    }

    public AuthData login(UserData userData) throws ResponseException {
        String path = "/session";
        return this.makeRequest("POST", path, userData, AuthData.class, null);
    }

    public AuthData register(UserData userData) throws ResponseException {
        String path = "/user";
        return this.makeRequest("POST", path, userData, AuthData.class, null);
    }

    public int createGame(String gameName, String authToken) throws ResponseException {
        String path = "/game";
        record GameID (int gameID) {}
        GameID response = this.makeRequest("POST", path, new NewGameName(gameName), GameID.class, authToken);
        return response.gameID;
    }

    public GameData[] ListGames(String authToken) throws ResponseException {
        String path = "/game";
        record listGames (GameData[] games) {
        }
        var response = this.makeRequest("GET", path, null, listGames.class, authToken);
        return response.games;
    }

    public void joinGame(int selectedGameID, String color, String authToken) throws ResponseException {
        String path = "/game";
        record JoinGameInfo (String playerColor, int gameID ) {}
        this.makeRequest("PUT", path, new JoinGameInfo(color, selectedGameID), null, authToken);
    }

    public void observeGame(int gameID, String authToken) throws ResponseException {
        joinGame(gameID, "null", authToken);
    }

    public void logout(String authToken) throws ResponseException {
        String path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public String getGameBoard(String authToken, int gameID) {
        throw new RuntimeException("Not implemented");
    }

    public void clearDataBase(String password) throws ResponseException {
        String path = "/db";
        if (password.equals("monkeypie")) {
            this.makeRequest("DELETE", path, null, null, null);
        } else {
            throw new RuntimeException("Invalid clear password");
        }
    }

    private record NewGameName(String gameName) {
    }

    public int getStatusCode() {
        return statusCode;
    }
}
