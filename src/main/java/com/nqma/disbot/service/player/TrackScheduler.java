package com.nqma.disbot.service.player;

import com.nqma.disbot.service.responsers.MessageSender;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Builder;

@Builder
public final class TrackScheduler extends AudioEventAdapter implements AudioLoadResultHandler {

    private final AudioPlayer player;
    private final GuildQueue guildQueue;

    public TrackScheduler(final AudioPlayer player, final GuildQueue guildQueue) {
        this.player = player;
        this.guildQueue = guildQueue;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        System.out.println("Track ended");
        if (endReason.mayStartNext) {
            // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
            guildQueue.playNextSong();
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        MessageSender.sendTo();
    }

    @Override
    public void trackLoaded(final AudioTrack track) {
        // LavaPlayer found an audio source for us to play
        System.out.println("Playing track " + track.getInfo());
        player.playTrack(track);
    }

    @Override
    public void playlistLoaded(final AudioPlaylist playlist) {
        System.out.println("Playlist loaded " + playlist.getName());
        // LavaPlayer found multiple AudioTracks from some playlist
    }

    @Override
    public void noMatches() {
        System.out.println("No matches found");
        // LavaPlayer did not find any audio to extract
    }

    @Override
    public void loadFailed(final FriendlyException exception) {
        // LavaPlayer could not parse an audio source for some reason
        System.out.println("Failed to load track" + exception);
    }
}