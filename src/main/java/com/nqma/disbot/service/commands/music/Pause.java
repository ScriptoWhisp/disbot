package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.service.commands.Commands;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Member;
import reactor.core.publisher.Mono;

public class Pause implements MusicSlashCommand {
    @Override
    public String getName() {
        return Commands.PAUSE.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return null;
    }

    @Override
    public void run(Member member, String link) {

    }
}
