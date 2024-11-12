package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private ChessClient client;

    public Repl(String serverUrl) {
        client = new LoggedOutClient(serverUrl);
    }

    public void run() {
        System.out.println(BLACK_KING + " Welcome to Chess! Sign-in to Start! " + BLACK_KING + "\n");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        while (!(client == null)) {
            System.out.print(printPrompt());
            String line = scanner.nextLine();
            System.out.println();

            try {
                client = client.eval(line);
                if (client == null) {
                    break;
                }
                System.out.print(client.getMessage());
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.println(msg);
            }
        }
        System.out.println("Thanks for playing!");
    }

    private String printPrompt() {
        return ("\n" + ">>> ");
    }
}
