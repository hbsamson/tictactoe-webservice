package com.svi.tictactoe.dto;

public class GameRecordDTO {
    private String gameid;
    private String playerid;
    private String playername;
    private String symbol;
    private String location;
    private String datesave;

    public GameRecordDTO() {}
    public GameRecordDTO(String gameid, String playerid, String symbol, String location, String datesave) {
        this.gameid = gameid;
        this.playerid = playerid;
        this.symbol = symbol;
        this.location = location;
        this.datesave = datesave;
    }

    public GameRecordDTO(String gameid, String playerid, String playername, String symbol, String location, String datesave) {
        this.gameid = gameid;
        this.playerid = playerid;
        this.playername = playername;
        this.symbol = symbol;
        this.location = location;
        this.datesave = datesave;
    }

    public String getGameid() { return gameid; }
    public String getPlayerid() { return playerid; }
    public String getPlayername() { return playername; }
    public String getSymbol() { return symbol; }
    public String getLocation() { return location; }
    public String getDatesave() { return datesave; }

    public void setGameid(String gameid) { this.gameid = gameid; }
    public void setPlayerid(String playerid) { this.playerid = playerid; }
    public void setPlayername(String playername) { this.playername = playername; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setLocation(String location) { this.location = location; }
    public void setDatesave(String datesave) { this.datesave = datesave; }

    public static GameRecordDTO fromRecordFormat(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length == 5) {
            // Old format without playername
            return new GameRecordDTO(parts[0], parts[1], parts[2], parts[3], parts[4]);
        } else if (parts.length == 6) {
            // New format with playername
            return new GameRecordDTO(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
        } else {
            throw new IllegalArgumentException("Invalid Record format");
        }
    }

    public String toRecordFormat() {
        if (playername != null && !playername.isEmpty()) {
            return String.format("%s,%s,%s,%s,%s,%s", gameid, playerid, playername, symbol, location, datesave);
        }
        return String.format("%s,%s,%s,%s,%s", gameid, playerid, symbol, location, datesave);
    }
}