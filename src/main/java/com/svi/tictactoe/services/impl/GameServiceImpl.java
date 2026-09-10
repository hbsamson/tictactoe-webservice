package com.svi.tictactoe.services.impl;

import com.svi.tictactoe.repository.CassandraGameRepository;
import com.svi.tictactoe.repository.GameRepository;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.RoomDTO;
import com.svi.tictactoe.dto.PlayerGameDTO;
import com.svi.tictactoe.dto.response.GameListResponseDTO;
import com.svi.tictactoe.dto.response.GameRecordListResponseDTO;
import com.svi.tictactoe.dto.response.SaveResponseDTO;
import com.svi.tictactoe.constants.ResponseMessage;
import com.svi.tictactoe.services.GameService;
import com.svi.tictactoe.utils.Validators;
import com.svi.tictactoe.exception.InvalidRequestException;
import com.svi.tictactoe.exception.LocationOccupiedException;
import com.svi.tictactoe.exception.PersistenceException;
import com.svi.tictactoe.exception.ResourceNotFoundException;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class GameServiceImpl implements GameService {
    private static final Object SAVE_MOVE_LOCK = new Object();
    private final GameRepository gameRepository;

    public GameServiceImpl() {
        this(new CassandraGameRepository()); // can be changed to FileGameRepository() if needed
    }

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = Objects.requireNonNull(gameRepository, "gameRepository");
    }

    @Override
    public SaveResponseDTO saveMove(String roomId, String gameId, GameRecordDTO record) {
        if (record == null) {
            throw new InvalidRequestException(ResponseMessage.RECORD_NOT_SAVED);
        }
        if (!Validators.isValidRoomCode(roomId)) {
            throw new InvalidRequestException(ResponseMessage.INVALID_ROOM_CODE);
        }
        if (!Validators.isValidUUID(gameId)
                || record.getGameId() == null
                || !gameId.equals(record.getGameId())) {
            throw new InvalidRequestException(ResponseMessage.INVALID_GAME_ID);
        }

        try {
            synchronized (SAVE_MOVE_LOCK) {
                List<GameRecordDTO> existingMoves = gameRepository.findMovesByGameId(record.getGameId());
                for (GameRecordDTO existingMove : existingMoves) {
                    if (existingMove != null && record.getLocation().equals(existingMove.getLocation())) {
                        throw new LocationOccupiedException();
                    }
                }

                gameRepository.saveMove(record);
                gameRepository.savePlayerGame(record);
                gameRepository.saveRoomGame(roomId, gameId, Instant.now().toString());
            }
            return new SaveResponseDTO(ResponseMessage.RECORD_SAVED);

        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public GameRecordListResponseDTO getGameDetails(String gameId) {
        if (gameId == null || gameId.trim().isEmpty() || !Validators.isValidUUID(gameId)) {
            throw new InvalidRequestException(ResponseMessage.INVALID_GAME_ID);
        }

        try {
            List<GameRecordDTO> gameRecords = gameRepository.findMovesByGameId(gameId);
            return new GameRecordListResponseDTO(gameRecords, ResponseMessage.RECORDS_FOUND);

        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().contains("Game ID not found")) {
                throw new ResourceNotFoundException(ResponseMessage.GAME_NOT_FOUND);
            }

            throw new PersistenceException(e);
        }
    }

    @Override
    public GameListResponseDTO getPlayerGames(String playerId) {
        if (playerId == null || playerId.trim().isEmpty() || !Validators.isValidUUID(playerId)) {
            throw new InvalidRequestException(ResponseMessage.INVALID_PLAYER_ID);
        }

        try {
            if (!gameRepository.playerExists(playerId)) {
                throw new ResourceNotFoundException(ResponseMessage.PLAYER_NOT_FOUND);
            }

            List<PlayerGameDTO> games = gameRepository.findGamesByPlayerId(playerId);
            List<GameListResponseDTO.GameItem> gameItems = new ArrayList<>();

            for (PlayerGameDTO game : games) {
                gameItems.add(new GameListResponseDTO.GameItem(
                        game.getGameId(), game.getPlayerName(), game.getPlayerAvatar(), game.getGameDate()));
            }
            return new GameListResponseDTO(gameItems, ResponseMessage.PLAYER_GAMES_FOUND);

        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public SaveResponseDTO saveRoom(RoomDTO roomRecord) {
        if (roomRecord == null
                || roomRecord.getRoomId() == null
                || roomRecord.getRoomId().trim().isEmpty()
                || !Validators.isValidRoomCode(roomRecord.getRoomId())
                || roomRecord.getGameIds() == null
                || roomRecord.getGameIds().isEmpty()) {
            throw new InvalidRequestException(ResponseMessage.INVALID_ROOM_ID);
        }

        for (String gameId : roomRecord.getGameIds()) {
            if (gameId == null || gameId.trim().isEmpty() || !Validators.isValidUUID(gameId)) {
                throw new InvalidRequestException(ResponseMessage.INVALID_GAME_IDS);
            }
        }

        try {
            String roomId = roomRecord.getRoomId().trim();
            String createdDate = Instant.now().toString();

            synchronized (SAVE_MOVE_LOCK) {
                for (String gameId : new LinkedHashSet<>(roomRecord.getGameIds())) {
                    gameRepository.saveRoomGame(roomId, gameId.trim(), createdDate);
                }
            }
            return new SaveResponseDTO(ResponseMessage.ROOM_GAMES_SAVED);
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void saveGameToRoom(String roomId, String gameId, String createdDate) {
        try {
            gameRepository.saveRoomGame(roomId, gameId, createdDate);
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<RoomDTO> getRoomGames(String roomId) {
        if (!Validators.isValidRoomCode(roomId)) {
            throw new InvalidRequestException(ResponseMessage.INVALID_ROOM_CODE);
        }
        try {
            if (!gameRepository.roomExists(roomId)) {
                throw new ResourceNotFoundException(ResponseMessage.ROOM_NOT_FOUND);
            }
            return gameRepository.findGamesByRoomId(roomId);
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<String> getRoomIds() {
        try {
            return gameRepository.findRoomIds();
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }
}
