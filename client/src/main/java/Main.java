import chess.*;
import ui.DisplayBoard;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        System.out.println(new DisplayBoard(new ChessGame().getBoard()).getBothBoards());
    }
}