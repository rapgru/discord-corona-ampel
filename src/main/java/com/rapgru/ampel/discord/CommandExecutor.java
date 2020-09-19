package com.rapgru.ampel.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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
    private static final List<String> ALLOWED_CHANNEL_IDS = List.of("755824972814876844", "755720275185893437");

    private final List<Command> commands = new ArrayList<>();

    CommandExecutor(JDA jda) {
        jda.addEventListener(this);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String rawMessage = event.getMessage().getContentRaw();
        Member member = event.getMember();

        // not a message from a user
        if (member == null) {
            return;
        }
        // fix exception when event was not in an text channel...
        if (!event.isFromType(ChannelType.TEXT)) {
            return;
        }
        TextChannel channel = event.getTextChannel();

        // check for allowed channels
        if (!ALLOWED_CHANNEL_IDS.contains(channel.getId())) {
            return;
        }

        // check command prefix
        if (!rawMessage.startsWith(COMMAND_PREFIX)) {
            return;
        }

        // delegate execution to corresponding command class
        commands.stream()
                .filter(command -> rawMessage.toLowerCase().startsWith(command.getName().toLowerCase(), 1))
                .findFirst()
                .ifPresent(command -> {
                    // no permissions
                    if (!command.getRoles().stream().allMatch(role -> hasRole(member, role))) {
                        command.roleNotFound(event.getMessage());
                        return;
                    }

                    // execute command
                    event.getMessage().addReaction("U+2611").submit();
                    event.getMessage().delete().queueAfter(Command.REMOVAL_TIME, TimeUnit.SECONDS);
                    LOGGER.info("User {} executed command {}", command.getName(), member.getUser().getName());
                    // execute now
                    String[] args = rawMessage.split(" ");
                    command.execute(
                            event.getMessage(),
                            Arrays.copyOfRange(args, 1, args.length)
                    );
                });
    }

    private boolean hasRole(Member member, String roleName) {
        return member.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }

    public void addCommands(Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }
}
