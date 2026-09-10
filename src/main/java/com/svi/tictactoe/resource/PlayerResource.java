package com.svi.tictactoe.resource;

import com.svi.tictactoe.services.GameService;
import com.svi.tictactoe.services.impl.GameServiceImpl;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/player")
public class PlayerResource {
    private final GameService gameService;

    public PlayerResource() {
        this.gameService = new GameServiceImpl();
    }

    @GET
    @Path("/{playerId}/games")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listPlayerGames(@PathParam("playerId") String playerId) {

        return Response.ok(gameService.getPlayerGames(playerId)).build();
    }
}
