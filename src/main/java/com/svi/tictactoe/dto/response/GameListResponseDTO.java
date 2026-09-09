package com.svi.tictactoe.dto.response;

import com.svi.tictactoe.constants.ResponseMessage;
import java.util.List;

public class GameListResponseDTO {
    private List<GameItem> list;
    private String msg;

    public GameListResponseDTO() {}

    public GameListResponseDTO(List<GameItem> list, ResponseMessage message) {
        this.list = list;
        this.msg = message.getValue();
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
        private String playerAvatar;
        private String gameDate;

        public GameItem() {}
        public GameItem(String id, String playerName) {
            this.id = id;
            this.playerName = playerName;
        }

        public GameItem(String id, String playerName, String playerAvatar, String gameDate) {
            this.id = id;
            this.playerName = playerName;
            this.playerAvatar = playerAvatar;
            this.gameDate = gameDate;
        }

        public String getId() { return id; }
        public String getPlayerName() { return playerName; }
        public String getPlayerAvatar() { return playerAvatar; }
        public String getGameDate() { return gameDate; }

        public void setId(String id) { this.id = id; }
        public void setPlayerName(String playerName) { this.playerName = playerName; }
        public void setPlayerAvatar(String playerAvatar) { this.playerAvatar = playerAvatar; }
        public void setGameDate(String gameDate) { this.gameDate = gameDate; }

        @Override
        public String toString() {
            return "PlayerListGames{" + "id='" + id +
            ", playerName='" + playerName + '\'' +
            '}';
        }
    }
}
