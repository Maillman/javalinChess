package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public static GameData updateGameInGameData(GameData oldGameData, ChessGame updatedGame) {
        return new GameData(oldGameData.gameID, oldGameData.whiteUsername(), oldGameData.blackUsername(), oldGameData.gameName(), updatedGame);
    }
}
