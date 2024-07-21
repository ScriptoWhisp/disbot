package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.initconfig.AudioConfiguration;
import com.nqma.disbot.service.commands.Message;
import com.nqma.disbot.service.commands.SlashCommand;
import com.nqma.disbot.initconfig.InitialConfiguration;
import com.nqma.disbot.service.ExternalService;
import com.nqma.disbot.service.commands.Commands;
import com.nqma.disbot.service.player.GuildQueue;
import com.nqma.disbot.service.player.LavaPlayerAudioProvider;
import com.nqma.disbot.service.player.TrackScheduler;
import com.nqma.disbot.service.responsers.MessageSender;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.voice.AudioProvider;
import reactor.core.publisher.Mono;

public class Play implements SlashCommand {

    @Override
    public String getName() {
        return Commands.PLAY.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {



        String link = event.getOption("url").orElse(null).getValue().orElse(null).asString();
        Member member = event.getInteraction().getMember().orElse(null);

        VoiceState voiceState = member.getVoiceState().block();
        if (voiceState == null) {
            return MessageSender.replyTo(event, Message.NOT_IN_VOICE_CHANNEL);
        }

        GuildQueue guildQueue = GuildQueue.getGuildQueue(voiceState);
        if (guildQueue == null) guildQueue = new GuildQueue(voiceState);

        if (voiceState.getChannel().block().getId().asLong() != guildQueue.getChannel().getId().asLong()) {
            return MessageSender.replyTo(event, Message.NOT_IN_VOICE_CHANNEL);
        }

        guildQueue.addSong(link);

        return MessageSender.replyTo(event, Message.PLAYING, link);
    }
}
