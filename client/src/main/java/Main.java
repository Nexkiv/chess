import chess.*;
import ui.DisplayBoard;
import ui.Repl;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        int port = 8080;
        var url = "http://localhost:" + port;
        Repl ui = new Repl(url);

        ui.run();
    }
}