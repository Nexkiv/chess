package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private final Stack<ChessMove> completedMoves = new Stack<>();
    private ChessBoard gameBoard;
    private TeamColor currentTurn = TeamColor.WHITE;
    private final short dimension = 8;
    private final Safety[][] dangerMap = new Safety[dimension][dimension];

    private enum Safety {
        SAFE,
        DANGER
    }

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> possibleMoves = gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);

        if (gameBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING) {
            generateDangerMap(gameBoard.getPiece(startPosition).getTeamColor());

            possibleMoves.removeIf(move -> dangerMap[move.getEndPosition().getFile() - 1][move.getEndPosition().getRank() - 1] == Safety.DANGER);
        }

        return possibleMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (gameBoard.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("No piece in the starting position.");
        } else if (!validMoves(move.getStartPosition()).contains(move)) {
            String errorMessage = move.getMoveName(gameBoard) + " is an invalid move.";
            throw new InvalidMoveException(errorMessage);
        }

        completedMoves.push(move);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = new ChessBoard(board);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return new ChessBoard(gameBoard);
    }

    private void generateDangerMap(TeamColor color) {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                dangerMap[i][j] = Safety.SAFE;
            }
        }

        HashSet<ChessMove> opponentMoves = new HashSet<>();
        for (int i = 1; i <= dimension; i++) {
            for (int j = 1; j <= dimension; j++) {
                ChessPosition currentPosition = new ChessPosition(i,j);
                ChessPiece pieceOfInterest = gameBoard.getPiece(currentPosition);
                if (pieceOfInterest != null && pieceOfInterest.getTeamColor() != color) {
                    opponentMoves.addAll(pieceOfInterest.captureMoves(gameBoard, currentPosition));
                }
            }
        }

        for (var move : opponentMoves) {
            ChessPosition dangerLocation = move.getEndPosition();
            dangerMap[dangerLocation.getFile() - 1][dangerLocation.getRank() - 1] = Safety.DANGER;
        }
    }
}
