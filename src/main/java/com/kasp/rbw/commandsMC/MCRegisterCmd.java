package com.kasp.rbw.commandsMC;

import com.kasp.rbw.EmbedType;
import com.kasp.rbw.RBW;
import com.kasp.rbw.config.Config;
import com.kasp.rbw.database.SQLPlayerManager;
import com.kasp.rbw.instance.Embed;
import com.kasp.rbw.instance.LinkManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MCRegisterCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /register <code>");

            return false;
        }

        int code = 0;
        try {
            code = Integer.parseInt(args[0]);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Please make sure the code is 6 " + ChatColor.UNDERLINE + "numbers");
            return false;
        }

        if (code == 0) {
            player.sendMessage(ChatColor.RED + "Something went wrong.. Please try again later");
            return false;
        }

        if (!LinkManager.isValidCode(code)) {
            player.sendMessage(ChatColor.RED + "This code is invalid. Please use =register again");
            return false;
        }

        Member member = null;
        try {
            member = RBW.guild.getMemberById(LinkManager.getMemberByCode(code));
        } catch (Exception ignored) {}

        if (member == null) {
            player.sendMessage(ChatColor.RED + "Something went wrong.. Please try again later");
            return false;
        }

        if (com.kasp.rbw.instance.Player.isRegistered(member.getId())) {
            player.sendMessage(ChatColor.RED + "You're already registered\nUse /rename to change your name");
            return false;
        }

        SQLPlayerManager.createPlayer(member.getId(), sender.getName());
        com.kasp.rbw.instance.Player plr = new com.kasp.rbw.instance.Player(member.getId());
        plr.fix();

        LinkManager.removePlayer(code);

        player.sendMessage(ChatColor.GREEN + "Successfully linked to " + ChatColor.DARK_GREEN + member.getUser().getAsTag());

        TextChannel alerts = RBW.guild.getTextChannelById(Config.getValue("alerts-channel"));
        Embed embed = new Embed(EmbedType.SUCCESS, "Successfully registered", "You have successfully registered as `" + player.getName() + "`", 1);
        alerts.sendMessage("<@" + member.getId() + ">").setEmbeds(embed.build()).queue();

        return true;
    }
}
