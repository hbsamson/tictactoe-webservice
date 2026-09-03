package com.svi.tictactoe.services;

import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.response.GameListResponseDTO;
import com.svi.tictactoe.dto.response.GameRecordListResponseDTO;
import com.svi.tictactoe.dto.response.SaveResponseDTO;
import com.svi.tictactoe.dto.response.ServiceResponseDTO;
import com.svi.tictactoe.dto.RoomDTO;
import java.io.IOException;
import java.util.List;

public interface GameService {
    ServiceResponseDTO<SaveResponseDTO> saveMove(GameRecordDTO record);
    ServiceResponseDTO<GameRecordListResponseDTO> getGameDetails(String gameId);
    ServiceResponseDTO<GameListResponseDTO> getPlayerGames(String playerId);
    
    void saveGameToRoom(String roomCode, String gameId, String createdDate) throws IOException;

    List<RoomDTO> getRoomGames(String roomCode) throws IOException;
}
