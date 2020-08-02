package vn.teamgamervn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands {
    public boolean sendHelp(CommandSender sender)
    {
        if (sender.hasPermission("ld.admin"))
        {
            sender.sendMessage("/ld [help] - Xem cái này");
            sender.sendMessage("/ld givebox <tên> [số lượng] - Đưa khối may mắn cho người chơi.");
            sender.sendMessage("/ld reload - Reload Config");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Bạn không có quyền thực hiện điều này.");
        return true;
    }

    public boolean reloadConfig(CommandSender sender) {
        Storage.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Config Reloaded. Lệnh này có thể thực hiện không đúng.");
        return true;
    }

    public boolean givebox(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return sendHelp(sender);
        }
        Player player = null;
        if (args.length == 2) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().equals(args[1])) {
                    player = p;
                    break;
                }
            }
        }
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Người chơi không Online.");
            return true;
        }
        player.getInventory().addItem(Storage.item);
        return true;
    }
}
