package com.nqma.disbot.service.commands.listener;

import com.nqma.disbot.repository.GuildSetting;
import com.nqma.disbot.service.commands.Message;
import com.nqma.disbot.service.commands.SlashCommand;
import com.nqma.disbot.service.guildsettings.GuildSettingService;
import com.nqma.disbot.service.player.GuildQueue;
import com.nqma.disbot.service.responsers.MessageSender;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.channel.Channel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Getter
public class SlashCommandListener {

    private final Collection<SlashCommand> slashCommands;

    private final GatewayDiscordClient client;

    @Autowired
    private GuildSettingService guildSettingService;

    public SlashCommandListener(List<SlashCommand> slashCommands, GatewayDiscordClient client) {
        this.slashCommands = slashCommands;
        this.client = client;

        client.on(ChatInputInteractionEvent.class, this::handle).subscribe();
    }

    public Mono<Void> handle(ChatInputInteractionEvent event) {
        Optional<Channel> channel = isRightChannel(event);

        return channel.map(value -> MessageSender.replyToEphemeral(event, Message.NOT_IN_CHANNEL, value.getMention())).orElseGet(() -> findCommand(event.getCommandName())
                .map(command -> command.handle(event))
                .orElse(Mono.empty()));

    }

    private Optional<Channel> isRightChannel(ChatInputInteractionEvent event) {
        Long guildId = event.getInteraction().getGuildId().get().asLong();

        // if the guild has a queue, check if the channel is the same
        GuildQueue guildQueue = GuildQueue.getGuildQueue(guildId);
        if (guildQueue != null && guildQueue.getMessageChannel().getId().asLong() != event.getInteraction().getChannel().block().getId().asLong()) {
            return Optional.ofNullable(guildQueue.getMessageChannel());
        }

        // if guild does not have a queue, check if the guild has a channel set in the database
        Optional<GuildSetting> guildSetting = guildSettingService.findGuildSettingById(guildId);
        if (guildSetting.isPresent() && guildSetting.get().getChannelId() != null && guildSetting.get().getChannelId() != event.getInteraction().getChannelId().asLong()) {
            return Optional.ofNullable(client.getChannelById(Snowflake.of(guildSetting.get().getChannelId())).block());
        }
        return Optional.empty();
    }

    public Optional<SlashCommand> findCommand(String commandName) {
        return slashCommands.stream()
                .filter(command -> command.getName().equalsIgnoreCase(commandName))
                .findFirst();

    }
}
