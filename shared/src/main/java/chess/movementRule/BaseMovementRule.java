package chess.movementRule;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public abstract class BaseMovementRule implements MovementRule {

    protected void calculateMoves(ChessBoard board, ChessPosition pos, int rankInc, int fileInc,
                                  Collection<ChessMove> moves, boolean allowDistance) {
        moves.add(new ChessMove(new ChessPosition(1,1),new ChessPosition(2,2),null));
    }

    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition position);
}

