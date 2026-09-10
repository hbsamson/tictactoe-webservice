package com.svi.tictactoe.exception;

import com.svi.tictactoe.dto.response.SaveResponseDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<ApplicationException> {
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class.getName());

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

        if (status.getStatusCode() >= 500) {
            LOGGER.log(Level.SEVERE, "Unhandled application failure: " + exception.getMessage(), exception);
        } else {
            LOGGER.log(Level.WARNING, "Request rejected: type={0}, message={1}",
                    new Object[] { exception.getClass().getSimpleName(), exception.getMessage() });
        }

        return Response.status(status)
                .entity(new SaveResponseDTO(exception.getResponseMessage()))
                .build();
    }
}
