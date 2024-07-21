package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.service.commands.Commands;
import com.nqma.disbot.service.commands.SlashCommand;
import com.nqma.disbot.service.player.GuildQueue;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.NonNull;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class Plist implements SlashCommand {
    @Override
    public String getName() {
        return Commands.PLIST.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        System.out.println("Playlist");
        return event.reply()
                .withEmbeds(GuildQueue.getGuildQueue(Objects.requireNonNull(event
                                .getInteraction()
                                .getMember()
                                .get()
                                .getVoiceState()
                                .block()))
                        .getQueueEmbed());
    }
}
