package com.svi.tictactoe.dto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

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

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]{4,6}$")
    public String getRoomId() { return roomId; }
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    public String getGameId() { return gameId; }
    public String getCreatedDate() { return createdDate; }
    @NotEmpty
    public List<String> getGameIds() { return gameIds; }

    public void setRoomId(String roomId) { this.roomId = roomId; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
    public void setGameIds(List<String> gameIds) { this.gameIds = gameIds; }

}
