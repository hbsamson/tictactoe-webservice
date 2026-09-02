package com.svi.tictactoe.services;

import com.svi.tictactoe.dao.GameDAO;
import com.svi.tictactoe.dao.impl.GameDAOImpl;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.GameListResponseDTO;
import com.svi.tictactoe.dto.RoomDTO;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GameService {
    private GameDAO gameDAO = new GameDAOImpl();

    public void saveMove(GameRecordDTO record) throws IOException {
        gameDAO.saveMove(record);
        gameDAO.addGameToPlayer(record.getPlayerid(), record.getGameid());
    }

    public void saveGameToRoom(String roomCode, String gameId, String createdDate) throws IOException {
        gameDAO.addGameToRoom(roomCode, gameId, createdDate);
    }

    public GameListResponseDTO getPlayerGames(String playerId) throws IOException {
        // Check if player file exists first
        if (!gameDAO.playerExists(playerId)) {
            throw new IOException("Player not found");
        }
        
        List<String> gameIds = gameDAO.readPlayerGames(playerId);
        List<GameListResponseDTO.GameItem> gameItems = gameIds.stream()
                .map(GameListResponseDTO.GameItem::new)
                .collect(Collectors.toList());
        return new GameListResponseDTO(gameItems, "Player games records found.");
    }

    public List<RoomDTO> getRoomGames(String roomCode) throws IOException {
        // Check if room exists first
        if (!gameDAO.roomExists(roomCode)) {
            throw new IOException("Room not found");
        }
        
        return gameDAO.readRoomGames(roomCode);
    }
}