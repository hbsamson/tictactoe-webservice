package com.svi.tictactoe.contextlistener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.svi.tictactoe.config.Config;

@WebListener
public class ContextInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Initialize file storage directories
        initializeFileStorage();
    }

    /**
     * Initialize required file storage directories for the application.
     * Creates /records, /records/playerid, /records/gameid, /records/roomid directories.
     */
    private void initializeFileStorage() {
        try {
            Path recordsDir = Config.getPath(Config.Keys.RECORDS_DIR.value());
            Path playerDir = recordsDir.resolve(Config.get(Config.Keys.PLAYER_DIR.value()));
            Path gameDir = recordsDir.resolve(Config.get(Config.Keys.GAME_DIR.value()));
            Path roomDir = recordsDir.resolve(Config.get(Config.Keys.ROOM_DIR.value()));

            Files.createDirectories(recordsDir);
            Files.createDirectories(playerDir);
            Files.createDirectories(gameDir);
            Files.createDirectories(roomDir);
            System.out.println("File storage directories initialized successfully.");
        } catch (IOException e) {
            System.err.println("Failed to initialize file storage directories: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // database close
        System.out.println("Application context destroyed.");
    }
}
