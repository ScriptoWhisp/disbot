package com.nqma.disbot.service.player.track;

import discord4j.core.object.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;


@Getter
public class Playlist extends Playable {

    private final Deque<Song> songs = new ArrayDeque<>();
    private Song currentSong;

    @Builder
    public Playlist(String url, String title, Member member, List<Song> songs) {
        super(url, title, member);
        this.songs.addAll(songs);
    }

    public Song next() {
        currentSong = songs.poll();
        return currentSong;
    }

    public Song peekNext() {
        return songs.peek();
    }

    public boolean hasNext() {
        return !songs.isEmpty();
    }

    @Override
    public String toString() {
        return title + " (Playlist)";
    }

    @Override
    public Integer getSize() {
        return songs.size();
    }

    public String toInnerString() {
        return "> **" + this + " - " + currentSong + "**\n> " + songs.stream()
                .map(Song::toString)
                .collect(Collectors.joining("\n> "));
    }
}



