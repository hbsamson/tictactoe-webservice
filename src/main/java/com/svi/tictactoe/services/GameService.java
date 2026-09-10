package com.svi.tictactoe.services;

import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.response.GameListResponseDTO;
import com.svi.tictactoe.dto.response.GameRecordListResponseDTO;
import com.svi.tictactoe.dto.response.SaveResponseDTO;
import com.svi.tictactoe.dto.RoomDTO;
import java.util.List;

public interface GameService {
    SaveResponseDTO saveMove(String roomId, String gameId, GameRecordDTO record);
    GameRecordListResponseDTO getGameDetails(String gameId);
    GameListResponseDTO getPlayerGames(String playerId);
    SaveResponseDTO saveRoom(RoomDTO roomRecord);
    
    void saveGameToRoom(String roomCode, String gameId, String createdDate);

    List<RoomDTO> getRoomGames(String roomCode);
    List<String> getRoomIds();
}
