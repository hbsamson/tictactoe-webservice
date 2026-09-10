package com.svi.tictactoe.services;

import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.RoomDTO;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/** Low-level flat-file storage retained for file-backed persistence and migration. */
public interface FileStorageService {
    Path getRecordsDirectory() throws IOException;

    Path getPlayerIdDirectory() throws IOException;

    Path getGameIdDirectory() throws IOException;

    Path getRoomIdDirectory() throws IOException;

    void appendMoveToGame(GameRecordDTO gameRecord) throws IOException;

    void appendGameToPlayer(String playerId, String gameId) throws IOException;

    List<String> readPlayerGames(String playerId) throws IOException;

    String readPlayerName(String gameId) throws IOException;

    List<String> readGames(String gameId) throws IOException;

    List<GameRecordDTO> readGameMoves(String gameId) throws IOException;

    boolean playerExists(String playerId) throws IOException;

    boolean gameExists(String gameId) throws IOException;

    void appendGameToRoom(String roomId, String gameId, String createdDate) throws IOException;

    List<RoomDTO> readRoomGames(String roomId) throws IOException;

    List<String> readRoomIds() throws IOException;

    boolean roomExists(String roomId) throws IOException;
}
