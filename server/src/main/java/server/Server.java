package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.MemoryDataAccess;
import exception.ResponseException;
import model.UserData;
import spark.*;
import service.Service;

public class Server {

    private final Service service;

    public Server() {
        service = new Service(new MemoryDataAccess());
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

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
        response.status(exception.StatusCode());
    }

    private Object clearData(Request request, Response response) throws ResponseException {
        service.clear();
        response.status(200);
        return "";
    }

    private Object registerUser(Request request, Response response) throws ResponseException {
        var user = new Gson().fromJson(request.body(), UserData.class);
        if (user.username() == null || user.password() == null || user.email() == null) {
            return error400(response);
        }
        var auth = service.register(user);
        if (auth == null) {
            response.status(403);
            return "{\"message\": \"Error: already taken\"}";
        }
        response.status(200);
        return auth.toJson();
    }

    private Object login(Request request, Response response) throws ResponseException {
        var user = new Gson().fromJson(request.body(), UserData.class);
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
        String gameName = request.queryParams("gameName");
        int gameID = service.create(authToken, gameName);
        response.status(200);
        return "{\"gameID\": " + gameID + "}";
    }

    private Object joinGame(Request request, Response response) throws ResponseException {
        String authToken = request.headers("Authorization");
        JsonObject jsonObject = new Gson().fromJson(request.body(), JsonObject.class);
        String playerColor = jsonObject.get("playerColor").getAsString();
        int gameID = jsonObject.get("gameID").getAsInt();
        service.join(authToken, playerColor, gameID);
        response.status(200);
        return "";
    }

    private Object listGames(Request request, Response response) throws ResponseException {
        String authToken = request.headers("Authorization");
        String games = "{\"games\": " + service.list(authToken) + "}";
        response.status(200);
        return games;
    }

    private String error400 (Response response) {
        response.status(400);
        return "{\"message\": \"Error: bad request\"}";
    }

    private String error401 (Response response) {
        response.status(401);
        return "{\"message\": \"Error: unauthorized\"}";
    }
}
