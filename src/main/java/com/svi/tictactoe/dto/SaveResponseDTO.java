package com.svi.tictactoe.dto;

public class SaveResponseDTO {
    private String msg;

    public SaveResponseDTO() {}
    public SaveResponseDTO(String msg) { this.msg = msg; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
}