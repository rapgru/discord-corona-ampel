package com.rapgru.ampel.service.discord;

import com.rapgru.ampel.dao.SubscriptionDAO;
import com.rapgru.ampel.discord.DiscordBot;
import com.rapgru.ampel.model.DistrictChange;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NotificationServiceListener implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceListener.class);

    private final SubscriptionDAO subscriptionDAO;
    private final DiscordBot discordBot;
    private final ChangeMessageService changeMessageService;

    public NotificationServiceListener(SubscriptionDAO subscriptionDAO, DiscordBot discordBot, ChangeMessageService changeMessageService) {
        this.subscriptionDAO = subscriptionDAO;
        this.discordBot = discordBot;
        this.changeMessageService = changeMessageService;
    }

    @Override
    public void pushChanges(List<DistrictChange> changes) {
        changes.forEach(districtChange -> {
            MessageEmbed notification = changeMessageService.buildPrivateMessage(districtChange);

            // user direct message
            subscriptionDAO.getUsernamesSubscribedTo(districtChange.getDataPoint().getDistrict().getGkz())
                    .forEach(userId -> discordBot.sendDirectMessage(userId, notification)
            );

            // channel message
        });
        LOGGER.info("sent direct message change notifications");

        List<MessageEmbed> broadcastMessage = changeMessageService.buildBroadcastMessage(changes);
        broadcastMessage.forEach(discordBot::broadcastToNotificationChannels);
        LOGGER.info("sent broadcast messages");
        //discordBot.broadcastToNotificationChannels(subscribers.size() + " direct messages sent!"); // debug
    }
}
