package com.nqma.disbot.service.commands.listener;

import com.nqma.disbot.service.commands.SlashCommand;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.Getter;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Getter
public class SlashCommandListener {

    private final Collection<SlashCommand> slashCommands;

    public SlashCommandListener(List<SlashCommand> slashCommands, GatewayDiscordClient client) {
        this.slashCommands = slashCommands;
        System.out.println("SlashCommandListener" + slashCommands.toString());

        client.on(ChatInputInteractionEvent.class, this::handle).subscribe();
    }

    public Mono<Void> handle(ChatInputInteractionEvent event) {
        System.out.println(event.getCommandName());
        return findCommand(event.getCommandName())
                .map(command -> command.handle(event))
                .orElse(Mono.empty());
    }

    public Optional<SlashCommand> findCommand(String commandName) {
        return slashCommands.stream()
                .filter(command -> command.getName().equalsIgnoreCase(commandName))
                .findFirst();

    }
}
