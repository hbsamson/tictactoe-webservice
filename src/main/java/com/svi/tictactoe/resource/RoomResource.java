package com.svi.tictactoe.resource;

import com.svi.tictactoe.dto.RoomDTO;
import com.svi.tictactoe.dto.RoomDTO;
import com.svi.tictactoe.dto.response.SaveResponseDTO;
import com.svi.tictactoe.dto.response.ServiceResponseDTO;
import com.svi.tictactoe.services.GameService;
import com.svi.tictactoe.services.impl.GameServiceImpl;
import com.svi.tictactoe.utils.Validators;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/room")
public class RoomResource {
    private final GameService gameService;

    public RoomResource() {
        this.gameService = new GameServiceImpl();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomIds() {
        try {
            return Response.ok(gameService.getRoomIds()).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new SaveResponseDTO("The server ran into an unexpected exception."))
                    .build();
        }
    }

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveGame(RoomDTO roomRecord) {
        ServiceResponseDTO<SaveResponseDTO> serviceResponse = gameService.saveRoom(roomRecord);
        return Response.status(serviceResponse.getStatus())
                .entity(serviceResponse.getData())
                .build();
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomGames(@PathParam("roomId") String roomId) {
        if (!Validators.isValidRoomCode(roomId)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new SaveResponseDTO("Invalid room code."))
                    .build();
        }

        try {
            return Response.ok(gameService.getRoomGames(roomId)).build();
        } catch (IOException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new SaveResponseDTO("Room record not found."))
                    .build();
        }
    }

}
