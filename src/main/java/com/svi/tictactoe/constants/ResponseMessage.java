package com.svi.tictactoe.constants;

public enum ResponseMessage {
    RECORD_NOT_SAVED("Record could not be saved"),
    LOCATION_OCCUPIED("Location is already occupied."),
    RECORD_SAVED("Record saved"),
    INVALID_GAME_ID("Invalid gameId format"),
    RECORDS_FOUND("Records found"),
    GAME_NOT_FOUND("Game record not found"),
    INVALID_PLAYER_ID("Invalid playerId format"),
    PLAYER_NOT_FOUND("Player ID not found"),
    PLAYER_GAMES_FOUND("Player games records found"),
    INVALID_ROOM_ID("Invalid roomId format"),
    INVALID_GAME_IDS("Invalid gameIds format"),
    ROOM_GAMES_SAVED("Room games saved"),
    SERVER_ERROR("The server ran into an unexpected exception"),
    INVALID_ROOM_CODE("Invalid room code."),
    ROOM_NOT_FOUND("Room record not found");

    private final String value;

    ResponseMessage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
