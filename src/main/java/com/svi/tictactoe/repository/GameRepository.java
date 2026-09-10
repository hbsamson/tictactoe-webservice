package com.svi.tictactoe.repository;

import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.PlayerGameDTO;
import com.svi.tictactoe.dto.RoomDTO;

import java.io.IOException;
import java.util.List;

/** Persistence operations for the game aggregate. */
public interface GameRepository {
    void saveMove(GameRecordDTO record) throws IOException;

    void savePlayerGame(GameRecordDTO record) throws IOException;

    void saveRoomGame(String roomId, String gameId, String createdDate) throws IOException;

    List<GameRecordDTO> findMovesByGameId(String gameId) throws IOException;

    List<PlayerGameDTO> findGamesByPlayerId(String playerId) throws IOException;

    String findPlayerNameByGameId(String gameId) throws IOException;

    List<RoomDTO> findGamesByRoomId(String roomId) throws IOException;

    List<String> findRoomIds() throws IOException;

    boolean playerExists(String playerId) throws IOException;

    boolean gameExists(String gameId) throws IOException;

    boolean roomExists(String roomId) throws IOException;
}
