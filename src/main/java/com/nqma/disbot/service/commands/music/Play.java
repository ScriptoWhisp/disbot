package com.nqma.disbot.service.commands.music;

import com.nqma.responseservice.controller.requests.SongPutRequest;
import com.nqma.responseservice.initconfig.InitialConfiguration;
import com.nqma.responseservice.service.ExternalService;
import com.nqma.responseservice.service.commands.Commands;
import com.nqma.responseservice.service.commands.music.player.LavaPlayerAudioProvider;
import com.nqma.responseservice.service.commands.music.player.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.voice.AudioProvider;
import reactor.core.publisher.Mono;

public class Play implements MusicSlashCommand {

    @Override
    public String getName() {
        return Commands.PLAY.toString();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        String link = event.getOption("url").orElse(null).getValue().orElse(null).asString();
        Member member = event.getInteraction().getMember().orElse(null);

        if (member.getVoiceState().block() == null) {
            return event.reply("Not in the voice chat");
        }

        // Request from audio service here ----->
        ExternalService externalService = new ExternalService();
        SongPutRequest songPutRequest = SongPutRequest.builder()
                .link(link)
                .memberId(member.getId().asLong())
                .guildId(member.getGuildId().asLong())
                .build();
        externalService.addSong(songPutRequest);
        // -----<

        return event.reply("Playing now");
    }

    @Override
    public void run(Member member, String link) {

        System.out.println("Playing now for " + member.getUsername() + " with link " + link);

        // Create an AudioPlayer so Discord4J can receive audio data
        AudioPlayer player = InitialConfiguration.getPlayerManager().createPlayer();

        // We will be creating LavaPlayerAudioProvider in the next step
        AudioProvider provider = new LavaPlayerAudioProvider(player);

        final TrackScheduler scheduler = new TrackScheduler(player);

        VoiceState voiceState = member.getVoiceState().block();

        voiceState.getChannel().block().join(spec -> spec.setProvider(provider)).block();

        InitialConfiguration.getPlayerManager().loadItem(link, scheduler);
    }
}
