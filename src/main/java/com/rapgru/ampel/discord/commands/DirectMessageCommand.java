package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.discord.AdminCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class DirectMessageCommand extends AdminCommand {

    public DirectMessageCommand() {
        setName("dm");
    }

    @Override
    public void execute(Message message, String[] args) {
        Member member = message.getMember();
        if (member == null) {
            return;
        }

        message.addReaction("U+2611").queue();

        member.getUser().openPrivateChannel().queue(privateChannel -> {
            StringBuilder directMessageBuilder = new StringBuilder();
            // offset of 1 to skip commandName
            for (int i = 1; i < args.length; i++) {
                directMessageBuilder.append(args[i]);
                directMessageBuilder.append(" ");
            }

            privateChannel.sendMessage(directMessageBuilder.toString()).queue();
        });
    }
}
