package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final String serverUrl;

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
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status == 200;
    }

    public String signin(UserData userData) throws ResponseException {
        String path = "/session";
        return this.makeRequest("POST", path, userData, String.class, null);
    }

    public String register(UserData userData) throws ResponseException {
        String path = "/user";
        return this.makeRequest("POST", path, userData, String.class, null);
    }

    public String createGame(String gameName, String authToken) throws ResponseException {
        String path = "/game";
        return this.makeRequest("POST", path, new NewGameName(gameName), String.class, authToken);
    }

    public GameData[] listGames(String authToken) throws ResponseException {
        String path = "/game";
        return this.makeRequest("GET", path, null, GameData[].class, authToken);
    }

    public void joinGame(int selectedGameID, String color, String authToken) throws ResponseException {
        String path = "/game";
        class JoinGameInfo {
            private String playerColor = color;
            private int gameID = selectedGameID;
            JoinGameInfo() {}
        }
        this.makeRequest("PUT", path, new JoinGameInfo(), null, authToken);
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

    private static class NewGameName {
        private final String name;
        NewGameName(String gameName) {
            this.name = gameName;
        }
    }
}
