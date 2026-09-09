package com.svi.tictactoe.contextlistener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.svi.tictactoe.connection.CassandraConnection;

@WebListener
public class ContextInitializer implements ServletContextListener {
    private CassandraConnection cassandraConnection;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        cassandraConnection = CassandraConnection.getInstance();
        cassandraConnection.initialize();
        sce.getServletContext().setAttribute("cassandraConnection", cassandraConnection);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (cassandraConnection != null) {
            cassandraConnection.close();
        }
        System.out.println("Application context destroyed.");
    }
}
