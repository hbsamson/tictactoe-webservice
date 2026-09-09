package com.svi.tictactoe.dto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class GameRecordDTO {
    private String gameId;
    private String playerId;
    private String playerName;
    private String playerAvatar;
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

    @Size(max = 10)
    @Pattern(regexp = "^[A-Za-z]*$")
    public String getPlayerAvatar() { return playerAvatar; }

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
    public void setPlayerAvatar(String playerAvatar) { this.playerAvatar = playerAvatar; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setLocation(String location) { this.location = location; }
    public void setDateSaved(String dateSaved) { this.dateSaved = dateSaved; }

}
