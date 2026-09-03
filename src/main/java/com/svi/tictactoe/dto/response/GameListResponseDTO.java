package com.svi.tictactoe.dto.response;

import java.util.List;

public class GameListResponseDTO {
    private List<GameItem> list;
    private String msg;

    public GameListResponseDTO() {}

    public GameListResponseDTO(List<GameItem> list, String msg) {
        this.list = list;
        this.msg = msg;
    }

    public List<GameItem> getList() {
        return list;
    }
    public String getMessage() {
        return msg;
    }

    public void setList(List<GameItem> list) {
        this.list = list;
    }
    public void setMessage(String msg) {
        this.msg = msg;
    }

     @Override
    public String toString() {
        return "GameListResponse{" +
                "list=" + list +
                ", msg='" + msg + '\'' +
                '}';
    }

    /**
     * Inner class representing a single item in the list (game ID).
     */
    public static class GameItem {
        private String id;
        private String playerName;

        public GameItem() {}
        public GameItem(String id, String playerName) {
            this.id = id;
            this.playerName = playerName;
        }

        public String getId() { return id; }
        public String getPlayerName() { return playerName; }

        public void setId(String id) { this.id = id; }
        public void setPlayerName(String playerName) { this.playerName = playerName; }

        @Override
        public String toString() {
            return "PlayerListGames{" + "id='" + id +
            ", playerName='" + playerName + '\'' +
            '}';
        }
    }
}
