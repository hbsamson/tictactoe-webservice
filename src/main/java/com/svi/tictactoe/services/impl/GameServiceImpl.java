package com.svi.tictactoe.services.impl;

import com.svi.tictactoe.dao.GameDAO;
import com.svi.tictactoe.dao.impl.GameDAOImpl;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.RoomDTO;
import com.svi.tictactoe.dto.PlayerGameDTO;
import com.svi.tictactoe.dto.response.ServiceResponseDTO;
import com.svi.tictactoe.dto.response.GameListResponseDTO;
import com.svi.tictactoe.dto.response.GameRecordListResponseDTO;
import com.svi.tictactoe.dto.response.SaveResponseDTO;
import com.svi.tictactoe.constants.ResponseMessage;
import com.svi.tictactoe.services.GameService;
import com.svi.tictactoe.utils.Validators;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import javax.ws.rs.core.Response;

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
        if (record == null) {
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO(ResponseMessage.RECORD_NOT_SAVED),
                    Response.Status.BAD_REQUEST
            );
        }

        try {
            synchronized (SAVE_MOVE_LOCK) {
                List<GameRecordDTO> existingMoves = gameDAO.readMoves(record.getGameId());
                for (GameRecordDTO existingMove : existingMoves) {
                    if (existingMove != null && record.getLocation().equals(existingMove.getLocation())) {
                        return new ServiceResponseDTO<>(
                                new SaveResponseDTO(ResponseMessage.LOCATION_OCCUPIED),
                                Response.Status.CONFLICT
                        );
                    }
                }

                gameDAO.saveMove(record);
                gameDAO.addGameToPlayer(record);
            }
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO(ResponseMessage.RECORD_SAVED),
                Response.Status.OK
            );

        } catch (IOException e) {
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO(ResponseMessage.SERVER_ERROR),
                    Response.Status.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ServiceResponseDTO<GameRecordListResponseDTO> getGameDetails(String gameId) {
        if (gameId == null || gameId.trim().isEmpty() || !Validators.isValidUUID(gameId)) {
            return new ServiceResponseDTO<>(
                    new GameRecordListResponseDTO(null, ResponseMessage.INVALID_GAME_ID),
                Response.Status.BAD_REQUEST
            );
        }

        try {
            List<GameRecordDTO> gameRecords = gameDAO.readMoves(gameId);
            return new ServiceResponseDTO<>(
                    new GameRecordListResponseDTO(gameRecords, ResponseMessage.RECORDS_FOUND),
                    Response.Status.OK
            );

        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().contains("Game ID not found")) {
                return new ServiceResponseDTO<>(
                        new GameRecordListResponseDTO(null, ResponseMessage.GAME_NOT_FOUND),
                        Response.Status.NOT_FOUND
                );
            }

            return new ServiceResponseDTO<>(
                new GameRecordListResponseDTO(null, ResponseMessage.SERVER_ERROR),
                    Response.Status.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ServiceResponseDTO<GameListResponseDTO> getPlayerGames(String playerId) {
        if (playerId == null || playerId.trim().isEmpty() || !Validators.isValidUUID(playerId)) {
            return new ServiceResponseDTO<>(
                    new GameListResponseDTO(null, ResponseMessage.INVALID_PLAYER_ID),
                    Response.Status.BAD_REQUEST
            );
        }

        try {
            if (!gameDAO.playerExists(playerId)) {
                return new ServiceResponseDTO<>(
                        new GameListResponseDTO(null, ResponseMessage.PLAYER_NOT_FOUND),
                        Response.Status.NOT_FOUND
                );
            }

            List<PlayerGameDTO> games = gameDAO.readPlayerGames(playerId);
            List<GameListResponseDTO.GameItem> gameItems = new ArrayList<>();

            for (PlayerGameDTO game : games) {
                gameItems.add(new GameListResponseDTO.GameItem(
                        game.getGameId(), game.getPlayerName(), game.getPlayerAvatar(), game.getGameDate()));
            }
            return new ServiceResponseDTO<>(
                    new GameListResponseDTO(gameItems, ResponseMessage.PLAYER_GAMES_FOUND),
                Response.Status.OK
            );

        } catch (IOException e) {
            return new ServiceResponseDTO<>(
                new GameListResponseDTO(null, ResponseMessage.SERVER_ERROR),
                    Response.Status.INTERNAL_SERVER_ERROR
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
                    new SaveResponseDTO(ResponseMessage.INVALID_ROOM_ID),
                Response.Status.BAD_REQUEST
            );
        }

        for (String gameId : roomRecord.getGameIds()) {
            if (gameId == null || gameId.trim().isEmpty() || !Validators.isValidUUID(gameId)) {
                return new ServiceResponseDTO<>(
                        new SaveResponseDTO(ResponseMessage.INVALID_GAME_IDS),
                    Response.Status.BAD_REQUEST
                );
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
                    new SaveResponseDTO(ResponseMessage.ROOM_GAMES_SAVED),
                Response.Status.OK
            );
        } catch (IOException e) {
            return new ServiceResponseDTO<>(
                    new SaveResponseDTO(ResponseMessage.SERVER_ERROR),
                Response.Status.INTERNAL_SERVER_ERROR
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
