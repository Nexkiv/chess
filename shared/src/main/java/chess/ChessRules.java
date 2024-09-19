package chess;


import chess.movementRule.BishopMovementRule;
import chess.movementRule.MovementRule;
import chess.ChessPiece.PieceType;
import java.util.HashMap;

public class ChessRules {
    /*
    static private final HashMap<PieceType, MovementRule> rules = new HashMap<>();

    static {
        rules.put(KING, new KingMovementRule());
        rules.put(QUEEN, new QueenMovementRule());
        rules.put(KNIGHT, new KnightMovementRule());
        rules.put(BISHOP, new BishopMovementRule());
        rules.put(ROOK, new RookMovementRule());
        rules.put(PAWN, new PawnMovementRule());
    }

    static public MovementRule pieceRule(PieceType type) {
        return rules.get(type);
    }
    */

    /**
     * A HashMap containing all the movement rules based on piece.
     */
    static private final HashMap<PieceType, MovementRule> rules = new HashMap<>();

    static {
//      rules.put(KING, new KingMovementRule());
//      rules.put(QUEEN, new QueenMovementRule());
//      rules.put(KNIGHT, new KnightMovementRule());
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
