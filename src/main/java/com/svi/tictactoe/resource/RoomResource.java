package com.svi.tictactoe.resource;

import com.svi.tictactoe.dto.RoomDTO;
import com.svi.tictactoe.services.GameService;
import com.svi.tictactoe.services.impl.GameServiceImpl;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/room")
public class RoomResource {
    private final GameService gameService;

    public RoomResource() {
        this.gameService = new GameServiceImpl();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomIds() {
        return Response.ok(gameService.getRoomIds()).build();
    }

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveGame(@Valid RoomDTO roomRecord) {
        return Response.ok(gameService.saveRoom(roomRecord)).build();
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomGames(@PathParam("roomId") String roomId) {
        return Response.ok(gameService.getRoomGames(roomId)).build();
    }

}
