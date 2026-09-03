package com.svi.tictactoe.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        DEFAULTS.setProperty(Keys.RECORDS_DIR.value(), "records");
        DEFAULTS.setProperty(Keys.PLAYER_DIR.value(), "playerid");
        DEFAULTS.setProperty(Keys.GAME_DIR.value(), "gameid");
        DEFAULTS.setProperty(Keys.ROOM_DIR.value(), "roomid");
        DEFAULTS.setProperty(Keys.FRONTEND_URLS.value(), "http://localhost:5500,http://127.0.0.1:5500");

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

    public static Path getPath(String key) {
        return Paths.get(get(key));
    }

    public enum Keys {
        RECORDS_DIR("RECORDS_DIR"),
        PLAYER_DIR("PLAYER_DIR"),
        GAME_DIR("GAME_DIR"),
        ROOM_DIR("ROOM_DIR"),
        FRONTEND_URLS("FRONTEND_URLS");

        private final String value;

        Keys(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}
