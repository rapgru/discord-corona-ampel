package com.rapgru.ampel.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandExecutor extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutor.class);
    private static final String COMMAND_PREFIX = "!";

    private final List<Command> commands = new ArrayList<>();

    CommandExecutor(JDA jda) {
        jda.addEventListener(this);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String rawMessage = event.getMessage().getContentRaw();
        Member member = event.getMember();
        if (!rawMessage.startsWith(COMMAND_PREFIX)) {
            return;
        }
        if (member == null) {
            return;
        }

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
                    String[] args = rawMessage.split(" ");
                    command.execute(
                            event.getMessage(),
                            Arrays.copyOfRange(args, 1, args.length)
                    );
                    LOGGER.info("User {} executed command {}", command.getName(), member.getUser().getName());
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
