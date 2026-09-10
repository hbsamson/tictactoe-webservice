package com.svi.tictactoe.contextlistener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.svi.tictactoe.connection.CassandraConnection;
import java.util.logging.Logger;

@WebListener
public class ContextInitializer implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(ContextInitializer.class.getName());
    private CassandraConnection cassandraConnection;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        cassandraConnection = CassandraConnection.getInstance();
        cassandraConnection.initialize();
        sce.getServletContext().setAttribute("cassandraConnection", cassandraConnection);
        LOGGER.info("Application context initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (cassandraConnection != null) {
            cassandraConnection.close();
        }
        LOGGER.info("Application context destroyed");
    }
}
