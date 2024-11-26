package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.MySqlDataAccess;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import service.websocket.WebSocketHandler;
import spark.*;
import service.Service;

import java.util.Collection;

public class Server {

    private final Service service;
    private final WebSocketHandler webSocketHandler;
    private DataAccess dataAccessObject;
    private final static Gson SERIALIZER = new Gson();

    public Server() {
        try {
            dataAccessObject = new MemoryDataAccess();
        } catch (Exception e) {
            dataAccessObject = new MemoryDataAccess();
        } finally {
            service = new Service(dataAccessObject);
            webSocketHandler = new WebSocketHandler(dataAccessObject);
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearData);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game", this::listGames);
        Spark.exception(ResponseException.class, this::exceptionHandler);


        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException exception, Request request, Response response) {
        response.status(exception.statusCode());
    }

    private Object clearData(Request request, Response response) throws ResponseException {
        service.clear();
        response.status(200);
        return "";
    }

    private Object registerUser(Request request, Response response) throws ResponseException {
        var user = SERIALIZER.fromJson(request.body(), UserData.class);
        if (user.username() == null || user.password() == null || user.email() == null) {
            return error400(response);
        }
        var userInfo = service.register(user);
        if (userInfo == null) {
            return error403(response);
        }
        response.status(200);
        return userInfo.toJson();
    }

    private Object login(Request request, Response response) throws ResponseException {
        var user = SERIALIZER.fromJson(request.body(), UserData.class);
        if (user.username() == null || user.password() == null) {
            return error400(response);
        }
        var auth = service.login(user);
        if (auth == null) {
            return error401(response);
        } else {
            response.status(200);
            return auth.toJson();
        }
    }

    private Object logout(Request request, Response response) throws ResponseException {
        String authToken = request.headers("Authorization");
        if (service.successfulLogout(authToken)) {
            response.status(200);
            return "";
        } else {
            return error401(response);
        }

    }

    private Object createGame(Request request, Response response) throws ResponseException {
        String authToken = request.headers("Authorization");
        JsonObject jsonObject = SERIALIZER.fromJson(request.body(), JsonObject.class);
        if (jsonObject.get("gameName") == null) {
            return error400(response);
        }
        String gameName = jsonObject.get("gameName").getAsString();
        int gameID = service.create(authToken, gameName);
        if (gameID == -401) {
            return error401(response);
        } else if (gameID == -400) {
            return error400(response);
        } else {
            response.status(200);
            return "{\"gameID\": " + gameID + "}";
        }
    }

    private Object joinGame(Request request, Response response) throws ResponseException {
        String authToken = request.headers("Authorization");
        JsonObject jsonObject = SERIALIZER.fromJson(request.body(), JsonObject.class);
        if (jsonObject.get("playerColor") == null || jsonObject.get("gameID") == null) {
            return error400(response);
        }
        String playerColor = jsonObject.get("playerColor").getAsString();
        int gameID = jsonObject.get("gameID").getAsInt();
        int statusCode = service.join(authToken, playerColor, gameID);

        return switch (statusCode) {
            case 200 -> {
                response.status(200);
                yield "";
            }
            case 400 -> error400(response);
            case 401 -> error401(response);
            case 403 -> error403(response);
            default -> throw new ResponseException(500, "something went wrong in joinGame()");
        };
    }

    private Object listGames(Request request, Response response) throws ResponseException {
        String authToken = request.headers("Authorization");
        Collection<GameData> games = service.list(authToken);
        if (games == null) {
            return error401(response);
        }
        String allGames = SERIALIZER.toJson(games);
        String gamesJson = "{\"games\": " + allGames + "}";
        response.status(200);
        return gamesJson;
    }

    private String error400 (Response response) {
        response.status(400);
        return "{\"message\": \"Error: bad request\"}";
    }

    private String error401 (Response response) {
        response.status(401);
        return "{\"message\": \"Error: unauthorized\"}";
    }

    private String error403 (Response response) {
        response.status(403);
        return "{\"message\": \"Error: already taken\"}";
    }
}
