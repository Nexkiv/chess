package chess.MovementRule;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public abstract class BaseMovementRule implements MovementRule {

    protected void calculateMoves(ChessBoard board, ChessPosition pos, int rankInc, int fileInc,
                                  Collection<ChessMove> moves, boolean allowDistance) {
        ChessPosition currentPos = new ChessPosition(pos);
        do {
            int newRank = currentPos.getRank() + rankInc;
            int newFile = currentPos.getFile() + fileInc;
            boolean isLegalPosition = (newRank <= board.getDimension() && newRank > 0 && newFile <= board.getDimension() && newFile > 0);
            if (isLegalPosition) {
                ChessPosition newPos = new ChessPosition(newRank, newFile);
                boolean isEmptyPosition = board.getPiece(newPos) == null;
                if (isEmptyPosition) {
                    moves.add(new ChessMove(pos, newPos,null));
                    currentPos = new ChessPosition(newPos);
                } else {
                    boolean canTake = board.getPiece(newPos).getTeamColor() != board.getPiece(pos).getTeamColor();
                    if (canTake) {
                        moves.add(new ChessMove(pos, newPos,null));
                    }
                    break;
                }
            } else {
                break;
            }
        } while (allowDistance);
    }

    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition position);
}

