package com.svi.tictactoe.dao.impl;

import com.svi.tictactoe.dao.GameDAO;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.RoomDTO;
import com.svi.tictactoe.services.FileStorageService;
import com.svi.tictactoe.services.impl.FileStorageServiceImpl;
import java.io.IOException;
import java.util.List;

public class GameDAOImpl implements GameDAO {
    private FileStorageService fileStorageService = new FileStorageServiceImpl();

    @Override
    public void saveMove(GameRecordDTO record) throws IOException {
        fileStorageService.appendMoveToGame(record);
    }

    @Override
    public void addGameToPlayer(String playerId, String gameId) throws IOException {
        fileStorageService.appendGameToPlayer(playerId, gameId);
    }

    @Override
    public void addGameToRoom(String roomCode, String gameId, String createdDate) throws IOException {
        fileStorageService.appendGameToRoom(roomCode, gameId, createdDate);
    }

    @Override
    public List<GameRecordDTO> readMoves(String gameId) throws IOException {
        return fileStorageService.readGameMoves(gameId);
    }

    @Override
    public List<String> readPlayerGames(String playerId) throws IOException {
        return fileStorageService.readPlayerGames(playerId);
    }

    @Override
    public String readPlayerName(String gameId) throws IOException {
        return fileStorageService.readPlayerName(gameId);
    }

    public List<String> readGames(String gameId) throws IOException {
        return fileStorageService.readGames(gameId);
    }

    @Override
    public List<RoomDTO> readRoomGames(String roomCode) throws IOException {
        return fileStorageService.readRoomGames(roomCode);
    }

    @Override
    public boolean playerExists(String playerId) throws IOException {
        return fileStorageService.playerExists(playerId);
    }

    @Override
    public boolean gameExists(String gameId) throws IOException {
        return fileStorageService.gameExists(gameId);
    }

    @Override
    public boolean roomExists(String roomCode) throws IOException {
        return fileStorageService.roomExists(roomCode);
    }
}
