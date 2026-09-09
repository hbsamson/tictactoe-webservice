package com.svi.tictactoe.dto.response;

import javax.ws.rs.core.Response;

public class ServiceResponseDTO<T> {

    private T data;
    private Response.Status status;

    public ServiceResponseDTO() {}

    public ServiceResponseDTO(T data, Response.Status status) {
        this.data = data;
        this.status = status;
    }

    public T getData() { return data; }
    public Response.Status getStatus() { return status; }

    public void setData(T data) { this.data = data; }
    public void setStatus(Response.Status status) { this.status = status; }
}
