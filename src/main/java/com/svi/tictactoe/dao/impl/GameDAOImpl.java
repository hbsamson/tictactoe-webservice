package com.svi.tictactoe.dao.impl;

import com.svi.tictactoe.dao.GameDAO;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.utils.FileStorageService;
import java.io.IOException;
import java.util.List;

public class GameDAOImpl implements GameDAO {

    @Override
    public void saveMove(GameRecordDTO record) throws IOException {
        FileStorageService.appendMoveToGame(record);
    }

    @Override
    public void addGameToPlayer(String playerId, String gameId) throws IOException {
        FileStorageService.appendGameToPlayer(playerId, gameId);
    }

    @Override
    public List<GameRecordDTO> readMoves(String gameId) throws IOException {
        return FileStorageService.readGameMoves(gameId);
    }

    @Override
    public List<String> readPlayerGames(String playerId) throws IOException {
        return FileStorageService.readPlayerGames(playerId);
    }

    @Override
    public boolean playerExists(String playerId) throws IOException {
        return FileStorageService.playerExists(playerId);
    }

    @Override
    public boolean gameExists(String gameId) throws IOException {
        return FileStorageService.gameExists(gameId);
    }
}