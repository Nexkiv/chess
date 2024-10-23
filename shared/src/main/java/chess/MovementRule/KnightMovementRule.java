package chess.MovementRule;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovementRule extends BaseMovementRule {

    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        // NorthEast
        calculateMoves(board, position, 2, 1, moves, false);
        calculateMoves(board, position, 1, 2, moves, false);

        // SouthEast
        calculateMoves(board, position, -1, 2, moves, false);
        calculateMoves(board, position, -2, 1, moves, false);

        // SouthWest
        calculateMoves(board, position, -2, -1, moves, false);
        calculateMoves(board, position, -1, -2, moves, false);

        // NorthWest
        calculateMoves(board, position, 1, -2, moves, false);
        calculateMoves(board, position, 2, -1, moves, false);

        return moves;
    }
}