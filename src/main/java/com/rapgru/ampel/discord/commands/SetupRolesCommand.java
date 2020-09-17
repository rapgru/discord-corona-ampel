package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.discord.AdminCommand;
import com.rapgru.ampel.model.District;
import com.rapgru.ampel.service.data.CoronaDataService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SetupRolesCommand extends AdminCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetupRolesCommand.class);

    private final CoronaDataService coronaDataService;

    public SetupRolesCommand(CoronaDataService coronaDataService) {
        setName("setupRoles");
        this.coronaDataService = coronaDataService;
    }

    @Override
    public void execute(Message message, String[] args) {
        Guild guild = message.getGuild();

        message.getChannel().sendMessage("start creation of district roles").complete();
        LOGGER.info("start creation of district roles");

        AtomicInteger counter = new AtomicInteger();
        List<District> districts = coronaDataService.getAllAustrianDistricts();
        final int districtCount = districts.size();

        districts.forEach(district -> {
            String districtName = district.getName();

            if (!guild.getRolesByName(districtName, true).isEmpty()) {
                LOGGER.info("District {} already exists", districtName);
                return;
            }

            LOGGER.info("create district role {}", districtName);
            guild.createRole()
                    .setName(district.getName())
                    .setMentionable(true)
                    .queue(role -> {
                        counter.getAndIncrement();
                        LOGGER.info("created district role {} out of {}", counter.get(), districtCount);

                        if (counter.get() == districtCount) {
                            message.getChannel().sendMessage("district setup done!").submit();
                            LOGGER.info("district setup done");
                        }
                    });

        });
    }
}
