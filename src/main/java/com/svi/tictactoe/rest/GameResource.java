package com.svi.tictactoe.rest;

import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.SaveResponseDTO;
import com.svi.tictactoe.dto.GameListResponseDTO;
import com.svi.tictactoe.dto.GameRecordListResponseDTO;
import com.svi.tictactoe.services.GameService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import java.io.IOException;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
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
        if (!isValidRecord(record)) {
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

    private boolean isValidRecord(GameRecordDTO record) {
        if (record == null) {
            return false;
        }
        return record.getGameId() != null && !record.getGameId().trim().isEmpty() && isValidUUID(record.getGameId()) &&
               record.getPlayerId() != null && !record.getPlayerId().trim().isEmpty() && isValidUUID(record.getPlayerId()) &&
               record.getSymbol() != null && ("X".equals(record.getSymbol().trim()) || "O".equals(record.getSymbol().trim())) &&
               record.getLocation() != null && record.getLocation().matches("[0-9]") &&
               record.getDateSaved() != null && !record.getDateSaved().trim().isEmpty();
    }

    private boolean isValidUUID(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @GET
    @Path("/list-games/{playerId}")
    public Response listPlayerGames(@PathParam("playerId") String playerId) {
        if (playerId == null || playerId.trim().isEmpty() || !isValidUUID(playerId)) {
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

    @GET
    @Path("/{gameId}")
    public Response getGameDetails(@PathParam("gameId") String gameId) {
        if (gameId == null || gameId.trim().isEmpty() || !isValidUUID(gameId)) {
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
