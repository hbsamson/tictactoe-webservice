package com.svi.tictactoe.resource;

import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.response.SaveResponseDTO;
import com.svi.tictactoe.dto.response.GameRecordListResponseDTO;
import com.svi.tictactoe.services.GameService;
import com.svi.tictactoe.services.impl.GameServiceImpl;
import com.svi.tictactoe.utils.Validators;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;

import static com.svi.tictactoe.utils.Validators.isValidUUID;


@Path("/game")
public class GameResource {
    private GameService gameService = new GameServiceImpl();

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveMove(GameRecordDTO record) {
        if (!Validators.isValidRecord(record)) {
            return Response.status(401)
                    .entity(new SaveResponseDTO("Record could not be saved"))
                    .build();
        }

        try {
            gameService.saveMove(record);
            return Response.ok(new SaveResponseDTO("Record saved.")).build();

        } catch (IOException e) {
            return Response.status(500)
                    .entity(new SaveResponseDTO("The server ran into an unexpected exception."))
                    .build();
        }
    }

    @GET
    @Path("/{gameId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getGameDetails(@PathParam("gameId") String gameId) {
        if (gameId == null || gameId.trim().isEmpty() || !Validators.isValidUUID(gameId)) {
            return Response.status(400)
                    .entity(new GameRecordListResponseDTO(null, "Invalid gameId format."))
                    .build();
        }

        try {
            GameRecordListResponseDTO response = gameService.getGameDetails(gameId);
            return Response.ok(response).build();
            
        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().contains("Game not found")) {
                return Response.status(402)
                        .entity(new GameRecordListResponseDTO(null, "Record not found"))
                        .build();
            }
            return Response.status(500)
                    .entity(new GameRecordListResponseDTO(null, "The server ran into an unexpected exception."))
                    .build();
        }
    }
}
