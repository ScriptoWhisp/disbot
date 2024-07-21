package com.nqma.disbot.service.player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class Song {

    private String url;
    private String title;
    private String author;

    @Override
    public String toString() {
        return title + " | " + author;
    }

    public void getTrackInfo(AudioPlayerManager playerManager) {
        playerManager.loadItem(url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                AudioTrackInfo info = track.getInfo();
                title = info.title;
                author = info.author;
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                // If you want to handle playlists, you can loop through them and call the callback for each track
            }

            @Override
            public void noMatches() {
                // Notify the user that the track could not be found
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                // Notify the user that the track could not be loaded
            }
        });
    }

}
