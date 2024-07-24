package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.service.commands.Commands;
import com.nqma.disbot.service.commands.SlashCommand;
import com.nqma.disbot.service.responsers.MessageSender;
import com.nqma.disbot.utils.MemoryUtil;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public class Status implements SlashCommand {
    @Override
    public String getName() {
        return Commands.STATUS.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.reply().withEmbeds(MemoryUtil.getMemoryUsage());
    }
}
