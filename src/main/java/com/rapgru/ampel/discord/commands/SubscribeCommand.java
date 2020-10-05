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
import java.util.Objects;

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

        String districtName = String.join(" ", args);
        String userId = Objects.requireNonNull(message.getMember()).getUser().getId();
        List<Subscription> subscriptions = subscriptionDAO.getSubscriptionWithUsername(userId);

        // get District from database
        District district = coronaDataService.getDistrictByName(districtName, true).orElse(null);
        if (district == null) {
            sendTimedMessageFormat(channel, "Gemeinde '%s' konnte nicht gefunden werden.", districtName);
            LOGGER.info("District {} not found.", districtName);
            return;
        }

        // check if already subscribed
        if (subscriptions.stream().anyMatch(sub -> sub.getGkz() == district.getGkz())) {
            sendTimedMessage(channel, "Du bist bereits zu dieser Gemeinde subscribed.");
            return;
        }

        // store subscription
        sendTimedMessageFormat(channel, "Für Gemeinde %s mit GKZ %d subscribed.", districtName, district.getGkz());
        subscriptionDAO.storeSubscription(new Subscription(Instant.now(), userId, district.getGkz()));
        LOGGER.info("userId {} subscribed to district {}", userId, districtName);
    }
}
