package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.discord.Command;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.Collections;

public class StopCommand extends Command {

    public StopCommand() {
        setName("stop");
        addRole("Admin");
    }

    @Override
    public void roleNotFound(Message message) {
        message.getChannel().sendMessage("This command is only for admins.").queue();
    }

    @Override
    public void execute(Message message, String[] args) {
        message.getChannel().sendMessage("good bye!").queue(than -> {
            than.getJDA().shutdown();
        });
    }
}
