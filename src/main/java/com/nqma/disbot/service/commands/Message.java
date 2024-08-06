package com.nqma.disbot.service.commands;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import lombok.Getter;

public enum Message {

    NOT_IN_VOICE_CHANNEL("You must be in a voice channel to use this command.", Color.RED),
    NOT_IN_SAME_VOICE_CHANNEL("You must be in the same voice channel as the bot to use this command.", Color.RED),
    NO_SONGS_IN_QUEUE("There are no songs in the queue.", Color.RED),
    NO_SONGS_PLAYING("There are no songs currently playing.", Color.RED),
    NO_PERMISSIONS("You do not have permission to use this command.", Color.RED),
    NO_RESULTS("No results found.", Color.RED),
    QUEUE_FULL("The queue is full. We are very sorry for this limitation, we already working on it", Color.RED),
    UNSUPPORTED("Unsupported operation: %s", Color.RED),
    NO_CURRENT_PLAYLIST("There is no current playlist.", Color.RED),
    NOT_IN_CHANNEL("You have to be in %s to use this command.", Color.RED),
    SETUP_ERROR("An error occurred while setting up the bot. Please try again.", Color.RED),

    PLAYING("Now playing: %s"),
    ADDED_TO_QUEUE("Added to queue: %s"),
    NEXT_SONG("Next song: %s"),
    PAUSED("Paused", Color.YELLOW),
    RESUMED("Resumed", Color.GREEN),
    STOPPED("Stopped", Color.GREEN),

    SETUP("Setup complete.", Color.DISCORD_WHITE);

    @Getter
    private final String message;
    private final Color color;

    Message(String message, Color color) {
        this.message = message;
        this.color = color;
    }

    Message(String message) {
        this(message, Color.BLUE);
    }

    public String format(Object... args) {
        return String.format(message, args);
    }

    public EmbedCreateSpec formatEmbed(Object... args) {
        return EmbedCreateSpec.builder().description(String.format(message, args)).color(color).build();
    }

    public EmbedCreateSpec getEmbed() {
        return EmbedCreateSpec.builder().description(message).color(color).build();
    }

    @Override
    public String toString() {
        return message;
    }
}
