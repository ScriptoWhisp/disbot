package com.nqma.disbot.service.player;

import com.nqma.disbot.initconfig.AudioConfiguration;
import com.nqma.disbot.service.player.track.Playable;
import com.nqma.disbot.service.player.track.Playlist;
import com.nqma.disbot.service.player.track.Song;
import com.nqma.disbot.service.player.track.TrackSearch;
import com.nqma.disbot.utils.LimitedSizeList;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.voice.AudioProvider;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class GuildQueue {

    private static final AudioPlayerManager playerManager = AudioConfiguration.getPlayerManager();

    private static final Integer MAX_QUEUE_SIZE = 50;
    private static final Integer MAX_HISTORY_SIZE = 10;
    private static final Map<Long, GuildQueue> guildQueues = new HashMap<>();


    @Getter
    private final Queue<Playable> queue = new ArrayDeque<>(); // Actual and future songs
    @Getter
    private Integer size = 0;
    @Getter
    private final AudioPlayer player = AudioConfiguration.getPlayerManager().createPlayer();
    @Getter
    private Song currentSong = null; // Currently playing song
    @Getter
    private Playlist currentPlaylist = null; // Currently playing playlist
    private final List<Playable> history = new LimitedSizeList<>(MAX_HISTORY_SIZE); // Previously played songs


    private final TrackScheduler scheduler;

    @Getter
    private VoiceChannel channel = null;
    @Getter
    private final MessageChannel messageChannel;

    public GuildQueue(VoiceChannel channel, long guildId, MessageChannel messageChannel) {
        guildQueues.put(guildId, this);
        this.channel = channel;
        this.messageChannel = messageChannel;

        // Create an AudioPlayer so Discord4J can receive audio data

        AudioProvider provider = new LavaPlayerAudioProvider(player);

        scheduler = TrackScheduler.builder().player(player).guildQueue(this).build();

        player.addListener(scheduler);

        channel.join(spec -> spec.setProvider(provider)).block();
    }

    public GuildQueue(VoiceState voiceState, MessageChannel messageChannel) {
        this(voiceState.getChannel().block(), voiceState.getGuildId().asLong(), messageChannel);
    }

    public String addSong(String link, Member member) {
        if (queue.size() >= MAX_QUEUE_SIZE) return null; // Queue is full

        TrackSearch search = TrackSearch.builder().url(link).member(member).build();
        Playable playable = search.getTrackInfo(playerManager).block();

        if (queue.isEmpty() && this.currentSong == null) {
            queue.add(playable);
            System.out.println("Playing now for " + channel.getName() + " with link " + link);
            playNextSong();
        } else {
            queue.add(playable);
        }
        addToSize(playable.getSize());
        return playable.toString();
    }

    public void playNextSong() {
        if (currentSong != null) history.add(currentSong);

        if (currentPlaylist != null) {
            System.out.println("test1");
            if (currentPlaylist.hasNext()) {
                currentSong = currentPlaylist.next();
                playSong(currentSong.getUrl());
                return;
            } else {
                currentPlaylist = null;
            }
        }

        System.out.println("test2");
        Playable playable = queue.poll();
        if (playable == null) return;
        if (playable instanceof Song) {
            currentSong = (Song) playable;
        } else if (playable instanceof Playlist) {
            currentPlaylist = (Playlist) playable;
            currentSong = currentPlaylist.next();
        }

        System.out.println("Playing next song " + currentSong.toString());

        playSong(currentSong.getUrl());
    }

    private void playSong(String link) {
        System.out.println("Playing now for " + channel.getName() + " with link " + link);
        AudioConfiguration.getPlayerManager().loadItem(link, scheduler);
    }

    public EmbedCreateSpec getQueueEmbed() {
        return EmbedCreateSpec.builder()
        .title("Queue")
        .description(history.stream()
                .map(Playable::toString)
                .collect(Collectors.joining("\n")))
        .addField("", getNowPlaying(), false)
        .addField("",
            queue.stream()
            .map(Playable::toString)
            .collect(Collectors.joining("\n")),
            false)
        .footer("This playlist have " + size + " Songs", null)
        .build();
    }

    private String getNowPlaying() {
        if (currentPlaylist != null) return currentPlaylist.toInnerString();
        return currentSong == null ? "Nothing, use /play" : "> " + currentSong;
    }

    public boolean pause() {
        boolean isPausedNow = !player.isPaused();
        player.setPaused(isPausedNow);
        return isPausedNow;
    }

    public boolean skip() {
        currentPlaylist = null;
        Playable playable = queue.poll();
        if (playable == null) return false;
        if (playable instanceof Song) {
            currentSong = (Song) playable;
        } else if (playable instanceof Playlist) {
            currentPlaylist = (Playlist) playable;
            currentSong = currentPlaylist.next();
        }
        return true;
    }

    public Song peekNextSong() {
        if (currentPlaylist != null) return currentPlaylist.peekNext();
        Playable next = queue.peek();
        if (next instanceof Song) return (Song) next;
        else if (next instanceof Playlist) return ((Playlist) next).peekNext();
        return null;
    }

    private void addToSize(int size) {
        this.size += size;
    }

    public static GuildQueue getGuildQueue(VoiceState voiceState) {
        return guildQueues.get(voiceState.getGuildId().asLong());
    }

    public static GuildQueue getGuildQueue(long guildId) {
        return guildQueues.get(guildId);
    }

    public static void removeAndClearGuildQueue(long guildId) {
        GuildQueue guild = guildQueues.get(guildId);
        guild.player.destroy();
        guild.queue.clear();
        guild.channel.sendDisconnectVoiceState().block();
        guildQueues.remove(guildId);
    }
}
