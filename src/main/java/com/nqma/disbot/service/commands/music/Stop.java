package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.service.commands.Commands;
import com.nqma.disbot.service.commands.Message;
import com.nqma.disbot.service.commands.SlashCommand;
import com.nqma.disbot.service.player.GuildQueue;
import com.nqma.disbot.service.responsers.MessageSender;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public class Stop implements SlashCommand {
    @Override
    public String getName() {
        return Commands.STOP.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        GuildQueue.removeAndClearGuildQueue(event.getInteraction().getGuildId().get().asLong());
        return MessageSender.replyTo(event, Message.STOPPED);
    }

}
