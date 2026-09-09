package com.svi.tictactoe.dao.impl;

import com.svi.tictactoe.dao.GameDAO;
import com.svi.tictactoe.dao.GameMoveDAO;
import com.svi.tictactoe.dao.PlayerGameDAO;
import com.svi.tictactoe.dao.RoomGameDAO;
import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.RoomDTO;
import com.svi.tictactoe.dto.PlayerGameDTO;
import com.svi.tictactoe.connection.CassandraConnection;
import com.datastax.driver.core.Session;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/** Compatibility facade that delegates each operation to its table-specific DAO. */
public final class GameDAOImpl implements GameDAO {
    private final GameMoveDAO gameMoves;
    private final PlayerGameDAO playerGames;
    private final RoomGameDAO roomGames;

    public GameDAOImpl() {
        this(CassandraConnection.getInstance().getSession());
    }

    public GameDAOImpl(Session session) {
        this(   new GameMoveDAOImpl(session),
                new PlayerGameDAOImpl(session),
                new RoomGameDAOImpl(session));
    }

    public GameDAOImpl(GameMoveDAO gameMoves, PlayerGameDAO playerGames, RoomGameDAO roomGames) {
        this.gameMoves = Objects.requireNonNull(gameMoves, "gameMoves");
        this.playerGames = Objects.requireNonNull(playerGames, "playerGames");
        this.roomGames = Objects.requireNonNull(roomGames, "roomGames");
    }

    @Override
    public void saveMove(GameRecordDTO record) throws IOException {
        gameMoves.save(record);
    }

    @Override
    public void addGameToPlayer(GameRecordDTO record) throws IOException {
        playerGames.save(record);
    }

    @Override
    public void addGameToRoom(String roomCode, String gameId, String createdDate) throws IOException {
        roomGames.save(roomCode, gameId, createdDate);
    }

    @Override
    public List<GameRecordDTO> readMoves(String gameId) throws IOException {
        return gameMoves.findByGameId(gameId);
    }

    @Override
    public List<PlayerGameDTO> readPlayerGames(String playerId) throws IOException {
        return playerGames.findByPlayerId(playerId);
    }

    @Override
    public String readPlayerName(String gameId) throws IOException {
        return gameMoves.findPlayerName(gameId);
    }

    @Override
    public List<RoomDTO> readRoomGames(String roomCode) throws IOException {
        return roomGames.findByRoomId(roomCode);
    }

    @Override
    public List<String> readRoomIds() throws IOException {
        return roomGames.findRoomIds();
    }

    @Override
    public boolean playerExists(String playerId) throws IOException {
        return playerGames.exists(playerId);
    }

    @Override
    public boolean gameExists(String gameId) throws IOException {
        return gameMoves.exists(gameId);
    }

    @Override
    public boolean roomExists(String roomCode) throws IOException {
        return roomGames.exists(roomCode);
    }
}
