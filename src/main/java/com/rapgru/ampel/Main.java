package com.rapgru.ampel;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        final String token = System.getenv("DISCORD_KEY");
        System.out.println(token);
        final DiscordClient client = DiscordClient.create(token);
        final GatewayDiscordClient gateway = client.login().block();

        Objects.requireNonNull(gateway).on(MessageCreateEvent.class).subscribe(event -> {
            final Message message = event.getMessage();
            if ("!ping".equals(message.getContent())) {
                final MessageChannel channel = message.getChannel().block();
                Objects.requireNonNull(channel).createMessage("Pong!").block();
            }
        });

        gateway.onDisconnect().block();
    }
}
