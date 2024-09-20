package chess;

import chess.movementRule.*;
import chess.ChessPiece.PieceType;
import java.util.HashMap;

public class ChessRules {
    /**
     * A HashMap containing all the movement rules based on piece.
     */
    static private final HashMap<PieceType, MovementRule> rules = new HashMap<>();

    static {
        rules.put(PieceType.KING, new KingMovementRule());
        rules.put(PieceType.QUEEN, new QueenMovementRule());
        rules.put(PieceType.KNIGHT, new KnightMovementRule());
        rules.put(PieceType.BISHOP, new BishopMovementRule());
//      rules.put(ROOK, new RookMovementRule());
//      rules.put(PAWN, new PawnMovementRule());
    }

    /**
     *
     * @param type the piece as an enum PieceType
     * @return the rules for the given piece
     */
    static public MovementRule getMovementRule(ChessPiece.PieceType type) {
        return rules.get(type);
    }
}
