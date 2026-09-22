package dev.sosis.replay.managers.proxymode.proxylobbymanager;

import dev.sosis.replay.ReplayPlugin;
import dev.sosis.replay.advancedreplayhook.ProxyData;
import dev.sosis.replay.commands.AutoConfigCommand;
import dev.sosis.replay.commands.GamesCommand;
import dev.sosis.replay.commands.ReloadCommand;
import dev.sosis.replay.commands.ViewCommand;
import dev.sosis.replay.commands.handler.MainCommand;
import dev.sosis.replay.managers.proxymode.IProxyManager;
import dev.sosis.replay.versionutils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import static dev.sosis.replay.ReplayPlugin.plugin;

public class ProxyLobbyManager implements IProxyManager {
    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(new ProxyLobbyListener(), ReplayPlugin.plugin);
        MainCommand command = new MainCommand(
                new GamesCommand(),
                new ViewCommand(),
                new ReloadCommand(),
                new AutoConfigCommand()
        );
        ReplayPlugin.plugin.getCommand("rp").setExecutor(command);
        ReplayPlugin.plugin.getCommand("rp").setTabCompleter(command);
    }

    @Override
    public void playRecording(Player p, String replayID) {
        String uuid = p.getUniqueId().toString();
        String name = p.getName();
        Bukkit.getScheduler().runTaskAsynchronously(ReplayPlugin.plugin, ()->{
            if(plugin.db.getGameReplayCache(replayID)==null){
                p.sendMessage(ChatColor.RED + "Replay not found!");
                return;
            }
            ProxyData data = new ProxyData(
                    uuid,
                    name,
                    replayID,
                    ReplayPlugin.plugin.mainConfig.getString("proxy-mode.lobby.server-name"),
                    null,null,null,null

            );
            ReplayPlugin.plugin.db.setProxyData(data);
            try {
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(byteArray);
                out.writeUTF("Connect");
                out.writeUTF(Util.getRandomElement(
                        plugin.mainConfig.getStringList("proxy-mode.lobby.playing-servers")
                ));
                p.sendPluginMessage(plugin, "BungeeCord", byteArray.toByteArray());
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
    }

    @Override
    public String getMode() {
        return "proxy-lobby-mode";
    }
}
