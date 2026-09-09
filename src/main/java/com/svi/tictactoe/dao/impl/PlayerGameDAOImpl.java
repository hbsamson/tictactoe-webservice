package com.svi.tictactoe.dao.impl;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.svi.tictactoe.config.ConfigLoader;
import com.svi.tictactoe.dao.PlayerGameDAO;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.PlayerGameDTO;
import com.svi.tictactoe.utils.DateFormatter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PlayerGameDAOImpl implements PlayerGameDAO {
    private final Session session;
    private final PreparedStatement insert;
    private final PreparedStatement selectByPlayer;
    private final PreparedStatement selectOne;
    private final PreparedStatement updateProfile;

    public PlayerGameDAOImpl(Session session) {
        this.session = session;
        String table = ConfigLoader.getInstance().getPlayerGamesTable();
        this.insert =
                session.prepare("INSERT INTO " + table
                        + " (player_id, game_date, game_id, player_name, player_avatar) VALUES (?, ?, ?, ?, ?)");
        this.selectByPlayer =
                session.prepare("SELECT game_date, game_id, player_name, player_avatar FROM " + table
                        + " WHERE player_id = ? ORDER BY game_date DESC");
        this.selectOne =
                session.prepare("SELECT player_id FROM " + table + " WHERE player_id = ? LIMIT 1");
        this.updateProfile =
                session.prepare("UPDATE " + table
                        + " SET player_name = ?, player_avatar = ? WHERE player_id = ? AND game_date = ? AND game_id = ?");
    }

    @Override
    public void save(GameRecordDTO record) throws IOException {
        try {
            UUID playerUuid = UUID.fromString(record.getPlayerId());
            UUID gameUuid = UUID.fromString(record.getGameId());
            String playerName = valueOrDefault(record.getPlayerName(), "Player");
            String playerAvatar = valueOrDefault(record.getPlayerAvatar(), "ren");
            for (Row row : session.execute(selectByPlayer.bind(playerUuid))) {
                if (gameUuid.equals(row.getUUID("game_id"))) {
                    session.execute(updateProfile.bind(playerName, playerAvatar, playerUuid,
                            row.getTimestamp("game_date"), gameUuid));
                    return;
                }
            }
            session.execute(insert.bind(playerUuid, DateFormatter.date(record.getDateSaved()),
                    gameUuid, playerName, playerAvatar));
        } catch (RuntimeException e) {
            throw failure("save a player's game", e);
        }
    }

    @Override
    public List<PlayerGameDTO> findByPlayerId(String playerId) throws IOException {
        try {
            List<PlayerGameDTO> games = new ArrayList<>();
            for (Row row : session.execute(selectByPlayer.bind(UUID.fromString(playerId)))) {
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

    private String valueOrDefault(String value, String defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
    }
}
