package com.nqma.disbot.initconfig;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.clients.AndroidTestsuite;
import dev.lavalink.youtube.clients.Music;
import dev.lavalink.youtube.clients.Web;
import dev.lavalink.youtube.clients.skeleton.Client;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
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
    private static AudioPlayerManager playerManager;

    private static GatewayDiscordClient client;

    /**
     * This method initializes the DiscordClient object.
     */
    @SneakyThrows
    @Bean
    public GatewayDiscordClient gatewayDiscordClient() {

        // Creates AudioPlayer instances and translates URLs to AudioTrack instances
        playerManager = new DefaultAudioPlayerManager();

        //Not seqmulluq but lavalink plugin
        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager(true, new Client[] { new Music(), new Web(), new AndroidTestsuite() });
        playerManager.registerSourceManager(ytSourceManager);

        // This is an optimization strategy that Discord4J can utilize.
        // It is not important to understand
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);

        // Allow playerManager to parse remote sources like YouTube links
        AudioSourceManagers.registerRemoteSources(playerManager, YoutubeAudioSourceManager.class);

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
