package dev.sosis.replay.managers.proxymode.proxylobbymanager;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.sosis.replay.advancedreplayhook.GameReplayCache;
import dev.sosis.replay.advancedreplayhook.GameReplayHandler;
import dev.sosis.replay.guis.GuiHandler;
import dev.sosis.replay.versionutils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

import static dev.sosis.replay.ReplayPlugin.plugin;

public class ProxyLobbyListener implements Listener {

        @EventHandler
        public void onJoin(PlayerJoinEvent e){
                Player p = e.getPlayer();
                UUID uuid = p.getUniqueId();
                String name = p.getName();
//                String info = p.getName()+":"+p.getUniqueId().toString();
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        String lang = plugin.db.getPlayerLanguage(uuid);
                        if (lang == null) {
                                plugin.playerLang.put(uuid, plugin.allLanguages.get("en"));
                        } else {
                                plugin.playerLang.put(uuid, plugin.allLanguages.get(lang));
                        }
                        List<GameReplayCache> caches = plugin.db.getGameReplayCaches(name);
                        GameReplayHandler.replayCachePerPlayer.put(uuid, caches);
                        for(GameReplayCache cache : caches){
                                GameReplayHandler.replayCacheID.put(cache.getReplayName(), cache);
                        }
                        Util.debug("loaded on join.");
                        Bukkit.getScheduler().runTask(plugin, ()->{
                           GuiHandler.onJoin(p);
                        });
                });
        }

        @EventHandler
        public void onLeave(PlayerQuitEvent e){
                if(GameReplayHandler.playingReplays.containsKey(e.getPlayer())) {
                        GameReplayHandler.playingReplays.get(e.getPlayer()).getChatTimelineHandler().terminate();
                        GameReplayHandler.playingReplays.remove(e.getPlayer());
                }
                for(GameReplayCache cache : GameReplayHandler.replayCachePerPlayer.get(e.getPlayer().getUniqueId())){
                        GameReplayHandler.replayCacheID.remove(cache.getReplayName());
                }
                GameReplayHandler.replayCachePerPlayer.remove(e.getPlayer().getUniqueId());
                GuiHandler.onLeave(e.getPlayer());
                GameReplayHandler.startQueue.remove(e.getPlayer());
                e.getPlayer().setFlySpeed(0.1F);
        }


}
