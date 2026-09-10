package com.svi.tictactoe.exception;

import com.svi.tictactoe.constants.ResponseMessage;

public class InvalidRequestException extends ApplicationException {
    public InvalidRequestException(ResponseMessage responseMessage) {
        super(responseMessage);
    }
}
