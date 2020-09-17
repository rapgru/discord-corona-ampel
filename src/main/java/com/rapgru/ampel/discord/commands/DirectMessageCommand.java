package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.discord.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class DirectMessageCommand extends Command {

    public DirectMessageCommand() {
        setName("dm");
        addRole("Admin");
    }

    @Override
    public void roleNotFound(Message message) {
        message.getChannel().sendMessage("this command is for admins.").queue();
    }

    @Override
    public void execute(Message message, String[] args) {
        Member member = message.getMember();
        if (member == null) {
            return;
        }

        message.addReaction(String.valueOf('\u008F')).queue();

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
