package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.service.commands.Commands;
import com.nqma.disbot.service.commands.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Member;
import reactor.core.publisher.Mono;

public class Pause implements SlashCommand {
    @Override
    public String getName() {
        return Commands.PAUSE.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return null;
    }
}
