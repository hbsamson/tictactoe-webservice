package com.svi.tictactoe.services;

import com.svi.tictactoe.dao.GameDAO;
import com.svi.tictactoe.dao.impl.GameDAOImpl;
import com.svi.tictactoe.dto.GameRecordDTO;
import java.io.IOException;

public class GameService {
    private GameDAO gameDAO = new GameDAOImpl();

    public void saveMove(GameRecordDTO record) throws IOException {
        gameDAO.saveMove(record);
        gameDAO.addGameToPlayer(record.getPlayerid(), record.getGameid());
    }
}