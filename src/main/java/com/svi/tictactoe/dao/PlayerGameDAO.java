package com.svi.tictactoe.dao;

import java.io.IOException;
import java.util.List;

public interface PlayerGameDAO {
    void save(String playerId, String gameId) throws IOException;
    List<String> findByPlayerId(String playerId) throws IOException;
    boolean exists(String playerId) throws IOException;
}
