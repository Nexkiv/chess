package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final PieceType type;
    private final ChessGame.TeamColor pieceColor;


    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    public ChessPiece(ChessPiece that) {
        this.pieceColor = that.pieceColor;
        this.type = that.type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
        ;

        @Override
        public String toString() {
            String capitalLetter = switch (this) {
                case KING -> "K";
                case QUEEN -> "Q";
                case BISHOP -> "B";
                case KNIGHT -> "N";
                case ROOK -> "R";
                case PAWN -> "";
            };

            return capitalLetter;
        }
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return ChessRules.getMovementRule(getPieceType()).moves(board, myPosition);
    }

    public Collection<ChessMove> captureMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece activePiece = board.getPiece(myPosition);
        if (activePiece == null) {
            throw new RuntimeException("Impossible move query.");
        } else if (activePiece.getPieceType() != PieceType.PAWN) {
            return pieceMoves(board, myPosition);
        } else {
            return ChessRules.getPawnMovementRule().captureMoves(board, myPosition);
        }
    }

    private String getPieceSymbol() {
        String pieceSymbol = switch (pieceColor) {
            case WHITE -> switch (type) {
                case KING -> "♔";
                case QUEEN -> "♕";
                case BISHOP -> "♗";
                case KNIGHT -> "♘";
                case ROOK -> "♖";
                case PAWN -> "♙";
            };
            case BLACK -> switch (type) {
                case KING -> "♚";
                case QUEEN -> "♛";
                case BISHOP -> "♝";
                case KNIGHT -> "♞";
                case ROOK -> "♜";
                case PAWN -> "♟";
            };
        };
        return pieceSymbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = 31 * type.ordinal() + pieceColor.ordinal();
        return result;
    }

    @Override
    public String toString() {
        return getPieceSymbol();
    }
}
