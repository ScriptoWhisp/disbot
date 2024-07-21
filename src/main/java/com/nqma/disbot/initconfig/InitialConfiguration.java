package com.nqma.disbot.initconfig;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialConfiguration {

    @Value("${token.main}")
    private String discordToken;

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
                .setInitialPresence(spec -> ClientPresence.online(ClientActivity.watching("you")))
                .login()
                .block();

        return client;
    }
}
