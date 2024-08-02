package com.nqma.disbot.service.ingamemode.game;

import java.util.Optional;

public class PUBG implements GameStrategy {

    @Override
    public boolean isInGame(Optional<String> description) {
        return description.filter(s -> !s.contains("Lobby")).isPresent();
    }
}
