package com.rapgru.ampel.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class DiscordBot {

    private final JDA discordClient;

    public DiscordBot(String token) throws LoginException {
        discordClient = JDABuilder.createDefault(token).build();
    }

    public void connectBlocking() throws InterruptedException {
        discordClient.awaitReady();
    }

    public void disconnect() {
        if (discordClient != null) {
            discordClient.shutdown();
        }
    }

    public void registerEventListeners(Object... listeners) {
        discordClient.addEventListener(listeners);
    }
}
