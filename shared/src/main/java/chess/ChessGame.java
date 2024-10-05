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
    private ChessBoard gameBoard = new ChessBoard();
    private TeamColor currentTurn = TeamColor.WHITE;
    private final short dimension = 8;
    private final Safety[][] dangerMap = new Safety[dimension][dimension];
    private boolean enPassantFlag = false;

    private enum Safety {
        SAFE,
        DANGER
    }

    public ChessGame() {
        this.gameBoard.resetBoard();
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
        ChessPosition enPassantPieceLocation = null;

        Collection<ChessMove> possibleMoves = gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);

        if (!completedMoves.isEmpty()) {
            if (gameBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN) {
                ChessMove previousMove = completedMoves.peek();
                ChessPosition previousMoveEnd = previousMove.getEndPosition();
                ChessPosition previousMoveStart = previousMove.getStartPosition();

                if (gameBoard.getPiece(previousMoveEnd).getPieceType() == ChessPiece.PieceType.PAWN) {
                    if ((previousMoveEnd.getRank() - previousMoveStart.getRank()) == 2 ||
                            (previousMoveEnd.getRank() - previousMoveStart.getRank()) == -2) {
                        enPassantFlag = true;
                        enPassantPieceLocation = new ChessPosition((previousMoveStart.getRank() + previousMoveEnd.getRank()) / 2, previousMoveEnd.getFile());
                        possibleMoves.add(new ChessMove(startPosition, enPassantPieceLocation, null));
                    }
                }
            }
        }

        possibleMoves.removeIf(this::moveIntoCheckCheck);

        return possibleMoves;
    }

    private boolean moveIntoCheckCheck(ChessMove move) {
        boolean moveIntoCheck = false;
        TeamColor teamColor = gameBoard.getPiece(move.getStartPosition()).getTeamColor();
        ChessBoard originalBoard = new ChessBoard(gameBoard);

        gameBoard.movePiece(move);
        if (isInCheck(teamColor)) {
            moveIntoCheck = true;
        }

        gameBoard = originalBoard;

        return moveIntoCheck;
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
        } else if (gameBoard.getPiece(move.getStartPosition()).getTeamColor() != currentTurn) {
            throw new InvalidMoveException("It is not your turn.");
        } else if (!validMoves(move.getStartPosition()).contains(move)) {
            String errorMessage = move.getMoveName(gameBoard) + " is an invalid move.";
            throw new InvalidMoveException(errorMessage);
        }

        gameBoard.movePiece(move);

        if (currentTurn == TeamColor.WHITE) {
            currentTurn = TeamColor.BLACK;
        } else {
            currentTurn = TeamColor.WHITE;
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
        generateDangerMap(teamColor);

        ChessPosition kingLocation = findKing(teamColor);
        if (kingLocation != null && dangerMap[kingLocation.getRank() - 1][kingLocation.getFile() - 1] == Safety.DANGER) {
            return true;
        } else {
            return false;
        }
    }

    private ChessPosition findKing(TeamColor teamColor) {
        ChessPosition kingLocation = null;

        for (int i = 1; i <= dimension; i++) {
            for (int j = 1; j <= dimension; j++) {
                ChessPiece pieceOfInterest = gameBoard.getPiece(new ChessPosition(i, j));
                if (pieceOfInterest != null && pieceOfInterest.getPieceType() == ChessPiece.PieceType.KING && pieceOfInterest.getTeamColor() == teamColor) {
                    kingLocation = new ChessPosition(i, j);
                    return kingLocation;
                }
            }
        }

        return kingLocation;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        } else {
            Collection<ChessMove> validMoves = allValidMoves(teamColor);
            if (validMoves.isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        } else {
            Collection<ChessMove> validMoves = allValidMoves(teamColor);
            if (validMoves.isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
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

    private void generateDangerMap(TeamColor teamColor) {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                dangerMap[i][j] = Safety.SAFE;
            }
        }

        TeamColor opposingColor = teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        Collection<ChessMove> opponentMoves = allPossibleMoves(opposingColor);

        for (var move : opponentMoves) {
            ChessPosition dangerLocation = move.getEndPosition();
            dangerMap[dangerLocation.getRank() - 1][dangerLocation.getFile() - 1] = Safety.DANGER;
        }
    }

    private Collection<ChessMove> allPossibleMoves (TeamColor teamColor) {
        HashSet<ChessMove> allMoves = new HashSet<>();

        for (int i = 1; i <= dimension; i++) {
            for (int j = 1; j <= dimension; j++) {
                ChessPosition currentPosition = new ChessPosition(i,j);
                ChessPiece pieceOfInterest = gameBoard.getPiece(currentPosition);
                if (pieceOfInterest != null && pieceOfInterest.getTeamColor() == teamColor) {
                    allMoves.addAll(pieceOfInterest.captureMoves(gameBoard, currentPosition));
                }
            }
        }

        return allMoves;
    }

    private Collection<ChessMove> allValidMoves (TeamColor teamColor) {
        HashSet<ChessMove> validMoves = new HashSet<>();

        for (int i = 1; i <= dimension; i++) {
            for (int j = 1; j <= dimension; j++) {
                ChessPosition currentPosition = new ChessPosition(i,j);
                ChessPiece pieceOfInterest = gameBoard.getPiece(currentPosition);
                if (pieceOfInterest != null && pieceOfInterest.getTeamColor() == teamColor) {
                    validMoves.addAll(validMoves(currentPosition));
                }
            }
        }

        return validMoves;
    }
}
