package com.svi.tictactoe.dao.impl;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.svi.tictactoe.config.ConfigLoader;
import com.svi.tictactoe.dao.RoomGameDAO;
import com.svi.tictactoe.dto.RoomDTO;
import com.svi.tictactoe.utils.DateFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class RoomGameDAOImpl implements RoomGameDAO {
    private final Session session;
    private final PreparedStatement insert;
    private final PreparedStatement selectByRoom;
    private final PreparedStatement selectOne;
    private final PreparedStatement selectRoomIds;

    public RoomGameDAOImpl(Session session) {
        this.session = session;
        String table = ConfigLoader.getInstance().getRoomGamesTable();
        this.insert =
                session.prepare("INSERT INTO " + table + " (room_id, game_date, game_id) VALUES (?, ?, ?)");
        this.selectByRoom =
                session.prepare("SELECT room_id, game_id, game_date FROM " + table + " WHERE room_id = ?");
        this.selectOne =
                session.prepare("SELECT room_id FROM " + table + " WHERE room_id = ? LIMIT 1");
        this.selectRoomIds =
                session.prepare("SELECT DISTINCT room_id FROM " + table);
    }

    @Override
    public void save(String roomId, String gameId, String createdDate) throws IOException {
        try {
            UUID gameUuid = UUID.fromString(gameId);
            for (Row row : session.execute(selectByRoom.bind(roomId))) {
                if (gameUuid.equals(row.getUUID("game_id"))) {
                    return;
                }
            }
            session.execute(insert.bind(roomId, DateFormatter.date(createdDate), gameUuid));
        } catch (RuntimeException e) {
            throw failure("save a room game", e);
        }
    }

    @Override
    public List<RoomDTO> findByRoomId(String roomId) throws IOException {
        try {
            List<RoomDTO> rooms = new ArrayList<>();
            for (Row row : session.execute(selectByRoom.bind(roomId))) {
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
    public boolean exists(String roomId) throws IOException {
        try {
            return session.execute(selectOne.bind(roomId)).one() != null;
        } catch (RuntimeException e) {
            throw failure("check a room", e);
        }
    }

    private IOException failure(String operation, RuntimeException cause) {
        return new IOException("Unable to " + operation + " in Cassandra", cause);
    }
}
