package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.discord.AdminCommand;
import net.dv8tion.jda.api.entities.Message;

public class StopCommand extends AdminCommand {

    public StopCommand() {}

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public void execute(Message message, String[] args) {
        message.getChannel().sendMessage("good bye!").queue(than -> {
            than.getJDA().shutdown();
        });
    }
}
