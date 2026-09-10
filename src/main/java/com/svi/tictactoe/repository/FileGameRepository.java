package com.svi.tictactoe.repository;

import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.PlayerGameDTO;
import com.svi.tictactoe.dto.RoomDTO;
import com.svi.tictactoe.services.FileStorageService;
import com.svi.tictactoe.services.impl.FileStorageServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Flat-file implementation of the game repository contract. */
public final class FileGameRepository implements GameRepository {
    private final FileStorageService fileStorage;

    public FileGameRepository() {
        this(new FileStorageServiceImpl());
    }

    public FileGameRepository(FileStorageService fileStorage) {
        this.fileStorage = Objects.requireNonNull(fileStorage, "fileStorage");
    }

    @Override
    public void saveMove(GameRecordDTO record) throws IOException {
        fileStorage.appendMoveToGame(record);
    }

    @Override
    public void savePlayerGame(GameRecordDTO record) throws IOException {
        fileStorage.appendGameToPlayer(record.getPlayerId(), record.getGameId());
    }

    @Override
    public void saveRoomGame(String roomId, String gameId, String createdDate) throws IOException {
        fileStorage.appendGameToRoom(roomId, gameId, createdDate);
    }

    @Override
    public List<GameRecordDTO> findMovesByGameId(String gameId) throws IOException {
        return fileStorage.readGameMoves(gameId);
    }

    @Override
    public List<PlayerGameDTO> findGamesByPlayerId(String playerId) throws IOException {
        List<PlayerGameDTO> games = new ArrayList<>();
        for (String gameId : fileStorage.readPlayerGames(playerId)) {
            games.add(toPlayerGame(playerId, gameId));
        }
        Collections.sort(games, new Comparator<PlayerGameDTO>() {
            @Override
            public int compare(PlayerGameDTO left, PlayerGameDTO right) {
                return valueOrDefault(right.getGameDate(), "")
                        .compareTo(valueOrDefault(left.getGameDate(), ""));
            }
        });
        return games;
    }

    @Override
    public String findPlayerNameByGameId(String gameId) throws IOException {
        return fileStorage.readPlayerName(gameId);
    }

    @Override
    public List<RoomDTO> findGamesByRoomId(String roomId) throws IOException {
        return fileStorage.readRoomGames(roomId);
    }

    @Override
    public List<String> findRoomIds() throws IOException {
        return fileStorage.readRoomIds();
    }

    @Override
    public boolean playerExists(String playerId) throws IOException {
        return fileStorage.playerExists(playerId);
    }

    @Override
    public boolean gameExists(String gameId) throws IOException {
        return fileStorage.gameExists(gameId);
    }

    @Override
    public boolean roomExists(String roomId) throws IOException {
        return fileStorage.roomExists(roomId);
    }

    private PlayerGameDTO toPlayerGame(String playerId, String gameId) throws IOException {
        GameRecordDTO matchingMove = null;
        for (GameRecordDTO move : fileStorage.readGameMoves(gameId)) {
            if (playerId.equals(move.getPlayerId())) {
                matchingMove = move;
                break;
            }
        }

        if (matchingMove == null) {
            return new PlayerGameDTO(gameId, "Player", "ren", null);
        }
        return new PlayerGameDTO(
                gameId,
                valueOrDefault(matchingMove.getPlayerName(), "Player"),
                valueOrDefault(matchingMove.getPlayerAvatar(), "ren"),
                matchingMove.getDateSaved());
    }

    private String valueOrDefault(String value, String defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
    }
}
