package com.rapgru.ampel.service.discord;

import com.rapgru.ampel.model.DistrictChange;
import com.rapgru.ampel.model.WarningColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.List;

public class ChangeMessageServiceImpl implements ChangeMessageService {

    @Override
    public MessageEmbed buildPrivateMessage(DistrictChange districtChange) {
        return buildPrivateMessage(
                districtChange.getDataPoint().getDistrict().getName(),
                districtChange.getFrom(),
                districtChange.getTo(),
                districtChange.getDataPoint().getReason()
        );
    }

    @Override
    public MessageEmbed buildBroadcastMessage(List<DistrictChange> changes) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if(changes.size() == 1) {
            embedBuilder.setTitle("Ampelschaltungen f\u00fcr 1 Gemeinde");
        } else {
            embedBuilder.setTitle(String.format("Ampelschaltungen f\u00fcr %d Gemeinden", changes.size()));
        }

        embedBuilder.setColor(Color.pink);

        embedBuilder.setDescription(String.format("Es gab Ampelschaltungen f\u00fcr %d \u00f6sterreichische Gemeinden." +
                " Siehe dir die \u00dcberg\u00e4nge unterbei an", changes.size()));

        changes.forEach(districtChange -> addTransitionField(embedBuilder, districtChange));

        return embedBuilder.build();
    }

    private void addTransitionField(EmbedBuilder embedBuilder, DistrictChange change) {
        embedBuilder.addField(
                change.getDataPoint().getDistrict().getName(),
                String.format("Die Warnstufe der Gemeinde **%s** wurde von **%s** auf **%s** ge\u00e4ndert. \n\n" +
                        "\u00dcbergang: :%s: :arrow_right: :%s:\n" +
                        "Grund: %s",
                        change.getDataPoint().getDistrict().getName(),
                        change.getFrom().getText(),
                        change.getTo().getText(),
                        change.getFrom().getEmoji(),
                        change.getTo().getEmoji(),
                        change.getDataPoint().getReason().isEmpty() ? "keine Angabe" : change.getDataPoint().getReason()
                ),
                false
        );
    }

    private MessageEmbed buildPrivateMessage(String gemeinde, WarningColor from, WarningColor to, String reason) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(String.format("Ampelschaltung f\u00fcr %s", gemeinde));

        embedBuilder.setColor(to.getColor());

        embedBuilder.setDescription(String.format(
                "Die Warnstufe f\u00fcr die Gemeinde **%s** wurde ge\u00e4ndert von **%s** auf **%s**",
                gemeinde,
                from.getText(),
                to.getText()
        ));

        embedBuilder.addField("\u00dcbergang", String.format(":%s: :arrow_right: :%s:", from.getEmoji(), to.getEmoji()), false);
        embedBuilder.addField("Grund", reason.isEmpty() ? "keine Angabe" : reason, false);

        return embedBuilder.build();
    }
}
