package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovementRule extends BaseMovementRule {

    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();
        // North, South, East, West
        calculateMoves(board, position, 1, 0, moves, true);
        calculateMoves(board, position, -1, 0, moves, true);
        calculateMoves(board, position, 0, 1, moves, true);
        calculateMoves(board, position, 0, -1, moves, true);

        // NorthEast, NorthWest, SouthEast, SouthWest
        calculateMoves(board, position, 1, 1, moves, true);
        calculateMoves(board, position, 1, -1, moves, true);
        calculateMoves(board, position, -1, 1, moves, true);
        calculateMoves(board, position, -1, -1, moves, true);

        return moves;
    }
}

