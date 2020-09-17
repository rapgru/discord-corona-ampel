package com.rapgru.ampel.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class DiscordBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordBot.class);

    private final JDA discordClient;
    private final CommandExecutor commandExecutor;

    public DiscordBot(String token) throws LoginException {
        discordClient = JDABuilder.createDefault(token).build();
        commandExecutor = new CommandExecutor(discordClient);
    }

    public void connectBlocking() throws InterruptedException {
        LOGGER.info("connect bot and block");
        discordClient.awaitReady();
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
}
