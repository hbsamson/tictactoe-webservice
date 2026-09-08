package com.svi.tictactoe.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("health")
public class HealthResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String check() {
        return "Server is running";
    }
}
