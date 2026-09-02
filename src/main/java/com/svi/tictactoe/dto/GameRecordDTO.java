package com.svi.tictactoe.dto;

public class GameRecordDTO {
    private String gameId;
    private String playerId;
    private String playerName;
    private String symbol;
    private String location;
    private String dateSaved;

    public GameRecordDTO() {}
    public GameRecordDTO(String gameId, String playerId, String symbol, String location, String dateSaved) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.symbol = symbol;
        this.location = location;
        this.dateSaved = dateSaved;
    }

    public GameRecordDTO(String gameId, String playerId, String playerName, String symbol, String location, String dateSaved) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.symbol = symbol;
        this.location = location;
        this.dateSaved = dateSaved;
    }

    public String getGameId() { return gameId; }
    public String getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    public String getSymbol() { return symbol; }
    public String getLocation() { return location; }
    public String getDateSaved() { return dateSaved; }

    public void setGameId(String gameId) { this.gameId = gameId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setLocation(String location) { this.location = location; }
    public void setDateSaved(String dateSaved) { this.dateSaved = dateSaved; }

    public static GameRecordDTO fromRecordFormat(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length == 5) {
            // Old format without playerName
            return new GameRecordDTO(parts[0], parts[1], parts[2], parts[3], parts[4]);
        } else if (parts.length == 6) {
            // New format with playerName
            return new GameRecordDTO(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
        } else {
            throw new IllegalArgumentException("Invalid Record format");
        }
    }

    public String toRecordFormat() {
        if (playerName != null && !playerName.isEmpty()) {
            return String.format("%s,%s,%s,%s,%s,%s", gameId, playerId, playerName, symbol, location, dateSaved);
        }
        return String.format("%s,%s,%s,%s,%s", gameId, playerId, symbol, location, dateSaved);
    }
    
}
