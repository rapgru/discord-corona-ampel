package com.rapgru.ampel.discord.commands;

import com.rapgru.ampel.Main;
import com.rapgru.ampel.discord.AdminCommand;
import com.rapgru.ampel.service.data.CoronaDataService;
import net.dv8tion.jda.api.entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteRolesCommand extends AdminCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private final CoronaDataService coronaDataService;

    public DeleteRolesCommand(CoronaDataService coronaDataService) {
        setName("deleteroles");
        this.coronaDataService = coronaDataService;
    }

    @Override
    public void execute(Message message, String[] args) {
        message.getChannel().sendMessage("start deletion of district roles").complete();
        LOGGER.info("start deletion of district roles");

        coronaDataService.getAllAustrianDistricts().forEach(district -> {
            String districtName = district.getName();

            LOGGER.info("delete district role {}", districtName);
            message.getGuild().getRolesByName(districtName, true).forEach(role -> role.delete().submit());
        });
    }
}
