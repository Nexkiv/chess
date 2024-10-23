package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.List;

public abstract class PawnMovementRuleAbstract extends BaseMovementRule {
    protected void addPawnMoves(ChessPosition pos, Collection<ChessMove> moves, ChessPosition newPos) {
        if (newPos.getRank() == 1 || newPos.getRank() == 8) {
            moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.KNIGHT));;
        } else {
            moves.add(new ChessMove(pos, newPos,null));
        }
    }
}
