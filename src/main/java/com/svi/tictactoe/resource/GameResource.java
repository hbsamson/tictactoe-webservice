package com.svi.tictactoe.resource;

import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.services.GameService;
import com.svi.tictactoe.services.impl.GameServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/game")
public class GameResource {
    private final GameService gameService;

    public GameResource() {
        this.gameService = new GameServiceImpl();
    }

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveMove(@Valid @NotNull GameRecordDTO record) {

        return Response.ok(gameService.saveMove(record)).build();
    }

    @GET
    @Path("/{gameId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGameDetails(@PathParam("gameId") String gameId) {

        return Response.ok(gameService.getGameDetails(gameId)).build();
    }
}
