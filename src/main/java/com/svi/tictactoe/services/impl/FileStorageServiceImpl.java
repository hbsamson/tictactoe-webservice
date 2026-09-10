package com.svi.tictactoe.services.impl;

import com.svi.tictactoe.config.Config;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.RoomDTO;
import com.svi.tictactoe.services.FileStorageService;
import com.svi.tictactoe.utils.RecordFormatUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Flat-file storage implementation recovered from the pre-Cassandra version. */
public final class FileStorageServiceImpl implements FileStorageService {
    private static final Logger LOGGER = Logger.getLogger(FileStorageServiceImpl.class.getName());
    private static final String PLAYER_ID_SUBDIRECTORY = Config.get(Config.Keys.PLAYER_DIR.value());
    private static final String GAME_ID_SUBDIRECTORY = Config.get(Config.Keys.GAME_DIR.value());
    private static final String ROOM_ID_SUBDIRECTORY = Config.get(Config.Keys.ROOM_DIR.value());

    @Override
    public Path getRecordsDirectory() throws IOException {
        Path recordsPath = Config.getPath(Config.Keys.RECORDS_DIR.value()).toAbsolutePath();
        Files.createDirectories(recordsPath);
        return recordsPath;
    }

    @Override
    public Path getPlayerIdDirectory() throws IOException {
        return createSubdirectory(PLAYER_ID_SUBDIRECTORY);
    }

    @Override
    public Path getGameIdDirectory() throws IOException {
        return createSubdirectory(GAME_ID_SUBDIRECTORY);
    }

    @Override
    public Path getRoomIdDirectory() throws IOException {
        return createSubdirectory(ROOM_ID_SUBDIRECTORY);
    }

    @Override
    public void appendMoveToGame(GameRecordDTO gameRecord) throws IOException {
        Path gameFile = recordFile(getGameIdDirectory(), gameRecord.getGameId());
        appendLine(gameFile, RecordFormatUtils.gameToCsv(gameRecord));
    }

    @Override
    public void appendGameToPlayer(String playerId, String gameId) throws IOException {
        Path playerFile = recordFile(getPlayerIdDirectory(), playerId);
        Set<String> existingGames = new HashSet<>(readNonBlankLines(playerFile));
        if (!existingGames.contains(gameId)) {
            appendLine(playerFile, gameId);
        }
    }

    @Override
    public List<String> readPlayerGames(String playerId) throws IOException {
        return readNonBlankLines(recordFile(getPlayerIdDirectory(), playerId));
    }

    @Override
    public String readPlayerName(String gameId) throws IOException {
        for (GameRecordDTO record : readGameMoves(gameId)) {
            if (record.getPlayerName() != null && !record.getPlayerName().trim().isEmpty()) {
                return record.getPlayerName().trim();
            }
        }
        return null;
    }

    @Override
    public List<String> readGames(String gameId) throws IOException {
        return readNonBlankLines(recordFile(getGameIdDirectory(), gameId));
    }

    @Override
    public List<GameRecordDTO> readGameMoves(String gameId) throws IOException {
        List<GameRecordDTO> moves = new ArrayList<>();
        for (String line : readGames(gameId)) {
            try {
                moves.add(RecordFormatUtils.gameFromCsv(line));
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Skipping malformed game record for gameId={0}: {1}",
                        new Object[] { gameId, e.getMessage() });
            }
        }
        return moves;
    }

    @Override
    public boolean playerExists(String playerId) throws IOException {
        return hasContent(recordFile(getPlayerIdDirectory(), playerId));
    }

    @Override
    public boolean gameExists(String gameId) throws IOException {
        return hasContent(recordFile(getGameIdDirectory(), gameId));
    }

    @Override
    public void appendGameToRoom(String roomId, String gameId, String createdDate) throws IOException {
        Path roomFile = recordFile(getRoomIdDirectory(), roomId);
        Set<String> existingGames = new HashSet<>();
        for (String line : readNonBlankLines(roomFile)) {
            String[] fields = line.split(",", 2);
            if (fields.length > 0) {
                existingGames.add(fields[0].trim());
            }
        }

        if (!existingGames.contains(gameId)) {
            appendLine(roomFile, gameId + "," + createdDate);
        }
    }

    @Override
    public List<RoomDTO> readRoomGames(String roomId) throws IOException {
        List<RoomDTO> roomGames = new ArrayList<>();
        Path roomFile = recordFile(getRoomIdDirectory(), roomId);
        for (String line : readNonBlankLines(roomFile)) {
            try {
                roomGames.add(RecordFormatUtils.roomFromCsv(roomId, line));
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Skipping malformed room record for roomId={0}: {1}",
                        new Object[] { roomId, e.getMessage() });
            }
        }
        return roomGames;
    }

    @Override
    public List<String> readRoomIds() throws IOException {
        List<String> roomIds = new ArrayList<>();
        try (DirectoryStream<Path> files = Files.newDirectoryStream(getRoomIdDirectory(), "*.txt")) {
            for (Path file : files) {
                if (Files.isRegularFile(file) && Files.size(file) > 0) {
                    String fileName = file.getFileName().toString();
                    roomIds.add(fileName.substring(0, fileName.length() - ".txt".length()));
                }
            }
        }
        Collections.sort(roomIds);
        return roomIds;
    }

    @Override
    public boolean roomExists(String roomId) throws IOException {
        return hasContent(recordFile(getRoomIdDirectory(), roomId));
    }

    private Path createSubdirectory(String name) throws IOException {
        Path directory = getRecordsDirectory().resolve(name);
        Files.createDirectories(directory);
        return directory;
    }

    private Path recordFile(Path directory, String id) {
        return directory.resolve(id + ".txt");
    }

    private List<String> readNonBlankLines(Path file) throws IOException {
        if (!Files.exists(file)) {
            return Collections.emptyList();
        }

        List<String> records = new ArrayList<>();
        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            if (line != null && !line.trim().isEmpty()) {
                records.add(line.trim());
            }
        }
        return records;
    }

    private void appendLine(Path file, String line) throws IOException {
        Files.write(file, (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private boolean hasContent(Path file) throws IOException {
        return Files.exists(file) && Files.size(file) > 0;
    }
}
