package com.svi.tictactoe.utils;

import com.svi.tictactoe.dto.GameRecordDTO;

import java.util.UUID;

public class Validators {
    public static boolean isValidRecord(GameRecordDTO record) {
        if (record == null) {
            return false;
        }
        return record.getGameId() != null && !record.getGameId().trim().isEmpty() && isValidUUID(record.getGameId()) &&
                record.getPlayerId() != null && !record.getPlayerId().trim().isEmpty() && isValidUUID(record.getPlayerId()) &&
                record.getSymbol() != null && ("X".equals(record.getSymbol().trim()) || "O".equals(record.getSymbol().trim())) &&
                record.getLocation() != null && record.getLocation().matches("[0-9]") &&
                record.getDateSaved() != null && !record.getDateSaved().trim().isEmpty();
    }

    public static boolean isValidUUID(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
