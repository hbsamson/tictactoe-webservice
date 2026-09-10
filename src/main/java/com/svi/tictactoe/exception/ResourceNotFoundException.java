package com.svi.tictactoe.exception;

import com.svi.tictactoe.constants.ResponseMessage;

public class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(ResponseMessage responseMessage) {
        super(responseMessage);
    }
}
