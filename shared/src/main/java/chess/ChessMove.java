package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    public String getMoveName(ChessBoard board) {
        ChessPiece movingPiece = board.getPiece(startPosition);

        StringBuilder moveName = new StringBuilder();
        moveName.append(movingPiece.getPieceType().toString());

        if (board.getPiece(endPosition) != null) {
            if (movingPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                moveName.append(startPosition.getFileAlgNotation());
            }
            moveName.append('x');
        }

        moveName.append(endPosition.toString());

        if (promotionPiece != null) {
            moveName.append('=');
            moveName.append(promotionPiece.toString());
        }

        return moveName.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove move = (ChessMove) o;
        return (startPosition.equals(move.startPosition) && endPosition.equals(move.endPosition)
                && promotionPiece == move.promotionPiece);
    }

    @Override
    public int hashCode() {
        var promotionCode = (promotionPiece == null ?
                9 : promotionPiece.ordinal());
        return (71 * startPosition.hashCode()) + endPosition.hashCode() + promotionCode;
    }

    @Override
    public String toString() {
        var p = (promotionPiece == null ? "" : ":" + promotionPiece);
        return String.format("%s:%s%s", startPosition.toString(), endPosition.toString(), p);
    }
}
