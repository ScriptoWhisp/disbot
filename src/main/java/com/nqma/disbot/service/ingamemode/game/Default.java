package com.nqma.disbot.service.ingamemode.game;

import discord4j.core.object.presence.Activity;

public class Default implements GameStrategy {
    @Override
    public boolean isInGame(Activity status) {
        return false;
    }
}
