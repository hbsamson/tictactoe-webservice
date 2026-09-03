package com.svi.tictactoe.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import com.svi.tictactoe.config.Config;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        String origin = request.getHeaderString("Origin");
        String frontendUrls = Config.get(Config.Keys.FRONTEND_URLS.value());

        if (origin != null) {
            String[] allowedOrigins = frontendUrls.split(",");
            for (String allowedOrigin : allowedOrigins) {
                if (origin.equals(allowedOrigin.trim())) {
                    response.getHeaders().putSingle("Access-Control-Allow-Origin", origin);
                    break;
                }
            }
        }

        response.getHeaders().putSingle(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS"
        );

        response.getHeaders().putSingle(
                "Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization"
        );
    }
}