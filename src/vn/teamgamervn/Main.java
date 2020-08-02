package vn.teamgamervn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin {

    @Override
    public void onEnable()
    {
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Storage.setup(this);
        Bukkit.getLogger().log(Level.INFO, ChatColor.GREEN + "LuckyDrop by TeamGamerVN. Coded by VinhGaming.");
        Storage.createItemStack();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Commands commands = new Commands();
        if (!sender.hasPermission("ld.admin")) {
            sender.sendMessage(ChatColor.RED + "Bạn có quyền thực hiện điều này. Hãy liên hệ với staff nếu bạn nghĩ đây là lỗi.");
            return true;
        }
        if (args.length == 0) return commands.sendHelp(sender);
        if (args[0].equalsIgnoreCase("reload")) {
            return commands.reloadConfig(sender);
        }
        if (args[0].equalsIgnoreCase("givebox")) {
            return commands.givebox(sender, args);
        }
        return commands.sendHelp(sender);
    }
}
