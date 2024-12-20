package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import org.glassfish.tyrus.core.WebSocketException;
import ui.client.ChessClient;
import ui.client.LoggedOutClient;
import websocket.NotificationHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private ChessClient client;

    public Repl(String serverUrl) {
        client = new LoggedOutClient(serverUrl, this);
    }

    public void run() {
        System.out.println(BLACK_KING + " Welcome to Chess! Sign-in to Start! " + BLACK_KING + "\n");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        while (!(client == null)) {
            printPrompt();
            String line = scanner.nextLine();
            System.out.println();

            try {
                client = client.eval(line);
                if (client == null) {
                    break;
                }
                System.out.print(client.getMessage());
            } catch (WebSocketException e) {
                String message = e.getMessage();
                String errorMsg = SET_TEXT_COLOR_RED + "ERROR: " + message +
                        "\n" + RESET_TEXT_COLOR + "Type help for commands";
                System.out.println(errorMsg);
            } catch (Throwable e) {
                var msg = e.getMessage();
                StringBuilder errorMsg = new StringBuilder(SET_TEXT_COLOR_RED + "ERROR: ");
                switch (msg) {
                    case "Wrong number of arguments" -> errorMsg.append("Wrong number of arguments.");
                    case "Invalid game selection" -> errorMsg.append("That game does not exist.");
                    case "failure: 401" -> {
                        if (client.getClass() == LoggedOutClient.class) {
                            errorMsg.append("Invalid username and password.");
                        } else {
                            errorMsg.append("You are not logged in.");
                        }
                    }
                    case "failure: 400" -> errorMsg.append("I'm sorry that is not possible.");
                    case "failure: 403" -> {
                        if (client.getClass() == LoggedOutClient.class) {
                            errorMsg.append("That username is not available.");
                        } else {
                            errorMsg.append("That player role is already taken.");
                        }

                    }
                    default -> errorMsg.append("Unknown error, please try again.");
                }
                errorMsg.append("\n").append(RESET_TEXT_COLOR).append("Type help for commands");
                System.out.println(errorMsg.toString());

            }
        }
        System.out.println("Thanks for playing!");
    }

    private void printPrompt() {
        System.out.print ("\n" + ">>> ");
    }

    @Override
    public void notify(String message) {
        System.out.println("\n" + message);
        printPrompt();
    }
}
