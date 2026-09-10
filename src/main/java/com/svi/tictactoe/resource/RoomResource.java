package com.svi.tictactoe.resource;

import com.svi.tictactoe.dto.RoomDTO;
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

@Path("/rooms")
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
    @Path("/{roomId}/games/{gameId}/moves")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveMove(@PathParam("roomId") String roomId,
            @PathParam("gameId") String gameId,
            @Valid @NotNull GameRecordDTO record) {
        return Response.status(Response.Status.CREATED)
                .entity(gameService.saveMove(roomId, gameId, record))
                .build();
    }

    @POST
    @Path("/{roomId}/games")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveGame(@PathParam("roomId") String roomId, RoomDTO roomRecord) {
        if (roomRecord != null) {
            roomRecord.setRoomId(roomId);
        }
        return Response.ok(gameService.saveRoom(roomRecord)).build();
    }

    @GET
    @Path("/{roomId}/games")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomGames(@PathParam("roomId") String roomId) {
        return Response.ok(gameService.getRoomGames(roomId)).build();
    }

}
