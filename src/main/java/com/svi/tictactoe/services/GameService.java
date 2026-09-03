package com.svi.tictactoe.services;

import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.response.GameListResponseDTO;
import com.svi.tictactoe.dto.response.GameRecordListResponseDTO;
import com.svi.tictactoe.dto.RoomDTO;
import java.io.IOException;
import java.util.List;

public interface GameService {
    void saveMove(GameRecordDTO record) throws IOException;
    void saveGameToRoom(String roomCode, String gameId, String createdDate) throws IOException;
    GameListResponseDTO getPlayerGames(String playerId) throws IOException;
    GameRecordListResponseDTO getGameDetails(String gameId) throws IOException;
    List<RoomDTO> getRoomGames(String roomCode) throws IOException;
}
