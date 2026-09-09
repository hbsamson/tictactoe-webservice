package com.svi.tictactoe.dao.impl;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.svi.tictactoe.config.ConfigLoader;
import com.svi.tictactoe.dao.GameMoveDAO;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.utils.DateFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class GameMoveDAOImpl implements GameMoveDAO {
    private final Session session;
    private final PreparedStatement insert;
    private final PreparedStatement selectByGame;
    private final PreparedStatement selectOne;
    private final PreparedStatement selectLastMove;

    public GameMoveDAOImpl(Session session) {
        this.session = session;
        String table = ConfigLoader.getInstance().getGameMovesTable();
        this.insert =
                session.prepare("INSERT INTO " + table + " (game_id, move_number, player_id, player_name, symbol, location, date_saved) VALUES (?, ?, ?, ?, ?, ?, ?)");
        this.selectByGame =
                session.prepare("SELECT game_id, move_number, player_id, player_name, symbol, location, date_saved FROM " + table + " WHERE game_id = ?");
        this.selectOne =
                session.prepare("SELECT game_id FROM " + table + " WHERE game_id = ? LIMIT 1");
        this.selectLastMove =
                session.prepare("SELECT move_number FROM " + table + " WHERE game_id = ? ORDER BY move_number DESC LIMIT 1");
    }

    @Override
    public void save(GameRecordDTO record) throws IOException {
        try {
            UUID gameId = UUID.fromString(record.getGameId());
            Row lastMove = session.execute(selectLastMove.bind(gameId)).one();
            int moveNumber = lastMove == null ? 1 : lastMove.getInt("move_number") + 1;
            session.execute(insert.bind(
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
    public List<GameRecordDTO> findByGameId(String gameId) throws IOException {
        try {
            List<GameRecordDTO> records = new ArrayList<>();
            for (Row row : session.execute(selectByGame.bind(UUID.fromString(gameId)))) {
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
    public String findPlayerName(String gameId) throws IOException {
        for (GameRecordDTO record : findByGameId(gameId)) {
            if (record.getPlayerName() != null && !record.getPlayerName().trim().isEmpty()) {
                return record.getPlayerName().trim();
            }
        }
        return null;
    }

    @Override
    public boolean exists(String gameId) throws IOException {
        try {
            return session.execute(selectOne.bind(UUID.fromString(gameId))).one() != null;
        } catch (RuntimeException e) {
            throw failure("check a game", e);
        }
    }

    private IOException failure(String operation, RuntimeException cause) {
        return new IOException("Unable to " + operation + " in Cassandra", cause);
    }
}
