package com.svi.tictactoe.dto.response;

import com.svi.tictactoe.dto.GameRecordDTO;

import java.util.List;

public class GameRecordListResponseDTO {
    private List<GameRecordDTO> list;
    private String msg;

    public GameRecordListResponseDTO() {}

    public GameRecordListResponseDTO(List<GameRecordDTO> list, String msg) {
        this.list = list;
        this.msg = msg;
    }

    public List<GameRecordDTO> getList() {
        return list;
    }

    public String getMsg() {
        return msg;
    }

    public void setList(List<GameRecordDTO> list) {
        this.list = list;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "GameRecordListResponseDTO{" +
                "list=" + list +
                ", msg='" + msg + '\'' +
                '}';
    }
}
