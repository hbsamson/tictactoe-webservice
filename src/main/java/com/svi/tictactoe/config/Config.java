package com.svi.tictactoe.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centralized application configuration loaded from config.properties.
 * Values can be overridden by system properties or environment variables.
 */
public final class Config {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties DEFAULTS = new Properties();
    private static final Properties PROPERTIES = new Properties(DEFAULTS);

    static {
        DEFAULTS.setProperty(Keys.FRONTEND_URLS.value(), "http://localhost:5500,http://127.0.0.1:5500");
        DEFAULTS.setProperty(Keys.CASSANDRA_IP.value(), "localhost");
        DEFAULTS.setProperty(Keys.CASSANDRA_PORT.value(), "9042");
        DEFAULTS.setProperty(Keys.CASSANDRA_KEYSPACE.value(), "batch1_2026_trainees");
        DEFAULTS.setProperty(Keys.CASSANDRA_GAME_MOVES_TABLE.value(), "samson_moves_table");
        DEFAULTS.setProperty(Keys.CASSANDRA_PLAYER_GAMES_TABLE.value(), "samson_games_table");
        DEFAULTS.setProperty(Keys.CASSANDRA_ROOM_GAMES_TABLE.value(), "samson_room_table");

        try (
            InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                PROPERTIES.load(inputStream);
            }

        } catch (IOException e) {
            throw new ExceptionInInitializerError(
                    "Failed to load " + CONFIG_FILE + ": " + e.getMessage()
            );
        }
    }

    private Config() {}

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }

        String environmentValue = System.getenv(key);
        if (environmentValue != null && !environmentValue.trim().isEmpty()) {
            return environmentValue.trim();
        }

        return PROPERTIES.getProperty(key);
    }

    public enum Keys {
        FRONTEND_URLS("FRONTEND_URLS"),
        CASSANDRA_IP("CASSANDRA_IP"),
        CASSANDRA_PORT("CASSANDRA_PORT"),
        CASSANDRA_KEYSPACE("CASSANDRA_KEYSPACE"),
        CASSANDRA_GAME_MOVES_TABLE("CASSANDRA_GAME_MOVES_TABLE"),
        CASSANDRA_PLAYER_GAMES_TABLE("CASSANDRA_PLAYER_GAMES_TABLE"),
        CASSANDRA_ROOM_GAMES_TABLE("CASSANDRA_ROOM_GAMES_TABLE");

        private final String value;

        Keys(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}
