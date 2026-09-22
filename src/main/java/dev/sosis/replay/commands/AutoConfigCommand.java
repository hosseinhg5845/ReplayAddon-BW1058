package dev.sosis.replay.commands;

import dev.sosis.replay.commands.handler.SubCommand;
import dev.sosis.replay.configs.confighelper.AutoConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AutoConfigCommand implements SubCommand {

    public AutoConfigCommand(){

    }

    @Override
    public boolean onSubCommand(CommandSender sender, Command cmd, String label, String[] args) {
        new AutoConfig().execute();
        sender.sendMessage("Auto-Configured. Please restart the server.");
        return true;
    }

    @Override
    public List<String> suggestTabCompletes(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public String getName() {
        return "autoConfig";
    }

    @Override
    public boolean isProtected() {
        return true;
    }

    @Override
    public String getPermission() {
        return "replayAddon.command.autoConfig";
    }
}
