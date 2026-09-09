package com.svi.tictactoe.dao.impl;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.svi.tictactoe.config.ConfigLoader;
import com.svi.tictactoe.dao.PlayerGameDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.UUID;

public final class PlayerGameDAOImpl implements PlayerGameDAO {
    private final Session session;
    private final PreparedStatement insert;
    private final PreparedStatement selectByPlayer;
    private final PreparedStatement selectOne;

    public PlayerGameDAOImpl(Session session) {
        this.session = session;
        String table = ConfigLoader.getInstance().getPlayerGamesTable();
        this.insert =
                session.prepare("INSERT INTO " + table + " (player_id, game_date, game_id) VALUES (?, ?, ?)");
        this.selectByPlayer =
                session.prepare("SELECT game_id FROM " + table + " WHERE player_id = ?");
        this.selectOne =
                session.prepare("SELECT player_id FROM " + table + " WHERE player_id = ? LIMIT 1");
    }

    @Override
    public void save(String playerId, String gameId) throws IOException {
        try {
            UUID playerUuid = UUID.fromString(playerId);
            UUID gameUuid = UUID.fromString(gameId);
            for (Row row : session.execute(selectByPlayer.bind(playerUuid))) {
                if (gameUuid.equals(row.getUUID("game_id"))) {
                    return;
                }
            }
            session.execute(insert.bind(playerUuid, new Date(), gameUuid));
        } catch (RuntimeException e) {
            throw failure("save a player's game", e);
        }
    }

    @Override
    public List<String> findByPlayerId(String playerId) throws IOException {
        try {
            List<String> gameIds = new ArrayList<>();
            for (Row row : session.execute(selectByPlayer.bind(UUID.fromString(playerId)))) {
                gameIds.add(row.getUUID("game_id").toString());
            }
            return gameIds;
        } catch (RuntimeException e) {
            throw failure("read a player's games", e);
        }
    }

    @Override
    public boolean exists(String playerId) throws IOException {
        try {
            return session.execute(selectOne.bind(UUID.fromString(playerId))).one() != null;
        } catch (RuntimeException e) {
            throw failure("check a player", e);
        }
    }

    private IOException failure(String operation, RuntimeException cause) {
        return new IOException("Unable to " + operation + " in Cassandra", cause);
    }
}
