package com.nqma.disbot.service.commands;

import discord4j.core.spec.EmbedCreateSpec;

public enum Message {

    NOT_IN_VOICE_CHANNEL("You must be in a voice channel to use this command."),
    NOT_IN_SAME_VOICE_CHANNEL("You must be in the same voice channel as the bot to use this command."),
    NO_SONGS_IN_QUEUE("There are no songs in the queue."),
    NO_SONGS_PLAYING("There are no songs currently playing."),
    NO_PERMISSIONS("You do not have permission to use this command."),
    NO_RESULTS("No results found."),

    PLAYING("Now playing: %s"),
    ADDED_TO_QUEUE("Added to queue: %s"),
    PAUSED("Paused"),
    RESUMED("Resumed"),
    STOPPED("Stopped");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }

    public EmbedCreateSpec getEmbed() {
        return EmbedCreateSpec.builder().description(message).build();
    }

    @Override
    public String toString() {
        return message;
    }
}
