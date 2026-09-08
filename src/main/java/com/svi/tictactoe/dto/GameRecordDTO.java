package com.svi.tictactoe.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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

    @NotBlank
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    public String getGameId() { return gameId; }
    @NotBlank
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    public String getPlayerId() { return playerId; }
    @Size(max = 10)
    @Pattern(regexp = "^[^\\r\\n]*$")
    public String getPlayerName() { return playerName; }
    @NotBlank
    @Pattern(regexp = "^[XO]$")
    public String getSymbol() { return symbol; }
    @NotBlank
    @Pattern(regexp = "^[0-8]$")
    public String getLocation() { return location; }
    @NotBlank
    @Pattern(regexp = "^[^,\\r\\n]+$")
    public String getDateSaved() { return dateSaved; }

    public void setGameId(String gameId) { this.gameId = gameId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setLocation(String location) { this.location = location; }
    public void setDateSaved(String dateSaved) { this.dateSaved = dateSaved; }

    public static GameRecordDTO fromRecordFormat(String csvLine) {
        String[] parts = parseCsvLine(csvLine);
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
            return String.format("%s,%s,%s,%s,%s,%s", gameId, playerId,
                    escapeCsvField(playerName), symbol, location, dateSaved);
        }
        return String.format("%s,%s,%s,%s,%s", gameId, playerId, symbol, location, dateSaved);
    }

    private static String escapeCsvField(String value) {
        if (value.indexOf(',') >= 0 || value.indexOf('"') >= 0) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private static String[] parseCsvLine(String line) {
        java.util.List<String> fields = new java.util.ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (character == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (character == ',' && !quoted) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }

        if (quoted) {
            throw new IllegalArgumentException("Unclosed CSV field");
        }
        fields.add(field.toString());
        return fields.toArray(new String[0]);
    }
    
}
