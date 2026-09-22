package dev.sosis.replay.managers.proxymode.proxyplayingmode;
import dev.sosis.replay.ReplayPlugin;
import dev.sosis.replay.advancedreplayhook.GameReplayHandler;
import dev.sosis.replay.commands.AutoConfigCommand;
import dev.sosis.replay.commands.JumpToCommand;
import dev.sosis.replay.commands.ReloadCommand;
import dev.sosis.replay.commands.ViewCommand;
import dev.sosis.replay.commands.handler.MainCommand;
import dev.sosis.replay.guis.CustomReplaySessionSettings;
import dev.sosis.replay.managers.proxymode.IProxyManager;
import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import me.jumper251.replay.utils.fetcher.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ProxyPlayingManager implements IProxyManager {
    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(new ProxyPlayingListener(), ReplayPlugin.plugin);
        MainCommand command = new MainCommand(
                new ViewCommand(),
                new JumpToCommand(),
                new ReloadCommand(),
                new AutoConfigCommand()
        );
        ReplayPlugin.plugin.getCommand("rp").setExecutor(command);
        ReplayPlugin.plugin.getCommand("rp").setTabCompleter(command);
    }

    @Override
    public void playRecording(Player p, String replayID) {
        if (ReplaySaver.exists(replayID) && !ReplayHelper.replaySessions.containsKey(p.getName())) {
            p.sendMessage(ReplaySystem.PREFIX + "Loading replay §e" + replayID + "§7...");
            try {
                ReplaySaver.load(replayID, new Consumer<Replay>() {

                    @Override
                    public void accept(Replay replay) {
                        p.sendMessage(ReplaySystem.PREFIX + "Replay loaded. Duration §e" + (replay.getData().getDuration() / 20) + "§7 seconds.");
                        GameReplayHandler.playingReplays.put(p, new CustomReplaySessionSettings(p, replayID));
                        replay.play(p);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();

                p.sendMessage(ReplaySystem.PREFIX + "§cError while loading §o" + replayID + ".replay. §r§cCheck console for more details.");
            }
        } else {
            p.sendMessage(ReplaySystem.PREFIX + "§cReplay not found.");
        }
    }

    @Override
    public String getMode() {
        return "proxy-playing-mode";
    }
}
