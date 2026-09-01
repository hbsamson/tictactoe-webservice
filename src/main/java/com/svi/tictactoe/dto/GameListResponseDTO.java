package com.svi.tictactoe.dto;

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
     * Inner class representing a single item in the list (e.g., a game ID).
     */
    public static class GameItem {
        private String id;

        public GameItem() {}
        public GameItem(String id) {
            this.id = id;
        }

        public String getId() { return id; }

        public void setId(String id) { this.id = id; }

        @Override
        public String toString() {
            return "GameItem{" + "id='" + id + '\'' + '}'; }
    }

   
}
