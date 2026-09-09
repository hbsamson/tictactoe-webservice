package com.svi.tictactoe.dto;

public class PlayerGameDTO {
    private final String gameId;
    private final String playerName;
    private final String playerAvatar;
    private final String gameDate;

    public PlayerGameDTO(String gameId, String playerName, String playerAvatar, String gameDate) {
        this.gameId = gameId;
        this.playerName = playerName;
        this.playerAvatar = playerAvatar;
        this.gameDate = gameDate;
    }

    public String getGameId() { return gameId; }
    public String getPlayerName() { return playerName; }
    public String getPlayerAvatar() { return playerAvatar; }
    public String getGameDate() { return gameDate; }
}
