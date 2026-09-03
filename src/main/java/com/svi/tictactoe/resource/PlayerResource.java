package com.svi.tictactoe.resource;

import com.svi.tictactoe.dto.response.GameListResponseDTO;
import com.svi.tictactoe.dto.response.ServiceResponseDTO;
import com.svi.tictactoe.services.GameService;
import com.svi.tictactoe.services.impl.GameServiceImpl;
import com.svi.tictactoe.utils.Validators;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public Response listPlayerGames(@PathParam("playerId") String playerId) {

        ServiceResponseDTO<GameListResponseDTO> serviceResponse =
                gameService.getPlayerGames(playerId);

        return Response
                .status(serviceResponse.getStatus())
                .entity(serviceResponse.getData())
                .build();
    }
}
