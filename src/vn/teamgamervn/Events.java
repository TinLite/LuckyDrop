package vn.teamgamervn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Events implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        Material block = event.getBlock().getType();
        if (!Storage.contains(block)) return;
        if (Math.random() < (Storage.getPercentage(block) / 100))
        {
            event.getPlayer().getInventory().addItem(Storage.item);

            if (Storage.config.getBoolean("Broadcast")) Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Storage.config.getString("BroadcastMsg").replaceAll("%player%", event.getPlayer().getName()).replaceAll("%block%", block.name())));

            if (Storage.config.getBoolean("PlayGlobalSound")) {
                for (Player p : Bukkit.getOnlinePlayers())
                    if (!(player.getName().equals(p.getName())))
                        p.playSound(player.getLocation(), Sound.valueOf(Storage.config.getString("GlobalSound")), 1, 1);
            }

            if (Storage.config.getBoolean("PlaySingleSound"))
                player.playSound(player.getLocation(), Sound.valueOf(Storage.config.getString("SingleSound")),1,1);

            if (Storage.config.getBoolean("Block." + block.name() + ".RemoveBaseItem"))
            {
                event.getBlock().setType(Material.AIR);
                event.setCancelled(true);
            }
        }
    }

    public void onClick(PlayerInteractEvent e)
    {
        if (e.getItem().isSimilar(Storage.item))
        {
            Set<String> set = Storage.config.getConfigurationSection("Commands").getKeys(false);
            org.bukkit.configuration.file.FileConfiguration config = Storage.config;
            int index = ThreadLocalRandom.current().nextInt(set.size());
            int i = 0;
            String rnd;
            for (String s : set)
            {
                if (index == i)
                {
                    rnd = s;
                    break;
                }
                i++;
            }
            e.getPlayer().getInventory().removeItem(Storage.item);
            e.setCancelled(true);
        }
    }
}
