package chess.MovementRule;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnCaptureMovementRule extends BaseMovementRule {

    protected void capturePiece (ChessBoard board, ChessPosition pos,
                                 Collection<ChessMove> moves) {
        ChessGame.TeamColor pawnColor = board.getPiece(pos).getTeamColor();
        int rankInc = switch (pawnColor) {
            case WHITE -> 1;
            case BLACK -> -1;
            default -> 0;
        };

        int newRank = pos.getRank() + rankInc;
        // To the right
        int newFile = pos.getFile() + 1;
        boolean isLegalPosition = (newRank <= board.getDimension() && newRank > 0 && newFile <= board.getDimension() && newFile > 0);
        if (isLegalPosition) {
            ChessPosition newPos = new ChessPosition(newRank, newFile);
            boolean canTake = (board.getPiece(newPos) == null) || (board.getPiece(newPos).getTeamColor() != board.getPiece(pos).getTeamColor());
            if (canTake) {
                // TODO: Encapsulate promotion potential
                if (newRank == 1 || newRank == 8) {
                    moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(pos, newPos,null));
                }
            }
        }

        // To the left
        newFile = pos.getFile() - 1;
        isLegalPosition = (newRank <= board.getDimension() && newRank > 0 && newFile <= board.getDimension() && newFile > 0);
        if (isLegalPosition) {
            ChessPosition newPos = new ChessPosition(newRank, newFile);
            boolean canTake = (board.getPiece(newPos) == null) || (board.getPiece(newPos).getTeamColor() != board.getPiece(pos).getTeamColor());
            if (canTake) {
                // TODO: Encapsulate promotion potential
                if (newRank == 1 || newRank == 8) {
                    moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(pos, newPos,null));
                }
            }
        }
    }

    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();

        capturePiece(board, position, moves);

        return moves;
    }
}
