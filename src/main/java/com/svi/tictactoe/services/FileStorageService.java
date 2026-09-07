package com.svi.tictactoe.services;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

import com.svi.tictactoe.dto.GameRecordDTO;
import com.svi.tictactoe.dto.RoomDTO;

/**
 * File structure:
 * - /records/playerid/<playerId>.txt: newline-delimited list of gameIds
 * - /records/gameid/<gameId>.txt: comma-delimited moves
 * - /records/roomid/<roomCode>.txt: newline-delimited list of gameIds with dates
 * - /records/roomkey/<roomKey>.txt: newline-delimited list of gameIds for a rematch group
 **/
public interface FileStorageService {
    Path getRecordsDirectory() throws IOException;
    Path getPlayerIdDirectory() throws IOException;
    Path getGameIdDirectory() throws IOException;
    Path getRoomIdDirectory() throws IOException;
    Path getRoomKeyDirectory() throws IOException;

    void appendMoveToGame(GameRecordDTO gameRecord) throws IOException;
    void appendGameToPlayer(String playerId, String gameId) throws IOException;
    List<String> readPlayerGames(String playerId) throws IOException;
    String readPlayerName(String gameId) throws IOException;
    List<String> readGames(String gameId) throws IOException;
    List<GameRecordDTO> readGameMoves(String gameId) throws IOException;
    boolean playerExists(String playerId) throws IOException;
    boolean gameExists(String gameId) throws IOException;
    void appendGameToRoom(String roomCode, String gameId, String createdDate) throws IOException;
    void appendGameIdsToRoomKey(String roomKey, List<String> gameIds) throws IOException;
    List<RoomDTO> readRoomGames(String roomCode) throws IOException;
    boolean roomExists(String roomCode) throws IOException;
}
