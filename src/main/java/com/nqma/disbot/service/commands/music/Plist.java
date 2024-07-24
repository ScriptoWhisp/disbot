package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.service.commands.Commands;
import com.nqma.disbot.service.commands.SlashCommand;
import com.nqma.disbot.service.player.GuildQueue;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public class Plist implements SlashCommand {
    @Override
    public String getName() {
        return Commands.PLIST.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        System.out.println("Playlist");
        return event.reply()
                .withEmbeds(GuildQueue
                        .getGuildQueue(event
                                .getInteraction()
                                .getGuildId()
                                .get()
                                .asLong())
                        .getQueueEmbed());
    }
}
