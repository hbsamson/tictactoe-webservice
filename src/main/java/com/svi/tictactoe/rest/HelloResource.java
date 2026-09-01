package com.svi.tictactoe.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.svi.tictactoe.service.Hello;

@Path("hello")
public class HelloResource {

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Hello hello(@QueryParam("name") String name) {
        if ((name == null) || name.trim().isEmpty()) {
            name = "world";
        }

        return new Hello(name);
    }
}
