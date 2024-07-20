package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.service.ExternalService;
import com.nqma.disbot.service.commands.Commands;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Member;
import reactor.core.publisher.Mono;

public class Next implements MusicSlashCommand {

    @Override
    public String getName() {
        return Commands.NEXT.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        return event.reply("Next song");
    }

    @Override
    public void run(Member member, String link) {
        System.out.println("Next song for " + member.getUsername() + " with link " + link);

        ExternalService externalService = new ExternalService();
        externalService.getNextSong();
    }
}
