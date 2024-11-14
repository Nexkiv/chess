package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class DisplayBoard {
    private final ChessBoard board;
    private static final String SET_BG_COLOR_SILVER = "\033[48;2;220;220;220m";
    private static final String SET_BG_COLOR_TAN = "\033[48;2;193;154;107m";
    private static final String SET_BG_COLOR_BROWN = "\033[48;2;84;42;24m";

    public DisplayBoard(ChessBoard board) {
        this.board = board;
    }

    public String getBoard(ChessGame.TeamColor teamColor) {
        StringBuilder boardDisplay = new StringBuilder();

        boardDisplay.append(headerAndFooter(teamColor));
        boardDisplay.append(RESET_BG_COLOR).append("\n");
        for (int i = 8; i > 0; i--) {
            boardDisplay.append(SET_BG_COLOR_SILVER).append(SET_TEXT_COLOR_BLACK).append(wrapText(i));
            for (int j = 1; j < 9; j++) {
                if ((i + j) % 2 != 0) {
                    boardDisplay.append(SET_BG_COLOR_TAN);
                } else {
                    boardDisplay.append(SET_BG_COLOR_BROWN);
                }
                if (teamColor == ChessGame.TeamColor.WHITE) {
                    boardDisplay.append(chessPiece(board.getPiece(new ChessPosition(i,j))));
                } else {
                    boardDisplay.append(chessPiece(board.getPiece(new ChessPosition(9 - i,9 - j))));
                }
            }
            boardDisplay.append(SET_BG_COLOR_SILVER).append(SET_TEXT_COLOR_BLACK).append(wrapText(i));
            boardDisplay.append(RESET_BG_COLOR).append("\n");
        }
        boardDisplay.append(headerAndFooter(teamColor));
        boardDisplay.append(RESET_BG_COLOR);

        return boardDisplay.toString();
    }

    private String headerAndFooter(ChessGame.TeamColor teamColor) {
        StringBuilder headerAndFooterDisplay = new StringBuilder();

        headerAndFooterDisplay.append(SET_BG_COLOR_SILVER);
        headerAndFooterDisplay.append(wrapText("\u2003"));
        if (teamColor == ChessGame.TeamColor.WHITE) {
            for (int i = 97; i < 105; i++) {
                headerAndFooterDisplay.append(wrapText((char) i));
            }
        } else {
            for (int i = 104; i > 96; i--) {
                headerAndFooterDisplay.append(wrapText((char) i));
            }
        }
        headerAndFooterDisplay.append(wrapText("\u2003"));

        return headerAndFooterDisplay.toString();
    }

    private String chessPiece (ChessPiece chessPiece) {


        if (chessPiece == null) {
            return wrapText("\u2003");
        }

        return wrapText(switch (chessPiece.getTeamColor()) {
            case WHITE -> SET_TEXT_COLOR_WHITE;
            case BLACK -> SET_TEXT_COLOR_BLACK;
        } +
                switch (chessPiece.getPieceType()) {
                    case KING -> "♚";
                    case QUEEN -> "♛";
                    case BISHOP -> "♝";
                    case KNIGHT -> "♞";
                    case ROOK -> "♜";
                    case PAWN -> "♟";
                });
    }
    
    private String wrapText(Object text) {
        return "\u2003" + text.toString() + "\u2003";
    }

    public String getBothBoards() {
        return getBoard(ChessGame.TeamColor.WHITE) + "\n\n" +  getBoard(ChessGame.TeamColor.BLACK) + "\n";
    }
}
