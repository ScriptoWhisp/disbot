package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.service.commands.Commands;
import com.nqma.disbot.service.commands.Message;
import com.nqma.disbot.service.commands.SlashCommand;
import com.nqma.disbot.service.player.GuildQueue;
import com.nqma.disbot.service.responsers.MessageSender;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class Next implements SlashCommand {

    @Override
    public String getName() {
        return Commands.NEXT.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        System.out.println("Next song");
        GuildQueue guildQueue = GuildQueue.getGuildQueue(event.getInteraction().getGuildId().get().asLong());
        if (guildQueue == null) {
            return MessageSender.replyToEphemeral(event, Message.NO_SONGS_IN_QUEUE);
        }

        if (guildQueue.getQueue().isEmpty()) {
            return MessageSender.replyToEphemeral(event, Message.NO_SONGS_IN_QUEUE);
        }

        guildQueue.playNextSong();

        return MessageSender.replyToEphemeral(event, Message.NEXT_SONG, guildQueue.getCurrentSong().toString());
    }
}
