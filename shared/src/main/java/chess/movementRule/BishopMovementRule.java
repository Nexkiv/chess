package chess.movementRule;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovementRule extends BaseMovementRule {

    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        calculateMoves(board, position, -1, -1, moves, true);
        calculateMoves(board, position, 1, -1, moves, true);
        calculateMoves(board, position, -1, 1, moves, true);
        calculateMoves(board, position, 1, 1, moves, true);
        return moves;
    }
}

