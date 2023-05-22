package com.kasp.rbw.listener;

import com.kasp.rbw.EmbedType;
import com.kasp.rbw.config.Config;
import com.kasp.rbw.instance.Embed;
import com.kasp.rbw.instance.Player;
import com.kasp.rbw.instance.cache.PlayerCache;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ServerJoin extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        if (Player.isRegistered(event.getMember().getId())) {
            Player player = PlayerCache.getPlayer(event.getMember().getId());
            player.fix();

            Embed embed = new Embed(EmbedType.DEFAULT, "Welcome Back", "I've noticed it's not your first time in this server" +
                    "\nI corrected your stats and updated your nickname - you don't need to register again!", 1);

            event.getGuild().getTextChannelById(Config.getValue("alerts-channel")).sendMessage(event.getMember().getAsMention()).setEmbeds(embed.build()).queue();
        }
    }
}
