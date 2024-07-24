package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.service.commands.Commands;
import com.nqma.disbot.service.commands.Message;
import com.nqma.disbot.service.commands.SlashCommand;
import com.nqma.disbot.service.player.GuildQueue;
import com.nqma.disbot.service.responsers.MessageSender;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import reactor.core.publisher.Mono;

public class Pause implements SlashCommand {
    @Override
    public String getName() {
        return Commands.PAUSE.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        System.out.println("Pause command");

        Member member = event.getInteraction().getMember().orElse(null);

        VoiceState voiceState = member.getVoiceState().block();
        if (voiceState == null) {
            return MessageSender.replyTo(event, Message.NOT_IN_VOICE_CHANNEL);
        }

        GuildQueue guildQueue = GuildQueue.getGuildQueue(voiceState);
        if (guildQueue == null) {
            return MessageSender.replyTo(event, Message.NO_SONGS_PLAYING);
        }

        if (voiceState.getChannel().block().getId().asLong() != guildQueue.getChannel().getId().asLong()) {
            return MessageSender.replyTo(event, Message.NOT_IN_VOICE_CHANNEL);
        }

        System.out.println("pausing");
        if (guildQueue.pause()) return MessageSender.replyTo(event, Message.PAUSED);
        return MessageSender.replyTo(event, Message.RESUMED);
    }
}
