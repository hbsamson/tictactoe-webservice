package com.svi.tictactoe.utils;

import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.RoomDTO;
import java.util.ArrayList;
import java.util.List;

public final class RecordFormatUtils {
    private RecordFormatUtils() { }

    public static String gameToCsv(GameRecordDTO record) {
        if (record.getPlayerName() != null && !record.getPlayerName().isEmpty()) {
            return String.format("%s,%s,%s,%s,%s,%s", record.getGameId(), record.getPlayerId(),
                    escapeCsv(record.getPlayerName()), record.getSymbol(), record.getLocation(), record.getDateSaved());
        }
        return String.format("%s,%s,%s,%s,%s", record.getGameId(), record.getPlayerId(),
                record.getSymbol(), record.getLocation(), record.getDateSaved());
    }

    public static GameRecordDTO gameFromCsv(String line) {
        String[] fields = parseCsv(line);
        if (fields.length == 5) return new GameRecordDTO(fields[0], fields[1], fields[2], fields[3], fields[4]);
        if (fields.length == 6) return new GameRecordDTO(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]);
        throw new IllegalArgumentException("Invalid Record format");
    }

    public static RoomDTO roomFromCsv(String roomId, String line) {
        String[] fields = line.split(",", -1);
        if (fields.length != 2) throw new IllegalArgumentException("Invalid Room record format");
        return new RoomDTO(roomId, fields[0], fields[1]);
    }

    private static String escapeCsv(String value) {
        return value.indexOf(',') >= 0 || value.indexOf('"') >= 0
                ? "\"" + value.replace("\"", "\"\"") + "\"" : value;
    }

    private static String[] parseCsv(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (character == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++;
                } else quoted = !quoted;
            } else if (character == ',' && !quoted) {
                fields.add(field.toString());
                field.setLength(0);
            } else field.append(character);
        }
        if (quoted) throw new IllegalArgumentException("Unclosed CSV field");
        fields.add(field.toString());
        return fields.toArray(new String[0]);
    }
}
