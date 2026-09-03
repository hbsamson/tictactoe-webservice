package com.svi.tictactoe.services.impl;

import com.svi.tictactoe.dao.GameDAO;
import com.svi.tictactoe.dao.impl.GameDAOImpl;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.RoomDTO;
import com.svi.tictactoe.dto.response.GameListResponseDTO;
import com.svi.tictactoe.dto.response.GameRecordListResponseDTO;
import com.svi.tictactoe.services.GameService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameServiceImpl implements GameService {
    private GameDAO gameDAO = new GameDAOImpl();

    @Override
    public void saveMove(GameRecordDTO record) throws IOException {
        gameDAO.saveMove(record);
        gameDAO.addGameToPlayer(record.getPlayerId(), record.getGameId());
    }

    @Override
    public void saveGameToRoom(String roomCode, String gameId, String createdDate) throws IOException {
        gameDAO.addGameToRoom(roomCode, gameId, createdDate);
    }

    @Override
    public GameListResponseDTO getPlayerGames(String playerId) throws IOException {
        // Check if player id file exists first
        if (!gameDAO.playerExists(playerId)) {
            throw new IOException("Player not found");
        }
        
        List<String> gameIds = gameDAO.readPlayerGames(playerId);
        List<GameListResponseDTO.GameItem> gameItems = new ArrayList<>();
        for (String gameId : gameIds) {
            gameItems.add(new GameListResponseDTO.GameItem(gameId, gameDAO.readPlayerName(gameId)));
        }
        return new GameListResponseDTO(gameItems, "Player games records found");
    }

    @Override
    public GameRecordListResponseDTO getGameDetails(String gameId) throws IOException {
        // Check if game file exists first
        if (!gameDAO.gameExists(gameId)) {
            throw new IOException("Game not found");
        }

        List<GameRecordDTO> gameRecords = gameDAO.readMoves(gameId);
        return new GameRecordListResponseDTO(gameRecords, "Records found");
    }

    @Override
    public List<RoomDTO> getRoomGames(String roomCode) throws IOException {
        // Check if room exists first
        if (!gameDAO.roomExists(roomCode)) {
            throw new IOException("Room not found");
        }
        
        return gameDAO.readRoomGames(roomCode);
    }
}
