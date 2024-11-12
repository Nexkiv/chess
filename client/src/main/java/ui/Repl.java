package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println(BLACK_KING + " Welcome to Chess! Sign-in to Start! " + BLACK_KING);
        System.out.print(client.help());
    }
}
