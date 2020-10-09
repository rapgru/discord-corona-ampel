package com.rapgru.ampel.discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Command {

    protected static final long REMOVAL_TIME = 5;

    private final List<String> roles = new ArrayList<>();

    private boolean hasWhitespace = false;

    public abstract void execute(Message message, String[] args);

    public abstract String getName();

    public void roleNotFound(Message message) {
        sendTimedMessage(message.getChannel(), "You dont have permissions for this command.");
    }

    public List<String> getRoles() {
        return roles;
    }

    public void addRole(String roleName) {
        roles.add(roleName);
    }

    public boolean hasWhitespace() {
        return hasWhitespace;
    }

    public void setHasWhitespace(boolean hasWhitespace) {
        this.hasWhitespace = hasWhitespace;
    }

    protected static void sendTimedMessage(MessageChannel channel, String message) {
        channel.sendMessage(message).queue(msg -> {
            msg.delete().queueAfter(REMOVAL_TIME, TimeUnit.SECONDS);
        });
    }

    protected static void sendTimedMessageFormat(MessageChannel channel, String format, Object... args) {
        channel.sendMessageFormat(format, args).queue(msg -> {
            msg.delete().queueAfter(REMOVAL_TIME, TimeUnit.SECONDS);
        });
    }
}
