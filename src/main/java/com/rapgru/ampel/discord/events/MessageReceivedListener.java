package com.rapgru.ampel.discord.events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceivedListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if ("!ping".equalsIgnoreCase(msg.getContentRaw())) {
            long timeStart = System.currentTimeMillis();
            event.getChannel().sendMessage("Pong!").queue(response -> {
                response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - timeStart).queue();
            });

            msg.addReaction("\u2713").submit();
        }
    }
}
