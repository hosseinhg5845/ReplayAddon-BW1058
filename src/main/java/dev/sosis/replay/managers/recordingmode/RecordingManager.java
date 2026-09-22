package dev.sosis.replay.managers.recordingmode;

import dev.sosis.replay.ReplayPlugin;
import dev.sosis.replay.commands.AutoConfigCommand;
import dev.sosis.replay.commands.ReloadCommand;
import dev.sosis.replay.commands.handler.MainCommand;
import dev.sosis.replay.databases.SQLite;
import dev.sosis.replay.managers.IManager;
import dev.sosis.replay.versionutils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RecordingManager implements IManager {

    @Override
    public void init(){

        if(Bukkit.getServer().getPluginManager().getPlugin("AdvancedReplay")==null
                || !Bukkit.getServer().getPluginManager().getPlugin("AdvancedReplay").getDescription().getAuthors().contains("MrF1yn")){
            Util.error("RecordingMode needs AdvancedReplay-Extended to run!");
            Bukkit.getServer().getPluginManager().disablePlugin(ReplayPlugin.plugin);
            return;
        }
        if (Bukkit.getServer().getPluginManager().getPlugin("BedWars1058") == null) {
            Util.error("RecordingMode needs BedWars1058 to run!");
            Bukkit.getServer().getPluginManager().disablePlugin(ReplayPlugin.plugin);
            return;
        }
        if  (ReplayPlugin.plugin.db instanceof SQLite){
            Util.error("Recording Mode needs either MySQL or PostgreSQL. Connect to the same database as the other servers.");
            Bukkit.getServer().getPluginManager().disablePlugin(ReplayPlugin.plugin);
            return;
        }

        Bukkit.getServer().getPluginManager().registerEvents(new RecordingListener(), ReplayPlugin.plugin);
        MainCommand command = new MainCommand(
                new ReloadCommand(),
                new AutoConfigCommand()
        );
        ReplayPlugin.plugin.getCommand("rp").setExecutor(command);
        ReplayPlugin.plugin.getCommand("rp").setTabCompleter(command);

    }

    @Override
    public String getMode() {
        return "recording-mode";
    }

    @Override
    public void playRecording(Player p, String replayID) {

    }

}
