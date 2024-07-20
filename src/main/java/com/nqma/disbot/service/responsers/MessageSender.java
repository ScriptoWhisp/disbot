package com.nqma.disbot.service.responsers;

import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateMono;

public abstract class MessageSender {

    public static MessageCreateMono sendTo(MessageChannel channel, String message) {
        return channel.createMessage(message);
    }

    public static MessageCreateMono sendTo(MessageChannel channel, EmbedCreateSpec message) {
        return channel.createMessage(message);
    }

}
