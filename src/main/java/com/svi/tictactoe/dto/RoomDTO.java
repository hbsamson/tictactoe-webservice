package com.svi.tictactoe.dto;

public class RoomDTO {
    private String roomCode;
    private String gameId;
    private String createdDate;

    public RoomDTO() {}

    public RoomDTO(String roomCode, String gameId, String createdDate) {
        this.roomCode = roomCode;
        this.gameId = gameId;
        this.createdDate = createdDate;
    }

    public String getRoomCode() { return roomCode; }
    public String getGameId() { return gameId; }
    public String getCreatedDate() { return createdDate; }

    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public String toRecordFormat() {
        return String.format("%s,%s", gameId, createdDate);
    }

    public static RoomDTO fromRecordFormat(String roomCode, String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid Room record format");
        }
        return new RoomDTO(roomCode, parts[0], parts[1]);
    }

    @Override
    public String toString() {
        return "RoomDTO{" +
                "roomCode='" + roomCode + '\'' +
                ", gameId='" + gameId + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
