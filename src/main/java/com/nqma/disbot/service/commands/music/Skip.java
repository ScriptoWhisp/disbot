package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.service.commands.Commands;
import com.nqma.disbot.service.commands.Message;
import com.nqma.disbot.service.commands.SlashCommand;
import com.nqma.disbot.service.player.GuildQueue;
import com.nqma.disbot.service.responsers.MessageSender;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public class Skip implements SlashCommand {
    @Override
    public String getName() {
        return Commands.SKIP.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        System.out.println("Skip playlist");
        GuildQueue guildQueue = GuildQueue.getGuildQueue(event.getInteraction().getGuildId().get().asLong());
        if (guildQueue == null || guildQueue.getQueue().isEmpty()) {
            return MessageSender.replyToEphemeral(event, Message.NO_SONGS_IN_QUEUE);
        } else if (guildQueue.getCurrentPlaylist() == null) {
            return MessageSender.replyToEphemeral(event, Message.NO_CURRENT_PLAYLIST);
        }

        guildQueue.skip();

        return MessageSender.replyToEphemeral(event, Message.NEXT_SONG, guildQueue.getCurrentSong().toString());
    }
}
