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

import java.time.Instant;
import java.util.List;

public class SubscribeCommand extends Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeCommand.class);

    private final SubscriptionDAO subscriptionDAO;
    private final CoronaDataService coronaDataService;

    public SubscribeCommand(SubscriptionDAO subscriptionDAO, CoronaDataService coronaDataService) {
        this.subscriptionDAO = subscriptionDAO;
        this.coronaDataService = coronaDataService;
    }

    @Override
    public String getName() {
        return "subscribe";
    }

    @Override
    public void execute(Message message, String[] args) {
        MessageChannel channel = message.getChannel();
        if (args.length == 0) {
            sendTimedMessage(channel, "Falsche Verwendung, bitte versuche es nochmal. \n```!subscribe <Gemeinde>```");
            return;
        }

        if (message.getMember() == null) {
            LOGGER.warn("member of message is null");
            return;
        }

        String districtName = String.join(" ", args);
        String userId = message.getMember().getUser().getId();
        List<Subscription> subscriptions = subscriptionDAO.getSubscriptionWithUsername(userId);

        // get District from database
        District district = coronaDataService.getDistrictByName(districtName, true).orElse(null);
        if (district == null) {
            sendTimedMessage(channel, "Gemeinde '" + districtName + "' konnte nicht gefunden werden.");
            LOGGER.info("District {} not found.", districtName);
            return;
        }

        // check if already subscribed
        if (subscriptions.stream().anyMatch(sub -> sub.getGkz() == district.getGkz())) {
            sendTimedMessage(channel, "Du bist bereits zu dieser Gemeinde subscribed.");
            return;
        }

        // store subscription
        sendTimedMessage(channel, "Für Gemeinde " + districtName + " mit GKZ " + district.getGkz() + " subscribed.");
        subscriptionDAO.storeSubscription(new Subscription(Instant.now(), userId, district.getGkz()));
        LOGGER.info("userId {} subscribed to district {}", userId, districtName);
    }
}
