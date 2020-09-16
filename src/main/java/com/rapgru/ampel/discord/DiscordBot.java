package com.rapgru.ampel.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class DiscordBot {

    private final String token;
    private JDA discordBot;

    public DiscordBot(String token) {
        this.token = token;

        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("token must be not null");
        }
    }

    public void connectBlocking() throws LoginException, InterruptedException {
        discordBot = JDABuilder.createDefault(token).build();
        discordBot.awaitReady();
    }

    public void disconnect() {
        if (discordBot != null) {
            discordBot.shutdown();
        }
    }

    public void registerEventListeners(Object... listeners) {
        discordBot.addEventListener(listeners);
    }
}
