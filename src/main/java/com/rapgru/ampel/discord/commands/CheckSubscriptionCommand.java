package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.dao.SubscriptionDAO;
import com.rapgru.ampel.discord.Command;
import com.rapgru.ampel.model.Subscription;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

public class CheckSubscriptionCommand extends Command {

    private final SubscriptionDAO subscriptionDAO;

    public CheckSubscriptionCommand(SubscriptionDAO subscriptionDAO) {
        setName("checkSubscription");
        this.subscriptionDAO = subscriptionDAO;
    }

    @Override
    public void execute(Message message, String[] args) {
        MessageChannel channel = message.getChannel();
        Member member = message.getMember();
        if (member == null) {
            sendMessage(channel, "can not find member.");
            return;
        }

        if (member.getUser().isBot()) {
            sendMessage(channel, "this command is only for humans.");
            return;
        }

        String userId = member.getUser().getId();
        List<Subscription> subscriptions = subscriptionDAO.getSubscriptionWithUsername(userId);

        if (subscriptions.isEmpty()) {
            sendMessage(channel, member.getUser().getName() + " hat keine subscriptions.");
            return;
        }

        // send subscriptions to user
        StringBuilder subscriptionListBuilder = new StringBuilder(
                member.getUser().getName() + " hat folgende Gemeinden subscribed: "
        );
        subscriptions.forEach(subscription -> {
            subscriptionListBuilder.append(subscription.getGkz()); //TODO: name of district instead of Gkz
            subscriptionListBuilder.append(" ");
        });
        sendMessage(channel, subscriptionListBuilder.toString());
    }
}
