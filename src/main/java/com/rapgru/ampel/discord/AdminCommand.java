package com.rapgru.ampel.discord;

import net.dv8tion.jda.api.entities.Message;

public abstract class AdminCommand extends Command {

    public AdminCommand() {
        addRole("Admin");
    }

    @Override
    public void roleNotFound(Message message) {
        sendTimedMessage(message.getChannel(), "This command is only for admins.");
    }
}
