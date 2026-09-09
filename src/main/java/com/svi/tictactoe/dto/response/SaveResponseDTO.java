package com.svi.tictactoe.dto.response;

import com.svi.tictactoe.constants.ResponseMessage;

public class SaveResponseDTO {
    private String msg;

    public SaveResponseDTO() {}
    public SaveResponseDTO(ResponseMessage message) {
        this.msg = message.getValue();
    }

    public String getMessage() { return msg; }

    public void setMessage(String msg) { this.msg = msg; }
}
