package com.svi.tictactoe.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public final class DateFormatter {
    private static final DateTimeFormatter LOCAL_DATE_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DateFormatter() {}

    public static Date date(String value) {
        try {
            return Date.from(Instant.parse(value));
        } catch (DateTimeParseException ignored) {
            LocalDateTime local = LocalDateTime.parse(value, LOCAL_DATE_TIME);
            return Date.from(local.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    public static String instant(Date value) {
        return value == null ? null : value.toInstant().toString();
    }
}
