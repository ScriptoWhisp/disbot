package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.repository.GuildSetting;
import com.nqma.disbot.service.commands.Commands;
import com.nqma.disbot.service.commands.Message;
import com.nqma.disbot.service.commands.SlashCommand;
import com.nqma.disbot.service.guildsettings.GuildSettingService;
import com.nqma.disbot.service.player.GuildQueue;
import com.nqma.disbot.service.responsers.MessageSender;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class Play implements SlashCommand {

    private final GuildSettingService guildSettingService;

    public Play(GuildSettingService guildSettingService) {
        this.guildSettingService = guildSettingService;
    }

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
            return MessageSender.replyToEphemeral(event, Message.NOT_IN_VOICE_CHANNEL);
        }

        GuildQueue guildQueue = GuildQueue.getGuildQueue(voiceState);
        if (guildQueue == null) {
            Optional<GuildSetting> guildSetting = guildSettingService.findGuildSettingById(event.getInteraction().getGuildId().get().asLong());
            boolean inGameMode = false;
            if (guildSetting.isPresent()) inGameMode = guildSetting.get().isInGameMode();
            guildQueue = new GuildQueue(voiceState, event.getInteraction().getChannel().block(), inGameMode);
        }


        if (voiceState.getChannel().block().getId().asLong() != guildQueue.getChannel().getId().asLong()) {
            return MessageSender.replyToEphemeral(event, Message.NOT_IN_VOICE_CHANNEL);
        }

        try {
            String songInfo = guildQueue.addSong(link, member);
            if (songInfo == null) return MessageSender.replyToEphemeral(event, Message.QUEUE_FULL); // null means queue is full
            return MessageSender.replyTo(event, Message.ADDED_TO_QUEUE, songInfo);
        } catch (UnsupportedOperationException e) {
            return MessageSender.replyToEphemeral(event, Message.UNSUPPORTED, e.getMessage());
        }
    }
}
