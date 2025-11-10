package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;

public class ChessBoardUI {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
//    private static final int LINE_WIDTH_IN_CHARS = 1;
//    private static final String EMPTY = "   ";
    private static final String[] LETTERS = new String[]{"a","b","c","d","e","f","g","h"};

    public static void main(String[] args) {
        drawChessBoard(System.out, new ChessGame(), GameplayUI.GameplayState.WHITE, null);
        drawChessBoard(System.out, new ChessGame(), GameplayUI.GameplayState.BLACK, null);
        drawChessBoard(System.out, new ChessGame(), GameplayUI.GameplayState.OBSERVER, null);
    }

    public static void drawChessBoard(PrintStream out, ChessGame theGame, GameplayUI.GameplayState perspective, ChessPosition position) {
        //TODO: Do something w/ position in the future!
        ChessBoard board = theGame.getBoard();
        for(int row = 0; row < BOARD_SIZE_IN_SQUARES; row++) {
            if(row == 0) {
                printColumnLetters(out, perspective);
            }
            for(int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
                if(col == 0){
                    printRowNumber(out, perspective, row);
                }
                if((row + col) % 2 == 1){
                    out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                } else {
                    out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                }
                out.print(' ');
                ChessPiece curPiece;
                if(perspective != GameplayUI.GameplayState.BLACK) {
                    curPiece = board.getPiece(new ChessPosition(9 - (row + 1), col + 1));
                } else {
                    curPiece = board.getPiece(new ChessPosition(row + 1, 9 - (col + 1)));
                }
                out.print(getString(curPiece));
                out.print(' ');
                if(col == 7){
                    out.print(EscapeSequences.RESET_BG_COLOR);
                    printRowNumber(out, perspective, row);
                }
            }
            out.print("\n");
            if(row == 7) {
                printColumnLetters(out, perspective);
            }
        }
    }

    private static void printRowNumber(PrintStream out, GameplayUI.GameplayState perspective, int row) {
        if (perspective != GameplayUI.GameplayState.BLACK) {
            out.print(" " + (9 - (row + 1)) + " ");
        } else {
            out.print(" " + (row + 1) + " ");
        }
    }

    private static void printColumnLetters(PrintStream out, GameplayUI.GameplayState perspective) {
        for(int j = 0; j < BOARD_SIZE_IN_SQUARES; j++) {
            if(j == 0) {
                out.print("   ");
            }
            out.print(" " + LETTERS[perspective != GameplayUI.GameplayState.BLACK ? j : 7 - j] + " ");
            if(j == 7) {
                out.print("   ");
            }
        }
        out.print("\n");
    }

    private static String getString(ChessPiece curPiece) {
        String printPiece = " ";
        if(curPiece !=null){
            printPiece = switch (curPiece.getPieceType()) {
                case QUEEN -> "Q";
                case BISHOP -> "B";
                case KNIGHT -> "N";
                case ROOK -> "R";
                case KING -> "K";
                case PAWN -> "P";
            };
            if(curPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                printPiece = printPiece.toLowerCase();
            }

        }
        return printPiece;
    }
}
