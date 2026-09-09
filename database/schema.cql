CREATE KEYSPACE IF NOT EXISTS batch1_2026_trainees
    WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE batch1_2026_trainees;
CREATE TABLE samson_room_table (
    room_id text,
    game_date timestamp,
    game_id uuid,
    PRIMARY KEY ((room_id), game_date, game_id)
) WITH CLUSTERING ORDER BY (game_date DESC);

CREATE TABLE samson_games_table (
    player_id uuid,
    game_date timestamp,
    game_id uuid,
    player_name text,
    player_avatar text,
    PRIMARY KEY ((player_id), game_date, game_id)
) WITH CLUSTERING ORDER BY (game_date DESC);

CREATE TABLE samson_moves_table (
    game_id uuid,
    move_number int,
    player_id uuid,
    player_name text,
    symbol text,
    location int,
    date_saved timestamp,
    PRIMARY KEY ((game_id), move_number)
) WITH CLUSTERING ORDER BY (move_number ASC);