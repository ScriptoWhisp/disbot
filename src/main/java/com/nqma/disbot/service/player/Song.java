package com.nqma.disbot.service.player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import discord4j.core.object.entity.Member;
import lombok.Builder;
import lombok.Getter;
import reactor.core.publisher.Mono;


@Builder
@Getter
public class Song {

    private String url;
    private String title;
    private String author;
    private Member member;

    @Override
    public String toString() {
        return title + " | " + author;
    }

    public Mono<AudioTrackInfo> getTrackInfo(AudioPlayerManager playerManager) {
        return Mono.create(sink -> playerManager.loadItem(url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                AudioTrackInfo info = track.getInfo();
                title = info.title;
                author = info.author;
                sink.success(info);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                // If you want to handle playlists, you can loop through them and call the callback for each track
                sink.error(new UnsupportedOperationException("Cannot load playlist"));
            }

            @Override
            public void noMatches() {
                // Notify the user that the track could not be found
                sink.error(new UnsupportedOperationException("No matches found"));
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                // Notify the user that the track could not be loaded
                sink.error(new UnsupportedOperationException("Load failed"));
            }
        }));
    }

}
