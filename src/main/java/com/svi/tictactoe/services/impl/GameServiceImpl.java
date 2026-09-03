package com.svi.tictactoe.services.impl;

import com.svi.tictactoe.dao.GameDAO;
import com.svi.tictactoe.dao.impl.GameDAOImpl;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.RoomDTO;
import com.svi.tictactoe.dto.response.ServiceResponseDTO;
import com.svi.tictactoe.dto.response.GameListResponseDTO;
import com.svi.tictactoe.dto.response.GameRecordListResponseDTO;
import com.svi.tictactoe.dto.response.SaveResponseDTO;
import com.svi.tictactoe.services.GameService;
import com.svi.tictactoe.utils.Validators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameServiceImpl implements GameService {
    private GameDAO gameDAO = new GameDAOImpl();

    @Override
    public ServiceResponseDTO<SaveResponseDTO> saveMove(GameRecordDTO record) {
        if (!Validators.isValidRecord(record)) {
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO("Record could not be saved"),
                    401
            );
        }

        try {
            gameDAO.saveMove(record);
            gameDAO.addGameToPlayer(record.getPlayerId(), record.getGameId());
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO("Record saved."),
                    200
            );

        } catch (IOException e) {
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO("The server ran into an unexpected exception."),
                    500
            );
        }
    }

    @Override
    public ServiceResponseDTO<GameRecordListResponseDTO> getGameDetails(String gameId) {
        if (gameId == null || gameId.trim().isEmpty() || !Validators.isValidUUID(gameId)) {
            return new ServiceResponseDTO<>(
                    new GameRecordListResponseDTO(null, "Invalid gameId format."),
                    400
            );
        }

        try {
            List<GameRecordDTO> gameRecords = gameDAO.readMoves(gameId);
            return new ServiceResponseDTO<>(
                    new GameRecordListResponseDTO(gameRecords, "Records found"),
                    200
            );

        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().contains("Game not found")) {
                return new ServiceResponseDTO<>(
                        new GameRecordListResponseDTO(null, "Game record not found"),
                        404
                );
            }

            return new ServiceResponseDTO<>(
                    new GameRecordListResponseDTO(null, "The server ran into an unexpected exception."),
                    500
            );
        }
    }

    @Override
    public ServiceResponseDTO<GameListResponseDTO> getPlayerGames(String playerId) {
        if (playerId == null || playerId.trim().isEmpty() || !Validators.isValidUUID(playerId)) {
            return new ServiceResponseDTO<>(
                    new GameListResponseDTO(null, "Invalid playerId format."),
                    400
            );
        }

        try {
            if (!gameDAO.playerExists(playerId)) {
                return new ServiceResponseDTO<>(
                        new GameListResponseDTO(null, "Player record not found"),
                        404
                );
            }

            List<String> gameIds = gameDAO.readPlayerGames(playerId);
            List<GameListResponseDTO.GameItem> gameItems = new ArrayList<>();

            for (String gameId : gameIds) {
                gameItems.add(new GameListResponseDTO.GameItem(gameId, gameDAO.readPlayerName(gameId)));
            }
            return new ServiceResponseDTO<>(
                    new GameListResponseDTO(gameItems, "Player games records found"),
                    200
            );

        } catch (IOException e) {
            return new ServiceResponseDTO<>(
                    new GameListResponseDTO(null, "The server ran into an unexpected exception."),
                    500
            );
        }
    }

    @Override
    public void saveGameToRoom(String roomCode, String gameId, String createdDate) throws IOException {
        gameDAO.addGameToRoom(roomCode, gameId, createdDate);
    }

    @Override
    public List<RoomDTO> getRoomGames(String roomCode) throws IOException {
        if (!gameDAO.roomExists(roomCode)) {
            throw new IOException("Room not found");
        }
        
        return gameDAO.readRoomGames(roomCode);
    }
}
