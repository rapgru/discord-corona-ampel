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
        setName("subscribe");
        this.subscriptionDAO = subscriptionDAO;
        this.coronaDataService = coronaDataService;
    }

    @Override
    public void execute(Message message, String[] args) {
        MessageChannel channel = message.getChannel();
        if (args.length == 0) {
            sendMessage(channel, "Falsche Verwendung, bitte versuche es nochmal. \n```!subscribe <Gemeinde>```");
            return;
        }

        if (message.getMember() == null) {
            return;
        }

        String districtName = String.join(" ", args);
        String userId = message.getMember().getUser().getId();
        List<Subscription> subscriptions = subscriptionDAO.getSubscriptionWithUsername(userId);

        // get District from database
        District district = coronaDataService.getDistrictByName(districtName, true).orElse(null);
        if (district == null) {
            sendMessage(channel, "Gemeinde '" + districtName + "' konnte nicht gefunden werden.");
            return;
        }

        // check if already subscribed
        //TODO: why not work
        if (subscriptions.stream().anyMatch(sub -> sub.getGkz() == district.getGkz())) {
            sendMessage(channel, "Du bist bereits zu dieser Gemeinde subscribed.");
            LOGGER.info("District " + districtName + " not found.");
            return;
        }

        // store subscription
        sendMessage(channel, "Für Gemeinde " + districtName + " mit GKZ " + district.getGkz() + " subscribed.");
        subscriptionDAO.storeSubscription(new Subscription(Instant.now(), userId, district.getGkz()));
        LOGGER.info("user " + userId + " subscribed to district " + districtName);
    }
}
