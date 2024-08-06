package com.nqma.disbot.service.ingamemode.game;

import discord4j.core.object.presence.Activity;

public interface GameStrategy {

    boolean isInGame(Activity status);

    static GameStrategy getGameStrategy(String game) {
        return switch (game) {
            case "PUBG: BATTLEGROUNDS" -> new PUBG();
            default -> new Default();
        };
    }

}
