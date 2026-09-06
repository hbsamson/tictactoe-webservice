package com.svi.tictactoe.utils;

import com.svi.tictactoe.dto.GameRecordDTO;

import java.util.UUID;
import java.util.regex.Pattern;

public final class Validators {
    private static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    );
    private static final Pattern BOARD_LOCATION_PATTERN = Pattern.compile("^[0-8]$");

    private Validators() {
    }

    public static boolean isValidRecord(GameRecordDTO record) {
        if (record == null) {
            return false;
        }
        return isValidUUID(record.getGameId()) &&
                isValidUUID(record.getPlayerId()) &&
                isValidSymbol(record.getSymbol()) &&
                isValidBoardLocation(record.getLocation()) &&
                isSafeRequiredText(record.getDateSaved()) &&
                isSafeOptionalText(record.getPlayerName());
    }

    public static boolean isValidUUID(String value) {
        if (value == null || !UUID_PATTERN.matcher(value).matches()) {
            return false;
        }

        try {
            UUID parsed = UUID.fromString(value);
            // Guard against JDKs that accept non-canonical UUID strings.
            if (!parsed.toString().equalsIgnoreCase(value)) {
                return false;
            }
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    private static boolean isValidSymbol(String value) {
        return "X".equals(value) || "O".equals(value);
    }

    private static boolean isValidBoardLocation(String value) {
        return value != null && BOARD_LOCATION_PATTERN.matcher(value).matches();
    }

    private static boolean isSafeRequiredText(String value) {
        return value != null && !value.trim().isEmpty() && !containsRecordDelimiter(value);
    }

    private static boolean isSafeOptionalText(String value) {
        return value == null || (value.length() <= 100 && !containsRecordDelimiter(value));
    }

    private static boolean containsRecordDelimiter(String value) {
        return value.indexOf(',') >= 0 || value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0;
    }
}
