package com.svi.tictactoe.exception;

import com.svi.tictactoe.dto.response.SaveResponseDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<ApplicationException> {
    @Override
    public Response toResponse(ApplicationException exception) {
        Response.Status status;
        if (exception instanceof InvalidRequestException) {
            status = Response.Status.BAD_REQUEST;
        } else if (exception instanceof ResourceNotFoundException) {
            status = Response.Status.NOT_FOUND;
        } else if (exception instanceof LocationOccupiedException) {
            status = Response.Status.CONFLICT;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }

        return Response.status(status)
                .entity(new SaveResponseDTO(exception.getResponseMessage()))
                .build();
    }
}
