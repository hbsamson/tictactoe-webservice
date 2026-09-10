package com.svi.tictactoe.filters;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Logs one completion record for every HTTP request.
 *
 * <p>The filter uses {@link java.util.logging}, so log destinations are managed
 * by the application server rather than this class. With Payara Micro, records
 * appear in the process console and in any configured server log handlers. When
 * the application is started through Eclipse, records normally appear in the
 * Eclipse Console view and in the selected server's log files (for Payara or
 * GlassFish, typically the domain's {@code logs/server.log}). Other Jakarta EE
 * servers route the records through their own JUL handlers and logging
 * configuration. The {@code X-Request-ID} response header can be used to
 * correlate a client request with its completion log.</p>
 */
@Provider
@Priority(Priorities.USER)
public class RequestLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger LOGGER = Logger.getLogger(RequestLoggingFilter.class.getName());
    private static final String REQUEST_ID_PROPERTY = RequestLoggingFilter.class.getName() + ".requestId";
    private static final String START_TIME_PROPERTY = RequestLoggingFilter.class.getName() + ".startTime";
    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        request.setProperty(REQUEST_ID_PROPERTY, UUID.randomUUID().toString());
        request.setProperty(START_TIME_PROPERTY, System.nanoTime());
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        String requestId = String.valueOf(request.getProperty(REQUEST_ID_PROPERTY));
        response.getHeaders().putSingle(REQUEST_ID_HEADER, requestId);

        Object startTime = request.getProperty(START_TIME_PROPERTY);
        long durationMs = startTime instanceof Long
                ? TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - (Long) startTime)
                : -1L;

        Level level = response.getStatus() >= 500 ? Level.SEVERE
                : response.getStatus() >= 400 ? Level.WARNING : Level.INFO;
        LOGGER.log(level, "requestId={0} method={1} path={2} status={3} durationMs={4}",
                new Object[] {
                    requestId,
                    request.getMethod(),
                    request.getUriInfo().getPath(),
                    response.getStatus(),
                    durationMs
                });
    }
}
