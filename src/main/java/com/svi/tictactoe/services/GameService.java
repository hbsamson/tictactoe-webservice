package com.svi.tictactoe.services;

import com.svi.tictactoe.dao.GameDAO;
import com.svi.tictactoe.dao.impl.GameDAOImpl;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.GameListResponseDTO;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GameService {
    private GameDAO gameDAO = new GameDAOImpl();

    public void saveMove(GameRecordDTO record) throws IOException {
        gameDAO.saveMove(record);
        gameDAO.addGameToPlayer(record.getPlayerid(), record.getGameid());
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
}