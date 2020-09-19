package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.dao.SubscriptionDAO;
import com.rapgru.ampel.discord.Command;
import com.rapgru.ampel.model.Subscription;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CheckSubscriptionCommand extends Command {

    private final SubscriptionDAO subscriptionDAO;

    public CheckSubscriptionCommand(SubscriptionDAO subscriptionDAO) {
        this.subscriptionDAO = subscriptionDAO;
    }

    @Override
    public String getName() {
        return "checkSubscription";
    }

    @Override
    public void execute(Message message, String[] args) {
        User user = Objects.requireNonNull(message.getMember()).getUser();
        List<Integer> subscriptions = subscriptionDAO.getSubscriptionWithUsername(user.getId())
                .stream()
                .map(Subscription::getGkz)
                .collect(Collectors.toList());

        if (subscriptions.isEmpty()) {
            sendTimedMessageFormat(message.getChannel(), "%s hat keine subscriptions.", user.getName());
            return;
        }

        sendTimedMessageFormat(
                message.getChannel(),
                "%s hat folgende GKZs subscribed: %s",
                user.getName(),
                subscriptions.toString()
        );
    }
}
