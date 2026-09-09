package com.svi.tictactoe.config;

/**
 * Typed Cassandra settings backed by the application's central configuration.
 */
public final class ConfigLoader {

    private ConfigLoader() {}

    private static class Holder {
        private static final ConfigLoader INSTANCE = new ConfigLoader();
    }

    public static ConfigLoader getInstance() {
        return Holder.INSTANCE;
    }

    public String getCassandraIp() {
        return getRequired(Config.Keys.CASSANDRA_IP);
    }

    public int getCassandraPort() {
        String value = getRequired(Config.Keys.CASSANDRA_PORT);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("CASSANDRA_PORT must be a number: " + value, e);
        }
    }

    public String getCassandraKeyspace() {
        return getRequired(Config.Keys.CASSANDRA_KEYSPACE);
    }

    public String getGameMovesTable() {
        return getIdentifier(Config.Keys.CASSANDRA_GAME_MOVES_TABLE);
    }

    public String getPlayerGamesTable() {
        return getIdentifier(Config.Keys.CASSANDRA_PLAYER_GAMES_TABLE);
    }

    public String getRoomGamesTable() {
        return getIdentifier(Config.Keys.CASSANDRA_ROOM_GAMES_TABLE);
    }

    private String getIdentifier(Config.Keys key) {
        String value = getRequired(key);
        if (!value.matches("[A-Za-z][A-Za-z0-9_]*")) {
            throw new IllegalStateException(key.value() + " is not a valid CQL identifier: " + value);
        }
        return value;
    }

    private String getRequired(Config.Keys key) {
        String value = Config.get(key.value());
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required config property: " + key.value());
        }
        return value.trim();
    }
}
