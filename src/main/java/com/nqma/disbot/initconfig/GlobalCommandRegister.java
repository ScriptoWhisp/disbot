package com.nqma.disbot.initconfig;


import com.nqma.disbot.service.commands.listener.SlashCommandListener;
import com.nqma.disbot.service.commands.music.*;
import com.nqma.disbot.service.files.FileService;
import discord4j.common.JacksonResources;
import discord4j.core.GatewayDiscordClient;
import discord4j.discordjson.json.ApplicationCommandRequest;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.ArrayList;
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
    public GlobalCommandRegister(GatewayDiscordClient client) {
        this.client = client;
    }

    @SneakyThrows
    @Bean
    public SlashCommandListener register() {
        // Get our application's ID
        long applicationId = client.getRestClient().getApplicationId().block();

        List<ApplicationCommandRequest> commands = new ArrayList<>();
        for (String command : FileService.listFiles(commandsDirectory)) {
            System.out.println("Command: " + command);

            ApplicationCommandRequest commandRequest = MAPPER.getObjectMapper().readValue(new File(command), ApplicationCommandRequest.class);

            commands.add(commandRequest);
        }

        //Register our slash command listener
        slashCommandListener = new SlashCommandListener(List.of(
                new Play(),
                new Next(),
                new Plist(),
                new Pause(),
                new Stop(),
                new Status()),
                client);

        client.getRestClient().getApplicationService().bulkOverwriteGlobalApplicationCommand(applicationId, commands)
                .doOnNext(ignore -> System.out.println("Successfully registered command"))
                .doOnError(error -> System.out.println("Error registering commands: " + error.getMessage()))
                .subscribe();

        return slashCommandListener;
    }


}
