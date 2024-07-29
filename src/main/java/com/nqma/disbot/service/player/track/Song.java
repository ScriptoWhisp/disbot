package com.nqma.disbot.service.player.track;

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



@Getter
public class Song extends Playable {

    private String author;

    @Builder
    public Song(String url, String title, String author, Member member) {
        super(url, title, member);
        this.author = author;
    }

    @Override
    public String toString() {
        return title + " | " + author + " (Song)";
    }

}
