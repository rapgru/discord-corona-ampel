package com.rapgru.ampel.discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command {

    private final List<String> roles = new ArrayList<>();

    public abstract void execute(Message message, String[] args);

    public void roleNotFound(Message message) {
        message.getChannel().sendMessage("You dont have permissions for this command.").submit();
    };

    public abstract String getName();

    public List<String> getRoles() {
        return roles;
    }

    public void addRole(String roleName) {
        roles.add(roleName);
    }

    public void addRoles(String... roleNames) {
        roles.addAll(Arrays.asList(roleNames));
    }

    protected static void sendMessage(MessageChannel channel, String message) {
        channel.sendMessage(message).submit();
    }
}
