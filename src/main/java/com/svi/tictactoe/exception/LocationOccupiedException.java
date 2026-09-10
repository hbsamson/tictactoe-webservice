package com.svi.tictactoe.exception;

import com.svi.tictactoe.constants.ResponseMessage;

public class LocationOccupiedException extends ApplicationException {
    public LocationOccupiedException() {
        super(ResponseMessage.LOCATION_OCCUPIED);
    }
}
