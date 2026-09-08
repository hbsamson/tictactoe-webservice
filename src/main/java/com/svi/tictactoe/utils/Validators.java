package com.svi.tictactoe.utils;

import java.util.UUID;
import java.util.regex.Pattern;

public final class Validators {
    private static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    );
    private static final Pattern ROOM_CODE_PATTERN = Pattern.compile("^[A-Za-z0-9]{4,6}$");

    private Validators() {
    }

    public static boolean isValidUUID(String value) {
        // Retained for path parameters and internal room game IDs, which do not
        // pass through DTO bean validation at the JAX-RS request boundary.
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

    public static boolean isValidRoomCode(String value) {
        return value != null && ROOM_CODE_PATTERN.matcher(value).matches();
    }

}
