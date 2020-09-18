package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.discord.Command;
import net.dv8tion.jda.api.entities.Message;

public class PingCommand extends Command {

    public PingCommand() {}

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public void execute(Message message, String[] args) {
        long timeStart = System.currentTimeMillis();

        message.getChannel().sendMessage("Pong!").queue(response -> {
            response.editMessageFormat("Pong! %d ms", System.currentTimeMillis() - timeStart).queue();
        });
    }
}
