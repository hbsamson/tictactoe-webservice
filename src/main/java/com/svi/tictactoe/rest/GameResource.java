package com.svi.tictactoe.rest;

import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.SaveResponseDTO;
import com.svi.tictactoe.services.GameService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/game")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameResource {
    private GameService gameService = new GameService();

    @POST
    @Path("/save")
    public Response saveMove(GameRecordDTO record) {
        try {
            gameService.saveMove(record);
            return Response.ok(new SaveResponseDTO("Record saved.")).build();
        } catch (IOException e) {
            return Response.status(500)
                    .entity(new SaveResponseDTO("The server ran into an unexpected exception."))
                    .build();
        }
    }
}
