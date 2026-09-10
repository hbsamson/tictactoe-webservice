package com.svi.tictactoe.repository;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.svi.tictactoe.config.ConfigLoader;
import com.svi.tictactoe.connection.CassandraConnection;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.PlayerGameDTO;
import com.svi.tictactoe.dto.RoomDTO;
import com.svi.tictactoe.utils.DateFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/** Cassandra-backed repository for moves, player history, and room history. */
public final class CassandraGameRepository implements GameRepository {
    private final Session session;

    private final PreparedStatement insertMove;
    private final PreparedStatement selectMovesByGame;
    private final PreparedStatement selectGame;
    private final PreparedStatement selectLastMove;

    private final PreparedStatement insertPlayerGame;
    private final PreparedStatement selectGamesByPlayer;
    private final PreparedStatement selectPlayer;

    private final PreparedStatement insertRoomGame;
    private final PreparedStatement selectGamesByRoom;
    private final PreparedStatement selectRoom;
    private final PreparedStatement selectRoomIds;

    public CassandraGameRepository() {
        this(CassandraConnection.getInstance().getSession());
    }

    public CassandraGameRepository(Session session) {
        this.session = session;

        ConfigLoader config = ConfigLoader.getInstance();
        String movesTable = config.getGameMovesTable();
        String playerGamesTable = config.getPlayerGamesTable();
        String roomGamesTable = config.getRoomGamesTable();

        // moves table
        insertMove = session.prepare("INSERT INTO " + movesTable
                + " (game_id, move_number, player_id, player_name, symbol, location, date_saved)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)");
        selectMovesByGame = session.prepare("SELECT game_id, move_number, player_id, player_name,"
                + " symbol, location, date_saved FROM " + movesTable + " WHERE game_id = ?");
        selectGame = session.prepare("SELECT game_id FROM " + movesTable + " WHERE game_id = ? LIMIT 1");
        selectLastMove = session.prepare("SELECT move_number FROM " + movesTable
                + " WHERE game_id = ? ORDER BY move_number DESC LIMIT 1");

        // games table
        insertPlayerGame = session.prepare("INSERT INTO " + playerGamesTable
                + " (player_id, game_date, game_id, player_name, player_avatar) VALUES (?, ?, ?, ?, ?)");
        selectGamesByPlayer = session.prepare("SELECT game_date, game_id, player_name, player_avatar FROM "
                + playerGamesTable + " WHERE player_id = ? ORDER BY game_date DESC");
        selectPlayer = session.prepare("SELECT player_id FROM " + playerGamesTable
                + " WHERE player_id = ? LIMIT 1");

        // rooms table
        insertRoomGame = session.prepare("INSERT INTO " + roomGamesTable
                + " (room_id, game_date, game_id) VALUES (?, ?, ?)");
        selectGamesByRoom = session.prepare("SELECT room_id, game_id, game_date FROM "
                + roomGamesTable + " WHERE room_id = ?");
        selectRoom = session.prepare("SELECT room_id FROM " + roomGamesTable + " WHERE room_id = ? LIMIT 1");
        selectRoomIds = session.prepare("SELECT DISTINCT room_id FROM " + roomGamesTable);
    }

    @Override
    public void saveMove(GameRecordDTO record) throws IOException {
        try {
            UUID gameId = UUID.fromString(record.getGameId());
            Row lastMove = session.execute(selectLastMove.bind(gameId)).one();
            int moveNumber = lastMove == null ? 1 : lastMove.getInt("move_number") + 1;
            session.execute(insertMove.bind(
                    gameId,
                    moveNumber,
                    UUID.fromString(record.getPlayerId()),
                    record.getPlayerName(),
                    record.getSymbol(),
                    Integer.parseInt(record.getLocation()),
                    DateFormatter.date(record.getDateSaved())));
        } catch (RuntimeException e) {
            throw failure("save a game move", e);
        }
    }

    @Override
    public void savePlayerGame(GameRecordDTO record) throws IOException {
        try {
            session.execute(insertPlayerGame.bind(
                    UUID.fromString(record.getPlayerId()),
                    DateFormatter.date(record.getDateSaved()),
                    UUID.fromString(record.getGameId()),
                    valueOrDefault(record.getPlayerName(), "Player"),
                    valueOrDefault(record.getPlayerAvatar(), "ren")));
        } catch (RuntimeException e) {
            throw failure("save a player's game", e);
        }
    }

    @Override
    public void saveRoomGame(String roomId, String gameId, String createdDate) throws IOException {
        try {
            UUID gameUuid = UUID.fromString(gameId);
            for (Row row : session.execute(selectGamesByRoom.bind(roomId))) {
                if (gameUuid.equals(row.getUUID("game_id"))) {
                    return;
                }
            }
            session.execute(insertRoomGame.bind(roomId, DateFormatter.date(createdDate), gameUuid));
        } catch (RuntimeException e) {
            throw failure("save a room game", e);
        }
    }

    @Override
    public List<GameRecordDTO> findMovesByGameId(String gameId) throws IOException {
        try {
            List<GameRecordDTO> records = new ArrayList<>();
            for (Row row : session.execute(selectMovesByGame.bind(UUID.fromString(gameId)))) {
                records.add(new GameRecordDTO(
                        row.getUUID("game_id").toString(),
                        row.getUUID("player_id").toString(),
                        row.getString("player_name"),
                        row.getString("symbol"),
                        Integer.toString(row.getInt("location")),
                        DateFormatter.instant(row.getTimestamp("date_saved"))));
            }
            return records;
        } catch (RuntimeException e) {
            throw failure("read game moves", e);
        }
    }

    @Override
    public List<PlayerGameDTO> findGamesByPlayerId(String playerId) throws IOException {
        try {
            List<PlayerGameDTO> games = new ArrayList<>();
            for (Row row : session.execute(selectGamesByPlayer.bind(UUID.fromString(playerId)))) {
                games.add(new PlayerGameDTO(
                        row.getUUID("game_id").toString(),
                        valueOrDefault(row.getString("player_name"), "Player"),
                        valueOrDefault(row.getString("player_avatar"), "ren"),
                        DateFormatter.instant(row.getTimestamp("game_date"))));
            }
            return games;
        } catch (RuntimeException e) {
            throw failure("read a player's games", e);
        }
    }

    @Override
    public String findPlayerNameByGameId(String gameId) throws IOException {
        for (GameRecordDTO record : findMovesByGameId(gameId)) {
            if (record.getPlayerName() != null && !record.getPlayerName().trim().isEmpty()) {
                return record.getPlayerName().trim();
            }
        }
        return null;
    }

    @Override
    public List<RoomDTO> findGamesByRoomId(String roomId) throws IOException {
        try {
            List<RoomDTO> rooms = new ArrayList<>();
            for (Row row : session.execute(selectGamesByRoom.bind(roomId))) {
                rooms.add(new RoomDTO(
                        row.getString("room_id"),
                        row.getUUID("game_id").toString(),
                        DateFormatter.instant(row.getTimestamp("game_date"))));
            }
            return rooms;
        } catch (RuntimeException e) {
            throw failure("read room games", e);
        }
    }

    @Override
    public List<String> findRoomIds() throws IOException {
        try {
            List<String> roomIds = new ArrayList<>();
            for (Row row : session.execute(selectRoomIds.bind())) {
                roomIds.add(row.getString("room_id"));
            }
            Collections.sort(roomIds);
            return roomIds;
        } catch (RuntimeException e) {
            throw failure("read room IDs", e);
        }
    }

    @Override
    public boolean playerExists(String playerId) throws IOException {
        try {
            return session.execute(selectPlayer.bind(UUID.fromString(playerId))).one() != null;
        } catch (RuntimeException e) {
            throw failure("check a player", e);
        }
    }

    @Override
    public boolean gameExists(String gameId) throws IOException {
        try {
            return session.execute(selectGame.bind(UUID.fromString(gameId))).one() != null;
        } catch (RuntimeException e) {
            throw failure("check a game", e);
        }
    }

    @Override
    public boolean roomExists(String roomId) throws IOException {
        try {
            return session.execute(selectRoom.bind(roomId)).one() != null;
        } catch (RuntimeException e) {
            throw failure("check a room", e);
        }
    }

    private IOException failure(String operation, RuntimeException cause) {
        return new IOException("Unable to " + operation + " in Cassandra", cause);
    }

    private String valueOrDefault(String value, String defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
    }
}
