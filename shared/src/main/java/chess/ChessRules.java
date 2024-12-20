package chess;

import chess.movement.*;
import chess.ChessPiece.PieceType;
import java.util.HashMap;

public class ChessRules {
    /**
     * A HashMap containing all the movement rules based on piece.
     */
    static private final HashMap<PieceType, MovementRule> RULES = new HashMap<>();

    static {
        RULES.put(PieceType.KING, new KingMovementRule());
        RULES.put(PieceType.QUEEN, new QueenMovementRule());
        RULES.put(PieceType.KNIGHT, new KnightMovementRule());
        RULES.put(PieceType.BISHOP, new BishopMovementRule());
        RULES.put(PieceType.ROOK, new RookMovementRule());
        RULES.put(PieceType.PAWN, new PawnMovementRule());
    }

    /**
     *
     * @param type the piece as an enum PieceType
     * @return the rules for the given piece
     */
    static public MovementRule getMovementRule(ChessPiece.PieceType type) {
        return RULES.get(type);
    }

    static public PawnMovementRule getPawnMovementRule() {
        return new PawnMovementRule();
    }
}
