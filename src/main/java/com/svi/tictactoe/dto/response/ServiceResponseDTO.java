package com.svi.tictactoe.dto.response;

public class ServiceResponseDTO<T> {

    private T data;
    private int status;

    public ServiceResponseDTO() {}

    public ServiceResponseDTO(T data, int status) {
        this.data = data;
        this.status = status;
    }

    public T getData() { return data; }
    public int getStatus() { return status; }

    public void setData(T data) { this.data = data; }
    public void setStatus(int status) { this.status = status; }
}