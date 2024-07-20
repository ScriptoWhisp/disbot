package com.nqma.disbot.controller;


import com.nqma.responseservice.controller.requests.SongRequest;
import com.nqma.responseservice.service.commands.music.Play;
import com.nqma.responseservice.service.commands.music.player.Song;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class ResponseController {

    @Autowired
    private final GatewayDiscordClient client;

    @PostMapping("/play")
    public Status play(@RequestBody SongRequest songRequest) {
        System.out.println("Song recieved"
        + songRequest.getSong().toString());


        Song song = songRequest.getSong();

        Snowflake guildId = Snowflake.of(song.getGuildId());
        Snowflake memberId = Snowflake.of(song.getAuthor());

        System.out.println("Guild ID: " + guildId.asString());
        System.out.println("Member ID: " + memberId.asString());

        // Retrieve the Guild
        Mono<Guild> guildMono = client.getGuildById(guildId);

        System.out.println("Guild Mono: " + guildMono.block());

        // Retrieve the Member
        Member member = guildMono.flatMap(guild -> guild.getMemberById(memberId)).block();

        System.out.println("Member: " + member);

        String link = song.getUrl();

        System.out.println("Link: " + link);

        Play playCommand = new Play();

        System.out.println("Play Command: " + playCommand);

        playCommand.run(member, link);

        return Status.OK;
    }

    @GetMapping("/test")
    public Status test() {
        System.out.println("Test recieved");
        return Status.OK;
    }


}
