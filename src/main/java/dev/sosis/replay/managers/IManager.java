package dev.sosis.replay.managers;

import org.bukkit.entity.Player;

public interface IManager {

    void init();

    String getMode();

    void playRecording(Player p, String replayID);

}
