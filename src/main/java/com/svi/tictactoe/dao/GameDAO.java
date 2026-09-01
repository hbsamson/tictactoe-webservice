package com.svi.tictactoe.dao;

import com.svi.tictactoe.dto.GameRecordDTO;
import java.io.IOException;
import java.util.List;

public interface GameDAO {
    void saveMove(GameRecordDTO record) throws IOException;
    void addGameToPlayer(String playerId, String gameId) throws IOException;
    List<GameRecordDTO> readMoves(String gameId) throws IOException;
    List<String> readPlayerGames(String playerId) throws IOException;
    boolean playerExists(String playerId) throws IOException;
    boolean gameExists(String gameId) throws IOException;
}