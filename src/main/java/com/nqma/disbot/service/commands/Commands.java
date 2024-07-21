package com.nqma.disbot.service.commands;

public enum Commands {
    PLAY("play"),
    PAUSE("pause"),
    STOP("stop"),
    NEXT("next"),
    PREVIOUS("previous"),
    PLIST("plist");

    private final String name;

    Commands(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
