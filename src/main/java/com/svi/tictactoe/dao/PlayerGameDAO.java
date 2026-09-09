package com.svi.tictactoe.dao;

import java.io.IOException;
import java.util.List;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.PlayerGameDTO;

public interface PlayerGameDAO {
    void save(GameRecordDTO record) throws IOException;
    List<PlayerGameDTO> findByPlayerId(String playerId) throws IOException;
    boolean exists(String playerId) throws IOException;
}
