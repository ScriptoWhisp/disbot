package com.nqma.disbot.service.player.track;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import discord4j.core.object.entity.Member;
import lombok.Builder;
import reactor.core.publisher.Mono;

import java.util.List;

@Builder
public class TrackSearch {

    private String url;
    private Member member;

    public Mono<Playable> getTrackInfo(AudioPlayerManager playerManager) {
        return Mono.create(sink -> playerManager.loadItem(url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                AudioTrackInfo info = track.getInfo();
                sink.success(Song.builder().url(url).title(info.title).author(info.author).member(member).build());
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                // If you want to handle playlists, you can loop through them and call the callback for each track
                sink.success(Playlist
                        .builder()
                        .url(url)
                        .title(playlist.getName())
                        .member(member)
                        .songs(playlist.getTracks().stream().map(audioTrack -> {
                            AudioTrackInfo info = audioTrack.getInfo();
                            return Song.builder().url(info.uri).title(info.title).author(info.author).member(member).build();
                        }).toList())
                        .build());
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
