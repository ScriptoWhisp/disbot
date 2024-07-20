package com.nqma.disbot.initconfig;


import com.nqma.disbot.service.commands.SlashCommand;
import com.nqma.disbot.service.commands.SlashCommandListener;
import com.nqma.disbot.service.commands.Test;
import com.nqma.disbot.service.commands.music.Play;
import com.nqma.disbot.service.files.FileService;
import discord4j.common.JacksonResources;
import discord4j.core.GatewayDiscordClient;
import discord4j.discordjson.json.ApplicationCommandRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class GlobalCommandRegister implements ApplicationRunner {

    @Value("${commands.directory}")
    private String commandsDirectory;

    private final GatewayDiscordClient client;

    @Getter
    private static SlashCommandListener slashCommandListener;

    private static final JacksonResources MAPPER = JacksonResources.create();


    public GlobalCommandRegister(GatewayDiscordClient client) {
        this.client = client;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {

        // Get our application's ID
        long applicationId = client.getRestClient().getApplicationId().block();

        List<ApplicationCommandRequest> commands = new ArrayList<>();
        for (String command : FileService.listFiles(commandsDirectory)) {
            System.out.println("Command: " + command);

            ApplicationCommandRequest commandRequest = MAPPER.getObjectMapper().readValue(new File(command), ApplicationCommandRequest.class);

            commands.add(commandRequest);
        }

        //Register our slash command listener
        List<SlashCommand> slashCommandClassList = List.of(new Play(), new Test());
        slashCommandListener = new SlashCommandListener(slashCommandClassList, client);

        client.getRestClient().getApplicationService().bulkOverwriteGlobalApplicationCommand(applicationId, commands)
                .doOnNext(ignore -> System.out.println("Successfully registered commands"))
                .doOnError(error -> System.out.println("Error registering commands: " + error.getMessage()))
                .subscribe();

    }


}
