package com.svi.tictactoe.dao;

import com.svi.tictactoe.dto.GameRecordDTO;
import java.io.IOException;
import java.util.List;

public interface GameMoveDAO {
    void save(GameRecordDTO record) throws IOException;
    List<GameRecordDTO> findByGameId(String gameId) throws IOException;
    String findPlayerName(String gameId) throws IOException;
    boolean exists(String gameId) throws IOException;
}
