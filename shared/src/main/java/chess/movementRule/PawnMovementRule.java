package chess.movementRule;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovementRule extends BaseMovementRule {

    protected void advanceForward(ChessBoard board, ChessPosition pos,
                                  Collection<ChessMove> moves) {
        int rankInc = switch (board.getPiece(pos).getTeamColor()) {
            case WHITE -> 1;
            case BLACK -> -1;
            default -> 0;
        };

        int newRank = pos.getRank() + rankInc;
        int currentFile = pos.getFile();
        boolean isLegalPosition = (newRank <= board.getDimension() && newRank > 0);
        if (isLegalPosition) {
            ChessPosition newPos = new ChessPosition(newRank, currentFile);
            boolean isEmptyPosition = board.getPiece(newPos) == null;
            if (isEmptyPosition) {
                moves.add(new ChessMove(pos, newPos,null));
            }
        }
    }

    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();

        advanceForward(board, position, moves);

        return moves;
    }
}

