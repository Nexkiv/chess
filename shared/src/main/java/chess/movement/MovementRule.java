package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public interface MovementRule {
    Collection<ChessMove> moves(ChessBoard board, ChessPosition pos);
}

