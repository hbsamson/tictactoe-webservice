package com.svi.tictactoe.dto;

public class GameRecordDTO {
    private String gameid;
    private String playerid;
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

    public String getGameid() { return gameid; }
    public void setGameid(String gameid) { this.gameid = gameid; }
    public String getPlayerid() { return playerid; }
    public void setPlayerid(String playerid) { this.playerid = playerid; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDatesave() { return datesave; }
    public void setDatesave(String datesave) { this.datesave = datesave; }

    public static GameRecordDTO fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 5) throw new IllegalArgumentException("Invalid CSV format");
        return new GameRecordDTO(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }

    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s", gameid, playerid, symbol, location, datesave);
    }
}