package com.rapgru.ampel.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

public class DiscordBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordBot.class);

    private final JDA discordClient;
    private final CommandExecutor commandExecutor;

    public DiscordBot(String token) throws LoginException {
        discordClient = JDABuilder.createDefault(token).build();
        commandExecutor = new CommandExecutor(discordClient);
    }

    public void awaitReady() throws InterruptedException {
        discordClient.awaitReady();
        LOGGER.info("discord bot is ready");
    }

    public void shutdown() {
        if (discordClient != null &&  discordClient.getStatus() == JDA.Status.CONNECTED) {
            LOGGER.info("shutdown discord bot");
            discordClient.shutdown();
        }
    }

    public void registerCommands(Command... commands) {
        commandExecutor.addCommands(commands);
    }

    public void sendNotification(String userId, String message) {
        User user = discordClient.getUserById(userId);
        if (user == null) {
            return;
        }
        user.openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(message).submit();
        });
    }

    public void broadcastToNotificationChannels(String message) {
        discordClient.getTextChannelsByName("warnstufen", true).forEach(textChannel -> {
            textChannel.sendMessage(message).submit();
        });
    }

    public void addShutdownHook(Runnable shutdownHook) {
        discordClient.addEventListener(new ListenerAdapter() {
            @Override
            public void onShutdown(@Nonnull ShutdownEvent event) {
                shutdownHook.run();
            }
        });
    }
}
