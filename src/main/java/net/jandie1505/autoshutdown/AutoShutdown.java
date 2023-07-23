package net.jandie1505.autoshutdown;

import net.jandie1505.configmanager.ConfigManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.util.List;

public class AutoShutdown extends JavaPlugin implements CommandExecutor, TabCompleter {
    private ConfigManager configManager;
    private boolean timerEnabled;
    private int time;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this.getDefaultConfig(), false, this.getDataFolder(), "config.yml");
        this.configManager.reloadConfig();

        this.timerEnabled = this.configManager.getConfig().optBoolean("enabled", false);
        this.time = this.configManager.getConfig().optInt("time", 3600);

        if (this.time < 60) {
            this.time = 60;
        }

        PluginCommand command = this.getCommand("autoshutdown");

        if (command == null) {
            this.getServer().getPluginManager().disablePlugin(this);
            this.getLogger().severe("Command does not exist. Stopping plugin...");
            return;
        }

        command.setExecutor(this);
        command.setTabCompleter(this);

        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {

            if (!this.timerEnabled) {
                return;
            }

            if (this.time == 60) {
                for (Player player : List.copyOf(this.getServer().getOnlinePlayers())) {
                    player.sendMessage("§4§lWARNING! §r§cAutomatic server shutdown in 60 seconds");
                }
            }

            if (this.time < 0) {
                this.getLogger().info("Time is up. Automatic shutdown of server triggered.");
                this.getServer().shutdown();
                return;
            }

            this.time--;

        }, 0L, 20L);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("autoshutdown.use")) {
            sender.sendMessage("§cNo permission");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUsage: /autoshutdown disable/enable/get/set");
            return true;
        }

        switch (args[0]) {
            case "disable":
                this.timerEnabled = false;
                sender.sendMessage("§aAuto-shutdown disabled (timer paused)");
                break;
            case "enable":
                this.timerEnabled = true;
                sender.sendMessage("§aAuto-shutdown enabled (timer resumed)");
                break;
            case "get":
                sender.sendMessage("§7Enabled: " + this.timerEnabled + "\n§7Timer: " + this.time + "s");
                break;
            case "set":

                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /autoshutdown set <time>");
                    return true;
                }

                try {
                    this.time = Integer.parseInt(args[1]);
                    sender.sendMessage("§aTime set to " + this.time);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("§cPlease specify a valid int value");
                }

                break;
            default:
                sender.sendMessage("§cUsage: /autoshutdown pause/resume/set");
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            return List.of("disable", "enable", "get", "set");
        }

        return List.of();
    }

    public boolean isTimerEnabled() {
        return this.timerEnabled;
    }

    public void setTimerEnabled(boolean timerEnabled) {
        this.timerEnabled = timerEnabled;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    private JSONObject getDefaultConfig() {
        JSONObject config = new JSONObject();

        config.put("enabled", false);
        config.put("time", 3600);

        return config;
    }
}
