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

    public String getCassandraTable() {
        return getRequired(Config.Keys.CASSANDRA_TABLE);
    }

    private String getRequired(Config.Keys key) {
        String value = Config.get(key.value());
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required config property: " + key.value());
        }
        return value.trim();
    }
}
