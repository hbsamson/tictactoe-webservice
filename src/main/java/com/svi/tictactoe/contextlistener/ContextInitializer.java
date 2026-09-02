package com.svi.tictactoe.contextlistener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.jsp.jstl.core.Config;

// import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@WebListener
public class ContextInitializer implements ServletContextListener {
    // private static final String CONFIG_INI_LOCATION = "CONFIG_INI_LOCATION";
    private ServletContext context;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.context = sce.getServletContext();
        // Config.setContext(context.getResourceAsStream(context.getInitParameter(CONFIG_INI_LOCATION)));

        // //setup config
        // //database connection
        // Initialize file storage directories
        initializeFileStorage();
    }

    /**
     * Initialize required file storage directories for the application.
     * Creates /records, /records/playerid, /records/gameid, /records/roomid directories.
     */
    private void initializeFileStorage() {
        try {
            Files.createDirectories(Paths.get("records"));
            Files.createDirectories(Paths.get("records/playerid"));
            Files.createDirectories(Paths.get("records/gameid"));
            Files.createDirectories(Paths.get("records/roomid"));
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