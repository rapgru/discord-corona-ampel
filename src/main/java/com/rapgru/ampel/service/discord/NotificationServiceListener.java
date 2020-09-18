package com.rapgru.ampel.service.discord;

import com.rapgru.ampel.dao.SubscriptionDAO;
import com.rapgru.ampel.discord.DiscordBot;
import com.rapgru.ampel.model.DistrictChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NotificationServiceListener implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceListener.class);

    private SubscriptionDAO subscriptionDAO;
    private DiscordBot discordBot;

    public NotificationServiceListener(SubscriptionDAO subscriptionDAO, DiscordBot discordBot) {
        this.subscriptionDAO = subscriptionDAO;
        this.discordBot = discordBot;
    }

    @Override
    public void pushChanges(List<DistrictChange> changes) {
        changes.forEach(districtChange -> {
            String notification = String.format(
                    "Die Warnstufe für die Gemeinde {} wurde geändert von {} auf {}. \n Begründung: {}",
                    districtChange.getDataPoint().getDistrict().getName(),
                    districtChange.getFrom().name(),
                    districtChange.getTo().name(),
                    districtChange.getDataPoint().getReason()
            );
            subscriptionDAO.getUsernamesSubscribedTo(districtChange.getDataPoint().getDistrict().getGkz()).forEach(
                    userId -> discordBot.sendNotification(userId, notification)
            );
        });
    }
}
