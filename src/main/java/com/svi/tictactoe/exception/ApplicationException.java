package com.svi.tictactoe.exception;

import com.svi.tictactoe.constants.ResponseMessage;

public abstract class ApplicationException extends RuntimeException {
    private final ResponseMessage responseMessage;

    protected ApplicationException(ResponseMessage responseMessage) {
        super(responseMessage.getValue());
        this.responseMessage = responseMessage;
    }

    protected ApplicationException(ResponseMessage responseMessage, Throwable cause) {
        super(responseMessage.getValue(), cause);
        this.responseMessage = responseMessage;
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }
}
