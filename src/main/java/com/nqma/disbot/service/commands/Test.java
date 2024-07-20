package com.nqma.disbot.service.commands;

import com.nqma.responseservice.service.ExternalService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public class Test implements SlashCommand {

    @Override
    public String getName() {
        return Commands.TEST.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        System.out.println("requesting audio service");
        ExternalService externalService = new ExternalService();
        externalService.test();
        return null;
    }
}
