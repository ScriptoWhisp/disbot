package com.nqma.disbot.service.responsers;

import com.nqma.disbot.service.commands.Message;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateMono;
import reactor.core.publisher.Mono;

public abstract class MessageSender {

    public static Mono<Void> replyTo(ChatInputInteractionEvent event, Message message) {
        return event.reply().withEmbeds(message.getEmbed());
    }

    public static Mono<Void> replyToEphemeral(ChatInputInteractionEvent event, Message message) {
        return event.reply().withEmbeds(message.getEmbed()).withEphemeral(true);
    }

    public static Mono<Void> replyToEphemeral(ChatInputInteractionEvent event, Message message, String text) {
        return event.reply().withEmbeds(message.formatEmbed(text)).withEphemeral(true);
    }

    public static Mono<Void> replyTo(ChatInputInteractionEvent event, Message message, String text) {
        return event.reply(message.format(text));
    }

    public static MessageCreateMono sendTo(MessageChannel channel, String message) {
        return channel.createMessage(message);
    }

    public static MessageCreateMono sendTo(MessageChannel channel, EmbedCreateSpec message) {
        return channel.createMessage(message);
    }

}
