package com.svi.tictactoe.exception;

import com.svi.tictactoe.constants.ResponseMessage;

public class PersistenceException extends ApplicationException {
    public PersistenceException(Throwable cause) {
        super(ResponseMessage.SERVER_ERROR, cause);
    }
}
