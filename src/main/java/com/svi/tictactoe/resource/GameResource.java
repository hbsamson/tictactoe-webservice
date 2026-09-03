package com.svi.tictactoe.resource;

import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.response.GameRecordListResponseDTO;
import com.svi.tictactoe.dto.response.SaveResponseDTO;
import com.svi.tictactoe.dto.response.ServiceResponseDTO;
import com.svi.tictactoe.services.GameService;
import com.svi.tictactoe.services.impl.GameServiceImpl;

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
    private GameService gameService = new GameServiceImpl();

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveMove(GameRecordDTO record) {

        ServiceResponseDTO<SaveResponseDTO> serviceResponse =
                gameService.saveMove(record);

        return Response
                .status(serviceResponse.getStatus())
                .entity(serviceResponse.getData())
                .build();
    }

    @GET
    @Path("/{gameId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGameDetails(@PathParam("gameId") String gameId) {

        ServiceResponseDTO<GameRecordListResponseDTO> serviceResponse =
                gameService.getGameDetails(gameId);

        return Response
                .status(serviceResponse.getStatus())
                .entity(serviceResponse.getData())
                .build();
    }
}