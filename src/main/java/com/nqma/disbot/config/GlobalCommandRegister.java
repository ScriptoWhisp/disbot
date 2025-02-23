package com.nqma.disbot.config;


import com.nqma.disbot.service.commands.Setup;
import com.nqma.disbot.service.commands.listener.SlashCommandListener;
import com.nqma.disbot.service.commands.music.*;
import com.nqma.disbot.utils.FileReader;
import com.nqma.disbot.service.guildsettings.GuildSettingService;
import discord4j.common.JacksonResources;
import discord4j.core.GatewayDiscordClient;
import discord4j.discordjson.json.ApplicationCommandRequest;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GlobalCommandRegister {

    @Value("${commands.directory}")
    private String commandsDirectory;

    private final GatewayDiscordClient client;

    @Getter
    private SlashCommandListener slashCommandListener;

    private static final JacksonResources MAPPER = JacksonResources.create();

    @Autowired
    private final GuildSettingService guildSettingService;

    @Autowired
    private FileReader fileReader;

    @Autowired
    public GlobalCommandRegister(GatewayDiscordClient client, GuildSettingService guildSettingService) {
        this.client = client;
        this.guildSettingService = guildSettingService;
    }

    @SneakyThrows
    @Bean
    public SlashCommandListener register() {
        // Get our application's ID
        long applicationId = client.getRestClient().getApplicationId().block();

        List<ApplicationCommandRequest> commands = fileReader.listFilesOfAppComReq(commandsDirectory);

        //Register our slash command listener
        slashCommandListener = new SlashCommandListener(List.of(
                new Play(guildSettingService),
                new Next(),
                new Plist(),
                new Pause(),
                new Stop(),
                new Status(),
                new Setup(guildSettingService),
                new Skip()),
                client);

        client.getRestClient().getApplicationService().bulkOverwriteGlobalApplicationCommand(applicationId, commands)
                .doOnNext(ignore -> System.out.println("Successfully registered command"))
                .doOnError(error -> System.out.println("Error registering commands: " + error.getMessage()))
                .subscribe();

        return slashCommandListener;
    }


}
