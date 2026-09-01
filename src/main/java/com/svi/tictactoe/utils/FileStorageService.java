package com.svi.tictactoe.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

import com.svi.tictactoe.dto.GameRecordDTO;

/**
 * Utility class for persisting game records to flat files.
 * 
 * File structure:
 * - /records/playerid/<playerId>.txt: newline-delimited list of gameIds
 * - /records/gameid/<gameId>.txt: comma-delimited moves (CSV format)
 */
public class FileStorageService {
    private static final String RECORDS_DIR = "records";
    private static final String PLAYERID_SUBDIR = "playerid";
    private static final String GAMEID_SUBDIR = "gameid";

    /**
     * Get or create the /records directory at project root.
     * Returns the directory path.
     */
    public static Path getRecordsDirectory() throws IOException {
        Path recordsPath = Paths.get(RECORDS_DIR).toAbsolutePath();
        Files.createDirectories(recordsPath);
        return recordsPath;
    }

    /**
     * Get or create the /records/playerid directory.
     * Returns the directory path.
     */
    public static Path getPlayerIdDirectory() throws IOException {
        Path playerIdPath = getRecordsDirectory().resolve(PLAYERID_SUBDIR);
        Files.createDirectories(playerIdPath);
        return playerIdPath;
    }

    /**
     * Get or create the /records/gameid directory.
     * Returns the directory path.
     */
    public static Path getGameIdDirectory() throws IOException {
        Path gameIdPath = getRecordsDirectory().resolve(GAMEID_SUBDIR);
        Files.createDirectories(gameIdPath);
        return gameIdPath;
    }

    /**
     * Append a move to a game's record file.
     * Creates the file if it doesn't exist.
     * 
     * @param gameRecord The move record to save
     * @throws IOException if file I/O fails
     */
    public static void appendMoveToGame(GameRecordDTO gameRecord) throws IOException {
        Path gameIdDir = getGameIdDirectory();
        String gameId = gameRecord.getGameid();
        Path gameFile = gameIdDir.resolve(gameId + ".txt");
        
        String csvLine = gameRecord.toCSV();
        
        // Append line to file (create if doesn't exist)
        Files.write(gameFile, (csvLine + "\n").getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /**
     * Add a gameId to a player's game list file.
     * Creates the file if it doesn't exist.
     * Avoids duplicate entries.
     * 
     * @param playerId The player's ID
     * @param gameId The game ID to add
     * @throws IOException if file I/O fails
     */
    public static void appendGameToPlayer(String playerId, String gameId) throws IOException {
        Path playerIdDir = getPlayerIdDirectory();
        Path playerFile = playerIdDir.resolve(playerId + ".txt");
        
        // Check if gameId already exists in file
        Set<String> existingGames = new HashSet<>();
        if (Files.exists(playerFile)) {
            List<String> lines = Files.readAllLines(playerFile, StandardCharsets.UTF_8);
            existingGames.addAll(lines);
        }
        
        // Add only if not already present
        if (!existingGames.contains(gameId)) {
            Files.write(playerFile, (gameId + "\n").getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }

    /**
     * Read all gameIds for a player.
     * Returns empty list if player file doesn't exist.
     * 
     * @param playerId The player's ID
     * @return List of gameIds played by this player
     * @throws IOException if file I/O fails
     */
    public static List<String> readPlayerGames(String playerId) throws IOException {
        Path playerIdDir = getPlayerIdDirectory();
        Path playerFile = playerIdDir.resolve(playerId + ".txt");
        
        if (!Files.exists(playerFile)) {
            return Collections.emptyList();
        }
        
        List<String> lines = Files.readAllLines(playerFile, StandardCharsets.UTF_8);
        
        // Filter out empty lines
        List<String> games = new ArrayList<>();
        for (String line : lines) {
            if (line != null && !line.trim().isEmpty()) {
                games.add(line.trim());
            }
        }
        
        return games;
    }

    /**
     * Read all moves for a game.
     * Returns empty list if game file doesn't exist.
     * Moves are returned in the order they appear in the file (date ascending per spec).
     * 
     * @param gameId The game's ID
     * @return List of GameRecord objects for this game
     * @throws IOException if file I/O fails
     */
    public static List<GameRecordDTO> readGameMoves(String gameId) throws IOException {
        Path gameIdDir = getGameIdDirectory();
        Path gameFile = gameIdDir.resolve(gameId + ".txt");
        
        if (!Files.exists(gameFile)) {
            return Collections.emptyList();
        }
        
        List<GameRecordDTO> moves = new ArrayList<>();
        List<String> lines = Files.readAllLines(gameFile, StandardCharsets.UTF_8);
        
        for (String line : lines) {
            if (line != null && !line.trim().isEmpty()) {
                try {
                    GameRecordDTO record = GameRecordDTO.fromCSV(line.trim());
                    moves.add(record);
                } catch (IllegalArgumentException e) {
                    // Skip malformed lines
                    System.err.println("Skipping malformed game record line: " + line);
                }
            }
        }
        
        return moves;
    }

    /**
     * Check if a player has any recorded games.
     * 
     * @param playerId The player's ID
     * @return true if player file exists and has content
     * @throws IOException if file I/O fails
     */
    public static boolean playerExists(String playerId) throws IOException {
        Path playerIdDir = getPlayerIdDirectory();
        Path playerFile = playerIdDir.resolve(playerId + ".txt");
        return Files.exists(playerFile) && Files.size(playerFile) > 0;
    }

    /**
     * Check if a game record exists.
     * 
     * @param gameId The game's ID
     * @return true if game file exists and has content
     * @throws IOException if file I/O fails
     */
    public static boolean gameExists(String gameId) throws IOException {
        Path gameIdDir = getGameIdDirectory();
        Path gameFile = gameIdDir.resolve(gameId + ".txt");
        return Files.exists(gameFile) && Files.size(gameFile) > 0;
    }
}
