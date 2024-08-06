package com.nqma.disbot.service.ingamemode.game;

import discord4j.core.object.presence.Activity;

public class PUBG implements GameStrategy {

    @Override
    public boolean isInGame(Activity description) {
        return description.getStart().isPresent();
    }
}
