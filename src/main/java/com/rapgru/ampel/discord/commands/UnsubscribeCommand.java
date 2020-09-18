package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.dao.SubscriptionDAO;
import com.rapgru.ampel.discord.Command;
import com.rapgru.ampel.model.District;
import com.rapgru.ampel.service.data.CoronaDataService;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            sendMessage(channel, "Falsche Verwendung, bitte versuche es nochmal. \n```!unsubscribe <Gemeinde>```");
            return;
        }

        String districtName = String.join(" ", args);
        District district = coronaDataService.getDistrictByName(districtName, true).orElse(null);
        if (district == null) {
            sendMessage(channel, "Gemeinde '" + districtName + "' konnte nicht gefunden werden");
            LOGGER.info("District {} can not be found.", districtName);
            return;
        }

        if (message.getMember() == null) {
            return;
        }
        String userId = message.getMember().getUser().getId();
        subscriptionDAO.getSubscriptionWithUsername(userId).stream()
                .filter(subscription -> subscription.getGkz() == district.getGkz())
                .forEach(subscriptionDAO::deleteSubscription);

        channel.sendMessage("Unsubscribed von Gemeinde " + districtName).submit();
    }
}
