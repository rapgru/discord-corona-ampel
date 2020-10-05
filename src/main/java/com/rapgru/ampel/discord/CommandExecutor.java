package com.rapgru.ampel.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandExecutor extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutor.class);
    private static final String COMMAND_PREFIX = "!";
    // development channel, commands channel
    private static final List<String> ALLOWED_CHANNELs_IDS = List.of("755824972814876844", "755720275185893437");
    private static final String COMMANDS_CHANNEL_ID = "755720275185893437";

    private final List<Command> commands = new ArrayList<>();

    CommandExecutor(JDA jda) {
        jda.addEventListener(this);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();
        if (!validateUserMessage(event, message)) {
            return;
        }
        String rawMessage = message.getContentRaw();
        TextChannel channel = event.getTextChannel();

        // check for allowed channels
        if (!ALLOWED_CHANNELs_IDS.contains(channel.getId())) {
            return;
        }

        deleteNonCommandMessages(event, rawMessage);

        // check command prefix
        if (!rawMessage.startsWith(COMMAND_PREFIX)) {
            return;
        }

        // delegate execution to corresponding command class
        commands.stream()
                .filter(command -> rawMessage.toLowerCase().startsWith(command.getName().toLowerCase(), 1))
                .findFirst()
                .ifPresent(command -> {
                    // add tick reaction
                    message.addReaction("U+2611").queue(aVoid ->
                            message.delete().queueAfter(Command.REMOVAL_TIME, TimeUnit.SECONDS));

                    // no permissions
                    if (!command.getRoles().stream().allMatch(role -> hasRole(event.getMember(), role))) {
                        command.roleNotFound(message);
                        return;
                    }

                    // execute command
                    String[] args = rawMessage.split(" ");
                    command.execute(message, Arrays.copyOfRange(args, 1, args.length));
                    LOGGER.info("User {} executed command {}", command.getName(), event.getMember().getUser().getName());
                });
    }

    private void deleteNonCommandMessages(MessageReceivedEvent event, String rawMessage) {
        TextChannel channel = event.getTextChannel();
        if (!channel.getId().equals(COMMANDS_CHANNEL_ID) || rawMessage.startsWith(COMMAND_PREFIX)) {
            return;
        }
        event.getMessage().delete().submit()
                .thenCompose(aVoid ->
                        channel.sendMessage("Um zu Schreiben wechsle bitte in den #chat channel.").submit())
                .thenCompose(msg -> msg.delete().submitAfter(Command.REMOVAL_TIME, TimeUnit.SECONDS));
    }

    private boolean validateUserMessage(MessageReceivedEvent event, Message message) {
        // not a message from a user
        if (message.getMember() == null || message.getMember().getUser().isBot()) {
            return false;
        }

        // fix exception when event was not in an text channel...
        if (!event.isFromType(ChannelType.TEXT)) {
            return false;
        }

        return true;
    }

    private boolean hasRole(Member member, String roleName) {
        return member.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }

    public void addCommands(Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }
}
