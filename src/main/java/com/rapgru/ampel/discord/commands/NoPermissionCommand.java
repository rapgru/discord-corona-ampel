package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.discord.Command;
import net.dv8tion.jda.api.entities.Message;

public class NoPermissionCommand extends Command {

    public NoPermissionCommand() {
        addRole("aNonExistentRole");
    }

    @Override
    public String getName() {
        return "noPermissions";
    }

    @Override
    public void execute(Message message, String[] args) {
        message.getChannel().sendMessage("If you can read this, something went wrong.").submit();
    }
}
