package com.rapgru.ampel.service.discord;

import com.rapgru.ampel.model.DistrictChange;
import com.rapgru.ampel.model.WarningColor;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public interface ChangeMessageService {

    MessageEmbed buildPrivateMessage(DistrictChange districtChange);

    List<MessageEmbed> buildBroadcastMessage(List<DistrictChange> changes);
}
