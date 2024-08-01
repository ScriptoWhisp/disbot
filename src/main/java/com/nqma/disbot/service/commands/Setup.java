package com.nqma.disbot.service.commands;

import com.nqma.disbot.repository.GuildSetting;
import com.nqma.disbot.repository.GuildSettingRepository;
import com.nqma.disbot.service.guildsettings.GuildSettingService;
import com.nqma.disbot.service.responsers.MessageSender;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        Optional<ApplicationCommandInteractionOption> channelId = event.getOption("channel_id");
        Long guildId = event.getInteraction().getGuildId().get().asLong();

        if (channelId.isEmpty()) {
            guildSettingService.deleteGuildSetting(guildId);
        } else {
            guildSettingService.addGuildSetting(new GuildSetting(guildId, Long.parseLong(channelId.get().getValue().get().asString())));
        }

        return MessageSender.replyTo(event, Message.SETUP);
    }
}
