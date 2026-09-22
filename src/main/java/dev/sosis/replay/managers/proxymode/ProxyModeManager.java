package dev.sosis.replay.managers.proxymode;

import dev.sosis.replay.ReplayPlugin;
import dev.sosis.replay.advancedreplayhook.ProxyData;
import dev.sosis.replay.databases.SQLite;
import dev.sosis.replay.managers.IManager;
import dev.sosis.replay.managers.proxymode.proxylobbymanager.ProxyLobbyManager;
import dev.sosis.replay.managers.proxymode.proxyplayingmode.ProxyPlayingManager;
import dev.sosis.replay.versionutils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ProxyModeManager implements IManager {
    private IProxyManager manager;
    public static HashMap<UUID, ProxyData> proxyDataCache = new HashMap<>();
    @Override
    public void init(){
        if  (ReplayPlugin.plugin.db instanceof SQLite){
            Util.error("Proxy Mode needs either MySQL or PostgreSQL. Connect to the same database as the other servers.");
            Bukkit.getServer().getPluginManager().disablePlugin(ReplayPlugin.plugin);
            return;
        }
        if(!ReplayPlugin.plugin.mainConfig.getBoolean("proxy-mode.lobby.enabled")) {
            if(Bukkit.getServer().getPluginManager().getPlugin("AdvancedReplay")==null
                    || !Bukkit.getServer().getPluginManager().getPlugin("AdvancedReplay").getDescription().getAuthors().contains("MrF1yn")){
                Util.error("Proxy-Playing-Mode needs AdvancedReplay-Extended to run!");
                Bukkit.getServer().getPluginManager().disablePlugin(ReplayPlugin.plugin);
                return;
            }
            manager = new ProxyPlayingManager();
        }else {
            manager = new ProxyLobbyManager();
        }
        manager.init();
        if (Bukkit.getServer().getMessenger().isOutgoingChannelRegistered(ReplayPlugin.plugin, "BungeeCord")){
            Bukkit.getServer().getMessenger().unregisterOutgoingPluginChannel(ReplayPlugin.plugin, "BungeeCord");
        }
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(ReplayPlugin.plugin, "BungeeCord");
    }

    public IProxyManager getManager(){
        return this.manager;
    }

    @Override
    public String getMode() {
        return manager.getMode();
    }

    @Override
    public void playRecording(Player p, String replayID) {
        manager.playRecording(p, replayID);
    }

}
