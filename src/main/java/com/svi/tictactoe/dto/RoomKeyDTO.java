package com.svi.tictactoe.dto;

import java.util.List;

public class RoomKeyDTO {
    private String roomKey;
    private List<String> gameIds;

    public RoomKeyDTO() {
    }

    public RoomKeyDTO(String roomKey, List<String> gameIds) {
        this.roomKey = roomKey;
        this.gameIds = gameIds;
    }

    public String getRoomKey() {
        return roomKey;
    }

    public List<String> getGameIds() {
        return gameIds;
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }

    public void setGameIds(List<String> gameIds) {
        this.gameIds = gameIds;
    }

    @Override
    public String toString() {
        return "RoomKeyDTO{" +
                "roomKey='" + roomKey + '\'' +
                ", gameIds=" + gameIds +
                '}';
    }
}
