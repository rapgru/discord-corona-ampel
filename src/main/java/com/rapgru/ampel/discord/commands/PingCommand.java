package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.discord.Command;
import net.dv8tion.jda.api.entities.Message;

import java.util.concurrent.TimeUnit;

public class PingCommand extends Command {

    public PingCommand() {}

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public void execute(Message message, String[] args) {
        long timeStart = System.currentTimeMillis();

        message.getChannel().sendMessage("Pong!").submit()
                .thenCompose(msg -> msg.editMessageFormat("Pong! %d ms.", System.currentTimeMillis() - timeStart).submit())
                .thenCompose(msg -> msg.delete().submitAfter(Command.REMOVAL_TIME, TimeUnit.SECONDS));
    }
}
