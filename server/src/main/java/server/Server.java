package server;

import com.google.gson.Gson;
import dataaccess.MemoryDataAccess;
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

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clearData(Request request, Response response) {
        service.clear();
        response.status(200);
        return "";
    }

    private Object registerUser(Request request, Response response) {
        var user = new Gson().fromJson(request.body(), UserData.class);
        var auth = service.register(user);
        response.status(200);
        return new Gson().toJson(auth);
    }

    private Object login(Request request, Response response) {
        var user = new Gson().fromJson(request.body(), UserData.class);
        var auth = service.login(user);
        response.status(200);
        return new Gson().toJson(auth);
    }

    private Object logout(Request request, Response response) {
        String authToken = new Gson().fromJson(request.body(), String.class);
        service.logout(authToken);
        response.status(200);
        return "";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
