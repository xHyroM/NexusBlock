package hyro.lib.commands;

import hyro.lib.Main;
import hyro.lib.structures.Nexus;
import hyro.lib.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class nexusblock implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;
        Boolean hasPermission = commandSender == null || p.hasPermission("nexusblock.admin");

        if((args.length == 0 || args[0].equalsIgnoreCase("info")) && !hasPermission) {
            Message.sendMessage(p, "Plugin by &c&nxHyroM#2851 &7available on github &c&nhttps://github.com/xHyroM/NexusBlock");
            return true;
        }

        if((args.length == 0 || args[0].equalsIgnoreCase("info")) && hasPermission) {
            Message.sendMessage(p, "Plugin by &c&nxHyroM#2851 &7available on github &c&nhttps://github.com/xHyroM/NexusBlock");
            Message.sendMessageNoPrefix(p, "\n&c&nAdmin Commands:\n\n&c/nexusblock reload &8- &7Reload plugin\n&c/nexusblock create\n&c/nexusblock remove");
            return true;
        }

        if(!hasPermission) {
            Message.sendMessage(p, "&cYou don't have permissions!");
            return true;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            try {
                Bukkit.getLogger().info("[NexusBlock] Reloading...");
                Main.fileConfig.load(Main.file);
                Main.reloadPlugin();

                Message.sendMessage(p, "Plugin has been reloaded!");
                Bukkit.getLogger().info("[NexusBlock] Reloaded!");
            } catch (Exception e) {
                Bukkit.getLogger().info("[NexusBlock] Reloading...");
                Main.reloadPlugin();

                Message.sendMessage(p, "Plugin has been reloaded!");
                Bukkit.getLogger().info("[NexusBlock] Reloaded!");
            }
        }

        if(args[0].equalsIgnoreCase("create")) {
            if(args.length < 5) {
                Message.sendMessage(p, "Please provide material, location x y z.");
                return true;
            }

            String material = args[1].toUpperCase();
            String x = args[2];
            String y = args[3];
            String z = args[4];
            String path = "nexuses."+material;

            List<String> rewards = new ArrayList<String>();
            rewards.add("say {player} win!");

            List<String> rewardsEveryBreak = new ArrayList<String>();
            rewardsEveryBreak.add("say {player} break!;100%");

            Main.fileConfig.set(path+".name", "&e&l" +material.split("_")[0] + " NEXUS");
            Main.fileConfig.set(path+".info", "Destory to get reward");
            Main.fileConfig.set(path+".maxHealth", 100);
            Main.fileConfig.set(path+".health", 0);
            Main.fileConfig.set(path+".healthBar", "{health}/{maxHealth}");
            Main.fileConfig.set(path+".location.x", Float.parseFloat(x));
            Main.fileConfig.set(path+".location.y", Float.parseFloat(y));
            Main.fileConfig.set(path+".location.z", Float.parseFloat(z));
            Main.fileConfig.set(path+".location.yaw", 0.0);
            Main.fileConfig.set(path+".location.pitch", 0.0);
            Main.fileConfig.set(path+".location.world", p.getWorld().getName());
            Main.fileConfig.set(path+".rewards", rewards);
            Main.fileConfig.set(path+".rewardsEveryBreak", rewardsEveryBreak);
            Main.fileConfig.set(path+".respawn", 5);

            try {
                Main.fileConfig.save(Main.file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Message.sendMessage(p, "Done! If you want to customize nexus more, go to config.yml. You can do &c&n/nexusblock reload.");
        }

        if(args[0].equalsIgnoreCase("remove")) {
            if(args.length < 2) {
                Message.sendMessage(p, "Please provide name (material type).");
                return true;
            }

            Nexus nexus = Main.nexuses.get(args[1]);
            if(nexus == null) {
                Message.sendMessage(p, "Invalid nexus name. (material type)");
                return true;
            }

            Main.HologramManager.deleteHologram(nexus.hologram);
            Main.nexuses.remove(args[1]);
            Main.fileConfig.set("nexuses."+args[1], null);

            try {
                Main.fileConfig.save(Main.file);
            } catch (IOException e) {
                Message.sendMessage(p, "Error, check console.");
                return true;
            }

            Message.sendMessage(p, "Done! You can do &c&n/nexusblock reload.");
        }

        return true;
    }
}
