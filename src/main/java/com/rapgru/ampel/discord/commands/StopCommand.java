package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.discord.AdminCommand;
import net.dv8tion.jda.api.entities.Message;

import java.util.concurrent.TimeUnit;

public class StopCommand extends AdminCommand {

    public StopCommand() {}

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public void execute(Message message, String[] args) {
        var msgFormat = "Shutting down in %s seconds.";
        message.getChannel().sendMessageFormat(msgFormat, 3).submit()
                .thenCompose(msg -> msg.editMessageFormat(msgFormat, 2).submitAfter(1, TimeUnit.SECONDS))
                .thenCompose(msg -> msg.editMessageFormat(msgFormat, 1).submitAfter(1, TimeUnit.SECONDS))
                .thenCompose(msg -> msg.delete().submitAfter(500, TimeUnit.MICROSECONDS))
                .thenRun(message.delete()::submit)
                .thenRun(message.getJDA()::shutdown);
    }

}
