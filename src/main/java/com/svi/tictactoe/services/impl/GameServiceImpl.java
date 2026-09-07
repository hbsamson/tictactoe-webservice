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
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class GameServiceImpl implements GameService {
    private static final Object SAVE_MOVE_LOCK = new Object();
    private final GameDAO gameDAO;

    public GameServiceImpl() {
        this(new GameDAOImpl());
    }

    public GameServiceImpl(GameDAO gameDAO) {
        this.gameDAO = Objects.requireNonNull(gameDAO, "gameDAO");
    }

    @Override
    public ServiceResponseDTO<SaveResponseDTO> saveMove(GameRecordDTO record) {
        if (record != null && !Validators.isValidUUID(record.getGameId())) {
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO("Invalid gameId format"),
                    401
            );
        }

        if (record != null && !Validators.isValidUUID(record.getPlayerId())) {
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO("Invalid playerId format"),
                    401
            );
        }

        if (record != null && !Validators.isValidSymbol(record.getSymbol())) {
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO("Invalid symbol"),
                    401
            );
        }

        if (!Validators.isValidRecord(record)) {
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO("Record could not be saved"),
                    401
            );
        }

        try {
            synchronized (SAVE_MOVE_LOCK) {
                List<GameRecordDTO> existingMoves = gameDAO.readMoves(record.getGameId());
                for (GameRecordDTO existingMove : existingMoves) {
                    if (existingMove != null && record.getLocation().equals(existingMove.getLocation())) {
                        return new ServiceResponseDTO<>(
                                new SaveResponseDTO("Location is already occupied."),
                                409
                        );
                    }
                }

                gameDAO.saveMove(record);
                gameDAO.addGameToPlayer(record.getPlayerId(), record.getGameId());
            }
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO("Record saved"),
                    200
            );

        } catch (IOException e) {
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO("The server ran into an unexpected exception"),
                    500
            );
        }
    }

    @Override
    public ServiceResponseDTO<GameRecordListResponseDTO> getGameDetails(String gameId) {
        if (gameId == null || gameId.trim().isEmpty() || !Validators.isValidUUID(gameId)) {
            return new ServiceResponseDTO<>(
                    new GameRecordListResponseDTO(null, "Invalid gameId format"),
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
            if (e.getMessage() != null && e.getMessage().contains("Game ID not found")) {
                return new ServiceResponseDTO<>(
                        new GameRecordListResponseDTO(null, "Game record not found"),
                        404
                );
            }

            return new ServiceResponseDTO<>(
                    new GameRecordListResponseDTO(null, "The server ran into an unexpected exception"),
                    500
            );
        }
    }

    @Override
    public ServiceResponseDTO<GameListResponseDTO> getPlayerGames(String playerId) {
        if (playerId == null || playerId.trim().isEmpty() || !Validators.isValidUUID(playerId)) {
            return new ServiceResponseDTO<>(
                    new GameListResponseDTO(null, "Invalid playerId format"),
                    400
            );
        }

        try {
            if (!gameDAO.playerExists(playerId)) {
                return new ServiceResponseDTO<>(
                        new GameListResponseDTO(null, "Player ID not found"),
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
                    new GameListResponseDTO(null, "The server ran into an unexpected exception"),
                    500
            );
        }
    }

    @Override
    public ServiceResponseDTO<SaveResponseDTO> saveRoom(RoomDTO roomRecord) {
        if (roomRecord == null
                || roomRecord.getRoomId() == null
                || roomRecord.getRoomId().trim().isEmpty()
                || !Validators.isValidRoomCode(roomRecord.getRoomId())
                || roomRecord.getGameIds() == null
                || roomRecord.getGameIds().isEmpty()) {
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO("Invalid roomId format"),
                    400
            );
        }

        if (roomRecord.getGameIds() != null) {
            for (String gameId : roomRecord.getGameIds()) {
                if (gameId == null || gameId.trim().isEmpty() || !Validators.isValidUUID(gameId)) {
                    return new ServiceResponseDTO<>(
                            new SaveResponseDTO("Invalid gameIds format"),
                            400
                    );
                }
            }
        }

        try {
            String roomId = roomRecord.getRoomId().trim();
            String createdDate = Instant.now().toString();

            synchronized (SAVE_MOVE_LOCK) {
                for (String gameId : new LinkedHashSet<>(roomRecord.getGameIds())) {
                    gameDAO.addGameToRoom(roomId, gameId.trim(), createdDate);
                }
            }
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO("Room games saved"),
                    200
            );
        } catch (IOException e) {
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO("The server ran into an unexpected exception"),
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

    @Override
    public List<String> getRoomIds() throws IOException {
        return gameDAO.readRoomIds();
    }
}
