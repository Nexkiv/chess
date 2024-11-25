package ui;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class DisplayBoard {
    private final ChessBoard board;
    private static final String SET_BG_COLOR_SILVER = "\033[48;2;220;220;220m";
    private static final String SET_BG_COLOR_TAN = "\033[48;2;193;154;107m";
    private static final String SET_BG_COLOR_RED_TAN = "\033[48;2;205;105;93m";
    private static final String SET_BG_COLOR_BROWN = "\033[48;2;84;42;24m";
    private static final String SET_BG_COLOR_RED_BROWN = "\033[48;2;184;42;24m";

    public DisplayBoard(ChessBoard board) {
        this.board = board;
    }

    public String getBoard(ChessGame.TeamColor teamColor) {
        Collection<ChessMove> emptyMoves = new ArrayList<>();
        return this.getHighlightedBoard(teamColor, emptyMoves);
    }

    public String getHighlightedBoard(ChessGame.TeamColor teamColor, Collection<ChessMove> validMoves) {
        StringBuilder boardDisplay = new StringBuilder();
        boolean[][] highlightMap = new boolean[8][8];
        boolean[][] pieceLocation = new boolean[8][8];

        for (ChessMove move : validMoves) {
            pieceLocation[move.getStartPosition().getRank() - 1][move.getStartPosition().getFile() - 1] = true;
            highlightMap[move.getEndPosition().getRank() - 1][move.getEndPosition().getFile() - 1] = true;
        }

        int row;
        int col;

        boardDisplay.append(headerAndFooter(teamColor));
        boardDisplay.append(RESET_BG_COLOR).append("\n");
        for (int i = 8; i > 0; i--) {
            if (teamColor == ChessGame.TeamColor.WHITE) {
                row = i;
            } else {
                row = 9 - i;
            }
            boardDisplay.append(SET_BG_COLOR_SILVER).append(SET_TEXT_COLOR_BLACK).append(wrapText(row));
            for (int j = 1; j < 9; j++) {
                if (pieceLocation[i - 1][j - 1]) {
                    boardDisplay.append(SET_BG_COLOR_SILVER);
                } else if ((i + j) % 2 != 0 && highlightMap[i - 1][j - 1]) {
                    boardDisplay.append(SET_BG_COLOR_RED_TAN);
                } else if ((i + j) % 2 != 0) {
                    boardDisplay.append(SET_BG_COLOR_TAN);
                } else if (highlightMap[i - 1][j - 1]) {
                    boardDisplay.append(SET_BG_COLOR_RED_BROWN);
                } else {
                    boardDisplay.append(SET_BG_COLOR_BROWN);
                }
                if (teamColor == ChessGame.TeamColor.WHITE) {
                    col = j;
                } else {
                    col = 9 - j;
                }
                boardDisplay.append(chessPiece(board.getPiece(new ChessPosition(row,col))));
            }
            boardDisplay.append(SET_BG_COLOR_SILVER).append(SET_TEXT_COLOR_BLACK).append(wrapText(row));
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
