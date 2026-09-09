# Tic Tac Toe web service

This Jakarta EE 8 REST service persists Tic Tac Toe moves, player history, rooms, and rematch groups as flat files. It is packaged as a WAR and runs with Payara Micro.

## Running

Requirements: Java 8+ (the Maven Wrapper is included), Docker, and Docker Compose.

Start the local Cassandra database first. The one-shot `cassandra-init` service
applies `database/schema.cql` after Cassandra is healthy:

```bash
docker compose up -d cassandra cassandra-init
```

The service connects to `localhost:9042` and the `batch1_2026_trainees` keyspace by default.
Override `CASSANDRA_IP`, `CASSANDRA_PORT`, `CASSANDRA_KEYSPACE`, or
`CASSANDRA_TABLE` with environment variables or Java system properties when needed.

```bash
./mvnw clean package payara-micro:start
```

On Windows:

```powershell
.\mvnw.cmd clean package payara-micro:start
```

When run with the Maven Payara Micro command, the configured context root is `/`; REST endpoints are rooted at:

```text
http://localhost:8080/api
```

The checked-in frontend currently defaults to `http://localhost:8080/tictactoe-webservice/api` in `tictactoe/js/api.js`, which is the URL to use when the WAR is deployed by Eclipse or an application server under the artifact name. Set `window.TICTACTOE_WEBSERVICE_API_BASE` before the frontend loads if the WAR is deployed at another context root. The frontend and backend must agree on this base URL.

Authentication is not implemented. JSON requests use `Content-Type: application/json`. CORS is configured by `FRONTEND_URLS` (default: `http://localhost:5500,http://127.0.0.1:5500`).

## REST API

### Save a move

```http
POST /api/game/save
```

```json
{
  "gameId": "1dffda51-f610-4adc-8720-8424dbab8bdc",
  "playerId": "cacc26e6-434f-478f-be6b-a3abf141da9e",
  "playerName": "Ann",
  "symbol": "X",
  "location": "0",
  "dateSaved": "2026-09-07T01:00:00Z"
}
```

`playerName` is optional. `gameId` and `playerId` must be canonical UUIDs; `symbol` must be `X` or `O`; `location` must be `0` through `8`; and `dateSaved` must be non-empty. The service does not generate `dateSaved`. A location cannot be saved twice in the same game.

Responses:

- `200`: `{ "msg": "Record saved" }`
- `401`: `{ "msg": "Invalid gameId format" }` or `{ "msg": "Invalid playerId format" }` for invalid UUIDs.
- `400`: `{ "msg": "Invalid symbol" }` for an invalid symbol; otherwise `{ "msg": "Record could not be saved" }`
- `409`: `{ "msg": "Location is already occupied." }`
- `500`: `{ "msg": "The server ran into an unexpected exception" }`

On success, the move is appended to the game file and the game ID is added to the player's history without duplication.

### List a player's games

```http
GET /api/player/{playerId}/games
```

There is no request body. `playerId` must be a canonical UUID.

```json
{
  "list": [
    { "id": "cacc26e6-434f-478f-be6b-a3abf141da9e", "playerName": "Ann" }
  ],
  "msg": "Player games records found"
}
```

`id` is the game ID. `playerName` is taken from the first named move and may be `null`.

- `400`: invalid player ID (`Invalid playerId format.`)
- `404`: player has no recorded history (`Player ID not found`)
- `500`: `{ "msg": "The server ran into an unexpected exception" }`

The supplied specification says `402` for a missing player record; the implementation uses the correct `404 Not Found` status.

### Get a game's move records

```http
GET /api/game/{gameId}
```

The path parameter is `gameId`, not `playerId` as shown in the supplied specification. It must be a canonical UUID.

```json
{
  "list": [
    {
      "gameId": "1dffda51-f610-4adc-8720-8424dbab8bdc",
      "playerId": "cacc26e6-434f-478f-be6b-a3abf141da9e",
      "playerName": "Ann",
      "symbol": "X",
      "location": "0",
      "dateSaved": "2021-11-11T01:00:00Z"
    }
  ],
  "msg": "Records found"
}
```

Records are returned in file/save order; the service does not sort by `dateSaved`. Thus clients should submit moves chronologically if they need chronological replay.

- `400`: invalid game ID (`Invalid gameId format`)
- `404`: game file not found (`Game record not found`)
- `500`: `{ "msg": "The server ran into an unexpected exception" }`

### Save room/rematch games

There is one room-save operation:

```http
POST /api/room/save
```

```json
{
  "roomId": "ABCDEF",
  "gameIds": ["1dffda51-f610-4adc-8720-8424dbab8bdc"]
}
```

`roomId` is alphanumeric and 4–6 characters; every game ID must be a canonical UUID. IDs are deduplicated in insertion order and saved under `records/roomid/<roomId>.txt`.

The request is deserialized into `RoomDTO`. The service creates one `createdDate` timestamp for the save operation and persists `gameId,createdDate` for each supplied game. The same `RoomDTO` is used for room responses, where `gameId` and `createdDate` describe an individual stored entry.

- `200`: `{ "msg": "Room games saved" }`
- `400`: `{ "msg": "Invalid roomId format" }` for an invalid room record, or `{ "msg": "Invalid gameIds format" }` for an invalid game ID.
- `500`: `{ "msg": "The server ran into an unexpected exception" }`

### Get games in a room

```http
GET /api/room/{roomId}
```

`roomId` is alphanumeric and 4–6 characters. A successful response (`200`) is an array:

```json
[
  {
    "roomId": "ABCDEF",
    "gameId": "1dffda51-f610-4adc-8720-8424dbab8bdc",
    "createdDate": "2021-11-11T01:00:00Z"
  }
]
```

Invalid room codes return `400` (`Invalid room code.`); missing rooms return `404` (`Room record not found.`).

### Example endpoint

```http
GET /api/hello?name=Ann
```

Returns the sample Hello resource. A missing or blank name defaults to `world`. Can be used for checking if the webservices server is online.

## Flat-file storage

The default records root is `records`:

```text
records/
├── gameid/<gameId>.txt       # one CSV move record per line
├── playerid/<playerId>.txt   # one game ID per line
└── roomid/<roomId>.txt       # gameId,createdDate per line
```

Move records use either format:

```text
gameId,playerId,symbol,location,dateSaved
gameId,playerId,playerName,symbol,location,dateSaved
```

Line breaks are rejected in fields. Commas are allowed in `playerName`; that field is quoted and CSV-escaped when written, so names such as `Smith, Alice` can be read back safely. Saves are synchronized to prevent concurrent duplicate-location writes.

Storage and database settings can be overridden in `src/main/resources/config.properties`, system properties, or environment variables: `RECORDS_DIR`, `PLAYER_DIR`, `GAME_DIR`, `ROOM_DIR`, `ROOM_KEY_DIR`, `FRONTEND_URLS`, `CASSANDRA_IP`, `CASSANDRA_PORT`, `CASSANDRA_KEYSPACE`, and `CASSANDRA_TABLE`.

## Frontend integration

The companion frontend is at [Tictactoe Webservices UI](https://github.com/hbsamson/tictactoe/tree/feat/webservices). Its web-service integration is in `js/api.js` and `js/room/room-service.js`:

- `POST /api/game/save` is called after each non-spectator move.
- `GET /api/player/{playerId}/games` loads the history table.
- `GET /api/game/{gameId}` loads the move details for each game.
- `POST /api/room/save` associates the room's round IDs with its room ID.
- `GET /api/room/{roomId}` is available to retrieve room records, although the current history flow does not require it.

The frontend also includes the required History link, player-ID search, game table, move details, and replay button. It sorts retrieved moves by `dateSaved` before rendering/replay. Gameplay room creation, board polling, moves, and reset still use the separate legacy game server configured as `BASEGAME_API`; the `/api` service is the persistence/history service.

The frontend sends `dateSaved` in local `YYYY-MM-DD HH:mm:ss` form. This is accepted by the backend because the backend currently validates that it is non-empty and safe, rather than enforcing a particular timestamp format.

## Project layout

- `src/main/java/com/svi/tictactoe/resource`: REST resources
- `src/main/java/com/svi/tictactoe/services`: business logic
- `src/main/java/com/svi/tictactoe/dao`: persistence abstraction
- `src/main/java/com/svi/tictactoe/dto`: JSON request/response models
- `src/main/resources/config.properties`: storage and CORS configuration
- `database/schema.cql`: local Cassandra keyspace and table definitions
- `docker-compose.yml`: local Cassandra service and schema initializer
- `src/main/webapp`: WAR welcome page and static assets
