package com.svi.tictactoe.resource;

import com.svi.tictactoe.services.GameService;
import com.svi.tictactoe.services.impl.GameServiceImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/games")
public class GameResource {
    private final GameService gameService;

    public GameResource() {
        this.gameService = new GameServiceImpl();
    }

    @GET
    @Path("/{gameId}/moves")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGameDetails(@PathParam("gameId") String gameId) {

        return Response.ok(gameService.getGameDetails(gameId)).build();
    }
}
