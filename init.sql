-- init.sql

CREATE TABLE IF NOT EXISTS guild_settings (
    guild_id BIGINT PRIMARY KEY, -- Mapping to Long type in Java
    channel_id BIGINT,           -- Mapping to Long type in Java
    in_game_mode BOOLEAN         -- Mapping to boolean type in Java
);

-- Add any other tables and columns you need