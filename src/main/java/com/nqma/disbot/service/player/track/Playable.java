package com.nqma.disbot.service.player.track;

import discord4j.core.object.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public abstract class Playable {

    String url;
    String title;
    Member member;

    public Playable(String url, String title, Member member) {
        this.url = url;
        this.title = title;
        this.member = member;
    }

    public Integer getSize() {
        return 1;
    }

}
