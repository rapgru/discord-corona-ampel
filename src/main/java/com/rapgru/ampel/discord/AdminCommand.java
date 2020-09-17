package com.rapgru.ampel.discord;

import net.dv8tion.jda.api.entities.Message;

public abstract class AdminCommand extends Command {

    public AdminCommand() {
        addRole("Admin");
    }

    @Override
    public void roleNotFound(Message message) {
        message.getChannel().sendMessage("This command is only for admins.").queue();
    }
}
