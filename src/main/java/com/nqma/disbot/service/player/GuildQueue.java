package com.nqma.disbot.service.player;

import com.nqma.disbot.initconfig.AudioConfiguration;
import com.nqma.disbot.service.player.track.Song;
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
    private final Queue<Song> queue = new ArrayDeque<>(); // Actual and future songs
    @Getter
    private final AudioPlayer player = AudioConfiguration.getPlayerManager().createPlayer();
    @Getter
    private Song currentSong = null; // Currently playing song
    private final List<Song> history = new LimitedSizeList<>(MAX_HISTORY_SIZE); // Previously played songs


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

        Song song = Song.builder().url(link).member(member).build();
        song.getTrackInfo(playerManager).block();

        if (queue.isEmpty() && currentSong == null) {
            queue.add(song);
            playNextSong();
        } else {
            queue.add(song);
        }
        return song.toString();
    }

    public void playNextSong() {
        if (currentSong != null) history.add(currentSong);
        currentSong = queue.poll();
        if (currentSong == null) return;
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
                .map(Song::toString)
                .collect(Collectors.joining("\n")))
        .addField("> " + (currentSong == null ? "Nothing, use /play" : currentSong.toString()),
                queue.stream()
                .map(Song::toString)
                .collect(Collectors.joining("\n")),
                false)
        .build();
    }

    public boolean pause() {
        boolean isPausedNow = !player.isPaused();
        player.setPaused(isPausedNow);
        return isPausedNow;
    }

    public Song peekNextSong() {
        return queue.isEmpty() ? null : queue.peek();
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
