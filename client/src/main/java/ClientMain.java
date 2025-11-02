import chess.*;
import client.ChessClient;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ChessClient client;
        if(args.length > 1) {
            System.out.println("Using command-line arguments for server host and port");
            client = new ChessClient(String.format("%s:%s", args[0], args[1]));
        } else {
            client = new ChessClient(8080);
        }
        client.run();
    }
}
