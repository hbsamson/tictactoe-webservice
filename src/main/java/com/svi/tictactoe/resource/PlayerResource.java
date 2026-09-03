package com.svi.tictactoe.resource;

import com.svi.tictactoe.dto.response.GameListResponseDTO;
import com.svi.tictactoe.services.GameService;
import com.svi.tictactoe.services.impl.GameServiceImpl;
import com.svi.tictactoe.utils.Validators;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/player")
public class PlayerResource {
    private GameService gameService = new GameServiceImpl();

    @GET
    @Path("/{playerId}/games")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listPlayerGames(@PathParam("playerId") String playerId) {
        if (playerId == null || playerId.trim().isEmpty() || !Validators.isValidUUID(playerId)) {
            return Response.status(400)
                    .entity(new GameListResponseDTO(null, "Invalid playerId format."))
                    .build();
        }

        try {
            GameListResponseDTO response = gameService.getPlayerGames(playerId);
            return Response.ok(response).build();

        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().contains("Player not found")) {
                return Response.status(402)
                        .entity(new GameListResponseDTO(null, "Record not found"))
                        .build();
            }
            return Response.status(500)
                    .entity(new GameListResponseDTO(null, "The server ran into an unexpected exception."))
                    .build();
        }
    }
}
