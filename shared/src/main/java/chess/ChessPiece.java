package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter signature of the
 * existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.pieceColor);
        hash = 79 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChessPiece other = (ChessPiece) obj;
        if (this.pieceColor != other.pieceColor) {
            return false;
        }
        return this.type == other.type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChessPiece{");
        sb.append("pieceColor=").append(pieceColor);
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to Does not take into
     * account moves that are illegal due to leaving the king in danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new HashSet<>();
        switch (board.getPiece(myPosition).getPieceType()) {
            case PAWN -> {
                PawnMovesCalculator pawnMovesCalculator = new PawnMovesCalculator(board.getPiece(myPosition).getTeamColor());
                possibleMoves.addAll(pawnMovesCalculator.pieceMoves(board, myPosition));
            }
            case BISHOP ->
                possibleMoves.addAll(new BishopMovesCalculator().pieceMoves(board, myPosition));
            case ROOK ->
                possibleMoves.addAll(new RookMovesCalculator().pieceMoves(board, myPosition));
            case QUEEN ->
                possibleMoves.addAll(new QueenMovesCalculator().pieceMoves(board, myPosition));
            case KNIGHT ->
                possibleMoves.addAll(new KnightMovesCalculator().pieceMoves(board, myPosition));
            case KING ->
                possibleMoves.addAll(new KingMovesCalculator().pieceMoves(board, myPosition));
            default ->
                throw new AssertionError();
        }
        return possibleMoves;
    }
}
