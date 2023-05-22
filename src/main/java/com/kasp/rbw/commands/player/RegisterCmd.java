package com.kasp.rbw.commands.player;

import com.kasp.rbw.CommandSubsystem;
import com.kasp.rbw.EmbedType;
import com.kasp.rbw.commands.Command;
import com.kasp.rbw.database.SQLPlayerManager;
import com.kasp.rbw.instance.Embed;
import com.kasp.rbw.instance.Player;
import com.kasp.rbw.messages.Msg;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class RegisterCmd extends Command {

    public RegisterCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
        super(command, usage, aliases, description, subsystem);
    }

    @Override
    public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
        if (args.length != 2) {
            Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
            msg.replyEmbeds(reply.build()).queue();
            return;
        }

        if (Player.isRegistered(sender.getId())) {
            Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("already-registered"), 1);
            msg.replyEmbeds(reply.build()).queue();
            return;
        }

        String ign = args[1].replaceAll("[^a-zA-Z0-9_-]", "");
        ign = ign.replaceAll(" ", "").trim();

        if (ign.length() > 16) {
            Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("ign-too-long"), 1);
            msg.replyEmbeds(reply.build()).queue();
            return;
        }

        SQLPlayerManager.createPlayer(sender.getId(), ign);
        Player player = new Player(sender.getId());
        player.fix();

        Embed reply = new Embed(EmbedType.SUCCESS, "", Msg.getMsg("successfully-registered"), 1);
        msg.replyEmbeds(reply.build()).queue();
    }
}
