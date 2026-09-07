package com.svi.tictactoe.dto;

import java.util.List;

public class RoomDTO {
    private String roomId;
    private String gameId;
    private List<String> gameIds;
    private String createdDate;

    public RoomDTO() {}

    public RoomDTO(String roomId, String gameId, String createdDate) {
        this.roomId = roomId;
        this.gameId = gameId;
        this.createdDate = createdDate;
    }

    public String getRoomId() { return roomId; }
    public String getGameId() { return gameId; }
    public String getCreatedDate() { return createdDate; }
    public List<String> getGameIds() { return gameIds; }

    public void setRoomId(String roomId) { this.roomId = roomId; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
    public void setGameIds(List<String> gameIds) { this.gameIds = gameIds; }

    public String toRecordFormat() {
        return String.format("%s,%s", gameId, createdDate);
    }

    public static RoomDTO fromRecordFormat(String roomId, String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid Room record format");
        }
        return new RoomDTO(roomId, parts[0], parts[1]);
    }

    @Override
    public String toString() {
        return "RoomDTO{" +
                "roomId='" + roomId + '\'' +
                ", gameId='" + gameId + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
