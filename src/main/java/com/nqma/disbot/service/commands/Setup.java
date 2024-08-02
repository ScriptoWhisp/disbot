package com.nqma.disbot.service.commands;

import com.nqma.disbot.repository.GuildSetting;
import com.nqma.disbot.service.guildsettings.GuildSettingService;
import com.nqma.disbot.service.responsers.MessageSender;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class Setup implements SlashCommand {

    private final GuildSettingService guildSettingService;

    public Setup(GuildSettingService guildSettingService) {
        this.guildSettingService = guildSettingService;
    }

    @Override
    public String getName() {
        return Commands.SETUP.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        try {
            Long channelId = parseChannelId(event.getOption("channel_id"));
            boolean inGameMode = parseInGameMOde(event.getOption("in_game_mode"));
            Long guildId = event.getInteraction().getGuildId().get().asLong();

            if (channelId == null && !inGameMode) {
                guildSettingService.deleteGuildSetting(guildId);
            } else {
                guildSettingService.addGuildSetting(new GuildSetting(guildId, channelId, inGameMode));
            }

            return MessageSender.replyTo(event, Message.SETUP);

        } catch (Exception e) {
            return MessageSender.replyToEphemeral(event, Message.SETUP_ERROR);
        }
    }

    private Long parseChannelId(Optional<ApplicationCommandInteractionOption> channelIdOptional) {
        if (channelIdOptional.isEmpty()) return null;
        else if (channelIdOptional.get().getValue().isEmpty()) return null;
        return Long.parseLong(channelIdOptional.get().getValue().get().asString());
    }

    private boolean parseInGameMOde(Optional<ApplicationCommandInteractionOption> inGameModeOptional) {
        if (inGameModeOptional.isEmpty()) return false;
        else if (inGameModeOptional.get().getValue().isEmpty()) return false;
        return inGameModeOptional.get().getValue().get().asBoolean();
    }
}
