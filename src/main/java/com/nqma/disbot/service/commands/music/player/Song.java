package com.nqma.disbot.service.commands.music.player;

import discord4j.core.spec.EmbedCreateSpec;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Song {

    private String title;
    private String url;
    private String duration;
    private String thumbnail;
    private String author;
    private String id;
    private String guildId;

    public EmbedCreateSpec toEmbed() {
        return EmbedCreateSpec.builder()
                .title(title)
                .url(url)
                .description("Author: " + author + "\nDuration: " + duration)
                .thumbnail(thumbnail)
                .build();
    }
}
