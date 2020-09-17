package com.rapgru.ampel.discord;

import com.rapgru.ampel.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandExecutor extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
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
        Optional<Command> commandOptional = commands.stream()
                .filter(command -> rawMessage.startsWith(command.getName(), 1))
                .findFirst();

        commandOptional.ifPresent(command -> {
            if (!command.getRoles().stream().allMatch(role -> hasRole(member, role))) {
                command.roleNotFound(event.getMessage());
                return;
            }

            LOGGER.info("User " + member.getUser().getName() + " executed command " + command.getName());
            commandOptional.get().execute(
                    event.getMessage(),
                    rawMessage.replaceFirst(COMMAND_PREFIX + command.getName(), "").split(" ")
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
