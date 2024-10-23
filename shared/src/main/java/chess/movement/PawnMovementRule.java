package chess.movement;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovementRule extends BaseMovementRule {

    protected void advanceForward(ChessBoard board, ChessPosition pos,
                                  Collection<ChessMove> moves) {
        ChessGame.TeamColor pawnColor = board.getPiece(pos).getTeamColor();
        int rankInc = switch (pawnColor) {
            case WHITE -> 1;
            case BLACK -> -1;
            default -> 0;
        };

        int newRank = pos.getRank() + rankInc;
        int currentFile = pos.getFile();
        boolean isLegalPosition = (newRank <= board.getDimension() && newRank > 0);
        if (isLegalPosition) {
            ChessPosition newPos = new ChessPosition(newRank, currentFile);
            boolean isEmptyPosition = (board.getPiece(newPos) == null);
            if (isEmptyPosition) {
                addPawnMoves(pos, moves, newPos);

                if ((pos.getRank() == 2 && pawnColor == ChessGame.TeamColor.WHITE)
                        || (pos.getRank() == 7 && pawnColor == ChessGame.TeamColor.BLACK)) {
                    newRank = newRank + rankInc;
                    currentFile = pos.getFile();
                    newPos = new ChessPosition(newRank, currentFile);
                    isEmptyPosition = (board.getPiece(newPos) == null);
                    if (isEmptyPosition) {
                        moves.add(new ChessMove(pos, newPos,null));
                    }
                }
            }
        }
    }

    private void capturePiece (ChessBoard board, ChessPosition pos,
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
        capturePieceIfLegal(board, pos, newRank, newFile, moves);

        // To the left
        newFile = pos.getFile() - 1;
        capturePieceIfLegal(board, pos, newRank, newFile, moves);
    }

    private void capturePieceIfLegal(ChessBoard board, ChessPosition pos, int newRank, int newFile, Collection<ChessMove> moves) {
        boolean isLegalPosition = (newRank <= board.getDimension() && newRank > 0 && newFile <= board.getDimension() && newFile > 0);
        if (isLegalPosition) {
            ChessPosition newPos = new ChessPosition(newRank, newFile);
            boolean canTake = (board.getPiece(newPos) != null) && (board.getPiece(newPos).getTeamColor() != board.getPiece(pos).getTeamColor());
            if (canTake) {
                addPawnMoves(pos, moves, newPos);
            }
        }
    }

    private void addPawnMoves(ChessPosition pos, Collection<ChessMove> moves, ChessPosition newPos) {
        if (newPos.getRank() == 1 || newPos.getRank() == 8) {
            moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(pos, newPos, ChessPiece.PieceType.KNIGHT));;
        } else {
            moves.add(new ChessMove(pos, newPos,null));
        }
    }

    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();

        advanceForward(board, position, moves);
        capturePiece(board, position, moves);

        return moves;
    }

    public Collection<ChessMove> captureMoves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();

        capturePiece(board, position, moves);

        return moves;
    }
}

