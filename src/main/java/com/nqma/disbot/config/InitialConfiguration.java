package com.nqma.disbot.config;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.PresenceUpdateEvent;
import discord4j.core.event.domain.guild.MemberUpdateEvent;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialConfiguration {

    @Value("${token.main}")
    private String discordToken;

    @Getter
    private static GatewayDiscordClient client;

    /**
     * This method initializes the DiscordClient object.
     */
    @SneakyThrows
    @Bean
    public GatewayDiscordClient gatewayDiscordClient() {

        // Create the Discord client
        client = DiscordClientBuilder.create(discordToken)
                .build()
                .gateway()
                .setEnabledIntents(IntentSet.of(
                        Intent.GUILDS,
                        Intent.GUILD_MEMBERS,
                        Intent.GUILD_PRESENCES,
                        Intent.GUILD_VOICE_STATES
                ))
                .setInitialPresence(spec -> ClientPresence.online(ClientActivity.watching("you")))
                .login()
                .block();

        return client;
    }
}
