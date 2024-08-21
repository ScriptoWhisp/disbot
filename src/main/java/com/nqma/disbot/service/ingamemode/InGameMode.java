package com.nqma.disbot.service.ingamemode;

import com.nqma.disbot.service.ingamemode.game.GameStrategy;
import com.nqma.disbot.service.player.GuildQueue;
import discord4j.core.object.entity.Member;
import discord4j.core.object.presence.Activity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@EnableScheduling
public class InGameMode {

    @Scheduled(fixedDelay = 30000) // 60000 milliseconds = 1 minute
    public void handle() {
        if (GuildQueue.getGuildQueues().isEmpty()) return;

        System.out.println("InGameMode.handle()");
        GuildQueue.getGuildsQueueWithGameMode().forEach(guildQueue -> {

            boolean gameStrategy = isInGame(guildQueue);
            System.out.println("is in game: " + gameStrategy + " for guild: " + guildQueue.isInGame());

            if (gameStrategy != guildQueue.isInGame()) {
                boolean b = guildQueue.setPaused(gameStrategy);
                System.out.println("is in game: " + b);
            }
        });
    }

    public static boolean isInGame(GuildQueue guildQueue) {
        if (guildQueue.getCurrentSong() == null) return false;

        Member member = guildQueue.getCurrentSong().getMember();
        Optional<Activity> memberActivity = member.getPresence().block().getActivity();

        if (memberActivity.isEmpty()) {
            System.out.println("Member: " + member.getUsername() + " is not playing anything.");
            return false;
        }
        System.out.println("Member: " + member.getUsername() + " is playing: " + memberActivity.get().getName());
        return GameStrategy.getGameStrategy(memberActivity.get().getName()).isInGame(memberActivity.get());
    }

}
