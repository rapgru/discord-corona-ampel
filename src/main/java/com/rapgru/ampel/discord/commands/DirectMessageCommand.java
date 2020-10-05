package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.discord.AdminCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.Objects;

public class DirectMessageCommand extends AdminCommand {

    public DirectMessageCommand() {}

    @Override
    public String getName() {
        return "dm";
    }

    @Override
    public void execute(Message message, String[] args) {
        User user = Objects.requireNonNull(message.getMember()).getUser();

        message.addReaction("U+2611").queue(); // unicode for check box icon

        user.openPrivateChannel().queue(privateChannel ->
                privateChannel.sendMessage(String.join(" ", args)).queue());
    }
}
