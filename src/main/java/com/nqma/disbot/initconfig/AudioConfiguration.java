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
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AudioConfiguration {

    @Getter
    private static AudioPlayerManager playerManager;

    /**
     * This method initializes the DiscordClient object.
     */
    @SneakyThrows
    @Bean
    public AudioPlayerManager audioPlayerManager() {

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

        return playerManager;
    }
}
