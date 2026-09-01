package com.svi.tictactoe.dto;

public class SaveResponseDTO {
    private String msg;

    public SaveResponseDTO() {}
    public SaveResponseDTO(String msg) {
        this.msg = msg;
    }

    public String getMessage() { return msg; }

    public void setMessage(String msg) { this.msg = msg; }
}