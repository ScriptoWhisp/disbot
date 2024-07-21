package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.service.ExternalService;
import com.nqma.disbot.service.commands.Commands;
import com.nqma.disbot.service.commands.SlashCommand;
import com.nqma.disbot.service.player.GuildQueue;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

public class Next implements SlashCommand {

    @Override
    public String getName() {
        return Commands.NEXT.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        System.out.println("Next song");
        GuildQueue guildQueue = GuildQueue.getGuildQueue(event.getInteraction().getMember().get().getVoiceState().block());

        guildQueue.playNextSong();

        return event.reply().withEmbeds(EmbedCreateSpec.builder().description("Next song").build());
    }
}
