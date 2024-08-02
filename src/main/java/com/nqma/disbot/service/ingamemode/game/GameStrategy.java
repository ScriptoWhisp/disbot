package com.nqma.disbot.service.ingamemode.game;

import java.util.Optional;

public interface GameStrategy {

    boolean isInGame(Optional<String> status);

    static GameStrategy getGameStrategy(String game) {
        switch (game) {
            case "PUBG: BATTLEGROUNDS":
                return new PUBG();
            default:
                return new Default();
        }
    }

}
