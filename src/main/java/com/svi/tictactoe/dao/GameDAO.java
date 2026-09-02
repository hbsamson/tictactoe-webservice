package com.svi.tictactoe.dao;

import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.RoomDTO;
import java.io.IOException;
import java.util.List;

public interface GameDAO {
    void saveMove(GameRecordDTO record) throws IOException;
    void addGameToPlayer(String playerId, String gameId) throws IOException;
    void addGameToRoom(String roomCode, String gameId, String createdDate) throws IOException;
    List<GameRecordDTO> readMoves(String gameId) throws IOException;
    List<String> readPlayerGames(String playerId) throws IOException;
    String readPlayerName(String gameId) throws IOException;
    List<RoomDTO> readRoomGames(String roomCode) throws IOException;
    boolean playerExists(String playerId) throws IOException;
    boolean gameExists(String gameId) throws IOException;
    boolean roomExists(String roomCode) throws IOException;
}
