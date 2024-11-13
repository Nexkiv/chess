package ui.client;

import exception.ResponseException;

public interface ChessClient {
    String help();
    ChessClient eval(String input) throws ResponseException;
    String getMessage();
}
