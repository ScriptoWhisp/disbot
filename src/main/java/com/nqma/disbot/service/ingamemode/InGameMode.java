package com.nqma.disbot.service.ingamemode;

import com.austinv11.servicer.Service;
import com.nqma.disbot.service.ingamemode.game.GameStrategy;
import com.nqma.disbot.service.player.GuildQueue;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.PresenceUpdateEvent;
import discord4j.core.event.domain.guild.MemberUpdateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.presence.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@EnableScheduling
public class InGameMode {

    private GatewayDiscordClient client;

    @Autowired
    public InGameMode(GatewayDiscordClient client) {
        this.client = client;
    }

    @Scheduled(fixedDelay = 40000) // 60000 milliseconds = 1 minute
    public void handle() {
        if (GuildQueue.getGuildQueues().isEmpty()) return;

        System.out.println("InGameMode.handle()");
        GuildQueue.getGuildsQueueWithGameMode().forEach(guildQueue -> {

            if (guildQueue.getCurrentSong() == null) return;

            Member member = guildQueue.getCurrentSong().getMember();
            Optional<Activity> memberActivity = member.getPresence().block().getActivity();

            if (memberActivity.isEmpty()) {
                System.out.println("Member: " + member.getUsername() + " is not playing anything.");
                return;
            }
            System.out.println("Member: " + member.getUsername() + " is playing: " + memberActivity.get().getName());
            GameStrategy gameStrategy = GameStrategy.getGameStrategy(memberActivity.get().getName());

            boolean b = guildQueue.setPaused(gameStrategy.isInGame(memberActivity.get().getDetails()));
            System.out.println("is in game: " + b);
        });
    }
}
