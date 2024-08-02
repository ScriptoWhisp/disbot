package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.service.commands.Commands;
import com.nqma.disbot.service.commands.SlashCommand;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.presence.Activity;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class Status implements SlashCommand {
    @Override
    public String getName() {
        return Commands.STATUS.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        event.getInteraction().getMember().get().getPresence().subscribe(presence -> {
            System.out.println("Presence: ");
            Optional<Activity> activityOptional = presence.getActivity();
            activityOptional.ifPresent(activity -> {
                // Log the activity
                System.out.println("Current activity: " + activity.getName());
            });
        }, error -> {
            // Handle errors
            System.err.println("Error retrieving presence: " + error.getMessage());
        });
        return event.reply().withContent("asd").withEphemeral(true);
    }
}
