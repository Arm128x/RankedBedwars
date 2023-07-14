package com.kasp.rbw.listener;

import com.andrei1058.bedwars.api.events.gameplay.GameEndEvent;
import com.andrei1058.bedwars.api.events.gameplay.TeamAssignEvent;
import com.andrei1058.bedwars.api.events.server.ArenaDisableEvent;
import com.andrei1058.bedwars.api.events.server.ArenaEnableEvent;
import com.kasp.rbw.GameState;
import com.kasp.rbw.RBW;
import com.kasp.rbw.instance.Game;
import com.kasp.rbw.instance.GameMap;
import com.kasp.rbw.instance.cache.GameCache;
import com.kasp.rbw.instance.cache.MapCache;
import com.kasp.rbw.instance.cache.PlayerCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class BW1058Events implements Listener {

    @EventHandler
    public void teamAssignEvent(TeamAssignEvent event) {
        if (event.getArena().getGroup().startsWith("rbw")) {
            event.setCancelled(true);

            event.getArena();
        }
    }

    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        Game game = null;

        for (Game g : GameCache.getGames().values()) {
            if (g.isCasual()) {
                continue;
            }

            if (!g.getState().equals(GameState.PLAYING)) {
                continue;
            }

            if (g.getMap().getName().equalsIgnoreCase(event.getArena().getArenaName())) {
                game = g;
                break;
            }
        }

        if (game != null) {

            Player maxKills = null;
            for (Player p : event.getArena().getPlayers()) {
                if (maxKills == null)
                    maxKills = p;

                if (event.getArena().getPlayerKills(maxKills, false) + event.getArena().getPlayerKills(maxKills, true) < event.getArena().getPlayerKills(p, false) + event.getArena().getPlayerKills(p, true)) {
                    maxKills = p;
                }
            }

            com.kasp.rbw.instance.Player mvp = PlayerCache.getPlayerByIgn(maxKills.getName());

            List<com.kasp.rbw.instance.Player> winningTeam;
            List<com.kasp.rbw.instance.Player> losingTeam;

            // check if arena winners contain the first player from game team1
            if (event.getWinners().contains(Bukkit.getPlayer(game.getTeam1().get(0).getIgn()).getUniqueId())) {
                winningTeam = game.getTeam1();
                losingTeam = game.getTeam2();
            }
            else {
                winningTeam = game.getTeam2();
                losingTeam = game.getTeam1();
            }

            game.scoreGame(winningTeam, losingTeam, mvp, RBW.guild.getMemberById(RBW.jda.getSelfUser().getId()));
        }
    }

    @EventHandler
    public void arenaEnableEvent(ArenaEnableEvent event) {
        if (!event.getArena().getGroup().startsWith("rbw")) {
            return;
        }

        if (!MapCache.getMaps().containsKey(event.getArena().getArenaName())) {
            new GameMap(event.getArena().getArenaName());
        }
    }

    @EventHandler
    public void arenaDisableEvent(ArenaDisableEvent event) {
        if (!RBW.bedwarsAPI.getArenaUtil().getArenaByName(event.getArenaName()).getGroup().startsWith("rbw")) {
            return;
        }

        if (MapCache.getMaps().containsKey(event.getArenaName())) {
            MapCache.removeMap(MapCache.getMap(event.getArenaName()));
        }
    }
}
