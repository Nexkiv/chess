package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private ChessClient client;

    public Repl(String serverUrl) {
        client = new LoggedOutClient(serverUrl);
    }

    public void run() {
        System.out.println(BLACK_KING + " Welcome to Chess! Sign-in to Start! " + BLACK_KING);
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            System.out.println(printPrompt());
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private String printPrompt() {
        return ("\n" + ">>> ");
    }
}
