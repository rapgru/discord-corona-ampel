package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.dao.SubscriptionDAO;
import com.rapgru.ampel.discord.Command;
import com.rapgru.ampel.model.District;
import com.rapgru.ampel.model.Subscription;
import com.rapgru.ampel.service.data.CoronaDataService;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class UnsubscribeCommand extends Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnsubscribeCommand.class);

    private final CoronaDataService coronaDataService;
    private final SubscriptionDAO subscriptionDAO;

    public UnsubscribeCommand(CoronaDataService coronaDataService, SubscriptionDAO subscriptionDAO) {
        this.coronaDataService = coronaDataService;
        this.subscriptionDAO = subscriptionDAO;
    }

    @Override
    public String getName() {
        return "unsubscribe";
    }

    @Override
    public void execute(Message message, String[] args) {
        MessageChannel channel = message.getChannel();
        if (args.length == 0) {
            sendTimedMessage(channel, "Falsche Verwendung, bitte versuche es nochmal. \n```!unsubscribe <Gemeinde>```");
            return;
        }

        String districtName = String.join(" ", args);
        District district = coronaDataService.getDistrictByName(districtName, true).orElse(null);
        if (district == null) {
            sendTimedMessageFormat(channel, "Gemeinde '%s' konnte nicht gefunden werden", districtName);
            LOGGER.info("District {} can not be found.", districtName);
            return;
        }

        String userId = Objects.requireNonNull(message.getMember()).getUser().getId();
        List<Subscription> subscriptions = subscriptionDAO.getSubscriptionWithUsername(userId);
        if (subscriptions.isEmpty()) {
            sendTimedMessageFormat(channel, "Du bist nicht bei der Gemeinde '%s' subscribed", districtName);
            return;
        }
        subscriptions.stream()
                .filter(subscription -> subscription.getGkz() == district.getGkz())
                .forEach(subscription -> {
                    subscriptionDAO.deleteSubscription(subscription);
                    sendTimedMessageFormat(channel, "Unsubscribed von Gemeinde '%s'", districtName);
                    LOGGER.info("userId {} unsubscribed from district {}", userId, districtName);
                });
    }
}
