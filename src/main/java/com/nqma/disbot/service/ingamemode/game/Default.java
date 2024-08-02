package com.nqma.disbot.service.ingamemode.game;

import java.util.Optional;

public class Default implements GameStrategy {
    @Override
    public boolean isInGame(Optional<String> status) {
        return false;
    }
}
