package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter signature of the
 * existing methods.
 */
public class ChessGame {

    ChessBoard board;

    TeamColor teamTurn;

    boolean wqCastle;
    boolean wkCastle;
    boolean bqCastle;
    boolean bkCastle;

    ChessMove prevMove;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
        this.wqCastle = true;
        this.wkCastle = true;
        this.bqCastle = true;
        this.bkCastle = true;
        this.prevMove = null;
    }

    public ChessGame(ChessBoard board, TeamColor team) {
        this.board = new ChessBoard(board);
        this.teamTurn = team;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.board);
        hash = 53 * hash + Objects.hashCode(this.teamTurn);
        hash = 53 * hash + (this.wqCastle ? 1 : 0);
        hash = 53 * hash + (this.wkCastle ? 1 : 0);
        hash = 53 * hash + (this.bqCastle ? 1 : 0);
        hash = 53 * hash + (this.bkCastle ? 1 : 0);
        hash = 53 * hash + Objects.hashCode(this.prevMove);
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
        final ChessGame other = (ChessGame) obj;
        if (this.wqCastle != other.wqCastle) {
            return false;
        }
        if (this.wkCastle != other.wkCastle) {
            return false;
        }
        if (this.bqCastle != other.bqCastle) {
            return false;
        }
        if (this.bkCastle != other.bkCastle) {
            return false;
        }
        if (!Objects.equals(this.board, other.board)) {
            return false;
        }
        if (this.teamTurn != other.teamTurn) {
            return false;
        }
        return Objects.equals(this.prevMove, other.prevMove);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return new HashSet<>();
        }
        TeamColor pieceColor = piece.getTeamColor();
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();
        Collection validMovesForColor = allValidMovesForColor(pieceColor);
        for (ChessMove move : possibleMoves) {
            if (validMovesForColor.contains(move)) {
                validMoves.add(move);
            }
        }
        validMoves.addAll(castlingMoves(validMoves, startPosition, piece, pieceColor));
        ChessMove enPassantMove = enPassantMove(startPosition, piece, pieceColor);
        if(enPassantMove!=null){
            validMoves.add(enPassantMove);
        }
        return validMoves;
    }

    private ChessMove enPassantMove(ChessPosition startPosition, ChessPiece piece, TeamColor pieceColor) {
        if (piece.getPieceType() != ChessPiece.PieceType.PAWN) {
            return null;
        }
        int row = startPosition.getRow();
        int col = startPosition.getColumn();
        ChessPiece opposingPawn = new ChessPiece(getOpposingColor(piece.getTeamColor()), ChessPiece.PieceType.PAWN);
        if (piece.getTeamColor() == TeamColor.WHITE && row == 5 ||
        piece.getTeamColor() == TeamColor.BLACK && row == 4) {
            int forward = piece.getTeamColor() == TeamColor.WHITE ? 1 : -1;
            ChessPosition leftPosition = new ChessPosition(row, col - 1);
            if(canEnPassant(opposingPawn, leftPosition, forward)){
                return new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+forward, leftPosition.getColumn()), null);
            }
            ChessPosition rightPosition = new ChessPosition(row, col + 1);
            if(canEnPassant(opposingPawn, rightPosition, forward)){
                return new ChessMove(startPosition, new ChessPosition(startPosition.getRow()+forward, rightPosition.getColumn()), null);
            }
        }
        return null;
    }

    private boolean canEnPassant(ChessPiece opposingPawn, ChessPosition oppPawnPosition, int forward) {
        return prevMove.getEndPosition().equals(oppPawnPosition)
                && this.board.getPiece(oppPawnPosition).equals(opposingPawn)
                && prevMove.getStartPosition().equals(new ChessPosition(oppPawnPosition.getRow() + forward * 2, oppPawnPosition.getColumn()));
    }

    private Collection<ChessMove> castlingMoves(Collection<ChessMove> validMoves, ChessPosition startPos, ChessPiece piece, TeamColor pieceColor) {
        if (piece.getPieceType() != ChessPiece.PieceType.KING) {
            return new HashSet<>();
        }
        Collection<ChessMove> castlingMoves = new HashSet<>();
        //int kingRow = pieceColor == TeamColor.WHITE ? 1 : 8;
        switch (pieceColor) {
            case WHITE -> {
                if (wkCastle) {
                    addCastlingMove(validMoves, startPos,
                        new ChessPosition(1, 6), new ChessPosition(1, 7), new ChessPosition(1, 8),
                        pieceColor, castlingMoves);
                }
                if (wqCastle) {
                    addCastlingMove(validMoves, startPos,
                    new ChessPosition(1, 4), new ChessPosition(1, 3), new ChessPosition(1, 1),
                    pieceColor, castlingMoves);
                }
            }
            case BLACK -> {
                if (bkCastle) {
                    addCastlingMove(validMoves, startPos,
                    new ChessPosition(8, 6), new ChessPosition(8, 7), new ChessPosition(8, 8),
                    pieceColor, castlingMoves);
                }
                if (bqCastle) {
                    addCastlingMove(validMoves, startPos,
                    new ChessPosition(8, 4), new ChessPosition(8, 3), new ChessPosition(8, 1),
                    pieceColor, castlingMoves);
                }
            }
            default ->
                throw new AssertionError();
        }
        return castlingMoves;
    }

    private void addCastlingMove(Collection<ChessMove> validMoves,
                                 ChessPosition startPos,
                                 ChessPosition toCastlePos,
                                 ChessPosition castlePos,
                                 ChessPosition rookPos,
                                 TeamColor pieceColor,
                                 Collection<ChessMove> castlingMoves) {
        if (validMoves.contains(new ChessMove(startPos, toCastlePos, null)) &&
        validMoves(rookPos).contains(new ChessMove(rookPos, toCastlePos, null))) {
            ChessGame checkGame = new ChessGame(this.board, this.teamTurn);
            ChessMove castlingMove = new ChessMove(startPos, castlePos, null);
            checkGame.forceMovePiece(castlingMove);
            if (!checkGame.isInCheck(pieceColor)) {
                castlingMoves.add(castlingMove);
            }
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("No piece there");
        }
        TeamColor colorOfPiece = piece.getTeamColor();
        if (colorOfPiece != this.teamTurn) {
            throw new InvalidMoveException("Piece moved out of turn");
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Piece can't go there");
        }
        movePiece(move);
        this.prevMove = move;
        switch (teamTurn) {
            case WHITE:
                this.teamTurn = TeamColor.BLACK;
                break;
            case BLACK:
                this.teamTurn = TeamColor.WHITE;
                break;
            default:
                this.teamTurn = TeamColor.WHITE;
        }
    }

    private void movePiece(ChessMove move) {
        ChessPiece piece = this.board.getPiece(move.getStartPosition());
        if (piece == null) {
            return;
        }
        if (isCastlingMove(piece, move)) {
            handleCastlingMove(move);
            noLongerCastle(piece.getTeamColor());
        } else if (isEnPassantMove(piece, move)){
            handleEnPassantMove(move);
        } else {
            forceMovePiece(move);
            checkCastling(piece, move);
        }
    }

    public void forceMovePiece(ChessMove move) {
        ChessPiece piece = this.board.getPiece(move.getStartPosition());
        if (piece == null) {
            return;
        }
        if (move.getPromotionPiece() != null) {
            ChessPiece newPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            this.board.addPiece(move.getEndPosition(), newPiece);
        } else {
            this.board.addPiece(move.getEndPosition(), piece);
        }
        this.board.addPiece(move.getStartPosition(), null);
    }

    private void handleEnPassantMove(ChessMove move) {
        ChessPosition capturedPosition = new ChessPosition(move.getStartPosition().getRow(), move.getEndPosition().getColumn());
        forceMovePiece(move);
        this.board.addPiece(capturedPosition, null);
    }

    private boolean isEnPassantMove(ChessPiece piece, ChessMove move) {
        if (piece.getPieceType() != ChessPiece.PieceType.PAWN) {
            return false;
        }
        return !piece.pieceMoves(this.board, move.getStartPosition()).contains(move);
    }

    private void checkCastling(ChessPiece piece, ChessMove move) {
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            noLongerCastle(piece.getTeamColor());
        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            if (piece.getTeamColor() == TeamColor.WHITE && move.getStartPosition().equals(new ChessPosition(1, 1))) {
                this.wqCastle = false;
            } else if (piece.getTeamColor() == TeamColor.WHITE && move.getStartPosition().equals(new ChessPosition(1, 8))) {
                this.wkCastle = false;
            } else if (piece.getTeamColor() == TeamColor.WHITE && move.getStartPosition().equals(new ChessPosition(8, 1))) {
                this.bqCastle = false;
            } else if (piece.getTeamColor() == TeamColor.WHITE && move.getStartPosition().equals(new ChessPosition(8, 8))) {
                this.bkCastle = false;
            }
        }
    }

    private void handleCastlingMove(ChessMove move) {
        ChessPosition kingEndPosition = move.getEndPosition();
        int castlingRow = kingEndPosition.getRow();
        ChessPosition rookStartPosition;
        ChessPosition rookEndPosition;
        if (kingEndPosition.getColumn() == 3) { //QueenSide
            rookStartPosition = new ChessPosition(castlingRow, 1);
            rookEndPosition = new ChessPosition(castlingRow, 4);
        } else { //KingSide
            rookStartPosition = new ChessPosition(castlingRow, 8);
            rookEndPosition = new ChessPosition(castlingRow, 6);

        }
        ChessMove moveRook = new ChessMove(rookStartPosition, rookEndPosition, null);
        forceMovePiece(move);
        forceMovePiece(moveRook);
    }

    private void noLongerCastle(TeamColor teamColor) {
        switch (teamColor) {
            case WHITE -> {
                this.wkCastle = false;
                this.wqCastle = false;
            }
            case BLACK -> {
                this.bkCastle = false;
                this.bqCastle = false;
            }
            default ->
                throw new AssertionError();
        }
    }

    private boolean isCastlingMove(ChessPiece piece, ChessMove move) {
        if (piece.getPieceType() != ChessPiece.PieceType.KING) {
            return false;
        }
        return !piece.pieceMoves(this.board, move.getStartPosition()).contains(move);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //Loop through all the pieces of the opposing color, get all the moves.
        //Find the king for our given color and see if any of the moves end at that spot.
        TeamColor oppColor = getOpposingColor(teamColor);
        Collection<ChessMove> allOpposingMoves = allMovesForColor(oppColor);
        ChessPosition kingPosition = getKingPosition(teamColor);
        for (ChessMove move : allOpposingMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    private TeamColor getOpposingColor(TeamColor teamColor) {
        return switch (teamColor) {
            case WHITE ->
                TeamColor.BLACK;
            case BLACK ->
                TeamColor.WHITE;
            default ->
                TeamColor.WHITE;
        };
    }

    private Collection<ChessMove> allValidMovesForColor(TeamColor teamColor) {
        Collection<ChessMove> allValidMoves = allMovesForColor(teamColor);
        Collection<ChessMove> movesToRemove = new HashSet<>();
        for (ChessMove move : allValidMoves) {
            //If the move still yields isInCheck, remove the move from the list
            ChessGame checkGame = new ChessGame(this.board, this.teamTurn);
            checkGame.forceMovePiece(move);
            if (checkGame.isInCheck(teamColor)) {
                movesToRemove.add(move);
            }
        }
        for (ChessMove move : movesToRemove) {
            allValidMoves.remove(move);
        }
        return allValidMoves;
    }

    private Collection<ChessMove> allMovesForColor(TeamColor teamColor) {
        Collection<ChessMove> allMoves = new HashSet<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition curChessPosition = new ChessPosition(i, j);
                ChessPiece curChessPiece = this.board.getPiece(curChessPosition);
                if (curChessPiece != null && curChessPiece.getTeamColor() == teamColor) {
                    allMoves.addAll(curChessPiece.pieceMoves(this.board, curChessPosition));
                }
            }
        }
        return allMoves;
    }

    private ChessPosition getKingPosition(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition curChessPosition = new ChessPosition(i, j);
                ChessPiece curChessPiece = this.board.getPiece(curChessPosition);
                if (curChessPiece != null && curChessPiece.getTeamColor() == teamColor && curChessPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    return curChessPosition;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        //Checkmate means that the current team has no possible valid moves.
        Collection<ChessMove> allValidMoves = allValidMovesForColor(teamColor);
        return allValidMoves.isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as
     * having no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        //Stalemate means that the current team has no possible moves.
        Collection<ChessMove> allValidMoves = allValidMovesForColor(teamColor);
        return allValidMoves.isEmpty();
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
