package com.svi.tictactoe.dao;

import com.svi.tictactoe.dto.RoomDTO;
import java.io.IOException;
import java.util.List;

public interface RoomGameDAO {
    void save(String roomId, String gameId, String createdDate) throws IOException;
    List<RoomDTO> findByRoomId(String roomId) throws IOException;
    List<String> findRoomIds() throws IOException;
    boolean exists(String roomId) throws IOException;
}
