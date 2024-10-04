package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final short dimension = 8;
    private final ChessPiece[][] squares = new ChessPiece[dimension][dimension];

    public ChessBoard() {
        
    }

    public ChessBoard(ChessBoard that) {
        // this.dimension = that.getDimension();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (that.squares[i][j] == null) {
                    continue;
                }
                this.squares[i][j] = new ChessPiece(that.squares[i][j]);
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRank() - 1][position.getFile() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        ChessPiece piece = squares[position.getRank() - 1][position.getFile() - 1];
        return piece;
    }

    private void setUpSide(ChessGame.TeamColor teamColor) {
        int homeRank = (teamColor == ChessGame.TeamColor.WHITE) ? 1 : 8;

        addPiece(new ChessPosition(homeRank, 1), new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(homeRank,2), new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(homeRank,3), new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(homeRank,4), new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(homeRank,5), new ChessPiece(teamColor, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(homeRank,6), new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(homeRank,7), new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(homeRank,8), new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));

        int pawnRank = (teamColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
        for (int i = 1; i <= dimension; i++) {
            addPiece(new ChessPosition(pawnRank,i), new ChessPiece(teamColor, ChessPiece.PieceType.PAWN));
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Clear board
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                squares[i][j] = null;
            }
        }

        // Set up White side
        setUpSide(ChessGame.TeamColor.WHITE);

        // Set up Black Side
        setUpSide(ChessGame.TeamColor.BLACK);
    }

    /**
     * Gets the dimensions of the board
     * @return the value of the dimension variable
     */
    public short getDimension() {
        return dimension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard chessBoard = (ChessBoard) o;
        if (dimension != chessBoard.dimension) return false;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (squares[i][j] == null && chessBoard.squares[i][j] == null) {
                    continue;
                }
                if (!squares[i][j].equals(chessBoard.squares[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = dimension;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                result = 31 * result + squares[i][j].hashCode();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (squares[i][j] != null) {
                    sb.append(squares[i][j].toString());
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
