import exception.ResponseException;
import server.Server;

public class Main {
    public static void main(String[] args) {
        Server chessServer = new Server();

        System.out.printf("Server running on port %d", chessServer.run(8080));
    }
}

