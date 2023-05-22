package com.kasp.rbw.listener;

import com.kasp.rbw.EmbedType;
import com.kasp.rbw.config.Config;
import com.kasp.rbw.instance.Embed;
import com.kasp.rbw.instance.Player;
import com.kasp.rbw.instance.Queue;
import com.kasp.rbw.instance.cache.PlayerCache;
import com.kasp.rbw.instance.cache.QueueCache;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class QueueJoin extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {


        // leave vc
        if (event.getChannelJoined() != null) {
            if (QueueCache.containsQueue(event.getChannelJoined().getId())) {
                TextChannel alerts = event.getGuild().getTextChannelById(Config.getValue("alerts-channel"));

                String ID = event.getMember().getId();
                Queue queue = QueueCache.getQueue(event.getChannelJoined().getId());

                Player player = PlayerCache.getPlayer(ID);

                if (!Player.isRegistered(ID)) {
                    event.getGuild().kickVoiceMember(event.getMember()).queue();

                    Embed embed = new Embed(EmbedType.ERROR, "You Cannot Queue", "", 1);
                    embed.setDescription("You are not registered. Please do `=register <your ign>` then try queueing again");
                    alerts.sendMessage(event.getMember().getAsMention()).setEmbeds(embed.build()).queue();
                    return;
                }

                if (player.isBanned()) {
                    event.getGuild().kickVoiceMember(event.getMember()).queue();

                    Embed embed = new Embed(EmbedType.ERROR, "You Cannot Queue", "", 1);
                    embed.addField("It appears that you've been banned", "If this is a mistake, please do `=fix`. If it still doesn't remove your banned role, you can open an appeal ticket / contact staff", false);
                    alerts.sendMessage(event.getMember().getAsMention()).setEmbeds(embed.build()).queue();
                }
                else {
                    queue.addPlayer(player);
                }
            }
        }
        // left vc
        if (event.getChannelLeft() != null) {
            if (QueueCache.containsQueue(event.getChannelLeft().getId())) {
                String ID = event.getMember().getId();
                Queue queue = QueueCache.getQueue(event.getChannelLeft().getId());

                Player player = PlayerCache.getPlayer(ID);

                queue.removePlayer(player);
            }
        }
    }
}
